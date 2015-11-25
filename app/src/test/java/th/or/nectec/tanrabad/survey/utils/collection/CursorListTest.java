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
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class CursorListTest {

    private Cursor mockedCursor = mock(Cursor.class);
    private CursorMapper mockedMapper = mock(CursorMapper.class);
    private List<String> cursorList = new CursorList<>(mockedCursor, mockedMapper);

    @Test
    public void testGetSize(){
        when(mockedCursor.getCount()).thenReturn(30);

        assertEquals(30, cursorList.size());
    }

    @Test
    public void testGet(){
        when(mockedMapper.map(mockedCursor)).thenReturn("Test String");

        assertEquals("Test String", cursorList.get(1));
        verify(mockedCursor).moveToPosition(1);
    }

    @Test
    public void testGetCursor(){
        assertEquals(mockedCursor, ((CursorList)cursorList).getCursor());
    }

    @Test
    public void testSet() {
        assertNull(cursorList.set(1, "Not save string"));
    }
}