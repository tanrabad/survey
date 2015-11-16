package th.or.nectec.tanrabad.domain;

import th.or.nectec.tanrabad.entity.Building;


public class SurveyBuilding {
    Building building;
    boolean isSurvey;

    public SurveyBuilding(Building building, boolean isSurvey) {
        this.building = building;
        this.isSurvey = isSurvey;
    }

    @Override
    public String toString() {
        return "SurveyBuilding{" +
                "building=" + building +
                ", isSurvey=" + isSurvey +
                '}';
    }

    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

    public boolean isSurvey() {
        return isSurvey;
    }

    public void setIsSurvey(boolean isSurvey) {
        this.isSurvey = isSurvey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SurveyBuilding that = (SurveyBuilding) o;

        if (isSurvey != that.isSurvey) return false;
        return !(building != null ? !building.equals(that.building) : that.building != null);

    }

    @Override
    public int hashCode() {
        int result = building != null ? building.hashCode() : 0;
        result = 31 * result + (isSurvey ? 1 : 0);
        return result;
    }
}
