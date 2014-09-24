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
$( "#collectaccordion" ).accordion({
	collapsible: true, active: false
});
$( "#aggregatedmetricsaccordion" ).accordion({
	collapsible: true, active: false
});
$( "#custommetricsaccordion" ).accordion({
	collapsible: true, active: false
});
});
</script>
<title>Experiment Details</title>
</head>
<body>
	<div id="wrapper">
		<%@ include file="/resources/templates/header.jsp" %>
		<div id="body" class="container">
			<h1>Experiment Details</h1>
			<br />
			<table>
				<tr><td><b>Experiment ID: </b></td><td><c:out value="${experiment.getId()}"></c:out></td></tr>
				<tr><td><b>Data Collection Start Time: </b></td><td><c:out value="${experiment.getDataCollectionStartTime()}"></c:out></td></tr>
				<tr><td><b>Data Collection End Time: </b></td><td><c:out value="${experiment.getDataCollectionEndTime()}"></c:out></td></tr>
				<tr><td><br /></td></tr>
				<tr><td><b>Auto Scaling Group Name: </b></td><td><c:out value="${experiment.getAsgName()}"></c:out></td></tr>
				<tr><td><b>Load Balancer Name: </b></td><td><c:out value="${experiment.getElbName()}"></c:out></td></tr>
				<tr><td><b>Rolling Upgrade Start Time: </b></td><td><c:out value="${experiment.getRollingStartTime()}"></c:out></td></tr>
				<tr><td><b>Rolling Upgrade End Time: </b></td><td><c:out value="${experiment.getRollingEndTime()}"></c:out></td></tr>
			</table>
			<c:if test="${experiment.getMetrics().size() == 0}">
				<br />
				<form action="<c:url value="/experiments/delete/${experiment.getId()}" />" method="post">
					<input type="submit" value="Delete this Experiment">
				</form>
			</c:if>
			<br />
			
			<p>There are <c:out value="${noAggregatedMetrics}"></c:out> Aggregated metric(s) and <c:out value="${noCustomMetrics}"></c:out> Custom metric(s) associated with this experiment.</p>
			
			<div id="collectaccordion">
				<c:if test="${noAggregatedMetrics == 0}">
					<h3>Collect Aggregated Metrics</h3>
					<form action="<c:url value="/metrics/aggregated" />" method="post">
						<input type="hidden" name="experimentId" value="${experiment.getId()}">
						<input type="submit" value="Collect all Aggregated Metrics">
					</form>
				</c:if>
				
				<h3>Create a Custom Metric</h3>
				<form action="<c:url value="/metrics/custom" />" method="post">
					<input type="hidden" name="experimentId" value="${experiment.getId()}">
					<table>
						<tr>
							<td>Metric Name:</td>
							<td><input type="text" name="metricName"></td>
						</tr>
						<tr>
							<td>Metric Namespace:</td>
							<td>
								<select name="namespace" id="namespace">
									<c:forEach var="n" items="${namespaces}">
										<option value="${n.getNamespace()}"><c:out value="${n}" /></option>
									</c:forEach>
								</select>
							</td>
						</tr>
						<tr>
							<td>Metric Dimension Key:</td>
							<td><input type="text" name="dimensionKey"></td>
						</tr>
						<tr>
							<td>Metric Dimension Value:</td>
							<td><input type="text" name="dimensionValue"></td>
						</tr>
						<tr>
							<td>Collection Start time:</td>
							<td><input type="text" name="start" id="startPicker"></td>
						</tr>
						<tr>
							<td>Collection End time:</td>
							<td><input type="text" name="end" id="endPicker"></td>
						</tr>
						<tr>
							<td>Collection Period (in seconds):</td>
							<td><input type="text" name="period"></td>
						</tr>
						<tr>
							<td colspan="2"><input type="submit" value="Create and Collect Custom Metric"></td>
						</tr>
					</table>
				</form>
			</div>
			
			<br />
			
			<c:if test="${experiment.getMetrics().size() > 0}">
				<p><a href="<c:url value="/metrics/download?experimentId=${experiment.getId()}&metricId=-1" />">Download consolidated metric data for this experiment</a></p>
				<h2>List of metrics</h2>
				<br /><h3>Aggregated Metrics:</h3><br />
				<div id="aggregatedmetricsaccordion">
					<c:forEach var="metric" items="${experiment.getMetrics()}">
						<c:if test="${metric.getType() == 'aggregated'}">
							<h3><c:out value="${metric.getName()}"></c:out></h3>
							<div>
								<table>
									<tr><td><b>Namespace: </b></td><td><c:out value="${metric.getNamespace()}"></c:out></td></tr>
									<tr><td><b>Statistics: </b></td><td><c:out value="${metric.getStatistics()}"></c:out></td></tr>
									<tr><td><b>Data Sample Interval: </b></td><td><c:out value="${metric.getPeriod()}"></c:out> seconds</td></tr>
								</table>
								<br />
								<p>Data in JSON format: <a href="<c:url value="/metrics/viewdata?experimentId=${experiment.getId()}&metricId=${metric.getId()}" />">View</a> <a href="<c:url value="/metrics/download?experimentId=${experiment.getId()}&metricId=${metric.getId()}" />">Download</a></p>
								Plot metric graphs:<br />
								<form action="<c:url value="/metrics/chart/${metric.getId()}" />" method="post">
									<c:forEach var="stat" items="${metric.getStatistics().split(\", \")}">
										<c:if test="${stat ne 'SampleCount'}">
											<input type="checkbox" name="statistics" value="${stat}"><c:out value="${stat}"></c:out>
										</c:if>
									</c:forEach>
									<br />
									<input type="submit" value="Plot Graph">
								</form>
								<br />
								<form action="<c:url value="/metrics/delete/${metric.getId()}" />" method="post">
									<input type="submit" value="Delete Metric">
								</form>
							</div>
						</c:if>
					</c:forEach>
				</div>
				<br /><h3>Custom Metrics:</h3><br />
				<div id="custommetricsaccordion">
					<c:forEach var="metric" items="${experiment.getMetrics()}">
						<c:if test="${metric.getType() == 'custom'}">
							<h3><c:out value="${metric.getName()}"></c:out></h3>
							<div>
								<table>
									<tr><td><b>Namespace: </b></td><td><c:out value="${metric.getNamespace()}"></c:out></td></tr>
									<tr><td><b>Dimensions: </b></td><td><c:out value="${metric.getDimensions()}"></c:out></td></tr>
									<tr><td><b>From: </b></td><td><c:out value="${metric.getStartTime()}"></c:out></td></tr>
									<tr><td><b>Until: </b></td><td><c:out value="${metric.getEndTime()}"></c:out></td></tr>
									<tr><td><b>Statistics: </b></td><td><c:out value="${metric.getStatistics()}"></c:out></td></tr>
									<tr><td><b>Data Sample Interval: </b></td><td><c:out value="${metric.getPeriod()}"></c:out> seconds</td></tr>
								</table>
								<p>Data in JSON format: <a href="<c:url value="/metrics/viewdata?experimentId=${experiment.getId()}&metricId=${metric.getId()}" />">View</a> <a href="<c:url value="/metrics/download?experimentId=${experiment.getId()}&metricId=${metric.getId()}" />">Download</a></p>
								<form action="<c:url value="/metrics/delete/${metric.getId()}" />" method="post">
									<input type="submit" value="Delete Metric">
								</form>
							</div>
						</c:if>
					</c:forEach>
				</div>
			</c:if>
		</div>
		<%@ include file="/resources/templates/footer.jsp" %>
	</div>
</body>
</html>