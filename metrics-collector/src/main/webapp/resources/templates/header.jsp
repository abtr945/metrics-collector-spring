<div id="header-wrapper">
	<div id="header" class="container">
		<div id="logo">
			<a href="<c:url value="/" />">CloudWatch Metrics Collector</a>
		</div>
		<div id="menu">
			<ul>
				<li><a href="<c:url value="/menu?command=search" />">Search for Metrics</a></li>
				<li><a href="<c:url value="/menu?command=collect" />">Collect Metrics</a></li>
			</ul>
		</div>
	</div>
</div>
<div id="banner">
	<div class="container">
		<form action="<c:url value="/aws/credentials" />" method="get">
			AWS Credentials: <b><font color="#66FF33">SET</font></b>
    		<input type="submit" value="Change AWS Account" />
		</form>
		<br />
		AWS Region selected:
		<c:choose>
   			<c:when test="${statistics.getSelectedRegion() ne null}">
      			<b><font color="#66FF33"><c:out value="${statistics.getSelectedRegion()}"></c:out></font></b>
   			</c:when>
   			<c:otherwise>
       			<b><font color="#FF0000">NOT SELECTED</font></b>
   			</c:otherwise>
		</c:choose>
		<br />
		<form action="<c:url value="/aws/region" />" method="post">
			<label for="region">Change Region: </label>
				<select name="region" id="region">
					<c:forEach var="r" items="${statistics.getRegions()}">
						<option value="${r.value()}"><c:out value="${r.value()}" /></option>
					</c:forEach>
				</select>
			<input type="submit" value="Change">
		</form>
		<br />
		Total Experiments: <b><c:out value="${statistics.getTotalExperiments()}"></c:out></b>, Total Metrics: <b><c:out value="${statistics.getTotalMetrics()}"></c:out></b>
	</div>
</div>