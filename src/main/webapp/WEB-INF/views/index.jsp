<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Shop</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" integrity="sha384-JcKb8q3iqJ61gNV9KGb8thSsNjpSL0n8PARn9HuZOnIxN0hoP+VmmDGMN5t9UJ0Z" crossorigin="anonymous">
    <link href="${pageContext.request.contextPath}/resources/style4.css" rel="stylesheet" type="text/css">
    <style>
        .center {
            width: 500px;
            padding: 10px;
            margin: auto;
        }
        a:focus {
            background-color: red;
        }
        .body1 {
            opacity: inherit;
            background: url(${pageContext.request.contextPath}/resources/unnamed.jpg) no-repeat;-moz-background-size: 100%; /* Firefox 3.6+ */
            -webkit-background-size: 100%;
            -o-background-size: 100%;
            background-size: cover;
        }
    </style>
</head>
<body class="body1">

<nav class="navbar navbar-default">
    <div class="center">
        <div class="navbar-header">
            <a href="#" class="navbar-brand">INTERNET-SHOP</a>
        </div>
        <div class="container-fluid">
            <ul class="square">
                <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/injectData">Inject test data into the DB</a></li>
                <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/users/all">All users</a></li>
                <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/product/all">All products</a></li>
                <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/product/all-for-admin">All products for admin</a></li>
                <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/order/all">All user orders</a></li>
                <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/order/all-for-admin">All orders for admin</a></li>
                <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/shoppingCart/products/">Shopping cart</a></li>
                <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/product/add">Add products</a></li>
                <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/registration">Create account</a></li>
            </ul>
        </div>


    </div>
</nav>



<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js" integrity="sha384-DfXdz2htPH0lsSSs5nCTpuj/zy4C+OGpamoFVy38MVBnE+IbbVYUew+OrCXaRkfj" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js" integrity="sha384-9/reFTGAW83EW2RDu2S0VKaIzap3H66lZH81PoYlFhbGU+6BZp6G7niu735Sk7lN" crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js" integrity="sha384-B4gt1jrGC7Jh4AgTPSdUtOBvfO8shuf57BaghqFfPlYxofvL8/KUEfYiJOMMV+rV" crossorigin="anonymous"></script>
</body>
</html>
