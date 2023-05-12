package RetrofitUtils.Response;

import java.util.List;

public class LoginResponse
{
    private String jsonrpc;
    private Object id;
    private List<Result > result = null;

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

        private Integer uid;
        private Boolean success;
        private String access_token;
        private Integer employee_id;
        private String employee_name;
        private String employee_image;
        private String identification_id;
        private String job_position;
        private String department;
        private String mobile;
        private String work_email;
        private String birthday;
        private String street;
        private String street2;
        private String city;
        private String state;
        private String zip;
        private String country;



        public String getIdentification_id() {
            return identification_id;
        }

        public void setIdentification_id(String identification_id) {
            this.identification_id = identification_id;
        }

        public String getEmployee_image() {
            return employee_image;
        }

        public void setEmployee_image(String employee_image) {
            this.employee_image = employee_image;
        }

        public Integer getUid() {
            return uid;
        }

        public void setUid(Integer uid) {
            this.uid = uid;
        }

        public Boolean getSuccess() {
            return success;
        }

        public void setSuccess(Boolean success) {
            this.success = success;
        }

        public String getAccess_token() {
            return access_token;
        }

        public void setAccess_token(String access_token) {
            this.access_token = access_token;
        }

        public Integer getEmployee_id() {
            return employee_id;
        }

        public void setEmployee_id(Integer employee_id) {
            this.employee_id = employee_id;
        }

        public String getEmployee_name() {
            return employee_name;
        }

        public void setEmployee_name(String employee_name) {
            this.employee_name = employee_name;
        }

        public String getJob_position() {
            return job_position;
        }

        public void setJob_position(String job_position) {
            this.job_position = job_position;
        }

        public String getDepartment() {
            return department;
        }

        public void setDepartment(String department) {
            this.department = department;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getWork_email() {
            return work_email;
        }

        public void setWork_email(String work_email) {
            this.work_email = work_email;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getStreet() {
            return street;
        }

        public void setStreet(String street) {
            this.street = street;
        }

        public String getStreet2() {
            return street2;
        }

        public void setStreet2(String street2) {
            this.street2 = street2;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getZip() {
            return zip;
        }

        public void setZip(String zip) {
            this.zip = zip;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }
    }
}
