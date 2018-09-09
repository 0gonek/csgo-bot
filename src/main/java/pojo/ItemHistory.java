package pojo;

public class ItemHistory {

    private boolean success;
    private long max;
    private long min;
    private long average;
    private long number;
    private PriceTime[] history;

    public ItemHistory() {
    }

    public ItemHistory(boolean success, long max, long min, long average, long number, PriceTime[] history) {
        this.success = success;
        this.max = max;
        this.min = min;
        this.average = average;
        this.number = number;
        this.history = history;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public long getMax() {
        return max;
    }

    public void setMax(long max) {
        this.max = max;
    }

    public long getMin() {
        return min;
    }

    public void setMin(long min) {
        this.min = min;
    }

    public long getAverage() {
        return average;
    }

    public void setAverage(long average) {
        this.average = average;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public PriceTime[] getHistory() {
        return history;
    }

    public void setHistory(PriceTime[] history) {
        this.history = history;
    }
}
