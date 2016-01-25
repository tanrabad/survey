package th.or.nectec.tanrabad.survey.repository;

import th.or.nectec.tanrabad.domain.address.SubdistrictRepository;
import th.or.nectec.tanrabad.domain.address.SubdistrictRepositoryException;
import th.or.nectec.tanrabad.entity.lookup.Subdistrict;

import java.util.ArrayList;
import java.util.List;

public class InMemorySubdistrictRepository implements SubdistrictRepository {

    private static InMemorySubdistrictRepository instance;
    ArrayList<Subdistrict> subdistricts = new ArrayList<>();

    public static InMemorySubdistrictRepository getInstance() {
        if (instance == null) {
            instance = new InMemorySubdistrictRepository();
        }
        return instance;
    }

    @Override
    public List<Subdistrict> findByDistrictCode(String districtCode) {
        ArrayList<Subdistrict> subdistrictsInsideDistrict = new ArrayList<>();
        for (Subdistrict eachSubdistrict : subdistricts) {
            if (eachSubdistrict.getDistrictCode().equals(districtCode))
                subdistrictsInsideDistrict.add(eachSubdistrict);
        }
        return subdistrictsInsideDistrict;
    }

    @Override
    public Subdistrict findByCode(String subdistrictCode) {
        for (Subdistrict eachSubdistrict : subdistricts) {
            if (eachSubdistrict.getCode().equals(subdistrictCode))
                return eachSubdistrict;
        }
        return null;
    }

    @Override
    public boolean save(Subdistrict subdistrict) {
        if (subdistricts.contains(subdistrict))
            throw new SubdistrictRepositoryException();
        subdistricts.add(subdistrict);
        return true;
    }

    @Override
    public boolean update(Subdistrict subdistrict) {
        if (!subdistricts.contains(subdistrict))
            throw new SubdistrictRepositoryException();
        subdistricts.set(subdistricts.indexOf(subdistrict), subdistrict);
        return true;
    }

    @Override
    public void updateOrInsert(List<Subdistrict> subdistricts) {
        for (Subdistrict subdistrict : subdistricts) {
            try {
                update(subdistrict);
            } catch (SubdistrictRepositoryException pre) {
                save(subdistrict);
            }
        }
    }
}
