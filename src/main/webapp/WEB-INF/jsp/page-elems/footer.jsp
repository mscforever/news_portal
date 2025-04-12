<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.time.LocalDate"%>
<footer>
	<div>
		<%="@ " + LocalDate.now().getYear() + ", " + request.getAttribute("title")%>
	</div>
	<div></div>

</footer>