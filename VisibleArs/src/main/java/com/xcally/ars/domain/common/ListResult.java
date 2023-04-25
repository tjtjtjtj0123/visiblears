package com.xcally.ars.domain.common;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.formula.functions.T;

public class ListResult extends SimpleResult{
	public List<T> ListData;
	public int intRecordCnt;
	
	public ListResult() {
		ListData = new ArrayList<T>();
		intRecordCnt = 0;
	}
}
