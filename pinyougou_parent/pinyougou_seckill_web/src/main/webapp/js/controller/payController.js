app.controller("payController",function ($scope,$location,payService) {

    //生成二维码
    $scope.createNative=function () {
        payService.createNative().success(function (response) {
            //计算支付金额转换为元单位，保留两位小数
            $scope.money = (response.total_fee / 100).toFixed(2);
            $scope.out_trade_no = response.out_trade_no;  //订单号

            //生成二维码
            /*var qr = new QRious({
                element: document.getElementById('qrious'),
                size: 260,
                value: response.code_url,
                level: 'L'
            });*/
            var qrcode = new QRCode(document.getElementById("qrious"), {
                width : 250,
                height : 250,
                correctLevel : QRCode.CorrectLevel.Q,
            });
            qrcode.makeCode(response.code_url);

            //二维码生成成功后，马上开始查询订单支付状态
            queryPayStatus($scope.out_trade_no);
        })
    }

    //查询订单状态
    queryPayStatus=function (out_trade_no) {
        payService.queryPayStatus(out_trade_no).success(function (response) {
            if(response.success){
                window.location.href = "paysuccess.html#?money=" + $scope.money;
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
});