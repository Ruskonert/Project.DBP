<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 11/1/2019
  Time: 6:51 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Redirecting...</title>
</head>
<body>
<%
    Object sessionObject = request.getAttribute("sessionUser");
    if(sessionObject == null) {
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }
    else {
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }
%>
</body>
</html>
