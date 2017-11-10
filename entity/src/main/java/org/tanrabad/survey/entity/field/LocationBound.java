package org.tanrabad.survey.entity.field;

public class LocationBound {
    private Location minimumLocation;
    private Location maximumLocation;

    public LocationBound(Location minimumLocation, Location maximumLocation) {
        this.minimumLocation = minimumLocation;
        this.maximumLocation = maximumLocation;
    }

    public Location getMinimumLocation() {
        return minimumLocation;
    }

    public Location getMaximumLocation() {
        return maximumLocation;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LocationBound that = (LocationBound) o;

        if (!minimumLocation.equals(that.minimumLocation)) return false;
        return maximumLocation.equals(that.maximumLocation);
    }

    @Override public int hashCode() {
        int result = minimumLocation.hashCode();
        result = 31 * result + maximumLocation.hashCode();
        return result;
    }
}
