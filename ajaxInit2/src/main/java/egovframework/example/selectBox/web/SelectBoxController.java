package egovframework.example.selectBox.web;

import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import egovframework.example.cmmn.JsonUtil;
import egovframework.example.selectBox.service.SelectBoxService;
import egovframework.rte.psl.dataaccess.util.EgovMap;

@Controller
public class SelectBoxController {

	@Resource
	private SelectBoxService selectBoxService;
	
	@ModelAttribute("parentList")
	public List<EgovMap> parentList() throws Exception {
		return selectBoxService.selectParentBoxList();
	}
	
	@RequestMapping(value = "/selectBox.do")
	public String selectBoxMain(ModelMap model) throws Exception {
		// List<EgovMap> parentList = selectBoxService.selectParentBoxList();
		
		// System.out.println("parentList : " + parentList);
		
		// model.addAttribute("parentList", parentList);
		
		System.out.println("모델에 담긴 값 : " + model);
		
		return "selectBox/selectBox.tiles";
	}
	
	@RequestMapping(value = "/childSelectBox.do")
	
	 /* 
	  *  8. 먼저 화면에서 data를 컨트롤러에 보낼때 post방식으로 보냈기 때문에 body에 담겨져 컨트롤러로 넘어온다.
	  *     RequestParam은 화면에서 올린 키를 적으면 value를 해당 변수에 넣어주는 역할을 하지만
	  *     RequestBody의 경우는 body에 담겨져 온 데이터를 해당 변수 안에 담아주는 기능을 한다.
	  *  8-1. 추가적으로 RequestParam을 쓰기 위한 두가지 조건이 있는데
	  *       쿼리스트링으로 올라와야하고, 화면에서 올린 name과 param에서의 변수가 같아야함
	  */
	public void childSelectBoxMain(@RequestBody String reqParam,
			HttpServletRequest request,
			HttpServletResponse response,
			ModelMap model) throws Exception {
		
		// 9. 아래에 빈 변수를 넣어주는 이유는 이후에 사용하기 위함
		String 	param	="";
		
		System.out.println("reqParam : " + reqParam);
		
		/*
		 * 10. 아래는 ajax의  content-type속성을 지정해주지 않고 default로 진행할 경우 추가적으로 입력해야 하는 부분  
		 * 10-1. body에 있는 데이터인 reqParam을 그대로 가져오게 되면 제대로 디코딩 되지 않은 부분을 확인할 수 있다.
		 *	          따라서 다국어 디코딩을 추가해준다. 현재 paramKey 변수에 들어가 있는 모양은  {"param ":"B00003"}= 가 된다.
		 *		 paramKey = URLDecoder.decode(reqParam,"utf-8");
		 *		
		 *		 System.out.println("decodeReqParam : " + paramKey);
		 * 		
		 * 10-2. {"param":"B00003"}= 모양으로 되어있는 paramKey에서 맨 마지막 '='을 제거하기 위해 아래 substring 메서드를 이용한다.
		 *		 paramKey = paramKey.substring(0, paramKey.length()-1);
		 *		
		 *		 System.out.println("paramKey.substring : " + paramKey);
		 *		
		 *		 System.out.println("contetnType 작성 후  RequestBody : " + reqParam);
		 */
		
		/*
		 * 11. 현재 reqParam에는 화면에서 올린 data 속성의 값인 Json Object String type의 {"param":parentList.brandCd} 가 들어가있다.
		 *     이에 JsonToMap 메서드를 이용하면 키=벨류 형태의 Map방식의 데이터로 바꿀 수 있게 되어
		 *     Map에 담을수 있는 형태가 된다. 따라서 param = parentList.brandCd가 들어간다.
		 */
		Map<String,Object> resMap =  JsonUtil.JsonToMap(reqParam);
		
		/*
		 * 12. Map의 get메서드의 파라미터에 키를 적으면 그에 해당하는 값을 가져온다.
		 *     여기서의 키 "param"에는 화면에서 올린 브랜드이름의  parentList.brandCd 값이 들어가 있다.
		 */
		param = (String) resMap.get("param");
		
		/*
		 * 13. List의 파라미터 값을 param 변수를 이용했는데 해당 변수에는 brandCd의 값이 들어가 있고
		 *     xml파일을 확인하면 where조건과  if문을 이용하여 컨트롤러에서 보낸 String이 널이나 빈값이 아니라면
		 *     T-PRD 테이블에서 BRAND_CD 컬럼의 값과 같은 데이터를 리스트로 구성한다.
		 */ 
		List<EgovMap> childList = selectBoxService.selectChildBoxList(param);
		
		HashMap<String,Object> resultMap = new HashMap<String, Object>();
		
		/*
		 * 14. 화면에 값을 내리기 위해서 Map을 하나 만들고 맵의 데이터에 2가지의 값을 넣는다.
		 *     먼저 result에는 SUCCESS라는 값을 넣는데 이는 화면의 ajax의 success 속성에서 사용할 데이터로 
		 *     데이터가 잘 받아지는지를 확인 하기 위해 사용되고
		 *     childList는 내가 누른 brandCd에 해당하는 brandNm 즉 품목들을 List로 구성된 데이터를 Map으로 화면에 내리기위해 만든다.
		 */
		resultMap.put("result", "SUCCESS");
		resultMap.put("childList", childList);
		
		/*
		 * 15. ajax는 기존과 다르게 HttpSerlvetRequest로 내릴 수 없고
		 *     HttpServletResponse를 이용해 내러야 한다.
		 *     화면으로 내릴때 한글이 깨져서 나온다.
		 *     ajax는 통신방법이 틀려서 톰캣과 연관이 없음, 이건 컨트롤러에서 response에 다국어 처리를 해줘야 된다. 
		 */
		response.setCharacterEncoding("utf-8");
		
		// 16. 화면으로 데이터를 출력하기 위해  작성한다.
		PrintWriter out = response.getWriter();
		
		/*
		 * 17. 현재 resultMap에는 result와 childList가 들어가 있으나 형태는 Map 타입으로 들어가있다.
		 *     out.write를 통해서 최종적으로 화면에 sql을 통해 가져온 데이터를 보내고 싶으나 
		 *     화면에서 받을 수 있는 데이터 타입은 Json Object String Type으로 보내줘야 하기 때문에
		 *     HashMapToJson 메서드를 이용한다.
		 */ 
		String resultMapToJson = JsonUtil.HashMapToJson(resultMap);
		
		System.out.println("**** resultMapToJson : " + resultMapToJson);
		
		// 18. 최종적으로 화면에 Json Object String Type으로 데이터를 내린다. 다음은 화면에서의 작업
		out.write(resultMapToJson);
		
		System.out.println("*** resultMap : " + resultMap);
	}
}
