package org.tanrabad.survey.nearby;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.Before;
import org.junit.Test;
import org.tanrabad.survey.entity.Place;
import org.tanrabad.survey.entity.field.Location;
import org.tanrabad.survey.nearby.ImpMergeAndSortNearbyPlaces;

import static org.junit.Assert.assertEquals;

public class ImpMergeAndSortNearbyPlacesTest {

    private Place tuKindergarten;
    private Place bangkokUniversity;
    private Place racha1School;
    private Place watPhonim;
    private Place watNivetdhammaprawat;
    private Place salapanSchool;
    private Place tuHospital;
    private Place bangPaInSchool;
    private Place navanakhon;
    private List<Place> placeWithoutLocation;
    private List<Place> placeWithLocation;

    @Before public void setup() {
        tuKindergarten = new Place(UUID.nameUUIDFromBytes("3".getBytes()), "โรงเรียนประถมศึกษาธรรมศาสตร์");
        tuKindergarten.setSubdistrictCode("130102");
        tuKindergarten.setWeight(0.4);

        bangkokUniversity = new Place(UUID.nameUUIDFromBytes("4".getBytes()), "มหาวิทยาลัยกรุุงเทพ (วิทยาเขตรังสิต)");
        bangkokUniversity.setSubdistrictCode("130102");
        bangkokUniversity.setWeight(0.5);

        racha1School = new Place(UUID.nameUUIDFromBytes("4".getBytes()), "โรงเรียนบางปะอิน \"ราชานุเคราะห์ ๑\"");
        racha1School.setSubdistrictCode("140601");
        racha1School.setWeight(0.8);

        watPhonim = new Place(UUID.nameUUIDFromBytes("4".getBytes()), "วัดโพธิ์นิ่ม");
        watPhonim.setSubdistrictCode("130709");
        watPhonim.setWeight(0.2);

        watNivetdhammaprawat = new Place(UUID.nameUUIDFromBytes("4".getBytes()), "วัดนิเวศธรรมประวัติ");
        watNivetdhammaprawat.setSubdistrictCode("140601");
        watNivetdhammaprawat.setWeight(0.1);

        salapanSchool = new Place(UUID.nameUUIDFromBytes("4".getBytes()), "โรงเรียนศาลาพัน");
        salapanSchool.setSubdistrictCode("130709");
        salapanSchool.setWeight(0.9);

        tuHospital = new Place(UUID.nameUUIDFromBytes("4".getBytes()), "โรงพยาบาลธรรมศาสตร์");
        tuHospital.setSubdistrictCode("130102");
        tuHospital.setLocation(new Location(5, 10));

        bangPaInSchool = new Place(UUID.nameUUIDFromBytes("4".getBytes()), "โรงเรียนบางปะอิน");
        bangPaInSchool.setSubdistrictCode("140601");
        bangPaInSchool.setLocation(new Location(11, 12));

        navanakhon = new Place(UUID.nameUUIDFromBytes("4".getBytes()), "นิคมฯนวนคร");
        navanakhon.setSubdistrictCode("130709");
        navanakhon.setLocation(new Location(11, 12));

        placeWithoutLocation = new ArrayList<>();
        placeWithoutLocation.add(tuKindergarten);
        placeWithoutLocation.add(bangkokUniversity);
        placeWithoutLocation.add(racha1School);
        placeWithoutLocation.add(watPhonim);
        placeWithoutLocation.add(watNivetdhammaprawat);
        placeWithoutLocation.add(salapanSchool);

        placeWithLocation = new ArrayList<>();
        placeWithLocation.add(tuHospital);
        placeWithLocation.add(navanakhon);
        placeWithLocation.add(bangPaInSchool);
    }

    @Test public void mergeAndSortTest() throws Exception {
        List<Place> mergeAndSortPlaces = new ArrayList<>();
        mergeAndSortPlaces.add(tuHospital);
        mergeAndSortPlaces.add(navanakhon);
        mergeAndSortPlaces.add(bangPaInSchool);
        mergeAndSortPlaces.add(salapanSchool);
        mergeAndSortPlaces.add(racha1School);
        mergeAndSortPlaces.add(bangkokUniversity);
        mergeAndSortPlaces.add(tuKindergarten);
        mergeAndSortPlaces.add(watPhonim);
        mergeAndSortPlaces.add(watNivetdhammaprawat);

        ImpMergeAndSortNearbyPlaces mergeAndSortNearbyPlaces = new ImpMergeAndSortNearbyPlaces();
        assertEquals(mergeAndSortPlaces,
                mergeAndSortNearbyPlaces.mergeAndSort(placeWithLocation, placeWithoutLocation));
    }
}
