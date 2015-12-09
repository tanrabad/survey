package th.or.nectec.tanrabad.survey.utils;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorRes;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import th.or.nectec.tanrabad.survey.utils.android.ResourceUtils;

public class MapUtils {
    public static BitmapDescriptor getIconBitmapDescriptor(Context context, @ColorRes int colorRes) {
        int color = ResourceUtils.from(context).getColor(colorRes);
        float hsv[] = new float[3];
        Color.colorToHSV(color, hsv);
        return BitmapDescriptorFactory.defaultMarker(hsv[0]);
    }
}
