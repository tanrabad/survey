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

package th.or.nectec.tanrabad.survey.utils.collection;

import android.database.Cursor;

import java.util.AbstractList;

public class CursorList<T> extends AbstractList<T> {

    private Cursor cursor;
    private CursorMapper<T> mapper;

    public CursorList(Cursor cursor, CursorMapper<T> mapper){
        this.cursor = cursor;
        this.mapper = mapper;
    }

    public Cursor getCursor() {
        return cursor;
    }

    @Override
    public T get(int i) {
        cursor.moveToPosition(i);
        return mapper.map(cursor);
    }

    @Override
    public T set(int location, T object) {
        return null;
    }

    @Override
    public int size() {
        return cursor.getCount();
    }



}
