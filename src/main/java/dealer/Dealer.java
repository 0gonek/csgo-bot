package dealer;

public class Dealer implements Runnable {

    public void run() {
        while (true){
            selectItemId();
            getItemHistory();
            getBestSellOffer();
            decideAndBuy();
            try {
                Thread.sleep(500);
            }catch (InterruptedException ex){
                ex.printStackTrace();
            }
        }
    }

    private void selectItemId(){}

    private void getItemHistory(){}

    private void getBestSellOffer(){}

    private void decideAndBuy(){}
}
