<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ include file="page-elems/title.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title><%=title%>
    </title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link
            href="https://fonts.googleapis.com/css2?family=Bad+Script&family=Caveat:wght@400..700&family=Montserrat:ital,wght@0,100..900;1,100..900&family=Oswald:wght@200..700&display=swap"
            rel="stylesheet">
    <link rel="stylesheet" type="text/css"
          href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="not_footer">
    <header>
        <%@ include file="page-elems/buttons-in-header.jsp" %>
    </header>
    <div class="form_to_fill_in">

        <form action="Controller" method="post">
            <c:if test="${not (sessionScope.regSuccess eq null)}">
                <div class="alert alert-success">${sessionScope.regSuccess}</div>
                <c:remove var="regSuccess" scope="session"/>
            </c:if>

            <c:if test="${not(sessionScope.logoutSuccess eq null)}">
                <h1> ${sessionScope.logoutSuccess}</h1>
                <c:remove var="logoutSuccess" scope="session"/>
            </c:if>

            <c:if test="${not (sessionScope.logoutFail eq null)}">
                <div class="alert alert-danger">${sessionScope.logoutFail}</div>
                <c:remove var="logoutFail" scope="session"/>
            </c:if>
            <h1>Пожалуйста, войдите</h1>
            <label><input type="email" name="email"
                          placeholder="Email адрес" required/></label> <br> <label><input
                type="password" name="password" placeholder="Пароль" required/></label> <br>
            <label><input type="checkbox" name="rememberMe">Запомнить
                меня</label>
            <c:if test="${not (sessionScope.authError eq null)}">
                <div class="alert alert-danger">${sessionScope.authError}</div>
                <c:remove var="authError" scope="session"/>
            </c:if>
            <button type="submit" name="command" value="DO_AUTH">Войти</button>
            <!-- <a href="Controller?command=GO_TO_REGISTRATION_PAGE">Регистрация
        нового аккаунта</a> -->
        </form>
        <form action="Controller" method="post">
            <button type="submit" name="command" value="SHOW_STUB_PAGE">Я забыл пароль</button>
        </form>
    </div>
</div>
<div class="footer">
    <%@ include file="page-elems/footer.jsp" %>
</div>
</body>
</html>