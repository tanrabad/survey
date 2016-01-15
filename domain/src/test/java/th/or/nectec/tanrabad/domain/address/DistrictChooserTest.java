package th.or.nectec.tanrabad.domain.address;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import th.or.nectec.tanrabad.entity.District;

import java.util.ArrayList;
import java.util.List;

public class DistrictChooserTest {

    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();
    private DistrictRepository repository;
    private DistrictListPresenter presenter;

    @Before
    public void setup() {
        repository = context.mock(DistrictRepository.class);
        presenter = context.mock(DistrictListPresenter.class);
    }

    @Test
    public void testFoundDistrictList() throws Exception {
        context.checking(new Expectations() {
            {
                List<District> districts = new ArrayList<>();
                District district = new District();
                district.setCode("6308");
                district.setName("อุ้มผาง");
                district.setProvinceCode("63");
                districts.add(district);

                allowing(repository).findByProvinceCode("63");
                will(returnValue(districts));

                oneOf(presenter).displayDistrictList(districts);
            }
        });

        DistrictChooser districtChooser = new DistrictChooser(repository, presenter);
        districtChooser.findByProvinceCode("63");
    }

    @Test
    public void testDistrictListNotfound() throws Exception {
        context.checking(new Expectations() {
            {
                allowing(repository).findByProvinceCode("21");
                will(returnValue(null));

                oneOf(presenter).alertDistrictNotFound();
            }
        });

        DistrictChooser districtChooser = new DistrictChooser(repository, presenter);
        districtChooser.findByProvinceCode("21");
    }
}
