package th.or.nectec.tanrabad.domain;

import java.util.List;

import th.or.nectec.tanrabad.entity.LocationEntity;

public interface DistanceSorter {

    List<LocationEntity> sort(List<LocationEntity> locationings);
}
