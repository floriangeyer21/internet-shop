<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>All products</title>
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
    </style>
</head>
<body>
<link href="${pageContext.request.contextPath}/resources/style4.css" rel="stylesheet" type="text/css">
<h1 class="font">All products</h1>
<table class="table_dark">
    <tr>
        <th>ID</th>
        <th>Name</th>
        <th>Price</th>
        <th>Buy</th>
        <th>Delete</th>
    </tr>
    <c:forEach var="product" items="${products}">
        <tr>
            <td>
                <c:out value="${product.id}"/>
            </td>
            <td>
                <c:out value="${product.name}"/>
            </td>
            <td>
                <c:out value="${product.price}"/>
            </td>
            <td>
                <a href="${pageContext.request.contextPath}/shoppingCart/products/add?id=${product.id}">Buy</a>
            </td>
            <td>
                <a href="${pageContext.request.contextPath}/product/delete?id=${product.id}">Delete</a>
            </td>
        </tr>
    </c:forEach>
</table>

<div class="font : font1">
    <a href="${pageContext.request.contextPath}/">To main page</a>
</div>
</body>
</html>
