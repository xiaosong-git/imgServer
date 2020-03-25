<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="/WEB-INF/lib/tld/c.tld"%>

<!DOCTYPE html>
<html lang="en">
	<head>
	    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
		<meta charset="utf-8" />
		<%
//String basePath = request.getScheme() + "://" + request.getServerName() + (request.getServerPort()==80?"":(":" + request.getServerPort())) + request.getContextPath() + "/";
String basePath = request.getContextPath();
request.setAttribute("basePath", basePath);

response.addHeader("Pragma", "No-Cache");
response.addHeader("Cache-Control", "no-cache");
response.addDateHeader("Expires", -1);
%>
	<script src="<%=basePath%>/resource/js/jquery-1.8.3.min.js"></script>
	<script src="<%=basePath%>/resource/js/jquery.js"></script>
	
	</head> 
<body class="no-skin">

    <div class="main-container" id="main-container"> 
     
<form id= "uploadForm">  
      <p >指定文件名： <input type="text" name="filename" value= ""/></p >  
      <p >上传文件： <input type="file" name="file"/></p>  
      <input type="button" value="上传" onclick="doUpload()" />  
</form>  
			
	</div>
</body>
<script type="text/javascript">

function doUpload() {  
    var formData = new FormData($( "#uploadForm" )[0]);  
    $.ajax({  
         url: "<%=basePath%>/goldccm/image/gainData?type=1" ,  
         type: 'POST',  
         data: formData,  
         async: false,  
         cache: false,  
         contentType: false,  
         processData: false,  
         success: function (returndata) {  
             alert(returndata);  
         },  
         error: function (returndata) {  
             alert(returndata);  
         }  
    });  
}  
</script>

</html>
