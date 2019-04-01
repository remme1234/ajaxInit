package egovframework.example.selectBox.service.impl;

import java.util.List;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import egovframework.rte.psl.dataaccess.util.EgovMap;

@Mapper
public interface SelectBoxMapper {

	List<EgovMap> selectParentBoxList()throws Exception;

	List<EgovMap> selectChildBoxList(String param)throws Exception;

}
