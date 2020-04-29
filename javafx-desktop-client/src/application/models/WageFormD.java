package application.models;

public class WageFormD
{
    private String id, name, unit, unitShort;

    public WageFormD() {
    }

    public WageFormD(String id, String name, String unit, String unitShort) {
        this.id = id;
        this.name = name;
        this.unit = unit;
        this.unitShort = unitShort;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getUnitShort() {
        return unitShort;
    }

    public void setUnitShort(String unitShort) {
        this.unitShort = unitShort;
    }

    public String toComboboxString()
    {
        return this.id+". / "+this.name+" / "+this.unitShort;
    }
}
