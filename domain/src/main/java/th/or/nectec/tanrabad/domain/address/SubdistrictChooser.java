package th.or.nectec.tanrabad.domain.address;

import th.or.nectec.tanrabad.entity.Subdistrict;

import java.util.List;

public class SubdistrictChooser {
    private SubdistrictRepository subdistrictRepository;
    private SubdistrictListPresenter subdistrictListPresenter;

    public SubdistrictChooser(SubdistrictRepository subdistrictRepository, SubdistrictListPresenter subdistrictListPresenter) {
        this.subdistrictRepository = subdistrictRepository;
        this.subdistrictListPresenter = subdistrictListPresenter;
    }

    public void findByDistrictCode(String districtCode) {
        List<Subdistrict> subdistrictList = subdistrictRepository.findByDistrictCode(districtCode);
        if (subdistrictList != null && !subdistrictList.isEmpty()) {
            subdistrictListPresenter.displaySubdistrictList(subdistrictList);
        } else {
            subdistrictListPresenter.alertSubdistrictNotFound();
        }
    }
}
