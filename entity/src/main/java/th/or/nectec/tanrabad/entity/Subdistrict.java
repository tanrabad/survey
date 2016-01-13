package th.or.nectec.tanrabad.entity;


import java.util.List;

public class Subdistrict {
    private String code;
    private String name;
    private String amphurCode;
    private String amphurName;
    private String provinceCode;
    private String provinceName;
    private List<Polygon> boundary;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmphurCode() {
        return amphurCode;
    }

    public void setAmphurCode(String amphurCode) {
        this.amphurCode = amphurCode;
    }

    public String getAmphurName() {
        return amphurName;
    }

    public void setAmphurName(String amphurName) {
        this.amphurName = amphurName;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public List<Polygon> getBoundary() {
        return boundary;
    }

    public void setBoundary(List<Polygon> boundary) {
        this.boundary = boundary;
    }

    @Override
    public int hashCode() {
        int result = code != null ? code.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (amphurCode != null ? amphurCode.hashCode() : 0);
        result = 31 * result + (amphurName != null ? amphurName.hashCode() : 0);
        result = 31 * result + (provinceCode != null ? provinceCode.hashCode() : 0);
        result = 31 * result + (provinceName != null ? provinceName.hashCode() : 0);
        result = 31 * result + (boundary != null ? boundary.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Subdistrict that = (Subdistrict) o;

        if (code != null ? !code.equals(that.code) : that.code != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (amphurCode != null ? !amphurCode.equals(that.amphurCode) : that.amphurCode != null) return false;
        if (amphurName != null ? !amphurName.equals(that.amphurName) : that.amphurName != null) return false;
        if (provinceCode != null ? !provinceCode.equals(that.provinceCode) : that.provinceCode != null) return false;
        if (provinceName != null ? !provinceName.equals(that.provinceName) : that.provinceName != null) return false;
        return boundary != null ? boundary.equals(that.boundary) : that.boundary == null;

    }
}
