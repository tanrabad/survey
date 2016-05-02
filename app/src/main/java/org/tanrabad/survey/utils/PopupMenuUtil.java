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

package org.tanrabad.survey.utils;

import android.view.Menu;
import org.tanrabad.survey.TanrabadApp;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PopupMenuUtil {
    public static void showPopupMenuIcon(Menu menu) {
        if (menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method m = menu.getClass().getDeclaredMethod(
                            "setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (NoSuchMethodException e) {
                    TanrabadApp.log(e);
                } catch (InvocationTargetException e) {
                    TanrabadApp.log(e);
                } catch (IllegalAccessException e) {
                    TanrabadApp.log(e);
                }
            }
        }
    }
}
