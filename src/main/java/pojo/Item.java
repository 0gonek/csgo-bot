package pojo;

import javafx.util.Pair;

public class Item {
    private String i_classid;
    private String i_instanceid;
    private String i_quality;
    private String i_name_color;
    private String i_market_hash_name;
    private String stickers;
    private double ui_price;
    private double w_price;


    public Item(){}

    public Item(
            String i_classid,
            String i_instanceid,
            String i_quality,
            String i_name_color,
            String i_market_hash_name,
            String stickers,
            double ui_price,
            double w_price
    ) {
        this.i_classid = i_classid;
        this.i_instanceid = i_instanceid;
        this.i_quality = i_quality;
        this.i_name_color = i_name_color;
        this.i_market_hash_name = i_market_hash_name;
        this.stickers = stickers;
        this.ui_price = ui_price;
        this.w_price = w_price;
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

    public String getI_market_hash_name() {
        return i_market_hash_name;
    }

    public void setI_market_hash_name(String i_market_hash_name) {
        this.i_market_hash_name = i_market_hash_name;
    }

    public String getStickers() {
        return stickers;
    }

    public void setStickers(String stickers) {
        this.stickers = stickers;
    }

    public double getUi_price() {
        return ui_price;
    }

    public void setUi_price(double ui_price) {
        this.ui_price = ui_price;
    }

    public double getW_price() {
        return w_price;
    }

    public void setW_price(double w_price) {
        this.w_price = w_price;
    }

    public String toSQLInsert(int mode) {
        return
                //language=sql
                "insert into buy_history (i_classid, i_instanceid, i_quality, i_name_color, i_market_hash_name," +
                        "stickers, ui_price, w_price, curr_time, mode)\n" +
                        "values (" + this.i_classid + "," + this.i_instanceid + ",'" + this.i_quality + "','" +
                        this.i_name_color + "','" + this.i_market_hash_name + "','" + this.stickers + "'," +
                        this.ui_price + "," + this.w_price + "," + System.currentTimeMillis() + "," + mode + ");";
    }

    @Override
    public String toString() {
        return "_class_" + this.i_classid + "_instance_" + this.i_instanceid + "_w_" + this.w_price + "_ui_" + this.ui_price;
    }

    public Pair<Long, Long> getPair() {
        return new Pair<Long, Long>(Long.parseLong(this.i_classid), Long.parseLong(this.i_instanceid));
    }
}
