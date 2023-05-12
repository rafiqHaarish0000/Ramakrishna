package RetrofitUtils.Request;

public class HistoryRequest {

    private Integer user_activity_id;
    private Integer uid;
    private String access_token;

    public Integer getUser_activity_id() {
        return user_activity_id;
    }

    public void setUser_activity_id(Integer user_activity_id) {
        this.user_activity_id = user_activity_id;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }
}
