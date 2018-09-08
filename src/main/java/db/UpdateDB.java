package db;

import com.google.gson.Gson;
import pojo.CSVFileName;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class UpdateDB implements Runnable {

    private static String CURRENT_DB_HTTP = "https://market.csgo.com/itemdb/current_730.json";
    private static String DOWNLOAD_CSV = "https://market.csgo.com/itemdb/";

    public void run() {

    }

    private CSVFileName checkDBName(){
        URL obj;
        HttpURLConnection con;
        StringBuffer response;
        try{
            obj = new URL(CURRENT_DB_HTTP);
            con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        Gson gson = new Gson();
        return gson.fromJson(response.toString(), CSVFileName.class);
    }

}
