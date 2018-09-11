package db;

import com.google.gson.Gson;
import http.HttpService;
import pojo.CSVFileName;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static resources.Props.CSV_FILE;

public class UpdateDBService implements Runnable {

    private static String CURRENT_DB_HTTP = "https://market.csgo.com/itemdb/current_730.json";
    private static String DOWNLOAD_CSV = "https://market.csgo.com/itemdb/";

    public void run() {
        while (true) {
            try {
                Gson gson = new Gson();
                CSVFileName fileName = gson.fromJson(HttpService.sendGETReq(CURRENT_DB_HTTP), CSVFileName.class);
                Thread.sleep(1000);
                HttpService.downLoadFile(DOWNLOAD_CSV+fileName.getDb(), CSV_FILE);
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }
        }
    }


}
