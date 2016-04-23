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

package org.tanrabad.survey.service.json;

import com.bluelinelabs.logansquare.typeconverters.TypeConverter;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import th.or.nectec.tanrabad.entity.field.Location;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MultiPolygonTypeConverter implements TypeConverter<List<JsonPolygon>> {
    @Override
    public List<JsonPolygon> parse(JsonParser jsonParser) throws IOException {

        List<JsonPolygon> polygonList;
        if (jsonParser.getCurrentToken() == JsonToken.START_ARRAY) {
            polygonList = new ArrayList<>();
            while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                ArrayList<List<Location>> polygon = new ArrayList<>();
                while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                    polygon.add(getChildPolygon(jsonParser));
                }
                polygonList.add(new JsonPolygon(getBoundary(polygon), getHoles(polygon)));
            }
        } else {
            polygonList = null;
        }
        return polygonList;
    }

    private List<Location> getChildPolygon(JsonParser jsonParser) throws IOException {
        List<Location> eachChildPolygon;
        if (jsonParser.getCurrentToken() == JsonToken.START_ARRAY) {
            eachChildPolygon = new ArrayList<>();
            while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                eachChildPolygon.add(getLocation(jsonParser));
            }
        } else {
            eachChildPolygon = null;
        }
        return eachChildPolygon;
    }

    private List<Location> getBoundary(List<List<Location>> polygon) {
        return polygon.get(0);
    }

    private List<Location>[] getHoles(List<List<Location>> polygon) {
        int holeSize = polygon.size() - 1;
        if (holeSize == 0)
            return null;

        List<Location>[] holes = new ArrayList[holeSize];
        for (int position = 0; position < holeSize; position++) {
            holes[position] = polygon.get(position + 1);
        }
        return holes;
    }

    private Location getLocation(JsonParser jsonParser) throws IOException {
        Location value3;
        if (jsonParser.getCurrentToken() == JsonToken.START_ARRAY) {
            ArrayList<Double> point = new ArrayList<>();
            while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                Double coordinate;
                coordinate = jsonParser.getCurrentToken() == JsonToken.VALUE_NULL
                        ? null : jsonParser.getValueAsDouble();
                point.add(coordinate);
            }
            value3 = new Location(point.get(1), point.get(0));
        } else {
            value3 = null;
        }
        return value3;
    }

    @Override
    public void serialize(
            List<JsonPolygon> coordinateMultiPolygon, String fieldName,
            boolean writeFieldNameForObject, JsonGenerator jsonGenerator) throws IOException {

        if (coordinateMultiPolygon != null) {
            jsonGenerator.writeFieldName("coordinates");
            jsonGenerator.writeStartArray();
            for (JsonPolygon eachPolygon : coordinateMultiPolygon) {
                if (eachPolygon != null) {
                    jsonGenerator.writeStartArray();
                    writePolygonArray(jsonGenerator, eachPolygon.getBoundary());
                    for (int subPolygonIndex = 0; subPolygonIndex < eachPolygon.getHolesCount(); subPolygonIndex++) {
                        List<Location> holePolygon = eachPolygon.getHole(subPolygonIndex);
                        if (holePolygon != null) {
                            writePolygonArray(jsonGenerator, holePolygon);
                        }
                    }
                    jsonGenerator.writeEndArray();
                }
            }
        }
        jsonGenerator.writeEndArray();
    }

    private void writePolygonArray(JsonGenerator jsonGenerator, List<Location> subPolygon) throws IOException {
        jsonGenerator.writeStartArray();
        for (Location eachLocation : subPolygon) {
            if (eachLocation != null) {
                jsonGenerator.writeStartArray();
                jsonGenerator.writeNumber(eachLocation.getLongitude());
                jsonGenerator.writeNumber(eachLocation.getLatitude());
                jsonGenerator.writeEndArray();
            }
        }
        jsonGenerator.writeEndArray();
    }
}
