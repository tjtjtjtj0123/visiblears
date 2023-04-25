package com.xcally.ars.domain.common;

import lombok.Data;

@Data
public class SimpleResult {
	public int ErrCode;
	public String ErrMsg;

    public SimpleResult()
    {
        ErrCode = 0;
        ErrMsg  = "";
    }

    public SimpleResult(int intRetVal)
    {
        ErrCode = intRetVal;
    }

    public SimpleResult(int intRetVal, String strErrMsg)
    {
        ErrCode = intRetVal;
        ErrMsg  = strErrMsg;
    }

    public boolean IsSuccess()
    {
        return ErrCode == 0;
    }
}
