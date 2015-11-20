package th.or.nectec.tanrabad.survey;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MapMarkerActivity extends TanrabadActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_marker);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_save_map_marker_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_marker_menu:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
