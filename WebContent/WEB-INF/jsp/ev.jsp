<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>ev</title>
</head>
<body>
	<h3>Prices</h3>
	<c:forEach var="stock" items="${stocks}" >
		<li>
			<c:out value="${stock.symbol}" />
			<c:out value="${stock.open}" />
			<c:out value="${stock.high}" />
			<c:out value="${stock.low}" />
			<c:out value="${stock.close}" />
			
		</li>	
	</c:forEach>
</body>
</html>