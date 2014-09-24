<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<link href="<c:url value="/resources/css/style.css" />" rel="stylesheet">
<link rel="stylesheet" href="//code.jquery.com/ui/1.10.4/themes/smoothness/jquery-ui.css">
<script src="//code.jquery.com/jquery-1.9.1.js"></script>
<script src="//code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
<script>
$(function() {
$( "#datepicker" ).datepicker( { dateFormat: 'dd/mm/yy' } );
});
</script>
<title>CloudWatch Metrics Collector</title>
</head>
<body>
	<div id="wrapper">
		<%@ include file="/resources/templates/header.jsp" %>
		<div id="body" class="container">
			<form action="<c:url value="/experiments/search" />" method="post">
				<h1>Search for Experiments</h1>
				<br />
				<table>
					<tr>
						<td>Experiment ID: </td>
						<td><input type="text" name="experimentId"></td>
						<td><input type="submit" name="searchType" value="Quick Search"></td>
					</tr>
				</table>
				<br />
				<h3>OR</h3>
				<br />
				<table>
					<tr>
						<td>Auto Scaling Group Name: </td>
						<td><input type="text" name="asgName"></td>
					</tr>
					<tr>
						<td>Load Balancer Name: </td>
						<td><input type="text" name="elbName"></td>
					</tr>
					<tr>
						<td>Date when the Rolling Upgrade is run: </td>
						<td><input type="text" name="date" id="datepicker"></td>
					</tr>
				</table>
				<input type="submit" name="searchType" value="Advanced Search">
			</form>
		</div>
		<%@ include file="/resources/templates/footer.jsp" %>
	</div>
</body>
</html>
