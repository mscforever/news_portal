<%@ page import="by.romanenko.web_project.model.Auth" %>
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
<body>


<div id="author_account_page">
    <header>
        <%@ include file="../page-elems/buttons-in-header.jsp" %>
    </header>
    <div class="body_center_flexbox">

        <div class="logged_user_profile">

            <div>
                <h4 class="greeting_message">
                    Добро пожаловать, <c:if test="${not empty sessionScope.auth.name}">
                    ${sessionScope.auth.name}
                </c:if>!
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

                <div id="authorPic">
                    <img src="${pageContext.request.contextPath}/images/authorPic.png" alt="Фото автора">
                    <br>
                    <a href="Controller?command=SHOW_STUB_PAGE">Заглушка</a>
                </div>
                <div class="logged_user_text_data">
                    <div>
                        <form action="Controller" method="post">
                            <div class="account_button" id="author_buttons">
                                <input type="hidden" name="formType" value="account">
                                <button type="submit" name="command" value="GO_TO_CHANGE_FORM">Изменить информацию в личном
                                    кабинете
                                </button>
                                <button type="submit" name="command" value="GO_TO_ADD_NEWS_FORM_PAGE">Добавить новость
                                </button>
                                <button type="submit" name="command" value="SHOW_ALL_AUTHOR_NEWS">Просмотреть все
                                    написанные статьи
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
    <div class="bio_desc">
        <h4>Биография</h4>
        <c:choose>
            <c:when test="${not empty sessionScope.updatedBio}">
                ${sessionScope.updatedBio}
            </c:when>
            <c:otherwise>
                ${sessionScope.bio}
            </c:otherwise>
        </c:choose>
        <br>
        <a href="Controller?command=GO_TO_CHANGE_FORM&formType=bio">Изменить биографию</a>
        <c:if test="${not empty sessionScope.changeBioSuccess}">
            <div class="alert alert-success">${sessionScope.changeBioSuccess}</div>
            <c:remove var="changeBioSuccess" scope="session"/>
        </c:if>
        <c:if test="${not empty sessionScope.changeBioError}">
            <div class="alert alert-danger">${sessionScope.changeBioError}</div>
            <c:remove var="changeBioError" scope="session"/>
        </c:if>
    </div>
</div>
</body>
<div class="footer">
    <%@ include file="../page-elems/footer.jsp" %>
</div>
</html>
