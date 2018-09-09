package db;

import com.google.gson.Gson;
import pojo.CSVFileName;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static resources.Props.CSV_FILE;

public class UpdateDB implements Runnable {

    private static String CURRENT_DB_HTTP = "https://market.csgo.com/itemdb/current_730.json";
    private static String DOWNLOAD_CSV = "https://market.csgo.com/itemdb/";

    public void run() {
        while (true) {
            try {
                CSVFileName fileName = checkDBName();
                Thread.sleep(1000);
                downloadCSV(fileName.getDb());
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }
        }
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

    private void downloadCSV(String nameDB) {
        URL obj;
        HttpURLConnection con;
        try {
            obj = new URL(DOWNLOAD_CSV + nameDB);
            con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            File csv = new File(CSV_FILE);
            String newLine;

            if (!csv.exists()) {
                csv.createNewFile();
            }

            BufferedWriter bw = new BufferedWriter(new FileWriter(csv));
            while ((newLine = br.readLine()) != null) {
                bw.write(newLine);
                bw.newLine();
            }

            bw.flush();
            bw.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
            return;
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }

}
