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

    public Long getGoodPrice(List<PriceTime> history) {
        return TestStrategy.simple(history, 0.8);
    }

    public Long getGoodPrice1(List<PriceTime> history) {
        return TestStrategy.simple(history, 0.8);
    }

    public Long getGoodPrice2(List<PriceTime> history) {
        return TestStrategy.simple(history, 0.9);
    }

    public Long getGoodPrice3(List<PriceTime> history) {
        return TestStrategy.getNonDecreasing(history, 0.8);
    }

    public Long getGoodPrice4(List<PriceTime> history) {
        return TestStrategy.getNonDecreasing(history, 0.9);
    }

    public void setGoodPrice(Pair<Long, Long> key, List<PriceTime> history) throws SQLException {
        Long goodPrice = getGoodPrice(history);
        if (goodPriceService.contains(key))
            goodPriceService.updatePrice(key, goodPrice);
        else
            goodPriceService.addPrice(key, goodPrice);
        GoodPriceCasheService.getGoodPriceCash.put(key, goodPrice);


        //

        Long goodPrice1 = getGoodPrice1(history);
        Long goodPrice2 = getGoodPrice2(history);
        Long goodPrice3 = getGoodPrice3(history);
        Long goodPrice4 = getGoodPrice4(history);

        GoodPriceCasheService.getGoodPriceCash1.put(key, goodPrice1);
        GoodPriceCasheService.getGoodPriceCash2.put(key, goodPrice2);
        GoodPriceCasheService.getGoodPriceCash3.put(key, goodPrice3);
        GoodPriceCasheService.getGoodPriceCash4.put(key, goodPrice4);

        //
    }

    public void removeKey(Pair<Long, Long> key) throws SQLException {
        if (goodPriceService.contains(key))
            goodPriceService.deleteFromCashe(key);
        GoodPriceCasheService.getGoodPriceCash.remove(key);
    }
}
