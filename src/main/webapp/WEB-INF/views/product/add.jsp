<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add product</title>
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
<h1 class="font">Please, fills form to add new product</h1>
<h4 style="color:red">${message}</h4>
<form method="post" action="${pageContext.request.contextPath}/product/add">
    <div class="login">
        <h1>Add product</h1>
        <p>Please fill in this form to add new product.</p>
        <hr>

        <label for="name"><b>Name</b></label>
        <input type="text" placeholder="Enter name" name="name" id="name" required>

        <label for="price"><b>Price</b></label>
        <input type="text" placeholder="Enter price" name="price" id="price" required>
        <hr>

        <button type="stylesheet" class="/product/add">Add product</button>
    </div>
</form>
<div class="font : font1">
    <a href="${pageContext.request.contextPath}/">To main page</a>
</div>
</body>
</html>
