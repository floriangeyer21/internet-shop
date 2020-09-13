<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>User orders</title>
    <link href="${pageContext.request.contextPath}/resources/style4.css" rel="stylesheet" type="text/css">
    <style>
        .font {
            color: #00CED1;
            font-size: 200%;
            font-family: Arial, Verdana, sans-serif;
            text-align: center;
        }
        .font1 {
            font-size: 120%;
        }
        .body1 {
            opacity: inherit;
            background: url(${pageContext.request.contextPath}/resources/unnamed.jpg) no-repeat;-moz-background-size: 100%; /* Firefox 3.6+ */
            -webkit-background-size: 100%; /* Safari 3.1+ и Chrome 4.0+ */
            -o-background-size: 100%; /* Opera 9.6+ */
            background-size: cover; /* Современные браузеры */
        }
        a:focus {
            background-color: red;
        }
    </style>
</head>
<body class="body1">
<h1 class="font">User orders</h1>
<table class="table_dark">
    <tr>
        <th>ID</th>
        <th>Price</th>
        <th>Detail</th>
        <th>Canceling</th>
    </tr>
    <c:forEach var="order" items="${orders}">
        <tr>
            <td>
                <c:out value="${order.id}"/>
            </td>
            <td>
                <c:out value="${order.price}"/>
            </td>
            <td>
                <a href="${pageContext.request.contextPath}/order/getDetail">Detail</a>
            </td>
            <td>
                <a href="${pageContext.request.contextPath}/order/canceling?id=${order.id}">Canceling</a>
            </td>
        </tr>
    </c:forEach>
</table>

<div class="font : font1">
    <a href="${pageContext.request.contextPath}/">To main page</a>
</div>
</body>
</html>
