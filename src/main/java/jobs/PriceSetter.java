package jobs;

import cashe.GoodPriceCasheService;
import db.GoodPriceService;
import javafx.util.Pair;
import pojo.PriceTime;
import stratagy.TestStrategy;

import java.sql.SQLException;
import java.util.List;

public class PriceSetter {

    GoodPriceService goodPriceService;

    public PriceSetter() {
        this.goodPriceService = new GoodPriceService();
    }

    public Long getGoodPrice(List<PriceTime> history){
        return TestStrategy.simple(history);
    }

    public void setGoodPrice(Pair<Long, Long> key, List<PriceTime> history) throws SQLException {
        Long goodPrice = getGoodPrice(history);
        if(goodPriceService.contains(key))
            goodPriceService.updatePrice(key, goodPrice);
        else
            goodPriceService.addPrice(key, goodPrice);
        GoodPriceCasheService.getGoodPriceCash.put(key, goodPrice);
    }

    public void removeKey(Pair<Long, Long> key) throws SQLException {
        if(goodPriceService.contains(key))
            goodPriceService.deleteFromCashe(key);
        GoodPriceCasheService.getGoodPriceCash.remove(key);
    }
}
