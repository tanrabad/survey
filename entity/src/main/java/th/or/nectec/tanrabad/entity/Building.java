package th.or.nectec.tanrabad.entity;

import java.util.UUID;

public class Building {

    private UUID id;
    private String name;
    private Place place;

    public Building(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public static Building withName(String name) {
        UUID uuid = UUID.randomUUID();
        return new Building(uuid, name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Building{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Building)) return false;

        Building building = (Building) o;

        return id.equals(building.id) && name.equals(building.name);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }
}
