package application.models;

public class PositionD
{
    String id, name, place, characteristic;

    public PositionD(String id, String name, String place, String characteristic) {
        this.id = id;
        this.name = name;
        this.place = place;
        this.characteristic = characteristic;
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

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getCharacteristic() {
        return characteristic;
    }

    public void setCharacteristic(String characteristic) {
        this.characteristic = characteristic;
    }
}
