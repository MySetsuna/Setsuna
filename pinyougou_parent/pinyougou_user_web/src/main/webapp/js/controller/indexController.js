app.controller("indexController",function ($scope,$location,loginService,payService) {

    //初始化数据
    $scope.initData=function () {
        loginService.loginName().success(function (response) {
            $scope.loginName = response.loginName;
        })
    }
    //查询用户订单
    $scope.findOrderByUserId=function (status) {
        loginService.findOrderByUserId(status).success(
            function (response) {
                $scope.orderList=response;
            }
        )
    }


//提交订单
    $scope.submitOrder=function(order){
        loginService.submitOrder(order).success(
            function(response){
                alert(response.success);
                if(response.success){
                    // var orderResult = JSON.stringify(response);
                   // var orderResult1 = JSON.parse(JSON.stringify(response));
                    //var orderResult = response;
                    var out_trade_no=response.out_trade_no;
                    // var code_urlStr=response.code_url;
                    // code_url.("/","|")
                // .replace(/\//g, '[')
                //     $scope.code_urlStr = response.code_url;
                //     var code_urlStr1 = response.code_url.substring(0, 15);
                //     var code_urlStr2 = response.code_url.substring(15);
                    var code_url = response.code_url.replace(/=/g, '[');
                    // var code_url = JSON.stringify(response.code_url);
                    var total_fee=response.total_fee;
                    window.location.href="pay.html#?out_trade_no="+out_trade_no+"&code_url="+code_url+"&total_fee="+total_fee;
                }
            }
        );
    }



    //生成二维码
    $scope.createNative=function () {
        //生成二维码前先获取微信接口返回的数据
       // var orderResult = JSON.parse($location.search()["orderResult"]);
        $scope.out_trade_no = $location.search()["out_trade_no"];
        $scope.code_url = $location.search()["code_url"].replace(/\[/g, '=');
        // alert($scope.code_url);
        $scope.total_fee =($location.search()["total_fee"]/100).toFixed(2);

        var qrcode = new QRCode(document.getElementById("qrious"), {
            width : 250,
            height : 250,
            correctLevel : QRCode.CorrectLevel.Q,
        });
        qrcode.makeCode($scope.code_url);

        //二维码生成成功后，马上开始查询订单支付状态
        queryPayStatus($scope.out_trade_no);
    }


    //查询订单状态
    queryPayStatus=function (out_trade_no) {
        payService.queryPayStatus(out_trade_no).success(function (response) {
            if(response.success){
                window.location.href = "paysuccess.html#?money=" + $scope.total_fee;
            }else{
                if("支付已超时" == response.message){
                    window.location.href = "paytimeout.html";
                }else{
                    window.location.href = "payfail.html";
                }

            }
        })
    }


    //支付成功页，加载支付金额
    $scope.loadMoney=function () {
        $scope.money = $location.search()["money"];
    }

})