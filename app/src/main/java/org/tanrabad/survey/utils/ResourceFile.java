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


import java.io.*;

public class ResourceFile {

    public static String read(String fileName) {
        return new ResourceFile().readStringFromFile(fileName);
    }

    private String readStringFromFile(String filename) {
        try {

            BufferedReader br = new BufferedReader(new FileReader(getResourceFile(filename)));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                line = br.readLine();
            }
            return sb.toString();
        } catch (FileNotFoundException exception) {
            System.out.print("Not found file " + filename);
            return null;
        } catch (IOException io){
            io.printStackTrace();
            return null;
        }
    }

    private File getResourceFile(String fileName) throws NullPointerException {
        ClassLoader classLoader = getClass().getClassLoader();
        return new File(classLoader.getResource(fileName).getFile());
    }
}
