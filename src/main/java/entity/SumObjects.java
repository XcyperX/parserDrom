package entity;

public class SumObjects {
    private String nameObject;
    private int countFirm;

    public SumObjects(String nameObject, int countFirm) {
        this.nameObject = nameObject;
        this.countFirm = countFirm;
    }

    public String getNameObject() {
        return nameObject;
    }

    public void setNameObject(String nameObject) {
        this.nameObject = nameObject;
    }

    public int getCountFirm() {
        return countFirm;
    }

    public void setCountFirm(int countFirm) {
        this.countFirm = countFirm;
    }
}
