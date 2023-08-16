package com.xcally.ars.repository;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.xcally.ars.domain.Partner;
import com.xcally.ars.domain.Attach;

@Mapper
public interface SeqMapper {
	public int callUpKeyNtGen(Map<String, Object> paramMap);
}
