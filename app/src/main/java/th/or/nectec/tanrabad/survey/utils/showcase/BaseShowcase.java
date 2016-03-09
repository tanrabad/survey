/*
 * Copyright (c) 2016 NECTEC
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

package th.or.nectec.tanrabad.survey.utils.showcase;

import android.app.Activity;
import android.widget.RelativeLayout;
import com.github.amlcurran.showcaseview.ShowcaseView;
import th.or.nectec.tanrabad.survey.R;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public abstract class BaseShowcase implements Showcase {

    private ShowcaseView.Builder builder;
    private Activity activity;

    public BaseShowcase(Activity activity) {
        this.activity = activity;
        builder = new ShowcaseView.Builder(activity)
                .setStyle(R.style.CustomShowcaseTheme)
                .withNewStyleShowcase()
                .hideOnTouchOutside()
                .setContentTextPaint(ShowcaseFontStyle.getContentStyle(activity))
                .setContentTitlePaint(ShowcaseFontStyle.getTitleStyle(activity));
    }

    public ShowcaseView.Builder getBuilder() {
        return builder;
    }

    @Override
    public void setTitle(String title) {
        builder.setContentTitle(title);
    }

    @Override
    public void setMessage(String message) {
        builder.setContentText(message);
    }

    @Override
    public void display() {
        if (new ShowcasePreference(activity).get()) {
            ShowcaseView build = builder.build();
            build.setButtonPosition(LeftButton());
        }
    }

    private RelativeLayout.LayoutParams LeftButton() {
        RelativeLayout.LayoutParams lps = new RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        lps.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lps.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        int margin = ((Number) (activity.getResources().getDisplayMetrics().density * 12)).intValue();
        lps.setMargins(margin, margin, margin, margin);
        return lps;
    }
}
