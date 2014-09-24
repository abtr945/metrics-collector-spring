<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="<c:url value="/resources/css/style.css" />" rel="stylesheet">
<title>List of Experiments</title>
</head>
<body>
	<div id="wrapper">
		<%@ include file="/resources/templates/header.jsp" %>
		<div id="body" class="container">
			<h1>List of Experiments matching the search criteria:</h1>
			<br />
			<table cellspacing="10" cellpadding="10">
				<tr>
					<td><b>ID</b></td>
					<td><b>Data Collection Start Time</b></td>
					<td><b>Data Collection End Time</b></td>
				</tr>
				<c:forEach var="experiment" items="${experiments.getExperiments()}">
					<tr>
						<td><a href="<c:url value="/experiments/${experiment.getId()}" />"><c:out value="${experiment.getId()}"></c:out></a></td>
						<td><c:out value="${experiment.getDataCollectionStartTime()}"></c:out></td>
						<td><c:out value="${experiment.getDataCollectionEndTime()}"></c:out></td>
					</tr>
				</c:forEach>
			</table>
		</div>
		<%@ include file="/resources/templates/footer.jsp" %>
	</div>
</body>
</html>