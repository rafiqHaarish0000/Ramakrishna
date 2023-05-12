package RetrofitUtils.Request;

public class DashboardRequest {

    private Integer uid;
    private String access_token;
    private String record_type;
    private String month_or_day;

    public String getMonth_or_day() {
        return month_or_day;
    }

    public void setMonth_or_day(String month_or_day) {
        this.month_or_day = month_or_day;
    }

    public String getRecord_type() {
        return record_type;
    }

    public void setRecord_type(String record_type) {
        this.record_type = record_type;
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
