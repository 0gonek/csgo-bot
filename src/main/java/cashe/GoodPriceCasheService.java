package cashe;

import db.GoodPriceService;
import javafx.util.Pair;

import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;

public class GoodPriceCasheService {

    private static GoodPriceService goodPriceService = new GoodPriceService();


    //

    public static ConcurrentHashMap<Pair<Long, Long>, Long> getGoodPriceCash;

    public static ConcurrentHashMap<Pair<Long, Long>, Long> getGoodPriceCash1;

    public static ConcurrentHashMap<Pair<Long, Long>, Long> getGoodPriceCash2;

    public static ConcurrentHashMap<Pair<Long, Long>, Long> getGoodPriceCash3;

    public static ConcurrentHashMap<Pair<Long, Long>, Long> getGoodPriceCash4;

    //


    public static void heatCashe() throws SQLException {
        getGoodPriceCash1 = new ConcurrentHashMap<Pair<Long, Long>, Long>();
        getGoodPriceCash2 = new ConcurrentHashMap<Pair<Long, Long>, Long>();
        getGoodPriceCash3 = new ConcurrentHashMap<Pair<Long, Long>, Long>();
        getGoodPriceCash4 = new ConcurrentHashMap<Pair<Long, Long>, Long>();
        getGoodPriceCash = new ConcurrentHashMap<Pair<Long, Long>, Long>(goodPriceService.getCasheFromTable());
    }

}
