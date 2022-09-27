public class Value {
    private int element_id;
    private double value;

    public Value(Integer id, double value) {
        this.element_id = id;
        this.value = value;
    }

    public int getElement_id() {
        return element_id;
    }

    public double getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "{" +
                "element_id=" + element_id +
                ", value=" + value +
                '}';
    }
}
