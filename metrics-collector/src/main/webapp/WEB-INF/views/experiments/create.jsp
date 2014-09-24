<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="<c:url value="/resources/css/style.css" />" rel="stylesheet">
<link rel="stylesheet" href="//code.jquery.com/ui/1.10.4/themes/smoothness/jquery-ui.css">
<link rel="stylesheet" href="<c:url value="/resources/css/timepicker.css" />">
<script src="<c:url value="/resources/js/jquery-1.11.0.min.js" />"></script>
<script src="//code.jquery.com/jquery-1.9.1.js"></script>
<script src="//code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
<script src="<c:url value="/resources/js/timepicker.js" />"></script>
<script>
$(function() {
$('#startPicker').datetimepicker({
	dateFormat: 'dd/mm/yy',
	timeFormat: 'HH:mm z'
});
$('#endPicker').datetimepicker({
	dateFormat: 'dd/mm/yy',
	timeFormat: 'HH:mm z'
});
$('#rollingStartPicker').datetimepicker({
	dateFormat: 'dd/mm/yy',
	timeFormat: 'HH:mm z'
});
$('#rollingEndPicker').datetimepicker({
	dateFormat: 'dd/mm/yy',
	timeFormat: 'HH:mm z'
});
});
</script>
<title>Create Experiment</title>
</head>
<body>
	<div id="wrapper">
		<%@ include file="/resources/templates/header.jsp" %>
		<div id="body" class="container">
			<h1>Please specify Experiment settings:</h1>
			<br />
			<form method="post" action="<c:url value="/experiments/create" />">
			<table>
			<tr>
			<td>Start Time:</td>
			<td><input type="text" name="dataCollectionStart" id="startPicker"></td>
			</tr>
			<tr>
			<td>End Time:</td>
			<td><input type="text" name="dataCollectionEnd" id="endPicker"></td>
			</tr>
			<tr><td><br /></td></tr>
			<tr>
			<td colspan="2"><b>The rest of the fields are optional:</b></td>
			</tr>
			<tr><td><br /></td></tr>
			<tr>
			<td>Auto Scaling Group Name:</td>
			<td><input type="text" name="asgName"></td>
			</tr>
			<tr>
			<td>Load Balancer Name:</td>
			<td><input type="text" name="elbName"></td>
			</tr>
			<tr>
			<td>Total Instances:</td>
			<td><input type="text" name="totalInstances"></td>
			</tr>
			<tr>
			<td>Rolling Upgrade Start Time:</td>
			<td><input type="text" name="rollingStart" id="rollingStartPicker"></td>
			</tr>
			<tr>
			<td>Rolling Upgrade End Time:</td>
			<td><input type="text" name="rollingEnd" id="rollingEndPicker"></td>
			</tr>
			<tr>
			<td>Number of Concurrent Upgrades:</td>
			<td><input type="text" name="concurrentUpgrades"></td>
			</tr>
			</table>
			<br />
			<input type="submit" value="Create Experiment">
			</form>
		</div>
		<%@ include file="/resources/templates/footer.jsp" %>
	</div>
</body>
</html>