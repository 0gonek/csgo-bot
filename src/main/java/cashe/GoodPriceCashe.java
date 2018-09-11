package cashe;

import db.GoodPriceService;
import javafx.util.Pair;

import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;

public class GoodPriceCashe {

    private static GoodPriceService goodPriceService = new GoodPriceService();

    public static ConcurrentHashMap<Pair<Long, Long>, Long> cash;

    public static void heatCashe() throws SQLException {
        cash = new ConcurrentHashMap<Pair<Long, Long>, Long>(goodPriceService.getCasheFromTable());
    }

}
