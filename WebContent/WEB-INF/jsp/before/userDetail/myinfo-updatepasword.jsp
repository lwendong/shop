<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String imgPath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+"/";
%>
<jsp:include page="myinfo.jsp"></jsp:include>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<base href="<%=basePath%>">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<script src="<%=path%>/jquery/jquery.min.js"></script>
<link href="<%=path%>/css/before/myinfo-manager.css" rel="stylesheet" type="text/css" />
<title>我的信息</title>
<script type="text/javascript">
<%-- var baselocation='<%=basePath%>';
var bruser='${bruser}'; --%>
</script>
</head>
<body>
<div class="modal" id="myModal">
    <div class="modal-header">
      <h3 class="modal-title">修改密码</h3>
    </div>
    <input type="hidden" id="id">
    <div class="modal-body">
    	<table>
    		<tr>
    			<td class="table_one">用户名:</td>
    			<td><input type="text" class="form-control" id="username"></td>
    		</tr>
    		<tr>
    			<td class="table_one">电子邮箱:</td>
    			<td><input type="text" class="form-control" id="email"></td>
    		</tr>
    		<tr>
    			<td class="table_one">密码:</td>
    			<td><input type="text" class="form-control" id="password"></td>
    		</tr>
    	</table>
		<button class="btn-info"  title="loadReport" onclick="updateInfo()">修改</button>
    </div>
</div>
<script type="text/javascript">
	function loadReportFormatter(value, row, index) {//格式化
	    var id = value;
	    var result = "";
	    result += '<button class="btn-info"  onclick="updateUserInfo(' + id + ')">修改</button>';
	    return result;
	}
	function updateUserInfo(id){
		var data=$.parseJSON(bruser);
		$("#username").val(data[0].name);
		$("#email").val(data[0].bemail);
		$("#password").val(data[0].bpwd);
		$("#id").val(data[0].id);
	}
	function updateInfo(){
		var user={};
		var email = $("#email").val();
		var name = $("#username").val();
		var pwd = $("#password").val();
		var reg = /^[a-zA-Z0-9_-]+@([a-zA-Z0-9]+\.)+(com|cn|net|org)$/;
		//邮箱不为空
		if(email != null && name != "" && pwd != ""){
			//检测邮箱格式
			if(reg.test(email)==false){
				alert("邮箱格式不正确！");
				return false;
			}
		}else{
			alert("没有输入密码或用户名或邮箱！");
			return false;
		}
		
		user.name=name;
		user.bemail=email;
		user.bpwd=password;
		user.id=$("#id").val();
		$.ajax({
	        url: baselocation + 'updatePassWordInfo',
	        type: 'post',
	        dateType:"json",
	        data:JSON.stringify(user),
	        contentType: 'application/json; charset=UTF-8',
	        success: function (retuslt) {
	            alert(JSON.stringify(user));
	        },
	        error: function (XMLHttpRequest, textStatus, errorThrown) {
	            console.log(textStatus, errorThrown);
	            alert("系统繁忙，请稍后再操作！");
	        }
	    });		
	}
	
	
	
</script>
</body>
</html>