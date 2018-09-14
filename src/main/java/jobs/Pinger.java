package jobs;

import http.HttpService;

import static resources.Props.HTTP_KEY;
import static resources.Props.MARKET;

public class Pinger implements Runnable{

    private static final String HTTP_PING = "https://" + MARKET + "/api/PingPong" + HTTP_KEY;

    public void run() {
        System.out.println("Pinger was ACTIVATED");
        while (true) {
            System.out.println(HTTP_PING);
            System.out.println(HttpService.sendGETReq(HTTP_PING));
            try {
                Thread.sleep(1000 * 60 * 2); //Раз в 2 минуты посылает пинг
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
