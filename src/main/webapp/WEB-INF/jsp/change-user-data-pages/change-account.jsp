<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="../page-elems/title.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title><%=title %>
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
    <div class="form_to_fill_in">

        <form action="Controller" method="post">
            <label for="newName">Новое имя:</label>
            <input type="text" id="newName" name="newName"  value="${auth.name}">

            <p>Чтобы изменить конфиденциальные данные, такие как email и пароль, требуется сначала прописать старые
                данные, чтобы система убедилась, что доступ в личный кабинет в руках у настоящего владельца.</p>
            <label for="oldEmail">Старый email:</label>
            <input type="email" id="oldEmail" name="oldEmail">
            <label for="newEmail">Новый email:</label>
            <input type="email" id="newEmail" name="newEmail">
            <hr>
            <label for="oldPassword">Старый пароль:</label>
            <input type="text" id="oldPassword" name="oldPassword">
            <label for="newPassword">Новый пароль:</label>
            <input type="text" id="newPassword" name="newPassword">

            <div class="news_button">
                <button type="submit" name="command" value="CHANGE_ACCOUNT">Подтверждаю все изменения</button>
            </div>
        </form>
    </div>
</div>
<div class="footer">
    <%@ include file="../page-elems/footer.jsp" %>
</div>
</body>
</html>