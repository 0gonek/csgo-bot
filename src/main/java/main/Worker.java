package main;

import cashe.GoodPriceCasheService;
import db.UpdateDBService;
import jobs.HistorySaver;
import jobs.ModeSetter;
import jobs.Pinger;
import websockets.NewItemGoListener;

import java.sql.SQLException;

public class Worker {
    public static void main(String[] args) throws SQLException {

        Thread updateFeed = new Thread(new UpdateDBService());
        updateFeed.setPriority(2);
        updateFeed.start();

        GoodPriceCasheService.heatCashe();  //  Подгрузка кеша из базы
        System.out.println("Cashe size = " + GoodPriceCasheService.getGoodPriceCash.size());

        Thread historySaver = new Thread(new HistorySaver());   //  Запуск джобы, обновляющей историю по фиду
        historySaver.setPriority(3);
        historySaver.start();

        Thread modeSetter = new Thread(new ModeSetter());   //  Запуск джобы, обновляющей рекомендованные цены и моды
        modeSetter.setPriority(5);
        modeSetter.start();

        try {
            Thread.sleep(1000 * 60 * 12); // 12 минут на заполнить кеши
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        System.out.println("Cashe_1 size = " + GoodPriceCasheService.getGoodPriceCash1.size());
        System.out.println("Cashe_2 size = " + GoodPriceCasheService.getGoodPriceCash2.size());
        System.out.println("Cashe_3 size = " + GoodPriceCasheService.getGoodPriceCash3.size());
        System.out.println("Cashe_4 size = " + GoodPriceCasheService.getGoodPriceCash4.size());

        Thread pingSender = new Thread(new Pinger());
        pingSender.start();

        System.out.println("Запуск веб-сокета..");

        NewItemGoListener nl = new NewItemGoListener();
        nl.connect();

//        try {
//            Thread.sleep(1000 * 60 * 60);
//        } catch (InterruptedException e ) {
//            e.printStackTrace();
//        }
//        nl.disconnect();
//
//        System.exit(0);

//
//        BuyHistoryService buy = new BuyHistoryService();
//        buy.insert(new Item("1111", "1111", "111111", "11111", "111111", "1111111", 8, 9 ));
//        buy.closeConnection();
//            nl.disconnect();

    }
}
