package org.tanrabad.survey.nearby;

import org.junit.Test;
import org.tanrabad.survey.domain.place.PlaceRepository;
import org.tanrabad.survey.entity.Place;
import org.tanrabad.survey.entity.field.Location;
import org.tanrabad.survey.nearby.repository.ImpNearbyPlaceRepository;
import org.tanrabad.survey.nearby.repository.NearbyPlaceRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class NearbyPlaceTest {


    @Test public void testFindNearByPlaces() {

        Place tuStadium = new Place(UUID.nameUUIDFromBytes("1".getBytes()), "สนามกีฬาธรรมศาสตร์");
        tuStadium.setLocation(new Location(14.067960, 100.598828));  //1.2กม.
        tuStadium.setSubdistrictCode("130201");
        Place tuHospital =
                new Place(UUID.nameUUIDFromBytes("2".getBytes()), "โรงพยาบาลธรรมศาสตร์เฉลิมพระเกียรติ");
        tuHospital.setLocation(new Location(14.074163, 100.614696)); //1.4กม.
        tuHospital.setSubdistrictCode("130201");
        Place navanakhon = new Place(UUID.nameUUIDFromBytes("3".getBytes()), "นิคมอุตสาหกรรมนวนคร");
        navanakhon.setLocation(new Location(14.1119521, 100.6020252)); //4กม.
        navanakhon.setSubdistrictCode("130201");
        Place vru = new Place(UUID.nameUUIDFromBytes("4".getBytes()), "มหาวิทยาลัยราชภัฏวไลยอลงกรณ์");
        vru.setLocation(new Location(14.1280585, 100.6048577)); // 6กม.
        vru.setSubdistrictCode("130201");
        Place chiangrakStation = new Place(UUID.nameUUIDFromBytes("5".getBytes()), "สถานีรถไฟเชียงราก");
        chiangrakStation.setLocation(new Location(14.0490811, 100.6012871)); //2กม.
        chiangrakStation.setSubdistrictCode("130201");
        Place bangpainPalace = new Place(UUID.nameUUIDFromBytes("11".getBytes()), "พระราชวังบางปะอิน");
        bangpainPalace.setLocation(new Location(14.2278557, 100.5768217)); // 6กม.
        bangpainPalace.setSubdistrictCode("140601");
        Place tuPrimarySchool =
                new Place(UUID.nameUUIDFromBytes("6".getBytes()), "โรงเรียนประถมศึกษาธรรมศาสตร์");
        tuPrimarySchool.setSubdistrictCode("130201");
        Place chiangraknoiStation = new Place(UUID.nameUUIDFromBytes("7".getBytes()), "สถานีรถไฟเชียงรากน้อย");
        chiangraknoiStation.setSubdistrictCode("140602");
        Place thaithaniDorm = new Place(UUID.nameUUIDFromBytes("8".getBytes()), "หอพักไทยธานี");
        thaithaniDorm.setSubdistrictCode("130201");
        Place pratunamPhraInPoliceStation =
                new Place(UUID.nameUUIDFromBytes("9".getBytes()), "สภ.ประตูน้ำพระอินทร์");
        pratunamPhraInPoliceStation.setSubdistrictCode("140602");
        Place tuKindergarten = new Place(UUID.nameUUIDFromBytes("10".getBytes()), "โรงเรียนอนุบาลธรรมศาสตร์");
        tuKindergarten.setSubdistrictCode("130201");
        Place taladThai = new Place(UUID.nameUUIDFromBytes("13".getBytes()), "ตลาดไท");
        taladThai.setSubdistrictCode("130201");
        Place racha1School =
                new Place(UUID.nameUUIDFromBytes("12".getBytes()), "โรงเรียนบางปะอิน \"ราชานุเคราะห์ ๑\"");
        racha1School.setSubdistrictCode("140601");
        Place tuPostOffice =
                new Place(UUID.nameUUIDFromBytes("14".getBytes()), "ไปรษณีย์มหาวิทยาลัยธรรมศาสตร์ ศูนย์รังสิต");
        tuPostOffice.setSubdistrictCode("130201");
        Place tuCanteen =
                new Place(UUID.nameUUIDFromBytes("15".getBytes()), "โรงอาหารกลางธรรมศาสตร์");
        tuCanteen.setSubdistrictCode("130201");
        Place medicalTu =
                new Place(UUID.nameUUIDFromBytes("16".getBytes()), "คณะแพทยศาสตร์ มหาวิทยาลัยธรรมศาสตร์");
        medicalTu.setSubdistrictCode("130201");

        List<Place> allPlaces = new ArrayList<>();
        allPlaces.add(tuStadium);
        allPlaces.add(tuHospital);
        allPlaces.add(navanakhon);
        allPlaces.add(vru);
        allPlaces.add(chiangrakStation);
        allPlaces.add(chiangraknoiStation);
        allPlaces.add(thaithaniDorm);
        allPlaces.add(pratunamPhraInPoliceStation);
        allPlaces.add(tuPrimarySchool);
        allPlaces.add(tuKindergarten);
        allPlaces.add(racha1School);
        allPlaces.add(bangpainPalace);
        allPlaces.add(taladThai);
        allPlaces.add(tuPostOffice);
        allPlaces.add(tuCanteen);
        allPlaces.add(medicalTu);

        final List<Place> nearbyPlaces = new ArrayList<>();
        nearbyPlaces.add(tuStadium);
        nearbyPlaces.add(tuHospital);
        nearbyPlaces.add(chiangrakStation);
        nearbyPlaces.add(navanakhon);
        nearbyPlaces.add(tuCanteen);
        nearbyPlaces.add(tuPostOffice);
        nearbyPlaces.add(medicalTu);
        nearbyPlaces.add(tuPrimarySchool);
        nearbyPlaces.add(tuKindergarten);
        nearbyPlaces.add(thaithaniDorm);
        nearbyPlaces.add(taladThai);

        PlaceRepository repository = mock(PlaceRepository.class);
        when(repository.find()).thenReturn(allPlaces);

        NearbyPlaceRepository nearbyPlaceRepository =
            new ImpNearbyPlaceRepository(repository, 5);

        MergeAndSortNearbyPlaces mergeAndSortNearbyPlaces = new ImpMergeAndSortNearbyPlaces();

        NearbyPlacePresenter presenter = mock(NearbyPlacePresenter.class);

        NearbyPlacesFinderController nearbyPlacesFinderController =
            new NearbyPlacesFinderController(nearbyPlaceRepository, mergeAndSortNearbyPlaces, presenter);
        nearbyPlacesFinderController.findNearbyPlaces(new Location(14.077756, 100.601380));

        verify(presenter, times(2)).displayNearbyPlaces(any(List.class));
    }
}
