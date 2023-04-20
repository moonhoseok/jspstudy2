<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%--/jspstudy2/src/main/webapp/view/board/list.jsp
 --%>    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시물 목록보기</title>
</head>
<body>
<div class="w3-container">
<h2>${boardName }</h2>
<table class="w3-table-all">
<c:if test = "${boardcount ==0}" > 
<tr><td colspan="5">등록된 게시글이 없습니다.</td></tr>
</c:if>
<c:if test="${boardcount > 0}">
<tr><td colspan="5" style="text-align: right"> 글 개수 :${boardcount}</td></tr>
<tr><th width="8%">번호</th><th width="50%">제목</th>
	<th width="14%">작성자</th><th width="17%">등록일</th>
	<th width="11%">조회수</th></tr>
<c:forEach var="b" items="${list}">	

<tr><td>${boardnum}</td>
<c:set var="boardnum" value="${boardnum-1 }"/>
	<td style="text-align: left;">
	<%-- 첨부파일 @로 표시하기 --%>
	<c:if test="${!empty b.file1}">
		<a href="../upload/board/${b.file1}">@</a>
	</c:if>
	<c:if test="${empty b.file1 }">&nbsp;&nbsp;&nbsp;
	</c:if>
	<c:if test="${b.grplevel >0}">
		<c:forEach var="i" begin="1" end="${b.grplevel}">
			&nbsp;&nbsp;&nbsp;&nbsp;
		</c:forEach>ㄴ	
	</c:if>
		<a href="info?num=${b.num}">${b.title}</a>
		<%--댓글의 갯수 뱃지 --%>
		<c:if test="${b.commcnt>0 }">
			<a href="info?num=${b.num }#comment"> <%--#comment 부분으로 이동 --%>
				<span class="w3-badge w3-blue w3-tiny">${b.commcnt}</span>
			</a>
		</c:if>
	</td>
	<td>${b.writer}</td>
	
		<fmt:formatDate value ="${today}" pattern="yyy-MM-dd" var="t"/>
		<fmt:formatDate value="${b.regdate}" pattern="yyyy-MM-dd" var="r"/>
	<td><c:if test="${t==r }">
		<fmt:formatDate value="${b.regdate}" pattern="HH:mm:ss"/>
		</c:if>
		<c:if test="${t!=r }">
		<fmt:formatDate value="${b.regdate }" pattern="yyyy-MM-dd HH:mm"/>
		</c:if>
	</td>
 		
	<td>${b.readcnt}</td></tr>
</c:forEach>
<%--페이지 처리하기 --%>
	<tr><td colspan="5" class="w3-center">
		<c:if test="${pageNum<=1 }">[이전]</c:if>
		<c:if test="${pageNum>1}">
			<a href="list?pageNum=${pageNum-1}">[이전]</a>
		</c:if>
		<c:forEach var="a" begin="${startpage}" end="${endpage}">
		
			<c:if test="${a ==pageNum}">[${a}]</c:if>
			<c:if test="${a !=pageNum}">
				<a href="list?pageNum=${a}">[${a}]</a>
			</c:if>
			
		</c:forEach>
			<c:if test="${pageNum >= maxpage }">[다음]</c:if>
			<c:if test="${pageNum < maxpage }">
				<a href="list?pageNum=${pageNum+1 }">[다음]</a>
			</c:if>
	</td></tr>
</c:if>
<tr><td colspan="5" style="text-align: right;">
	<c:if test ="${boardid != '1' || sessionScope.login == 'admin' }">	
		<p align="right"><a href="writeForm">[글쓰기]</a></p>
	</c:if>
</td></tr>
</div>
</table>
</body>
</html>









