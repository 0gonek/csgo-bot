package pojo;

public class WSAuth {

    private boolean success;
    private String wsAuth;

    public WSAuth() {}

    public WSAuth(boolean success, String wsAuth) {
        this.success = success;
        this.wsAuth = wsAuth;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getWsAuth() {
        return wsAuth;
    }

    public void setWsAuth(String wsAuth) {
        this.wsAuth = wsAuth;
    }
}
