package com.xcally.ars.repository;

import org.apache.ibatis.annotations.Mapper;

import com.xcally.ars.domain.Partner;
import com.xcally.ars.domain.Attach;

@Mapper
public interface AttachMapper {
	public int InsAttach(Attach attach);
}
