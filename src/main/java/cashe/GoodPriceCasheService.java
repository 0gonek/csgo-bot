package cashe;

import db.GoodPriceService;
import javafx.util.Pair;

import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;

public class GoodPriceCasheService {

    private static GoodPriceService goodPriceService = new GoodPriceService();

    public static ConcurrentHashMap<Pair<Long, Long>, Long> getGoodPriceCash;

    public static void heatCashe() throws SQLException {
        getGoodPriceCash = new ConcurrentHashMap<Pair<Long, Long>, Long>(goodPriceService.getCasheFromTable());
    }

}
