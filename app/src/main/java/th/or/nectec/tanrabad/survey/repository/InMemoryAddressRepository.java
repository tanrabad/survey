package th.or.nectec.tanrabad.survey.repository;

import th.or.nectec.tanrabad.domain.address.AddressRepository;
import th.or.nectec.tanrabad.domain.address.DistrictRepository;
import th.or.nectec.tanrabad.domain.address.ProvinceRepository;
import th.or.nectec.tanrabad.domain.address.SubdistrictRepository;
import th.or.nectec.tanrabad.entity.utils.Address;

public class InMemoryAddressRepository implements AddressRepository {

    private static InMemoryAddressRepository instance;
    private static SubdistrictRepository subdistrictRepository;
    private static DistrictRepository districtRepository;
    private static ProvinceRepository provinceRepository;

    public static InMemoryAddressRepository getInstance() {
        return getInstance(InMemorySubdistrictRepository.getInstance(),
                InMemoryDistrictRepository.getInstance(),
                InMemoryProvinceRepository.getInstance());
    }

    public static InMemoryAddressRepository getInstance(SubdistrictRepository subdistrictRepository,
                                                        DistrictRepository districtRepository,
                                                        ProvinceRepository provinceRepository) {
        InMemoryAddressRepository.subdistrictRepository = subdistrictRepository;
        InMemoryAddressRepository.districtRepository = districtRepository;
        InMemoryAddressRepository.provinceRepository = provinceRepository;
        if (instance == null) {
            instance = new InMemoryAddressRepository();
        }
        return instance;
    }

    @Override
    public Address findBySubdistrictCode(String subdistrictCode) {
        Address address = new Address();
        address.setAddressCode(subdistrictCode);
        address.setSubdistrict(subdistrictRepository.findByCode(subdistrictCode).getName());
        address.setDistrict(districtRepository.findByCode(subdistrictCode.substring(0, 4)).getName());
        address.setProvince(provinceRepository.findByCode(subdistrictCode.substring(0, 2)).getName());
        return address;
    }
}
