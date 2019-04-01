<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<script>
	var select = {
			
		displayChildSelectBox : function(childList) {
			
			/* 
				22. each반복문을 이용하여  첫번째 파라미터의 길이만큼 반복하고, i는 인덱스의 길이만큼, item은 키가 아닌 한 줄을 의미하여
					forEach문의 var의 기능을 한다고 생각하면 이해하기 편리하다.
			*/
			if (childList.length > 0) {
				$.each(childList,function(i, item) {
					var optionStr = "<option>" +  item.prdNm + "</option>"
					
					/* 
						23. append 메서드를 이용해 id가 childSelectBox인 selectBox의 맨 마지막 자식태그에 붙인다.
							이러한 행위를 동적으로 HTML을 생성했다고 표현한다.
					*/ 
					$("#childSelectBox").append(optionStr);
				})
			} else {
				$("#childSelectBox").append("<option value=''>없음</option>");
			}
		}
	}
		
	$(function() {
		$("#parentSelectBox").change(function() {

			// 1. 내가 누른 parentSelectBox의 option태그의 value속성의 값인 parentList.brandCd 의 값을 가져온다.
			var thisParam = $(this).val();
			
			// 2. 변수 form에 Json Object String tpye으로 "param"이라는 키에 "parentList.brandCd"의 값을 담아준다.
			var form = {"param" : thisParam};
			
			 $("#childSelectBox").children().remove();
			 
			/* 
				3. ajax를 쓰는 이유는 화면을 새로 호출할 필요 없이 데이터를 전송해야 하기 때문이다.
				4. type을 get방식으로 보내게 되면 body에 담아서 보낼수 없어 controller에서 많은 단계를 거쳐야 하며
				      실무상 get방식을 쓰기보다는 post방식을 더 많이 사용하고있다.
				5. data는  xml에서 where조건을 만들기 위해서 화면에서 가져가는 데이터, 내가 컨트롤러로 값을 가져가고싶을 때 쓴다.
				      위에서 정의한 form이란 변수를 컨트롤러로 가져가는데 Json.stringify 메서드를 사용한다.
				   Json.stringify는 Json Object를 Json Object String으로 바꿔주는 것 Json.parse와 정반대의 메소드
				6. content-type속성은  데이터를 컨트롤러로 보낼때 type을 지정해 주는 것으로 만약 content-type을 정해주지 않으면
				   ajax 전처리기가 우리가 원하는대로 인코딩을 하지 못하기 때문에 우리가 원하는 값을 가져오기 위해서 디코딩하는 작업을 해야한다.
				7. success속성은  우리가 컨트롤러에서 데이터를 가져온 결과물이 success의 매개변수로 들어간다.
				     이후 SelectBoxController에서 작업을 한다.
			*/ 
			$.ajax({
				type 		: "post",
				url 		: "childSelectBox.do",
				data 		: JSON.stringify(form), 
				contentType : "application/Json",
				success 	: function(data) {
					
					/*
						19. 먼저 파라미터에 있는 data는 컨트롤러를 통해 가져온
							Json Object String Type의 result와 childList가 들어가 있다.
						20. Json.parse 메서드는 Json Object String Type을 다시 Json Object Type으로 
							변환하는 역할을 한다. 이 메서드를 거치지 않은 데이터들은 단순히 문자열로 인식이되고
							해당 메서드를 거치게되면 해당하는 값들은 맵에 배열로 들어가게 된다.
					*/
					var jObj = JSON.parse(data);
					
					/* 
						21. 두개의 데이터 중 result의 값이 SUCCESS인 조건을 이용하여 데이터가 잘 들어왔는지의 여부를 확인하고
						    select 객체의 displayChildSelectBox에 파라미터 jObj.childList를 넣어준다.
					*/
					if (jObj.result === "SUCCESS") { 
						select.displayChildSelectBox(jObj.childList);
					}
				}
			})
		});
	})
</script>
<div class="content">
	<div class="container-fluid">
      	<div class="row">
          	<div class="col-md-12">
              	<div class="card ">
	                <div class="header">
	                    <h4 class="title">셀렉트박스</h4>
	                    <p class="category">ajax 잘 모르고 쓰면 어렵지~</p>
	                </div>
	                <div class="content">
	                	<select id="parentSelectBox" name="parentSelectBox">
	                		<c:forEach items="${parentList}" var="parentList">
	                			<option value="<c:out value='${parentList.brandCd}' />">
	                				<c:out value="${parentList.brandNm}" />
	                			</option>
		                	</c:forEach>
	                	</select>
	                	<select id="childSelectBox" name="childSelectBox">
	                		<option value="">없음</option>
	                	</select>
	                	
	                </div>
                </div>
            </div>
        </div>
    </div>
</div>