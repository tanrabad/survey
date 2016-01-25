package th.or.nectec.tanrabad.domain.address;

import th.or.nectec.tanrabad.entity.lookup.Province;

import java.util.List;


public interface ProvinceListPresenter {
    void displayProvinceList(List<Province> provinces);

    void alertProvinceNotFound();
}
