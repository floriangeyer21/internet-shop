
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Registration</title>
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
<h1 class="font">Hello! Please provide your user details</h1>
<h4 style="color:red">${message}</h4>

<form method="post" action="${pageContext.request.contextPath}/registration">
    <h1 class="font">Register</h1>
    <div class="login">
        <p>Please fill in this form to create an account.</p>
        <hr>
        <label for="name"><b>Name</b></label>
        <input class="orange-btn" type="text" placeholder="Enter name" name="name" id="name" required>

        <label for="login"><b>Login</b></label>
        <input type="text" placeholder="Enter login" name="login" id="login" required>
        <hr>

        <label for="psw"><b>Password</b></label>
        <input type="password" placeholder="Enter Password" name="psw" id="psw" required>

        <label for="psw-repeat"><b>Repeat Password</b></label>
        <input type="password" placeholder="Repeat Password" name="psw-repeat" id="psw-repeat" required>
        <hr>

        <button type="submit" class="/registration">Submit</button>
    </div>
</form>


<div class="font">
<a href="${pageContext.request.contextPath}/">To main page</a>
</div>
</body>
</html>


