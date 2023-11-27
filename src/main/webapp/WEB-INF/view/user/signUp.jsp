<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ include file="/WEB-INF/view/layout/header.jsp"%>
    <!-- TODO 여기서부터 main영역 -->
    <div class="col-sm-8">
      <h2>회원가입</h2>
      <h5>어서오세요</h5>
      <div>
		<form action="/user/sign-up" method="post">
		  <div class="form-group">
		    <label for="username">username</label>
		    <input type="text" class="form-control" placeholder="Enter username" id="username" name="username">
		  </div>
		  <div class="form-group">
		    <label for="pwd">Password:</label>
		    <input type="password" class="form-control" placeholder="Enter password" id="pwd" name="password">
		  </div>
		  <div class="form-group">
		    <label for="fullname">fullname:</label>
		    <input type="text" class="form-control" placeholder="Enter fullname" id="fullname" name="fullname">
		  </div>
		  <div class="form-group form-check">
		    <label class="form-check-label">
		      <input class="form-check-input" type="checkbox"> Remember me
		    </label>
		  </div>
		  <button type="submit" class="btn btn-primary">회원가입</button>
		</form>
      </div>
    </div>
  </div>
</div>

<%@ include file="/WEB-INF/view/layout/footer.jsp"%>