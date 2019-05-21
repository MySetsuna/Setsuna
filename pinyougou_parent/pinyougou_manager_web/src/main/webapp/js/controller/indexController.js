app.controller("indexController",function ($scope,loginService) {
    //获取用户信息
    $scope.getUserInfo=function () {
        loginService.getUserInfo().success(function (response) {
            $scope.loginName = response.loginName;
        })
    }
});