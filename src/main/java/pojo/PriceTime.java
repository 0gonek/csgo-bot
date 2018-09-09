package pojo;

public class PriceTime {

    private long l_price;
    private long l_time;

    public PriceTime() {
    }

    public PriceTime(long l_price, long l_time) {
        this.l_price = l_price;
        this.l_time = l_time;
    }

    public long getL_price() {
        return l_price;
    }

    public void setL_price(long l_price) {
        this.l_price = l_price;
    }

    public long getL_time() {
        return l_time;
    }

    public void setL_time(long l_time) {
        this.l_time = l_time;
    }
}
