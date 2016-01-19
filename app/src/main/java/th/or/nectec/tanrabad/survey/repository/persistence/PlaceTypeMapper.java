package th.or.nectec.tanrabad.survey.repository.persistence;

import android.util.SparseIntArray;
import th.or.nectec.tanrabad.entity.Place;

public class PlaceTypeMapper {
    public static final int สำนักงานสาธารณสุขจังหวัด = 1;
    public static final int สำนักงานสาธารณสุขอำเภอ = 2;
    public static final int โรงพยาบาลส่งเสริมสุขภาพตำบล = 3;
    public static final int โรงพยาบาลทั่วไป = 4;
    public static final int โรงพยาบาลชุมชน = 5;
    public static final int โรงพยาบาลสังกัดกระทรวงอื่น = 6;
    public static final int โรงพยาบาลเอกชน = 7;
    public static final int ศูนย์สุขภาพชุมชน = 8;
    public static final int ศูนย์วิชาการ = 9;
    public static final int ชุมชนแออัด = 10;
    public static final int ชุมชนพักอาศัย = 11;
    public static final int ชุมชนพาณิชย์ = 12;
    public static final int วัด = 13;
    public static final int โบสถ์ = 14;
    public static final int มัสยิด = 15;
    public static final int โรงเรียน = 16;
    public static final int โรงงาน = 17;
    private static PlaceTypeMapper instances;
    private SparseIntArray placeSubTypeMapping = new SparseIntArray();

    private PlaceTypeMapper() {
        placeSubTypeMapping.put(สำนักงานสาธารณสุขจังหวัด, Place.TYPE_HOSPITAL);
        placeSubTypeMapping.put(สำนักงานสาธารณสุขอำเภอ, Place.TYPE_HOSPITAL);
        placeSubTypeMapping.put(โรงพยาบาลส่งเสริมสุขภาพตำบล, Place.TYPE_HOSPITAL);
        placeSubTypeMapping.put(โรงพยาบาลทั่วไป, Place.TYPE_HOSPITAL);
        placeSubTypeMapping.put(โรงพยาบาลชุมชน, Place.TYPE_HOSPITAL);
        placeSubTypeMapping.put(โรงพยาบาลสังกัดกระทรวงอื่น, Place.TYPE_HOSPITAL);
        placeSubTypeMapping.put(โรงพยาบาลเอกชน, Place.TYPE_HOSPITAL);
        placeSubTypeMapping.put(ศูนย์สุขภาพชุมชน, Place.TYPE_HOSPITAL);
        placeSubTypeMapping.put(ศูนย์วิชาการ, Place.TYPE_HOSPITAL);
        placeSubTypeMapping.put(ชุมชนแออัด, Place.TYPE_VILLAGE_COMMUNITY);
        placeSubTypeMapping.put(ชุมชนพักอาศัย, Place.TYPE_VILLAGE_COMMUNITY);
        placeSubTypeMapping.put(ชุมชนพาณิชย์, Place.TYPE_VILLAGE_COMMUNITY);
        placeSubTypeMapping.put(วัด, Place.TYPE_WORSHIP);
        placeSubTypeMapping.put(โบสถ์, Place.TYPE_WORSHIP);
        placeSubTypeMapping.put(มัสยิด, Place.TYPE_WORSHIP);
        placeSubTypeMapping.put(โรงเรียน, Place.TYPE_SCHOOL);
        placeSubTypeMapping.put(โรงงาน, Place.TYPE_FACTORY);
    }

    public static PlaceTypeMapper getInstance() {
        if (instances == null)
            instances = new PlaceTypeMapper();
        return instances;
    }

    public int findBySubType(int subtypeID) {
        return placeSubTypeMapping.get(subtypeID, subtypeID);
    }

    public int getDefaultPlaceTyoe(int placeTypeID) {
        switch (placeTypeID) {
            case Place.TYPE_VILLAGE_COMMUNITY:
                return ชุมชนพักอาศัย;
            case Place.TYPE_WORSHIP:
                return วัด;
            case Place.TYPE_SCHOOL:
                return โรงเรียน;
            case Place.TYPE_HOSPITAL:
                return โรงพยาบาลทั่วไป;
            case Place.TYPE_FACTORY:
                return โรงงาน;
            default:
                return -1;
        }
    }
}
