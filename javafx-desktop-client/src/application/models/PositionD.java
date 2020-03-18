package application.models;

public class PositionD
{
    String id, name, place, characteristic,
        placeID, placeName,
        levelID, levelLevel;

    public PositionD() {
    }

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

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getLevelLevel() {
        return levelLevel;
    }

    public void setLevelLevel(String levelLevel) {
        this.levelLevel = levelLevel;
    }

    public String getPlaceID() {
        return placeID;
    }

    public void setPlaceID(String placeID) {
        this.placeID = placeID;
    }

    public String getLevelID() {
        return levelID;
    }

    public void setLevelID(String levelID) {
        this.levelID = levelID;
    }
}
