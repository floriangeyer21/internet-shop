<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Shop</title>
    <style>
        .center {
            width: 500px;
            padding: 10px;
            margin: auto;
        }
        .font {
            color: #7FFF00;
            font-size: 200%;
            font-family: Arial, Verdana, sans-serif;
            text-align: center;
        }
    </style>
</head>
<body>
<link href="${pageContext.request.contextPath}/resources/style4.css" rel="stylesheet" type="text/css">
<div class="center">
<ul class="square">
    <li><a href="${pageContext.request.contextPath}/injectData">Inject test data into the DB</a></li>
    <li><a href="${pageContext.request.contextPath}/users/all">All users</a></li>
    <li><a href="${pageContext.request.contextPath}/product/all">All products</a></li>
    <li><a href="${pageContext.request.contextPath}/shoppingCart/products/">Shopping cart</a></li>
    <li><a href="${pageContext.request.contextPath}/product/add">Add products</a></li>
    <li><a href="${pageContext.request.contextPath}/registration">Create account</a></li>
</ul>
</div>
</body>
</html>
