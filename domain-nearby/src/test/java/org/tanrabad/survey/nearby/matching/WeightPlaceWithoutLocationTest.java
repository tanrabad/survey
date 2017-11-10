package org.tanrabad.survey.nearby.matching;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.Test;
import org.tanrabad.survey.entity.Place;
import org.tanrabad.survey.entity.field.Location;
import org.tanrabad.survey.nearby.matching.WeightPlaceWithoutLocation;

import static org.junit.Assert.assertEquals;

public class WeightPlaceWithoutLocationTest {
    @Test public void calculateWeightedScoreOfPlaceWithoutLocation() throws Exception {
        Place tuHospital = new Place(UUID.nameUUIDFromBytes("1".getBytes()), "โรงพยาบาลธรรมศาสตร์เฉลิมพระเกียรติ");
        tuHospital.setLocation(new Location(5, 5));
        Place tuRangsit = new Place(UUID.nameUUIDFromBytes("2".getBytes()), "มหาวิทยาลัยธรรมศาสตร์ (ศูนย์รังสิต)");
        tuHospital.setLocation(new Location(4, 4));

        List<Place> placeWithLocation = new ArrayList<>();
        placeWithLocation.add(tuHospital);
        placeWithLocation.add(tuRangsit);

        Place tuKindergarten = new Place(UUID.nameUUIDFromBytes("3".getBytes()), "โรงเรียนประถมศึกษาธรรมศาสตร์");
        Place bangkokUniversity =
                new Place(UUID.nameUUIDFromBytes("4".getBytes()), "มหาวิทยาลัยกรุุงเทพ (วิทยาเขตรังสิต");

        List<Place> placeWithoutLocation = new ArrayList<>();
        placeWithoutLocation.add(tuKindergarten);
        placeWithoutLocation.add(bangkokUniversity);

        Map<UUID, Double> weightPlaceWithoutLocation = new HashMap<>();
        weightPlaceWithoutLocation.put(UUID.nameUUIDFromBytes("3".getBytes()), 0.4380952380952381);
        weightPlaceWithoutLocation.put(UUID.nameUUIDFromBytes("4".getBytes()), 0.3342857142857143);

        assertEquals(weightPlaceWithoutLocation,
                WeightPlaceWithoutLocation.calculate(placeWithLocation, placeWithoutLocation));
    }
}
