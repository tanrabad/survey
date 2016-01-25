package th.or.nectec.tanrabad.domain.address;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import th.or.nectec.tanrabad.entity.lookup.Subdistrict;

import java.util.ArrayList;
import java.util.List;

public class SubdistrictChooserTest {

    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();
    private SubdistrictRepository repository;
    private SubdistrictListPresenter presenter;

    @Before
    public void setup() {
        repository = context.mock(SubdistrictRepository.class);
        presenter = context.mock(SubdistrictListPresenter.class);
    }

    @Test
    public void testFoundDistrictList() throws Exception {
        context.checking(new Expectations() {
            {
                List<Subdistrict> subdistricts = new ArrayList<>();
                Subdistrict subdistrict = new Subdistrict();
                subdistrict.setCode("840212");
                subdistrict.setName("ทุ่งรัง");
                subdistrict.setDistrictCode("8402");
                subdistricts.add(subdistrict);

                allowing(repository).findByDistrictCode("8402");
                will(returnValue(subdistricts));

                oneOf(presenter).displaySubdistrictList(subdistricts);
            }
        });

        SubdistrictChooser subdistrictChooser = new SubdistrictChooser(repository, presenter);
        subdistrictChooser.findByDistrictCode("8402");
    }

    @Test
    public void testDistrictListNotfound() throws Exception {
        context.checking(new Expectations() {
            {
                allowing(repository).findByDistrictCode("8403");
                will(returnValue(null));

                oneOf(presenter).alertSubdistrictNotFound();
            }
        });

        SubdistrictChooser subdistrictChooser = new SubdistrictChooser(repository, presenter);
        subdistrictChooser.findByDistrictCode("8403");
    }
}
