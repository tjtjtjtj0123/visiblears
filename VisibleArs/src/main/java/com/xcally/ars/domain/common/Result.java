package com.xcally.ars.domain.common;

import lombok.Data;



@Data
public class Result<T> {

	public T data;
	public int errCode;
	public String errMsg;
	
    public Result()
    {
    	errCode = 0;
    }
    public Result(int intRetVal, String strErrMsg)
    {
    	errCode = intRetVal;
    	errMsg  = strErrMsg;
    }

    public Result(T TData, int intRetVal, String strErrMsg)
    {
    	data    = TData;
    	errCode = intRetVal;
    	errMsg  = strErrMsg;
    }
    public boolean IsSuccess()
    {
        return errCode==0;
    }
}

