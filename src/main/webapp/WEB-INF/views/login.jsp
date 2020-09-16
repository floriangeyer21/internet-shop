<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login</title>
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
        a:focus {
            background-color: red;
        }
        .body1 {
            background: url(${pageContext.request.contextPath}/resources/unnamed.jpg) no-repeat;-moz-background-size: 100%;
            -webkit-background-size: 100%; /* Safari 3.1+ и Chrome 4.0+ */
            -o-background-size: 100%; /* Opera 9.6+ */
            background-size: cover; /* Современные браузеры */
        }
    </style>
    <link href="${pageContext.request.contextPath}/resources/style4.css" rel="stylesheet" type="text/css">
    <link href="${pageContext.request.contextPath}/resources/nature.png" rel="stylesheet" type="text/css">
</head>
<body class="body1">
<h1 class="font">Hello! Please provide your login and password to authentication</h1>
<h4 style="color:red" class="font : font1">${errMessage}</h4>
<form method="post" action="${pageContext.request.contextPath}/login">
    <h1 class="font">Sign in</h1>
    <div class="login">
        <p>Please fill in this form to create an account.</p>
        <hr>
        <label for="login"><b>Login</b></label>
        <input type="text" placeholder="Enter login" name="login" required="required" value=${login}>
        <label for="psw"><b>Password</b></label>
        <input type="password" placeholder="Enter Password" name="pwd" required>
        <hr>
        <button type="submit" class="/login">Sign in</button>
    </div>
</form>
<div class="font">
    <a href="${pageContext.request.contextPath}/">To main page</a>
    <h5> or </h5>
    <a href="${pageContext.request.contextPath}/registration">Create new account</a>
</div>
</body>
</html>

