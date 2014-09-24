<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="<c:url value="/resources/css/style.css" />" rel="stylesheet">
<title>Confirmation</title>
</head>
<body>
	<div id="wrapper">
		<%@ include file="/resources/templates/header.jsp" %>
		<div id="body" class="container">
			<h1>Confirmation</h1>
			<br />
			<p>The <c:out value="${thingDeleted}" /> with ID = <c:out value="${deletedObject.getId()}" /> has been successfully deleted.</p>
		</div>
		<%@ include file="/resources/templates/footer.jsp" %>
	</div>
</body>
</html>