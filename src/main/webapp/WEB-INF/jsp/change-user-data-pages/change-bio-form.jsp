<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ include file="../page-elems/title.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

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
            <label for="newBio">Отредактируйте биографию:</label>
            <textarea id="newBio" name="newBio" required><c:choose><c:when
                    test="${not empty sessionScope.updatedBio}">${sessionScope.updatedBio}</c:when><c:otherwise>${sessionScope.bio}</c:otherwise></c:choose></textarea>
            <div class = "wide_button">
                <button type="submit" name="command" value="CHANGE_BIO">Готово</button>
            </div>
        </form>
    </div>
</div>
<div class="footer">
    <%@ include file="../page-elems/footer.jsp" %>
</div>
</body>
</html>