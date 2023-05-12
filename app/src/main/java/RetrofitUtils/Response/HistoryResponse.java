package RetrofitUtils.Response;

import java.util.List;

public class HistoryResponse
{
    private String jsonrpc;
    private Object id;
    private List<Result> result = null;

    public String getJsonrpc() {
        return jsonrpc;
    }

    public void setJsonrpc(String jsonrpc) {
        this.jsonrpc = jsonrpc;
    }

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public List<Result> getResult() {
        return result;
    }

    public void setResult(List<Result> result) {
        this.result = result;
    }

    public class Result {

        private Integer user_activity_id;
        private Integer res_id;
        private Integer assigned_id;
        private String assigned_to;
        private String note;
        private String summary;
        private String location;
        private String activity_type;
        private String activity_description;
        private String enquiry_name;
        private String due_date;

        private List<Input_datum> input_data = null;

        private String contact_name;
        private String contact_mobile;

        public String getContact_name() {
            return contact_name;
        }

        public void setContact_name(String contact_name) {
            this.contact_name = contact_name;
        }

        public String getContact_mobile() {
            return contact_mobile;
        }

        public void setContact_mobile(String contact_mobile) {
            this.contact_mobile = contact_mobile;
        }

        public Integer getUser_activity_id() {
            return user_activity_id;
        }

        public void setUser_activity_id(Integer user_activity_id) {
            this.user_activity_id = user_activity_id;
        }

        public Integer getRes_id() {
            return res_id;
        }

        public void setRes_id(Integer res_id) {
            this.res_id = res_id;
        }

        public Integer getAssigned_id() {
            return assigned_id;
        }

        public void setAssigned_id(Integer assigned_id) {
            this.assigned_id = assigned_id;
        }

        public String getAssigned_to() {
            return assigned_to;
        }

        public void setAssigned_to(String assigned_to) {
            this.assigned_to = assigned_to;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getActivity_type() {
            return activity_type;
        }

        public void setActivity_type(String activity_type) {
            this.activity_type = activity_type;
        }

        public String getActivity_description() {
            return activity_description;
        }

        public void setActivity_description(String activity_description) {
            this.activity_description = activity_description;
        }

        public String getEnquiry_name() {
            return enquiry_name;
        }

        public void setEnquiry_name(String enquiry_name) {
            this.enquiry_name = enquiry_name;
        }

        public String getDue_date() {
            return due_date;
        }

        public void setDue_date(String due_date) {
            this.due_date = due_date;
        }

        public List<Input_datum> getInput_data() {
            return input_data;
        }

        public void setInput_data(List<Input_datum> input_data) {
            this.input_data = input_data;
        }
    }

    public class Input_datum {

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
