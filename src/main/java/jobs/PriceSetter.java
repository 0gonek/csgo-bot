package jobs;

import cashe.GoodPriceCashe;
import db.GoodPriceService;
import javafx.util.Pair;
import pojo.PriceTime;

import java.sql.SQLException;
import java.util.List;

public class PriceSetter {

    GoodPriceService goodPriceService;

    public PriceSetter() {
        this.goodPriceService = new GoodPriceService();
    }

    public Long getGoodPrice(List<PriceTime> history){
        return history.get(history.size()-1).getL_price();
    }

    public void setGoodPrice(Pair<Long, Long> key, List<PriceTime> history) throws SQLException {
        Long goodPrice = getGoodPrice(history);
        if(goodPriceService.contains(key))
            goodPriceService.updatePrice(key, goodPrice);
        else
            goodPriceService.addPrice(key, goodPrice);
        GoodPriceCashe.cash.put(key, goodPrice);
    }

    public void removeKey(Pair<Long, Long> key) throws SQLException {
        if(goodPriceService.contains(key))
            goodPriceService.deleteFromCashe(key);
        GoodPriceCashe.cash.remove(key);
    }
}
