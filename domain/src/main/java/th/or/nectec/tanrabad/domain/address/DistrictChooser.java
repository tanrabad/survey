package th.or.nectec.tanrabad.domain.address;

import th.or.nectec.tanrabad.entity.District;

import java.util.List;

public class DistrictChooser {
    private DistrictRepository districtRepository;
    private DistrictListPresenter districtListPresenter;

    public DistrictChooser(DistrictRepository districtRepository, DistrictListPresenter districtListPresenter) {
        this.districtRepository = districtRepository;
        this.districtListPresenter = districtListPresenter;
    }

    public void findByProvinceCode(String provinceCode) {
        List<District> districtList = districtRepository.findByProvinceCode(provinceCode);
        if (districtList != null && !districtList.isEmpty()) {
            districtListPresenter.displayDistrictList(districtList);
        } else {
            districtListPresenter.alertDistrictNotFound();
        }
    }
}
