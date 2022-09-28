import com.google.gson.Gson;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.stream.Collectors;

public class ViewSpotFinder {
    private static Mesh mesh;
    private static ArrayList<Location> locations;
    private static HashMap<Integer, Double> valueLookup;

    private static void parseFile(String filepath) {
        Gson gson = new Gson();
        try {
            FileReader fr = new FileReader(filepath);
            mesh = gson.fromJson(fr, Mesh.class);
        } catch (IOException e) {
            System.err.println(filepath);
            e.printStackTrace();
        }
    }

    private static void splitMeshIntoLocations() {
        locations = new ArrayList<>();
        valueLookup = new HashMap<>();
        mesh.getValues().forEach(value -> valueLookup.put(value.getElement_id(), value.getValue()));
        mesh.getNodes()
                .forEach(node -> {
                    Location loc = new Location(mesh.getElements().stream()
                            .filter(element -> element.getNodes().contains(node.getId()))
                            .map(element -> element.getId())
                            .collect(Collectors
                                    .toCollection(ArrayList::new)));
                    if (!loc.elementIds.isEmpty())
                        locations.add(loc);
                });
    }

    private static ArrayList<Value> getHighestViewPointsOfLocation(int nrOfSpots) {
        ArrayList<Value> result = new ArrayList<>();
        locations.forEach(location -> {
            int id = Collections.max(location.elementIds, (element1, element2) -> Double.compare(valueLookup.get(element1), valueLookup.get(element2)));
            result.add(new Value(id, valueLookup.get(id)));
        });
        result.sort((value1, value2) -> Double.compare(value2.getValue(), value1.getValue()));
        return result.stream().limit(nrOfSpots).collect(Collectors
                .toCollection(ArrayList::new));
    }

    public static void main(String[] args) {
//        long startTime = System.nanoTime();
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
        if (mesh == null || mesh.getElements().isEmpty()) {
            System.err.println("No input");
            return;
        }
//        long startTime2 = System.nanoTime();
        splitMeshIntoLocations();
        ArrayList<Value> result = getHighestViewPointsOfLocation(nrSpots);
//        long endTime2 = System.nanoTime() - startTime2;
//        double seconds2 = (double)endTime2 / 1_000_000_000.0;
//        System.out.println(seconds2);
        String json = new Gson().toJson(result); // not sure if this or the lower part is expected
        System.out.println(json);
//        System.out.println("[");
//        result.forEach(value -> System.out.println(value));
//        System.out.println("]");
//        long endTime = System.nanoTime() - startTime;
//        double seconds = (double) endTime / 1_000_000_000.0;
//        System.out.println(seconds);
    }
}
