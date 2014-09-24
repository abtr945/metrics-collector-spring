<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="<c:url value="/resources/css/style.css" />" rel="stylesheet">
<title>AWS Settings</title>
</head>
<body>
	<div id="wrapper">
		<%@ include file="/resources/templates/header_noregion.jsp" %>
		<div id="body" class="container">
			<h1>Change AWS Settings</h1>
			<br />
			<p>Please provide your AWS Credentials and select the AWS Region to continue:</p>
			<form action="<c:url value="/aws/credentials" />" method="post">
				<table>
					<tr>
						<td>AWS Access Key ID: </td>
						<td><input type="text" name="awsAccessKeyId"></td>
					</tr>
					<tr>
						<td>AWS Secret Access Key: </td>
						<td><input type="text" name="awsSecretAccessKey"></td>
					</tr>
					<tr>
						<td>AWS Region: </td>
						<td>
							<select name="region" id="region">
			  					<c:forEach var="r" items="${regions}">
									<option value="${r.value()}"><c:out value="${r.value()}" /></option>
								</c:forEach>
							</select>
						</td>
					</tr> 
					<tr><td colspan="2"><input type="submit" value="Submit"></td></tr>
				</table>
			</form>
		</div>
		<%@ include file="/resources/templates/footer.jsp" %>
	</div>
</body>
</html>