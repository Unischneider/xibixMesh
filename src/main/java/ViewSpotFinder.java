import com.google.gson.Gson;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ViewSpotFinder {
    private static Mesh mesh;
    private static ArrayList<Mesh> localMeshes;

    private static void parseFile(String filepath) {
        Gson gson = new Gson();
        try {
            FileReader fr = new FileReader(filepath);
            mesh = gson.fromJson(fr, Mesh.class);
        } catch (IOException e) {
            System.out.println("why is it not there");
            System.out.println(filepath);
            e.printStackTrace();
        }
    }

    private static void sortNeighborHoods() {
        localMeshes = new ArrayList<Mesh>();
        mesh.getNodes()
                .forEach(node -> localMeshes.add(new Mesh(mesh.getElements().stream()
                        .filter(element -> element.getNodes().contains(node.getId()))
                        .collect(Collectors
                                .toCollection(ArrayList::new)))));
    }

    private static ArrayList<Value> getNHighestOfNeighorhood(int nrOfSpots) {
        ArrayList<Value> result = new ArrayList<>();
        ArrayList<Mesh> localValues = new ArrayList<>();

        for (int i = 0; i < localMeshes.size(); i++){
            Mesh neighborhood = localMeshes.get(i);
            ArrayList<Integer> ids = new ArrayList<>();
            neighborhood.getElements().forEach(element -> ids.add(element.getId()));
            ArrayList<Value> values = new ArrayList<>();
            ids.forEach(id -> values.add(new Value(id, mesh.getValues().stream().filter(value -> value.getElement_id() == id).findFirst().get().getValue())));
            Mesh valueMesh = new Mesh();
            valueMesh.setValues(values);
            localValues.add(valueMesh);
        }


        localValues.forEach(mesh1 -> result.add(Collections.max(mesh1.getValues(), (value1, value2) -> Double.compare(value1.getValue(), value2.getValue()) )));
        result.sort((value1, value2) -> Double.compare(value2.getValue(),value1.getValue()));
        return result.stream().limit(nrOfSpots).collect(Collectors
                .toCollection(ArrayList::new));
//return result;
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Wrong number of arguments.");
            return;
        }

        String filename = args[0];
        int nrSpots = Integer.valueOf(args[1]);
        if (nrSpots <= 0) {
            System.out.println(0);
            return;
        }
        parseFile(filename);
        System.out.println(mesh);
        sortNeighborHoods();
        localMeshes.stream().forEach(mesh1 -> {
            System.out.println("This is a new Neighborhood");
            mesh1.getElements().stream().forEach(element -> System.out.println(element.getId()));
        });
        System.out.println("Number of neighborhoods: " + localMeshes.size());
        ArrayList<Value> result = getNHighestOfNeighorhood(nrSpots);
        System.out.println("The highest values are: " + result.size());
        result.forEach(value -> System.out.println(value));
    }
}
