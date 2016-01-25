package th.or.nectec.tanrabad.survey.service.json;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import th.or.nectec.tanrabad.entity.field.Polygon;

import java.util.ArrayList;
import java.util.List;

@JsonObject
public class GeoJsonMultipolygon {

    @JsonField
    public String type;

    @JsonField(name = "coordinates", typeConverter = MultiPolygonTypeConverter.class)
    public List<JsonPolygon> jsonPolygons;

    public static GeoJsonMultipolygon parse(List<Polygon> polygonList) {
        GeoJsonMultipolygon geoJsonMultipolygon = new GeoJsonMultipolygon();
        geoJsonMultipolygon.type = "MultiPolygon";
        geoJsonMultipolygon.jsonPolygons = new ArrayList<>();
        for (Polygon eachPolygon : polygonList) {
            geoJsonMultipolygon.jsonPolygons.add(new JsonPolygon(eachPolygon.getBoundary(), eachPolygon.getAllHoles()));
        }
        return geoJsonMultipolygon;
    }

    public List<Polygon> getEntities() {
        List<Polygon> multiplePolygon = new ArrayList<>();
        for (JsonPolygon eachPolygon : jsonPolygons) {
            multiplePolygon.add(new Polygon(eachPolygon.getBoundary(), eachPolygon.getAllHoles()));
        }
        return multiplePolygon;
    }

    public JsonPolygon getPolygon(int position) {
        return jsonPolygons.get(position);
    }
}
