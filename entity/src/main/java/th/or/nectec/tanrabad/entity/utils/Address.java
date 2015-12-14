package th.or.nectec.tanrabad.entity.utils;

public class Address {
    private String subdistrict;
    private String district;
    private String province;
    private String addressCode;

    public String getSubdistrict() {
        return subdistrict;
    }

    public void setSubdistrict(String subdistrict) {
        this.subdistrict = subdistrict;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getAddressCode() {
        return addressCode;
    }

    public void setAddressCode(String addressCode) {
        this.addressCode = addressCode;
    }

    @Override
    public int hashCode() {
        int result = subdistrict.hashCode();
        result = 31 * result + district.hashCode();
        result = 31 * result + province.hashCode();
        result = 31 * result + addressCode.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Address address = (Address) o;

        if (!subdistrict.equals(address.subdistrict)) return false;
        if (!district.equals(address.district)) return false;
        if (!province.equals(address.province)) return false;
        return addressCode.equals(address.addressCode);

    }

    @Override
    public String toString() {
        return "Address{" +
                "subdistrict='" + subdistrict + '\'' +
                ", district='" + district + '\'' +
                ", province='" + province + '\'' +
                ", addressCode='" + addressCode + '\'' +
                '}';
    }
}
