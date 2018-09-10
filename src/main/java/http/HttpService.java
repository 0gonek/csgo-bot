package http;

import com.google.gson.Gson;
import pojo.CSVFileName;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpService {

    public static String sendGETReq(String addr){
        URL obj;
        HttpURLConnection con;
        StringBuffer response;
        try{
            obj = new URL(addr);
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

        return response.toString();

    }

    public static void downLoadFile(String url, String fileName) {
        URL obj;
        HttpURLConnection con;
        try {
            obj = new URL(url);
            con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            File csv = new File(fileName);
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
