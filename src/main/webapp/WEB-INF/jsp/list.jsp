<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="path" value="${requestScope['javax.servlet.forward.servlet_path']}" />
<html>
<head>
    <title>Title</title>
</head>
<body>
<table>
    <thead>
    <tr>
        <td>제목</td>
        <td>링크</td>
        <td>작성일</td>
        <td>작성자</td>
        <td>내용</td>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${pagingInfo.items}" var="notice" varStatus="status">
        <tr>
            <td> ${notice.title} </td>
            <td> ${notice.link} </td>
            <td> ${notice.writedate} </td>
            <td> ${notice.writer} </td>
            <td> ${notice.content} </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<div class="pagingArea">
    <c:forEach begin="${pagingInfo.beginPage}" end="${pagingInfo.endPage}" varStatus="status">
        <a class="${status.index == pagingInfo.curPage ? "on" : ""}"
           href="${status.index}${pathParam}">${status.index + 1}</a>
    </c:forEach>
</div>
</body>
</html>
