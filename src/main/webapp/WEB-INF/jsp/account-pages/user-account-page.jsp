<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>

<%@ include file="../page-elems/title.jsp" %>
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
<header>
    <%@ include file="../page-elems/buttons-in-header.jsp" %>
</header>
<body>

<div class="not_footer">
    <div id="user_account_page">
        <div class="body_center_flexbox">
            <div class="logged_user_profile">
                <div>
                    <h4 class="greeting_message">
                        Добро пожаловать,
                        <c:choose>
                            <c:when test="${not empty sessionScope.auth.name}">
                                ${sessionScope.auth.name}
                            </c:when>
                        </c:choose>
                    </h4>
                </div>
                <div class="alerts">
                    <c:if test="${not empty sessionScope.authError}">
                        <div class="alert alert-danger">${sessionScope.authError}</div>
                        <c:remove var="authError" scope="session"/>
                    </c:if>

                    <c:if test="${not empty sessionScope.changeNameError}">
                        <div class="alert alert-danger">${sessionScope.changeNameError}</div>
                        <c:remove var="changeNameError" scope="session"/>
                    </c:if>

                    <c:if test="${not empty sessionScope.changeEmailError}">
                        <div class="alert alert-danger">${sessionScope.changeEmailError}</div>
                        <c:remove var="changeEmailError" scope="session"/>
                    </c:if>

                    <c:if test="${not empty sessionScope.changePasswordError}">
                        <div class="alert alert-danger">${sessionScope.changePasswordError}</div>
                        <c:remove var="changePasswordError" scope="session"/>
                    </c:if>

                    <c:if test="${not empty sessionScope.changeAccountSuccess}">
                        <div class="alert alert-success">${sessionScope.changeAccountSuccess}</div>
                        <c:remove var="changeAccountSuccess" scope="session"/>
                    </c:if>

                    <c:if test="${not empty sessionScope.changeAccountError}">
                        <div class="alert alert-danger">${sessionScope.changeAccountError}</div>
                        <c:remove var="changeAccountError" scope="session"/>
                    </c:if>
                </div>
                <div class="photo_text_align">
                    <div id="userPic">
                        <img src="${pageContext.request.contextPath}/images/userPic.png" alt="Фото пользователя">
                        <br>
                        <a href="Controller?command=SHOW_STUB_PAGE">Заглушка</a>
                    </div>
                    <div class="logged_user_text_data">

                        <form action="Controller" method="post">
                            <div class="account_button" id="user_buttons">
                                <input type="hidden" name="formType" value="account">
                                <button type="submit" name="command" value="GO_TO_CHANGE_FORM">Изменить информацию в
                                    личном
                                    кабинете
                                </button>
                                <button type="submit" name="command" value="SHOW_STUB_PAGE">Просмотреть избранные
                                    новости
                                </button>
                                <button type="submit" name="command" value="SHOW_STUB_PAGE">Просмотреть все
                                    оставленные комментарии
                                </button>
                            </div>
                        </form>

                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="footer">
    <%@ include file="../page-elems/footer.jsp" %>
</div>
</body>

</html>
