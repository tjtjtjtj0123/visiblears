package com.xcally.ars.domain.common;

import lombok.Data;



@Data
public class Result<T> {

	public T Data;
	public int ErrCode;
	public String ErrMsg;
	
    public Result()
    {
        ErrCode = 0;
    }
    public Result(int intRetVal, String strErrMsg)
    {
        ErrCode = intRetVal;
        ErrMsg  = strErrMsg;
    }

    public Result(T TData, int intRetVal, String strErrMsg)
    {
        Data    = TData;
        ErrCode = intRetVal;
        ErrMsg  = strErrMsg;
    }
    public boolean IsSuccess()
    {
        return ErrCode==0;
    }
}

