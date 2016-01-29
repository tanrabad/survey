package th.or.nectec.tanrabad.survey.job;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import th.or.nectec.tanrabad.domain.WritableRepository;
import th.or.nectec.tanrabad.survey.service.RestService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class WritableRepoUpdateJobTest {

    @Mock
    public RestService<String> restService;
    @Mock
    public WritableRepository<String> writableRepository;

    private WritableRepoUpdateJob<String> job;

    @Before
    public void setUp() throws Exception {
        job = new WritableRepoUpdateJob<>(restService, writableRepository);
    }

    @Test
    public void testServiceReturnData() throws Exception {
        when(restService.getUpdate()).thenReturn(firstData());
        when(restService.hasNextRequest()).thenReturn(false);

        job.execute();

        verify(writableRepository).updateOrInsert(firstData());
    }

    @Test
    public void testServiceHaveNextRequest() throws Exception {
        when(restService.getUpdate()).thenReturn(firstData(), secondData());
        when(restService.hasNextRequest()).thenReturn(true, false);

        job.execute();

        verify(writableRepository).updateOrInsert(firstData());
        verify(writableRepository).updateOrInsert(secondData());
    }

    @Test
    public void testServiceNotReturnData() throws Exception {
        when(restService.getUpdate()).thenReturn(new ArrayList<String>());
        when(restService.hasNextRequest()).thenReturn(false);

        job.execute();

        verify(writableRepository, never()).updateOrInsert(anyList());
    }

    private List<String> firstData() {
        List<String> data = new ArrayList<>();
        data.add("Hello");
        data.add("How");
        data.add("R");
        data.add("U");
        return data;
    }

    private List<String> secondData() {
        List<String> data = new ArrayList<>();
        data.add("How");
        data.add("About");
        data.add("U");
        return data;
    }
}