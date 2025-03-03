package com.minda.iconnect.iql;

/**
 * Created by deepakchauhan on 07/08/17.
 */
public class Result {

    private int totalCount;
    private int size;
    private String[] headers;

    private Object[][] data;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String[] getHeaders() {
        return headers;
    }

    public void setHeaders(String[] headers) {
        this.headers = headers;
    }

    public Object[][] getData() {
        return data;
    }

    public void setData(Object[][] data) {
        this.data = data;
    }

    /*public static void main(String[] args) throws Exception {
        ObjectMapper mapper =new ObjectMapper();
        Result result = new Result();
        result.setSize(2);
        result.setTotalCount(20);
        result.setHeaders(new String[]{"a", "b", "c"});
        Object[] d1 = new Object[]{"ddd", 1, 1.2};
        Object[] d2 = new Object[]{"sasa", 1, 1.2};
        result.setData(new Object[][]{d1, d2});

        mapper.writeValue(System.out, result);
    }*/
}
