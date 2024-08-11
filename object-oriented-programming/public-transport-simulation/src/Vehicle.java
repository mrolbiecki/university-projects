public class Vehicle {
    private final int sideNumber;
    private final Line line;

    public Vehicle(int sideNumber, Line line) {
        this.sideNumber = sideNumber;
        this.line = line;
    }

    public int getSideNumber() {
        return this.sideNumber;
    }

    public Line getLine () {
        return this.line;
    }
}
