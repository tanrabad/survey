package th.or.nectec.tanrabad.domain.address;

import th.or.nectec.tanrabad.entity.Subdistrict;

import java.util.List;


public interface SubdistrictListPresenter {
    void displaySubdistrictList(List<Subdistrict> subdistricts);

    void alertSubdistrictNotFound();
}
