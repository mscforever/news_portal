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
    <div class="body_flexbox">
        <div class="header_and_news">
            <div class="rotated_part">
                <h3>Новостной агрегатор</h3>
            </div>
            <div class="not_rotated_part">
                <h1>
                    Все новости, <br>которые Вы боялись упустить
                </h1>
                <div class="news_block">
                    <!-- переменная regularNewsSize = длина переданного списка -->
                    <c:set var="regularNewsSize" value="${fn:length(regularNews)}"/>
                    <!-- флаг isBreakingNews = true, если в переменной breakingNews передалось что-то-->
                    <c:set var="isBreakingNews" value="${not empty breakingNews}"/>
                    <!-- флаг isTopNews = true, если в переменной topNews передалось что-то-->
                    <c:set var="isTopNews" value="${not empty topNews}"/>

                    <div class="left_news_block">
                        <div class="news_item">
                            <c:choose>
                                <c:when test="${isBreakingNews}">
                                    <c:if test="${not empty topNews}">
                                        <div class="image-container news-item">
                                            <a
                                                    href="Controller?command=GO_TO_NEWS_PAGE&newsId=${topNews.newsId}">
                                                <img src="${topNews.imageUrl}" alt="Top News Image"/>
                                                <h4 class="news_header">
                                                    <c:out value="${topNews.title}"/>
                                                </h4>
                                            </a>
                                        </div>
                                    </c:if>
                                    <c:forEach var="index" begin="0"
                                               end="${(regularNewsSize > 2 ? 1 : regularNewsSize - 1)}">
                                        <c:if test="${regularNewsSize - 1 - index >= 0}">
                                            <div class="image-container news-item">
                                                <a
                                                        href="Controller?command=GO_TO_NEWS_PAGE&newsId=${regularNews[regularNewsSize - 1 - index].newsId}">
                                                    <img
                                                            src="${regularNews[regularNewsSize - 1 - index].imageUrl}"
                                                            alt="Regular News Image"/>
                                                    <h4 class="news_header">
                                                        <c:out
                                                                value="${regularNews[regularNewsSize - 1 - index].title}"/>
                                                    </h4>
                                                </a>
                                            </div>
                                        </c:if>
                                    </c:forEach>
                                </c:when>
                                <c:when test="${isTopNews}">
                                    <c:forEach var="index" begin="0"
                                               end="${(regularNewsSize > 3 ? 2 : regularNewsSize - 1)}">
                                        <c:if test="${regularNewsSize - 1 - index >= 0}">
                                            <div class="image-container news-item">
                                                <a
                                                        href="Controller?command=GO_TO_NEWS_PAGE&newsId=${regularNews[regularNewsSize - 1 - index].newsId}">
                                                    <img
                                                            src="${regularNews[regularNewsSize - 1 - index].imageUrl}"
                                                            alt="Regular News Image"/>
                                                    <h4 class="news_header">
                                                        <c:out
                                                                value="${regularNews[regularNewsSize - 1 - index].title}"/>
                                                    </h4>
                                            </div>
                                        </c:if>
                                    </c:forEach>
                                </c:when>
                            </c:choose>
                        </div>
                    </div>

                    <div class="center_news_block">
                        <div class="news_item">
                            <div class="image-container">
                                <c:choose>
                                    <c:when test="${isBreakingNews}">
                                        <a
                                            href="Controller?command=GO_TO_NEWS_PAGE&newsId=${breakingNews.newsId}">
                                            <img src="${breakingNews.imageUrl}"
                                                 alt="Breaking News Image"/>
                                        </a>
                                    </c:when>
                                    <c:when test="${isTopNews}">
                                        <a
                                             href="Controller?command=GO_TO_NEWS_PAGE&newsId=${topNews.newsId}">
                                            <img src="${topNews.imageUrl}" alt="Top News Image"/>
                                        </a>
                                    </c:when>
                                </c:choose>
                            </div>
                            <div>
                                <h4 class="news_header">
                                    <c:choose>
                                        <c:when test="${isBreakingNews}">
                                            <div class="breaking_news">
                                                <c:out value="${breakingNews.title}"/>
                                            </div>
                                        </c:when>
                                        <c:when test="${isTopNews}">
                                            <c:out value="${topNews.title}"/>
                                        </c:when>
                                    </c:choose>
                                </h4>
                            </div>
                            <div class="news_short_description-container">
                                <p class="news_short_description">
                                    <c:choose>
                                        <c:when test="${isBreakingNews}">
                                            <c:out value="${breakingNews.brief}"/>
                                        </c:when>
                                        <c:when test="${isTopNews}">
                                            <c:out value="${topNews.brief}"/>
                                        </c:when>
                                    </c:choose>
                                </p>
                            </div>
                        </div>
                    </div>

                    <div class="right_news_block">
                        <div class="news_item">
                            <c:choose>
                                <c:when test="${isBreakingNews}">
                                    <c:forEach var="index" begin="2"
                                               end="${(regularNewsSize > 5 ? 4 : regularNewsSize - 1)}">
                                        <c:if test="${regularNewsSize - 1 - index >= 0}">
                                            <div class="image-container news-item">
                                                <a
                                                        href="Controller?command=GO_TO_NEWS_PAGE&newsId=${regularNews[regularNewsSize - 1 - index].newsId}">
                                                    <img
                                                            src="${regularNews[regularNewsSize - 1 - index].imageUrl}"
                                                            alt="Regular News Image"/>
                                                    <h4 class="news_header">${regularNews[regularNewsSize - 1 - index].title}</h4>
                                                </a>
                                            </div>
                                        </c:if>
                                    </c:forEach>
                                </c:when>
                                <c:when test="${isTopNews}">
                                    <c:forEach var="index" begin="3"
                                               end="${(regularNewsSize > 4 ? 5 : regularNewsSize - 1)}">
                                        <c:if test="${regularNewsSize - 1 - index >= 0}">
                                            <div class="image-container news-item">
                                                <a
                                                        href="Controller?command=GO_TO_NEWS_PAGE&newsId=${regularNews[regularNewsSize - 1 - index].newsId}">
                                                    <img
                                                            src="${regularNews[regularNewsSize - 1 - index].imageUrl}"
                                                            alt="Regular News Image"/>
                                                    <h4 class="news_header">${regularNews[regularNewsSize - 1 - index].title}</h4>
                                                </a>
                                            </div>
                                        </c:if>
                                    </c:forEach>
                                </c:when>
                            </c:choose>
                        </div>
                    </div>
                </div>
                <form class="news_button">
                    <button type="submit" name="command" value="SHOW_ALL_NEWS">Показать все новости</button>
                </form>
            </div>
        </div>

        <div class="email-admin-form">
            <div class="white-back">
                <form action="Controller" method="post"
                      enctype="multipart/form-data">
                    <label for="email">Email:</label>
                    <!-- если передается сообщение об ошибке, то в значение поля записывается то, что было передано ранее -->
                    <input type="email" id="email" name="email"
                           value="${not (sessionScope.errorMessage eq null) ? sessionScope.email : ''}"
                           required> <br> <label for="message">Текст
                    сообщения:</label>
                    <!-- если передается сообщение об ошибке, то в значение поля записывается то, что было передано ранее -->
                    <textarea id="message" name="message" rows="4"
                              required>${not (sessionScope.errorMessage eq null) ? requestScope.message : ''}</textarea>
                    <br> <label for="inputFile">Прикрепите файл в формате
                    .png или .pdf:</label> <input type="file" id="inputFile" name="inputFile">
                    <br>
                    <div>
                        <!-- ищем атрибут в области сессии, НЕ ЗАПРОСА -->
                        <c:if test="${not (sessionScope.successMessage eq null)}">
                            <div class="alert alert-success">${sessionScope.successMessage}</div>
                            <!-- чтобы после отображения оповещения при повторном вводе данных не дублировались оповещения -->
                            <c:remove var="successMessage" scope="session"/>
                        </c:if>
                        <!-- ищем атрибут в области сессии, НЕ ЗАПРОСА -->
                        <c:if test="${not (sessionScope.errorMessage eq null)}">
                            <div class="alert alert-danger">${sessionScope.errorMessage}</div>
                            <!-- чтобы после отображения оповещения при повторном вводе данных не дублировались оповещения -->
                            <c:remove var="errorMessage" scope="session"/>
                        </c:if>
                    </div>

                    <button type="submit" name="command" value="WRITE_ADMIN">Отправить</button>
                </form>
            </div>
        </div>
    </div>
</div>

<div class="footer">
    <%@ include file="page-elems/footer.jsp" %>
</div>
</body>
</html>
