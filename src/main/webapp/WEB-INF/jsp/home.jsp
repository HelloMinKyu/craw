<%--
  Created by IntelliJ IDEA.
  User: JHR_Office
  Date: 2022-03-07
  Time: 오후 3:51
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<h3>가호동 열린마당 공지사항</h3>
<form action="/noticecrawling" method="post">
    <input type="text" name="PAGE_INDEX" placeholder="몇번째 페이지?">
    <br>
    <button type="submit" value="시작">시작</button>
</form>

<h3>가호동 열린마당 자유게시판</h3>
<form action="/freecrawling" method="post">
    <input type="text" name="PAGE_INDEX" placeholder="몇번째 페이지?">
    <br>
    <button type="submit" value="시작">시작</button>
</form>

<h3>가호동 주민차지센터 주민차지위원회</h3>
<form action="/citicommitteecrawling" method="post">
    <input type="text" name="PAGE_INDEX" placeholder="몇번째 페이지?">
    <br>
    <button type="submit" value="시작">시작</button>
</form>

<h3>가호동 주민차지센터 프로그램/행사안내</h3>
<form action="/eventeecrawling" method="post">
    <input type="text" name="PAGE_INDEX" placeholder="몇번째 페이지?">
    <br>
    <button type="submit" value="시작">시작</button>
</form>

</body>
</html>
