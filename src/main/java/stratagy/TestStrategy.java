package stratagy;

import pojo.PriceTime;

import java.util.*;

public class TestStrategy {

    // Потестить, что сохраняется в историю покупок. Покупает вообще любые предметы
    public static long getTenThousand(List<PriceTime> history) {
        return 1000000; // 1000 в копейках
    }

    public static long simple(List<PriceTime> history) {
        double av = 0L;
        for (int i = 0; i < history.size(); i++) {
            av += history.get(i).getL_price();
        }
        av /= history.size();

        double finalCoef = 0.85;

        return (long) (av * finalCoef);
    }

    public static long getNonDecreasing(List<PriceTime> history) {
        double av1 = 0L;
        double av2 = 0L;
        double av = 0L;
        for (int i = 0; i < history.size() / 2; i++) {
            av1 += history.get(i).getL_price();
        }
        for (int i = history.size() / 2; i < history.size(); i++) {
            av2 += history.get(i).getL_price();
        }
        av = (av1 + av2) / history.size();
        av1 /= history.size() / 2;
        av2 /= history.size() - history.size() / 2;
        if (av1 * 0.8 > av2)    // В последнее ремя подешевела на 20%
            return -1L; // Не покупать

        TreeMap<Long, Long> timedHistory = new TreeMap<Long, Long>();   // Отсортивованная по времени история
        for (PriceTime priceTime :
                history) {
            timedHistory.put(priceTime.getL_time(), priceTime.getL_price());
        }

        double allTime = timedHistory.lastKey() - timedHistory.firstKey();
        double lastTime = timedHistory.lastKey();

        HashMap<Long, Long> validHistory = new HashMap<Long, Long>();

        for (Map.Entry<Long, Long> entry :
                timedHistory.entrySet()) {
            if (entry.getValue() >= av * 0.5 && entry.getValue() <= av * 1.5)
                validHistory.put(entry.getKey(), entry.getValue());
        }

        double goodPrice = 0;
        double sumWeigth = 0;
        int timeCoef = 5;

        for (Map.Entry<Long, Long> entry :
                validHistory.entrySet()) {
            double k = 1 + (allTime - (lastTime - entry.getKey()) + 0.0) / allTime * (timeCoef - 1);
            goodPrice += entry.getValue() * k;
            sumWeigth += k;
        }

        goodPrice = goodPrice / sumWeigth;

        Double finalCoef = 0.9;

        return (long)(finalCoef * Math.min(av, goodPrice));
    }
}
