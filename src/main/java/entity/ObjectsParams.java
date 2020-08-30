package entity;

public class ObjectsParams {
    private int id;
    private String nameObject;
    private String firmName;
    private float priceObject;

    public ObjectsParams() {

    }

    public ObjectsParams(int id, String nameObject, String firmName, float priceObject) {
        this.id = id;
        this.nameObject = nameObject;
        this.firmName = firmName;
        this.priceObject = priceObject;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameObject() {
        return nameObject;
    }

    public void setNameObject(String nameObject) {
        this.nameObject = nameObject;
    }

    public String getFirmName() {
        return firmName;
    }

    public void setFirmName(String firmName) {
        this.firmName = firmName;
    }

    public float getPriceObject() {
        return priceObject;
    }

    public void setPriceObject(float priceObject) {
        this.priceObject = priceObject;
    }
}

