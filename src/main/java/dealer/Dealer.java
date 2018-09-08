package dealer;

public class Dealer implements Runnable {

    private Requests requests;

    public Dealer() {
        this.requests = new Requests();
    }

    public void run() {
        while (true){
            requests.getBestSellOffer();
            try {
                Thread.sleep(300);
            }catch (InterruptedException ex){
                ex.printStackTrace();
            }
        }
    }
}
