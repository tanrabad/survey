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

package org.tanrabad.survey.utils.collection;

import android.database.Cursor;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

public class CursorList<T> extends AbstractList<T> {

    private CursorMapper<T> mapper;
    private List<T> objests = new ArrayList<>();

    public CursorList(Cursor cursor, CursorMapper<T> mapper) {

        this.mapper = mapper;
        try {
            if (!cursor.isClosed()) {
                while (!cursor.isClosed() && cursor.moveToNext()) {
                    objests.add(mapper.map(cursor));
                }
                cursor.close();
            }
        } catch (IllegalStateException ignore) {
        }
    }

    @Override
    public T get(int i) {
        return objests.get(i);
    }

    @Override
    public T set(int location, T object) {
        return null;
    }

    @Override
    public int size() {
        return objests.size();
    }
}
