package th.or.nectec.tanlabad.domain;

import th.or.nectec.tanrabad.entity.Building;

import java.util.ArrayList;
import java.util.UUID;

public interface BuildingRepository {
    ArrayList<Building> findBuildingInPlace(UUID with);
}
