package com.xcally.ars.domain.common;
import lombok.Data;

public class DataResponse<T> {
    private SimpleResult simpleResult;
    private T data;

    public DataResponse() {
        this.simpleResult = new SimpleResult();
    }

    public DataResponse(T data) {
        this.simpleResult = new SimpleResult();
        this.data = data;
    }

    public DataResponse(int errCode, String errMsg) {
        this.simpleResult = new SimpleResult(errCode, errMsg);
    }

    public SimpleResult getSimpleResult() {
        return simpleResult;
    }

    public void setSimpleResult(SimpleResult simpleResult) {
        this.simpleResult = simpleResult;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return simpleResult.IsSuccess();
    }
}
