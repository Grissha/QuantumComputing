public class Qubit {
    private double x;
    private double y;

    public Qubit(double z) {
        double n = Math.sqrt(1 - Math.pow(z, 2));
        x = n;
        y = z;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
