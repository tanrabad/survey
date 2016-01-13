package th.or.nectec.tanrabad.entity;

import java.util.Arrays;
import java.util.List;

public class Polygon {
    private List<Location> boundary;
    private List<Location>[] holes;

    public Polygon(List<Location> boundary, List<Location>[] holes) {
        this.boundary = boundary;
        this.holes = holes;

    }

    @Override
    public int hashCode() {
        int result = boundary.hashCode();
        result = 31 * result + Arrays.hashCode(holes);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Polygon polygon = (Polygon) o;

        if (!boundary.equals(polygon.boundary)) return false;
        return Arrays.equals(holes, polygon.holes);
    }

    @Override
    public String toString() {
        return "Polygon{" +
                "boundary=" + boundary +
                ", holes=" + Arrays.toString(holes) +
                '}';
    }

    public List<Location> getBoundary() {
        return boundary;
    }

    public List<Location> getHole(int holeIndex) {
        return holes[holeIndex];
    }

    public List<Location>[] getAllHoles() {
        return holes;
    }

    public int getHolesCount() {
        return holes.length;
    }
}
