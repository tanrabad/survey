/*
 * Copyright (c) 2019 NECTEC
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

package org.tanrabad.survey.presenter;

import android.support.annotation.NonNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Version implements Comparable<Version> {
    public final int major;
    public final int minor;
    public final int patch;

    public Version(String name) {
        Pattern pattern = Pattern.compile("^(\\d+\\.)(\\d+\\.)(\\*|\\d+)(.*)");
        Matcher matcher = pattern.matcher(name.trim());

        if (matcher.matches()) {
            major = Integer.parseInt(matcher.group(1).replace(".", ""));
            minor = Integer.parseInt(matcher.group(2).replace(".", ""));
            patch = Integer.parseInt(matcher.group(3));
        } else {
            throw new IllegalArgumentException("version name not match pattern " + name);
        }
    }

    @Override
    public int compareTo(@NonNull Version o) {
        Integer major = Integer.compare(this.major, o.major);
        if (major != 0)
            return major;
        Integer cMinor = Integer.compare(minor, o.minor);
        if (cMinor != 0)
            return cMinor;
        return Integer.compare(patch, o.patch);
    }

    @Override
    public String toString() {
        return major +"."+ minor + "." + patch;
    }
}
