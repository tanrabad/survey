package th.or.nectec.tanlabad.domain;

import th.or.nectec.tanrabad.Building;

import java.util.ArrayList;

public interface BuildingRepository {
    ArrayList<Building> findBuildingInPlace(int with);
}
