<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<script>
	$(document).ready(function() {
		var pageName = "<c:out value="${param.pageName}"/>";
		 
		if (pageName != "") {
			$(".menu").removeClass("active");
			$("#" + pageName).addClass("active");
		}
		
		$(".menu").click(function() {
			var menu = $(this).attr("id");
			left.pageSubmitFn(menu);	
		});
	});
	
	var left = {
			pageSubmitFn : function(menu) {
				$("#frm").attr("action", menu + ".do");
				$("#pageName").val(menu);
				$("#frm").submit();
			}		
		};
		
</script>

<form id="frm">
	<input type="hidden" id="pageName" name="pageName"/>
</form>

<div class="sidebar" data-color="orange" data-image="img/full-screen-image-3.jpg">
     <div class="logo">
        <a href="https://hanqea.co.kr/" class="logo-text">
            한큐에자바
        </a>
    </div>
	<div class="logo logo-mini">
		<a href="https://hanqea.co.kr/" class="logo-text">
			한큐에자바
		</a>
	</div>
   	<div class="sidebar-wrapper">
        <div class="user">
            <div class="photo">
                <img src="img/default-hanq.png" />
            </div>
            <div class="info">
                <a data-toggle="collapse" href="#collapseExample" class="collapsed">
                    Hanqea
                    <b class="caret"></b>
                </a>
                <div class="collapse" id="collapseExample">
                    <ul class="nav">
                        <li><a href="#">My Profile</a></li>
                        <li><a href="#">Edit Profile</a></li>
                        <li><a href="#">Settings</a></li>
                    </ul>
                </div>
            </div>
        </div>
        <ul class="nav">
            <li id="main" class="menu active">
                <a href="#">
                    <i class="pe-7s-home"></i>
                    <p>Home</p>
                </a>
            </li>
            <li id="selectBox" class="menu">
                <a href="#">
                    <i class="pe-7s-refresh-2"></i>
                    <p>멀티 셀렉트박스</p>
                </a>
            </li>
        </ul>
   	</div>
</div>
