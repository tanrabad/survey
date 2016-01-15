package th.or.nectec.tanrabad.survey.repository;

import th.or.nectec.tanrabad.domain.address.DistrictRepository;
import th.or.nectec.tanrabad.domain.address.DistrictRepositoryException;
import th.or.nectec.tanrabad.entity.District;

import java.util.ArrayList;
import java.util.List;

public class InMemoryDistrictRepository implements DistrictRepository {

    private static InMemoryDistrictRepository instance;
    ArrayList<District> districts = new ArrayList<>();

    public static InMemoryDistrictRepository getInstance() {
        if (instance == null) {
            instance = new InMemoryDistrictRepository();
        }
        return instance;
    }

    @Override
    public List<District> findByProvinceCode(String provinceCode) {
        ArrayList<District> districtsInsideProvince = new ArrayList<>();
        for (District eachDistrict : districts) {
            if (eachDistrict.getProvinceCode().equals(provinceCode))
                districtsInsideProvince.add(eachDistrict);
        }
        return districtsInsideProvince;
    }

    @Override
    public District findByCode(String districtCode) {
        for (District eachDistrict : districts) {
            if (eachDistrict.getCode().equals(districtCode))
                return eachDistrict;
        }
        return null;
    }

    @Override
    public boolean save(District district) {
        if (districts.contains(district))
            throw new DistrictRepositoryException();
        districts.add(district);
        return true;
    }

    @Override
    public boolean update(District district) {
        if (!districts.contains(district))
            throw new DistrictRepositoryException();
        districts.set(districts.indexOf(district), district);
        return true;
    }

    @Override
    public void updateOrInsert(List<District> districts) {
        for (District district : districts) {
            try {
                update(district);
            } catch (DistrictRepositoryException pre) {
                save(district);
            }
        }
    }
}
