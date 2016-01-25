package th.or.nectec.tanrabad.domain.address;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import th.or.nectec.tanrabad.entity.lookup.Province;

import java.util.ArrayList;
import java.util.List;

public class ProvinceChooserTest {

    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();
    private ProvinceRepository repository;
    private ProvinceListPresenter presenter;

    @Before
    public void setup() {
        repository = context.mock(ProvinceRepository.class);
        presenter = context.mock(ProvinceListPresenter.class);
    }

    @Test
    public void testFoundDistrictList() throws Exception {
        context.checking(new Expectations() {
            {
                List<Province> provinces = new ArrayList<>();
                Province province = new Province();
                province.setCode("63");
                province.setName("ตาก");
                provinces.add(province);

                allowing(repository).find();
                will(returnValue(provinces));

                oneOf(presenter).displayProvinceList(provinces);
            }
        });

        ProvinceChooser districtChooser = new ProvinceChooser(repository, presenter);
        districtChooser.find();
    }

    @Test
    public void testProvinceListNotfound() throws Exception {
        context.checking(new Expectations() {
            {
                allowing(repository).find();
                will(returnValue(null));

                oneOf(presenter).alertProvinceNotFound();
            }
        });

        ProvinceChooser districtChooser = new ProvinceChooser(repository, presenter);
        districtChooser.find();
    }
}
