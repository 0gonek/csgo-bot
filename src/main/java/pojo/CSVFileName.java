package pojo;

public class CSVFileName {

    private long time;
    private String db;

    public CSVFileName() {}

    public CSVFileName(long time, String db) {
        this.time = time;
        this.db = db;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getDb() {
        return db;
    }

    public void setDb(String db) {
        this.db = db;
    }
}
