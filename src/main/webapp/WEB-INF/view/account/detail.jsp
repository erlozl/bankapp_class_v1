<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ include file="/WEB-INF/view/layout/header.jsp"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<style>
	.user--box {
		border : 1px solid black;
		padding : 10px; 
	}
</style>   
    <!-- TODO 여기서부터 main영역 -->
    <div class="col-sm-8">
      <h2>계좌 상세보기 ( 인증 )</h2>
      <div class="bg-light p-md-5 h-75">
      	<div class="user--box">
      		${principal.username}님 계좌<br>
      		계좌번호 : ${account.number} <br>
      		잔액 : ${account.balance}원
      	</div>
      	<br>
      	<div>
      		<a href="/account/detail/${account.id}">전체조회</a>&nbsp;
      		<a href="/account/detail/${account.id}?type=deposit">입금조회</a>&nbsp;
      		<a href="/account/detail/${account.id}?type=withdraw">출금조회</a>&nbsp;
      	</div>
      	</br>
      	<table class="table">
      	
      		<thead>
      			<tr>
      				<th>날짜</th>
      				<th>보낸이</th>
      				<th>받은이</th>
      				<th>입출금금액</th>
      				<th>계좌잔액</th>
      			</tr>
      		</thead>
      		<c:forEach var="historys" items="${historyList}">
      		<tbody>
      			<tr>
      				<td>${historys.formatCreatedAt()}</td>
      				<td>${historys.sender}</td>
      				<td>${historys.receiver}</td>
      				<td>${historys.amount}</td>
      				<td>${historys.formatBalance()}</td>
      			</tr>
      		</tbody>
      		</c:forEach>
      	</table>
      </div>
    </div>
  </div>
</div>

<%@ include file="/WEB-INF/view/layout/footer.jsp"%>