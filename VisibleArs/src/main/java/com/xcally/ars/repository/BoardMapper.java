package com.xcally.ars.repository;

import org.apache.ibatis.annotations.Mapper;

import com.xcally.ars.domain.Board;

@Mapper
public interface BoardMapper {
	public int InsBoard(Board board);
}
