<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ include file="/WEB-INF/view/layout/header.jsp"%>
    <!-- TODO 여기서부터 main영역 -->
    <div class="col-sm-8">
      <h2>로그인</h2>
      <h5>어서오세요</h5>
      <div>
		<form action="/user/sign-in" method="post">
		  <div class="form-group">
		    <label for="username">username</label>
		    <input type="text" class="form-control" placeholder="Enter username" id="username" name="username" value="길동">
		  </div>
		  <div class="form-group">
		    <label for="pwd">Password:</label>
		    <input type="password" class="form-control" placeholder="Enter password" id="pwd" name="password" value="1234">
		  </div>
		  <div class="form-group form-check">
		    <label class="form-check-label">
		      <input class="form-check-input" type="checkbox"> Remember me
		    </label>
		  </div>
		  <button type="submit" class="btn btn-primary">로그인</button>
		  <a href="https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=7704f1d9216c56f7151b9a631f727027&redirect_uri=http://localhost:80/user/kakao-callback">
		  	<img alt="" src="/images/kakao_login_small.png" width="75" height="40">
		  </a>
		</form>
      </div>
    </div>
  </div>
</div>

<%@ include file="/WEB-INF/view/layout/footer.jsp"%>