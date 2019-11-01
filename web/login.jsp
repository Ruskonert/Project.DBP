<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 11/1/2019
  Time: 5:58 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <link rel="stylesheet" href="css/reset.css">
    <link rel="stylesheet" href="css/style.css" media="screen" type="text/css" />
    <title>로그인</title>
</head>
<body>
    <div class="wrap">
        <%
            String message = (String)request.getAttribute("error_message");
            if(message != null) {
        %>
        <style>
            p {
                color: red;
                font-family: 순천향체, monospace;
            }
        </style>
        <div>
            <p><%= message %></p>
        </div>
        <%
            request.setAttribute("error_message", null);
        }
        %>
        <div class="avatar">
            <img src="https://images.vexels.com/media/users/3/147101/isolated/preview/b4a49d4b864c74bb73de63f080ad7930-instagram-profile-button-by-vexels.png" alt="avatar">
        </div>
        <form method="post">
            <label>
                <input type="text" placeholder="username" required>
            </label>
            <div class="bar">
                <i></i>
            </div>
            <label>
                <input type="password" placeholder="password" required>
            </label>
            <button type="submit">Sign in</button>
        </form>
    </div>
    <div>

    </div>
    <script src="js/login.js"></script>
</body>
</html>
