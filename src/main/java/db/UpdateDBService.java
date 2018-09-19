package db;

import com.google.gson.Gson;
import http.HttpService;
import pojo.CSVFileName;
import resources.Props;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;

import static resources.Props.CSV_FILE;

public class UpdateDBService implements Runnable {

    private static String CURRENT_DB_HTTP = "https://market.csgo.com/itemdb/current_730.json";
    private static String DOWNLOAD_CSV = "https://market.csgo.com/itemdb/";

    private NewFeedService newFeedService;

    public UpdateDBService() {
        this.newFeedService = new NewFeedService();
    }

    public void run() {
        while (true) {
            try {
                Gson gson = new Gson();
                CSVFileName fileName = gson.fromJson(HttpService.sendGETReq(CURRENT_DB_HTTP), CSVFileName.class);
                Thread.sleep(1000);
                HttpService.downLoadFile(DOWNLOAD_CSV + fileName.getDb(), CSV_FILE);
                newFeedService.createConnection();
                newFeedService.deleteNewFeed();
                newFeedService.uploadCsvToNewFeed();
                newFeedService.mergeWithFeed();
                newFeedService.closeConnection();

                Thread.sleep(1000 * 60 * 60 * 10); //10 часов
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }




}
