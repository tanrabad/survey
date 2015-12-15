/*
 * Copyright (c) 2015 NECTEC
 *   National Electronics and Computer Technology Center, Thailand
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package th.or.nectec.tanrabad.survey.presenter.maps;

import th.or.nectec.tanrabad.entity.Location;

public class LiteMapFragment {

    public static BaseMapFragment newInstance() {
        return newInstance(null);
    }

    public static BaseMapFragment newInstance(Location location) {

        BaseMapFragment supportMapFragment;

        if (location == null) {
            supportMapFragment = new BaseMapFragment();
        } else {
            supportMapFragment = MapMarkerFragment.newInstanceWithLocation(location);
        }

        supportMapFragment.setMoveToMyLocation(false);
        supportMapFragment.setMapCanScrolled(false);
        supportMapFragment.setMapZoomable(false);
        supportMapFragment.setShowMyLocation(false);
        supportMapFragment.setMoveToMyLocationButtonEnabled(false);

        return supportMapFragment;
    }
}
