package pojo;

public class Item {
    private String i_quality;
    private String i_name_color;
    private String i_class_id;
    private String i_instance_id;
    private String i_market_hash_name;
    private String i_market_name;
    private double ui_price;
    private String app;

    public Item(){}

    public Item(String i_quality, String i_name_color, String i_class_id, String i_instance_id, String i_market_hash_name, String i_market_name, double ui_price, String app) {
        this.i_quality = i_quality;
        this.i_name_color = i_name_color;
        this.i_class_id = i_class_id;
        this.i_instance_id = i_instance_id;
        this.i_market_hash_name = i_market_hash_name;
        this.i_market_name = i_market_name;
        this.ui_price = ui_price;
        this.app = app;
    }

    public String getI_quality() {
        return i_quality;
    }

    public void setI_quality(String i_quality) {
        this.i_quality = i_quality;
    }

    public String getI_name_color() {
        return i_name_color;
    }

    public void setI_name_color(String i_name_color) {
        this.i_name_color = i_name_color;
    }

    public String getI_class_id() {
        return i_class_id;
    }

    public void setI_class_id(String i_class_id) {
        this.i_class_id = i_class_id;
    }

    public String getI_instance_id() {
        return i_instance_id;
    }

    public void setI_instance_id(String i_instance_id) {
        this.i_instance_id = i_instance_id;
    }

    public String getI_market_hash_name() {
        return i_market_hash_name;
    }

    public void setI_market_hash_name(String i_market_hash_name) {
        this.i_market_hash_name = i_market_hash_name;
    }

    public String getI_market_name() {
        return i_market_name;
    }

    public void setI_market_name(String i_market_name) {
        this.i_market_name = i_market_name;
    }

    public double getUi_price() {
        return ui_price;
    }

    public void setUi_price(double ui_price) {
        this.ui_price = ui_price;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    @Override
    public String toString() {
        return this.i_class_id + "_" + this.i_instance_id;
    }
}
