package th.or.nectec.tanrabad.domain;

import java.util.List;

import th.or.nectec.tanrabad.entity.Locationing;

public interface DistanceSorter {

    List<Locationing> sort(List<Locationing> locationings);
}
