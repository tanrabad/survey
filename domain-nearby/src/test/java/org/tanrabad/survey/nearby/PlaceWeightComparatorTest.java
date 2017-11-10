package org.tanrabad.survey.nearby;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.junit.Test;
import org.tanrabad.survey.entity.Place;

import static org.junit.Assert.assertEquals;

public class PlaceWeightComparatorTest {

    private final Place โรงเรียนประถมศึกษาธรรมศาสตร์ =
        new Place(UUID.randomUUID(), "โรงเรียนประถมศึกษาธรรมศาสตร์");
    private final Place มหาวิทยาลัยกรุุงเทพ =
        new Place(UUID.randomUUID(), "มหาวิทยาลัยกรุุงเทพ (วิทยาเขตรังสิต)");
    private final Place โรงเรียนบางปะอิน =
        new Place(UUID.randomUUID(), "โรงเรียนบางปะอิน ราชานุเคราะห์ ๑");
    private final Place วัดโพธิ์นิ่ม =
        new Place(UUID.randomUUID(), "วัดโพธิ์นิ่ม");
    private final Place วัดนิเวศธรรมประวัติ =
        new Place(UUID.randomUUID(), "วัดนิเวศธรรมประวัติ");
    private final Place โรงเรียนศาลาพัน =
        new Place(UUID.randomUUID(), "โรงเรียนศาลาพัน");

    @Test public void compare() throws Exception {
        โรงเรียนประถมศึกษาธรรมศาสตร์.setWeight(0.4);
        มหาวิทยาลัยกรุุงเทพ.setWeight(0.5);
        โรงเรียนบางปะอิน.setWeight(0.8);
        วัดโพธิ์นิ่ม.setWeight(0.2);
        วัดนิเวศธรรมประวัติ.setWeight(0.1);
        โรงเรียนศาลาพัน.setWeight(0.9);

        List<Place> places = new ArrayList<>();
        places.add(โรงเรียนประถมศึกษาธรรมศาสตร์);
        places.add(มหาวิทยาลัยกรุุงเทพ);
        places.add(โรงเรียนบางปะอิน);
        places.add(วัดโพธิ์นิ่ม);
        places.add(วัดนิเวศธรรมประวัติ);
        places.add(โรงเรียนศาลาพัน);

        List<Place> sortedPlaces = new ArrayList<>();
        sortedPlaces.add(โรงเรียนศาลาพัน);
        sortedPlaces.add(โรงเรียนบางปะอิน);
        sortedPlaces.add(มหาวิทยาลัยกรุุงเทพ);
        sortedPlaces.add(โรงเรียนประถมศึกษาธรรมศาสตร์);
        sortedPlaces.add(วัดโพธิ์นิ่ม);
        sortedPlaces.add(วัดนิเวศธรรมประวัติ);

        Collections.sort(places, new PlaceWeightComparator());
        assertEquals(sortedPlaces, places);
    }
}
