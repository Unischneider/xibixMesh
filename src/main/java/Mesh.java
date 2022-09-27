import java.util.ArrayList;

public class Mesh {

    private ArrayList<Node> nodes;
    private ArrayList<Element> elements;
    private ArrayList<Value> values;

    public Mesh(ArrayList<Element> elements) {
        this.elements = elements;
    }

    public Mesh() {
    }

    public ArrayList<Node> getNodes() {
        return nodes;
    }

    public ArrayList<Element> getElements() {
        return elements;
    }

    public ArrayList<Value> getValues() {
        return values;
    }

    public void setValues(ArrayList<Value> values) {
        this.values = values;
    }

    public int findHighestViewSpots(){
//        for(int i = 0; i < nodes.length; i++){
//            Element[] neighboringElements = containNode();
//        }
        return 0;
    }

    private void sortViewSpotsDescending(){
        values.sort((element1, element2 ) -> Double.compare(element1.getValue(), element2.getValue()));
    }


    private boolean isNeighbour(int node1, int node2){

        return false;
    }

}
