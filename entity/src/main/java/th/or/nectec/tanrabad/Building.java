package th.or.nectec.tanrabad;

public class Building {

    private int id;
    private String name;

    public Building(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static Building withIdAndName(int id, String name){
        return new Building(id, name);
    }

    @Override
    public String toString() {
        return "id"+ this.id;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Building)) return false;

        Building building = (Building) o;

        if (id != building.id) return false;
        return !(name != null ? !name.equals(building.name) : building.name != null);

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
