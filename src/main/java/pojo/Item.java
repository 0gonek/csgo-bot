package pojo;

public class Item {
    private String i_quality;
    private String i_classid;
    private String i_instanceid;
    private double ui_price;

    public Item(){}

    public Item(String i_quality, String i_classid, String i_instanceid, double ui_price) {
        this.i_quality = i_quality;
        this.i_classid = i_classid;
        this.i_instanceid = i_instanceid;
        this.ui_price = ui_price;
    }

    public String getI_quality() {
        return i_quality;
    }

    public void setI_quality(String i_quality) {
        this.i_quality = i_quality;
    }

    public String getI_classid() {
        return i_classid;
    }

    public void setI_classid(String i_classid) {
        this.i_classid = i_classid;
    }

    public String getI_instanceid() {
        return i_instanceid;
    }

    public void setI_instanceid(String i_instanceid) {
        this.i_instanceid = i_instanceid;
    }

    public double getUi_price() {
        return ui_price;
    }

    public void setUi_price(double ui_price) {
        this.ui_price = ui_price;
    }

    @Override
    public String toString() {
        return this.i_classid + "_" + this.i_instanceid;
    }
}
