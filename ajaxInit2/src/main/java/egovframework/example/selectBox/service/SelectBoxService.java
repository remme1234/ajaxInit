package egovframework.example.selectBox.service;

import java.util.List;

import egovframework.rte.psl.dataaccess.util.EgovMap;

public interface SelectBoxService {

	List<EgovMap> selectParentBoxList() throws Exception;

	List<EgovMap> selectChildBoxList(String param)throws Exception;

}
