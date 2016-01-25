package th.or.nectec.tanrabad.domain.address;

import th.or.nectec.tanrabad.entity.lookup.District;

import java.util.List;


public interface DistrictListPresenter {
    void displayDistrictList(List<District> districts);

    void alertDistrictNotFound();
}
