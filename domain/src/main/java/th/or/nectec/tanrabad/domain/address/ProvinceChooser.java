package th.or.nectec.tanrabad.domain.address;

import th.or.nectec.tanrabad.entity.Province;

import java.util.List;

public class ProvinceChooser {
    private ProvinceRepository provinceRepository;
    private ProvinceListPresenter provinceListPresenter;

    public ProvinceChooser(ProvinceRepository provinceRepository, ProvinceListPresenter provinceListPresenter) {
        this.provinceRepository = provinceRepository;
        this.provinceListPresenter = provinceListPresenter;
    }

    public void find() {
        List<Province> provinceList = provinceRepository.find();
        if (provinceList != null && !provinceList.isEmpty()) {
            provinceListPresenter.displayProvinceList(provinceList);
        } else {
            provinceListPresenter.alertProvinceNotFound();
        }
    }
}
