package th.or.nectec.tanrabad.survey.validator;

import android.text.TextUtils;

import th.or.nectec.tanrabad.domain.BuildingValidator;
import th.or.nectec.tanrabad.entity.Building;
import th.or.nectec.tanrabad.entity.Place;
import th.or.nectec.tanrabad.survey.R;
import th.or.nectec.tanrabad.survey.utils.alert.Alert;

public class SaveBuildingValidator implements BuildingValidator {
    @Override
    public boolean validate(Building building) {

        if (TextUtils.isEmpty(building.getName())) {
            if (building.getPlace().getType() == Place.TYPE_VILLAGE_COMMUNITY)
                Alert.highLevel().show(R.string.please_define_house_no);
            else
                Alert.highLevel().show(R.string.please_define_building_name);

            return false;
        }

        if (building.getLocation() == null) {
            Alert.highLevel().show(R.string.please_define_building_location);
            return false;
        }

        return true;
    }
}
