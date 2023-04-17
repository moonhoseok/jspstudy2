<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시물 상세보기</title>
</head>
<body>
<div class="w3-container">
<h2>${boardName }</h2>
<table class="w3-table-all">

<tr>
	<th width="20%">
		글쓴이
	</th>
		<td width="80%" style="text-align: left;">
			${b.writer}
		</td>
</tr>

<tr>
	<th>
		제목
	</th>
		<td style="text-align: left;">
			${b.title}
		</td>
</tr>
	
<tr>
	<th>
		내용
	</th>
		<td style="width: 100%; height: 250px;">
<table>
			<tr>
				<td style="border-width: 0px; 
					vertical-align: top; text-align: left;">
					${b.content }
				</td>
</tr>
</table>
		</td>
			</tr>

<tr>
	<th>첨부파일</th>
	
		<td><c:if test="${b.file1 ==null || b.file1 =='' }">
				&nbsp;	
			</c:if>
			<%--첨부파일이 존재 --%>
			<c:if test="${!empty b.file1}">
				<a href="upload/board/${b.file1}">${b.file1}</a>
			</c:if>
		</td>
</tr>
<tr>
	<td colspan="2">
		<a href="replyForm?num=${b.num}">[답변]</a>
	
		
		<c:if test ="${!boardid != '1' || sessionScope.login == 'admin' }">	
			<a href="updateForm?num=${b.num }">[수정]</a>
			<a href="deleteForm?num=${b.num}">[삭제]</a>
		</c:if>
		
		<a href="list">[목록]</a>
	</td>
</tr>

</table>
</div>
</body>
</html>