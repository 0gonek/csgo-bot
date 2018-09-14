package main;

import cashe.GoodPriceCasheService;
import db.DBUtils;
import jobs.HistorySaver;
import jobs.ModeSetter;
import websockets.NewItemGoListener;

import java.sql.SQLException;

public class Worker {
    public static void main(String[] args) throws SQLException {


        GoodPriceCasheService.heatCashe();  //  Подгрузка кеша из базы
        System.out.println("Cashe size = " + GoodPriceCasheService.getGoodPriceCash.size());


        Thread historySaver = new Thread(new HistorySaver());   //  Запуск джобы, обновляющей историю по фиду
        historySaver.setPriority(3);
        historySaver.start();

        Thread modeSetter = new Thread(new ModeSetter());   //  Запуск джобы, обновляющей рекомендованные цены и моды
        modeSetter.setPriority(5);
        modeSetter.start();


        NewItemGoListener nl = new NewItemGoListener();
        nl.connect();
        System.out.println("NewItemGoListener was connected");
        try {
            Thread.sleep(1000 * 60 * 60);
        } catch (InterruptedException e ) {
            e.printStackTrace();
        }
        nl.disconnect();

//
//        BuyHistoryService buy = new BuyHistoryService();
//        buy.insert(new Item("1111", "1111", "111111", "11111", "111111", "1111111", 8, 9 ));
//        buy.closeConnection();
//            nl.disconnect();

    }
}
