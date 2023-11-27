<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ include file="/WEB-INF/view/layout/header.jsp"%>
    <!-- TODO 여기서부터 main영역 -->
    <div class="col-sm-8">
      <h2>계좌 생성 페이지(인증)</h2>
      <div class="bg-light p-md-5 h-75">
      	<form action="/account/save" method="post">
		  <div class="form-group">
		    <label for="number">계좌번호</label>
		    <input type="text" class="form-control" placeholder="Enter number" id="number" name="number" value="9999">
		  </div>
		  <div class="form-group">
		    <label for="password">계좌 비밀번호</label>
		    <input type="password" class="form-control" placeholder="Enter password" id="password" name="password" value="1234">
		  </div>
		  <div class="form-group">
		    <label for="balance">입금 금액 : </label>
		    <input type="text" class="form-control" placeholder="Enter balance" id="balance" name="balance" value="10000">
		  </div>
		  <button type="submit" class="btn btn-primary">계좌생성</button>
		</form>
      </div>
    </div>
  </div>
</div>

<%@ include file="/WEB-INF/view/layout/footer.jsp"%>