app.service("loginService",function ($http) {
    this.loginName=function () {
        return $http.get("login/info.do");
    }
    this.findOrderByUserId=function (status) {
        return $http.get("login/findOrderByUserId.do?status="+status);
    }

    //提交订单
    this.submitOrder=function(order){
        return $http.post('pay/submitOrder.do?',order);
    }
})