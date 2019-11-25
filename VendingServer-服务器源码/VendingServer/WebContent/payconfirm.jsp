<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, minimum-scale=1, user-scalable=no, minimal-ui">
<title>确认支付</title>
</head>
<body>
	<form action="pay" method="post">
		<input type="hidden" name="check-name" value="<%=request.getParameter("qr")%>">
		<input type="hidden" name="terminal-uuid" value="<%=request.getParameter("uuid")%>"/>
		<input type="hidden" name="action" value="confirm"/>
		<input type="hidden" name="version" value="4"/>
		<input type="hidden" name="position" value="<%=request.getParameter("position")%>"/>
		<input type='submit' name='submit'/>
	</form>
</body>
</html>