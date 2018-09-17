package jobs;

import cashe.GoodPriceCasheService;
import com.google.gson.Gson;
import db.BuyHistoryService;
import pojo.Item;
import websockets.NewItemGoListener;


public class Buyer implements Runnable {

    private String message;

    public Buyer(String message) {
        this.message = message;
    }

    public void run() {
        //Извлечение json из поля data
        char[] arr = new char[message.length() - 32]; //30 число символов до нужной { + 2 лишние символа в конце
        int j = 0;
        for (int i = 30; i < message.length() - 2; ++i) {
            if (!(message.charAt(i) == '\\' && message.charAt(i - 1) != '\\')) {
                arr[j++] = message.charAt(i);
            }
        }

        while (j < arr.length) {
            arr[j++] = ' ';
        }
        //Извлекли

        //Проверяем в кэше котим ли мы покупать этот предмет
        Item item = new Gson().fromJson(new String(arr), Item.class);

        Long cashPrice1 = GoodPriceCasheService.getGoodPriceCash1.get(item.getPair());
        if (cashPrice1 != null && cashPrice1 >= item.getUi_price() * 100) {
            BuyHistoryService buy = new BuyHistoryService();
            Item copy = new Item(item.getI_classid(), item.getI_instanceid(), item.getI_quality(),
                    item.getI_name_color(), item.getI_market_hash_name(), item.getStickers(),
                    item.getUi_price() * 100, cashPrice1);
            buy.insert(copy, 1);
            buy.closeConnection();
            System.out.println(item + " - куплен - mode = 1.");
        }
        Long cashPrice2 = GoodPriceCasheService.getGoodPriceCash2.get(item.getPair());
        if (cashPrice2 != null && cashPrice2 >= item.getUi_price() * 100) {
            BuyHistoryService buy = new BuyHistoryService();
            Item copy = new Item(item.getI_classid(), item.getI_instanceid(), item.getI_quality(),
                    item.getI_name_color(), item.getI_market_hash_name(), item.getStickers(),
                    item.getUi_price() * 100, cashPrice2);
            buy.insert(copy, 2);
            buy.closeConnection();
            System.out.println(item + " - куплен - mode = 2.");
        }
        Long cashPrice3 = GoodPriceCasheService.getGoodPriceCash3.get(item.getPair());
        if (cashPrice3 != null && cashPrice3 >= item.getUi_price() * 100) {
            BuyHistoryService buy = new BuyHistoryService();
            Item copy = new Item(item.getI_classid(), item.getI_instanceid(), item.getI_quality(),
                    item.getI_name_color(), item.getI_market_hash_name(), item.getStickers(),
                    item.getUi_price() * 100, cashPrice3);
            buy.insert(copy, 3);
            buy.closeConnection();
            System.out.println(item + " - куплен - mode = 3.");
        }
        Long cashPrice4 = GoodPriceCasheService.getGoodPriceCash4.get(item.getPair());
        if (cashPrice4 != null && cashPrice4 >= item.getUi_price() * 100) {
            BuyHistoryService buy = new BuyHistoryService();
            Item copy = new Item(item.getI_classid(), item.getI_instanceid(), item.getI_quality(),
                    item.getI_name_color(), item.getI_market_hash_name(), item.getStickers(),
                    item.getUi_price() * 100, cashPrice4);
            buy.insert(copy, 4);
            buy.closeConnection();
            System.out.println(item + " - куплен - mode = 4.");
        }

        NewItemGoListener.countOfThreads.decrementAndGet();
    }

}
