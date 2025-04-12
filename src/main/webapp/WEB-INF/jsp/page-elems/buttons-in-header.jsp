<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<div class="header_buttons">
    <div class="enter_button">
        <form action="Controller" method="Get">
            <button type="submit" name="command" value="GO_TO_INDEX_PAGE">Главная</button>
        </form>
    </div>

    <div>
        <!-- Показать кнопки, если пользователь авторизован -->
        <c:if test="${not empty sessionScope.auth}">
            <div class="enter_button">
                <form action="Controller" method="Get">
                    <c:set var="commandValue" value="GO_TO_${sessionScope.auth.getRole()}_ACCOUNT_PAGE" />
                    <button type="submit" name="command" value="${commandValue}">Личный кабинет</button>

                    <button type="submit" name="command" value="LOGOUT">Выйти</button>
                </form>
            </div>
        </c:if>

        <!-- Показать кнопки, если пользователь не авторизован -->
        <c:if test="${empty sessionScope.auth}">
            <div class="enter_button">
                <form action="Controller" method="Get">
                    <button type="submit" name="command" value="GO_TO_AUTHENTICATION_PAGE">Войти</button>

                    <button type="submit" name="command" value="GO_TO_REGISTRATION_PAGE">Регистрация</button>
                </form>
            </div>
        </c:if>
    </div>
</div>