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
                allowing(surveyRepository).findByBuildingAndUserIn7Day(with(building), with(user));
                will(returnValue(surveys));
                oneOf(surveyPresenter).onEditSurvey(with(surveys));
            }
        });

        SurveyController surveyController = new SurveyController(surveyRepository, surveyPresenter);
        surveyController.checkThisBuildingAndUserCanSurvey(building, user);
    }

    @Test
    public void testStartNewSurvey() throws Exception {
        context.checking(new Expectations() {
            {
                allowing(surveyRepository).findByBuildingAndUserIn7Day(with(building), with(user));
                will(returnValue(null));
                oneOf(surveyPresenter).onNewSurvey(with(building), with(user));
            }
        });

        SurveyController surveyController = new SurveyController(surveyRepository, surveyPresenter);
        surveyController.checkThisBuildingAndUserCanSurvey(building, user);
    }

    @Test
    public void testNotFoundUser() throws Exception {
        context.checking(new Expectations() {
            {
                never(surveyRepository);
                oneOf(surveyPresenter).alertUserNotFound();
            }
        });

        SurveyController surveyController = new SurveyController(surveyRepository, surveyPresenter);
        surveyController.checkThisBuildingAndUserCanSurvey(building, null);
    }

    @Test
    public void testNotFoundBuilding() throws Exception {
        context.checking(new Expectations() {
            {
                never(surveyRepository);
                oneOf(surveyPresenter).alertBuildingNotFound();
            }
        });

        SurveyController surveyController = new SurveyController(surveyRepository, surveyPresenter);
        surveyController.checkThisBuildingAndUserCanSurvey(null, user);
    }
}
