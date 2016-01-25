package th.or.nectec.tanrabad.survey.repository;

import th.or.nectec.tanrabad.domain.address.ProvinceRepository;
import th.or.nectec.tanrabad.domain.address.ProvinceRepositoryException;
import th.or.nectec.tanrabad.entity.lookup.Province;

import java.util.ArrayList;
import java.util.List;

public class InMemoryProvinceRepository implements ProvinceRepository {

    private static InMemoryProvinceRepository instance;
    List<Province> provinces = new ArrayList<>();

    public static InMemoryProvinceRepository getInstance() {
        if (instance == null) {
            instance = new InMemoryProvinceRepository();
        }
        return instance;
    }

    @Override
    public List<Province> find() {
        return provinces;
    }

    @Override
    public Province findByCode(String provinceCode) {
        for (Province eachProvince : provinces) {
            if (eachProvince.getCode().equals(provinceCode))
                return eachProvince;
        }
        return null;
    }

    @Override
    public boolean save(Province province) {
        if (provinces.contains(province))
            throw new ProvinceRepositoryException();
        provinces.add(province);
        return true;
    }

    @Override
    public boolean update(Province province) {
        if (!provinces.contains(province))
            throw new ProvinceRepositoryException();
        provinces.set(provinces.indexOf(province), province);
        return true;
    }

    @Override
    public void updateOrInsert(List<Province> provinces) {
        for (Province province : provinces) {
            try {
                update(province);
            } catch (ProvinceRepositoryException pre) {
                save(province);
            }
        }
    }
}
