package RetrofitUtils.Request;

import java.util.List;

import RetrofitUtils.Response.DashboardResponse;

public class LocationRequest
{
    private Integer uid;
    private String access_token;
    private Integer user_activity_id;
    private Double latitude;
    private Double longitude;
    private List<Input_datum> input_data = null;
    private String remarks;

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public List<Input_datum> getInput_data() {
        return input_data;
    }

    public void setInput_data(List<Input_datum> input_data) {
        this.input_data = input_data;
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

    public Integer getUser_activity_id() {
        return user_activity_id;
    }

    public void setUser_activity_id(Integer user_activity_id) {
        this.user_activity_id = user_activity_id;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public static class Input_datum {

        private Integer input_type;
        private Integer input_id;
        private String input_name;
        private Boolean status;

        public Integer getInput_type() {
            return input_type;
        }

        public void setInput_type(Integer input_type) {
            this.input_type = input_type;
        }

        public Integer getInput_id() {
            return input_id;
        }

        public void setInput_id(Integer input_id) {
            this.input_id = input_id;
        }

        public String getInput_name() {
            return input_name;
        }

        public void setInput_name(String input_name) {
            this.input_name = input_name;
        }

        public Boolean getStatus() {
            return status;
        }

        public void setStatus(Boolean status) {
            this.status = status;
        }
    }
}
