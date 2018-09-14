package jobs;

import com.google.gson.Gson;
import db.BuyHistoryService;
import pojo.Item;
import websockets.NewItemGoListener;

import static cashe.GoodPriceCasheService.getGoodPriceCash;

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
            if (message.charAt(i) == '\\' && (message.charAt(i + 1) == '\\' || message.charAt(i + 1) == '\"')) {
                continue;
            } else {

            }
            arr[j++] = message.charAt(i);


        }

        while (j < arr.length) {
            arr[j++] = ' ';
        }
        //Извлекли

        //Проверяем в кэше котим ли мы покупать этот предмет
        Item item = new Gson().fromJson(new String(arr), Item.class);
        Long cashPrice = getGoodPriceCash.get(item.getPair());
        if (cashPrice == null) {
            NewItemGoListener.countOfThreads.decrementAndGet();
            return;
        }
        item.setW_price((double) cashPrice);
        item.setUi_price(item.getUi_price()*100);

        if (item.getW_price() >= item.getUi_price()) {
            BuyHistoryService buy = new BuyHistoryService();
            buy.insert(item);
            buy.closeConnection();
            System.out.println(item + " - хорошая цена");
        } else {
            System.out.println(item + " - плохая цена");
        }

        NewItemGoListener.countOfThreads.decrementAndGet();
    }

}
