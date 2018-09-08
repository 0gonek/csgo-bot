package dealer;

public class HistorySaver implements Runnable {

    public void run() {
        while (true){
            getItemHistoryRequest();
            saveItemHistory();
            try {
                Thread.sleep(1000);
            }catch (InterruptedException ex){
                ex.printStackTrace();
            }
        }
    }

    private void getItemHistoryRequest(){}

    private void saveItemHistory(){}
}
