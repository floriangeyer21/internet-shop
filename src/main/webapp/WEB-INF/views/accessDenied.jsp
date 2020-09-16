<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>AccessDenied</title>
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
            opacity: unset;
            background: url(${pageContext.request.contextPath}/resources/unnamed.jpg) no-repeat;-moz-background-size: 100%; /* Firefox 3.6+ */
            -webkit-background-size: 100%; /* Safari 3.1+ и Chrome 4.0+ */
            -o-background-size: 100%; /* Opera 9.6+ */
            background-size: cover; /* Современные браузеры */
        }
    </style>
</head>
<body class="body1">
<link href="${pageContext.request.contextPath}/resources/style4.css" rel="stylesheet" type="text/css">
<h1 class="font">Sorry, you don't have permission access to this page.</h1>
<div class="font : font1">
    <a href="${pageContext.request.contextPath}/">To main page</a>
</div>
</body>
</html>