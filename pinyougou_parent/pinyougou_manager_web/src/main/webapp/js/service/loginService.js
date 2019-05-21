app.service("loginService",function ($http) {
    //获取登录用户信息
    this.getUserInfo=function () {
        return $http.get("../login/name.do");
    }
});