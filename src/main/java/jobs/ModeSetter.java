package jobs;

import db.FeedService;
import javafx.util.Pair;

import java.sql.SQLException;

public class ModeSetter implements Runnable {

    private FeedService feedService;

    public ModeSetter() {
        this.feedService = new FeedService();
    }

    public void run() {
        try {
            setAllMods();
        }catch (SQLException ex){
            ex.printStackTrace();
        }
    }

    // Изменить мод у сущности
    public int setMode(Pair<Long, Long> key) throws SQLException{
        return feedService.changeMode(key);
    }

    // Изменить мод у всех сущнностей в Feed
    private void setAllMods() throws SQLException {
        int rowsCount = feedService.getFeedRowCount();
        for (int i = 0; i < rowsCount; i++) {
            feedService.changeMode(
                    feedService.getClassIdEntityIdByRowNum(i)
            );
            if(i%1000 == 0)
                System.out.println("Mode setter set row number " + i);
        }
    }
}
