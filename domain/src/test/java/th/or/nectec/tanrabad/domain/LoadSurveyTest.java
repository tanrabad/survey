package th.or.nectec.tanrabad.domain;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import th.or.nectec.tanrabad.entity.Building;
import th.or.nectec.tanrabad.entity.Survey;
import th.or.nectec.tanrabad.entity.User;

public class LoadSurveyTest {
    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();
    private SurveyRepository surveyRepository;
    private SurveyPresenter surveyPresenter;
    private Building building;
    private User user;

    @Before
    public void setUp() throws Exception {
        surveyRepository = context.mock(SurveyRepository.class);
        surveyPresenter = context.mock(SurveyPresenter.class);

        building = Building.withName("33/5");
        user = User.fromUsername("chncs23");
    }

    @Test
    public void testLoadSurveySuccess() throws Exception {

        final Survey surveys = new Survey(user, building);

        context.checking(new Expectations() {
            {
                allowing(surveyRepository).findByBuildingAndUser(building, user);
                will(returnValue(surveys));
                oneOf(surveyPresenter).loadSurveySuccess(with(surveys));
            }
        });

        SurveyController surveyController = new SurveyController(surveyRepository, surveyPresenter);
        surveyController.findSurveyByBuildingAndUser(building, user);
    }

    @Test
    public void testLoadSurveyFail() throws Exception {
        context.checking(new Expectations() {
            {
                allowing(surveyRepository).findByBuildingAndUser(building, user);
                will(returnValue(null));
                oneOf(surveyPresenter).startNewSurvey(building, user);
            }
        });

        SurveyController surveyController = new SurveyController(surveyRepository, surveyPresenter);
        surveyController.findSurveyByBuildingAndUser(building, user);
    }
}
