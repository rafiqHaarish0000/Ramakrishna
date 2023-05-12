package RetrofitUtils.Response;

import java.util.List;

public class CountResponse
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

        private Integer today;
        private Integer future;
        private Integer overdue;

        public Integer getToday() {
            return today;
        }

        public void setToday(Integer today) {
            this.today = today;
        }

        public Integer getFuture() {
            return future;
        }

        public void setFuture(Integer future) {
            this.future = future;
        }

        public Integer getOverdue() {
            return overdue;
        }

        public void setOverdue(Integer overdue) {
            this.overdue = overdue;
        }
    }
}
