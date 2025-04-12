<%@ page import="by.romanenko.web_project.model.Auth" %>
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
</div>


<div class="all_news_page">
    <c:if test="${not empty sessionScope.addNewsSuccess}">
        <div class="alert alert-success">${sessionScope.addNewsSuccess}</div>
        <c:remove var="addNewsSuccess" scope="session"/>
    </c:if>
    <h2>Все новости</h2>
    <c:if test="${not empty sessionScope.changeArticleSuccess}">
        <div class="alert alert-success">${sessionScope.changeArticleSuccess}</div>
        <c:remove var="changeArticleSuccess" scope="session"/>
    </c:if>
    <c:if test="${not empty sessionScope.changeArticleError}">
        <div class="alert alert-danger">${sessionScope.changeArticleError}</div>
        <c:remove var="changeArticleError" scope="session"/>
    </c:if>
    <!-- Проверяем, есть ли новости -->
    <c:if test="${not empty newsAuthorList}">
        <ul>
            <c:forEach var="news" items="${newsAuthorList}">
                <li>
                    <div class="news_preview">
                        <div>
                            <img src="${news.imageUrl}" alt="Изображение новости"/>
                        </div>
                        <div>
                            <!--<strong>${news.newsId}</strong><br/>-->
                            <strong>${news.title}</strong><br/>
                            <em>${news.category}</em><br/>
                            <p>${news.brief}</p><br/>
                        </div>
                    </div>
                    <p>${news.content}</p>
                    <!-- Отображаем кнопку только для зарегистрированных авторов -->
                    <c:if test="${sessionScope.role == 'author'}">
                        <form action="Controller" method="POST">
                            <input type="hidden" name="newsId" value="${news.newsId}"/>
                            <input type="hidden" name="formType" value="newsArticle"/>
                            <button type="submit" name="command" value="GO_TO_CHANGE_FORM">Изменить</button>
                        </form>
                    </c:if>
                    <hr/>
                </li>
            </c:forEach>
        </ul>
    </c:if>

    <!-- Если нет новостей -->
    <c:if test="${empty newsAuthorList}">
        <p>Нет новостей для отображения.</p>
    </c:if></div>
<div class="footer">
    <%@ include file="page-elems/footer.jsp" %>
</div>
</body>
</html>
