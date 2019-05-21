app.controller("cartController",function ($scope,cartService) {
    //查询购物车列表
    $scope.findCartList= function () {
        cartService.findCartList().success(function (response) {
            $scope.cartList = response;

            //计算总数量与总金额
            $scope.totalValue={totalNum:0,totalMoney:0};
            for(var i = 0; i < response.length; i++){
                var itemList = response[i].orderItemList;
                for(var j = 0; j < itemList.length; j++){
                    //统计数量
                    $scope.totalValue.totalNum += itemList[j].num;
                    //统计金额
                    $scope.totalValue.totalMoney += itemList[j].totalFee;
                }
            }
        });
    }

    //修改购物车
    $scope.addGoodsToCartList=function (itemId,num) {
        cartService.addGoodsToCartList(itemId,num).success(function (response) {
            if(response.success){
                //刷新数据
                $scope.findCartList();
            }else{
                alert(response.message);
            }
        })
    }

    //获取登录用户的收件人列表
    $scope.findAddressList=function () {
        cartService.findAddressList().success(function (response) {
            //默认勾选地址
            for(var i = 0; i < response.length; i++){
                if(response[i].isDefault == "1"){
                    $scope.address = response[i];
                    break;
                }
            }
            $scope.addressList = response;
        })
    }

    //用户勾选地址
    $scope.selectAddress=function (address) {
        $scope.address = address;
    }

    //订单传参对象
    $scope.order={paymentType:1};

    //修改支付方式
    $scope.selectPayType=function (type) {
        $scope.order.paymentType = type;
    }

    /**
     * 保存订单
     */
    $scope.submitOrder=function () {
        $scope.order.receiverAreaName = $scope.address.address;//地址
        $scope.order.receiverMobile = $scope.address.mobile;//手机
        $scope.order.receiver = $scope.address.contact;//联系人

        cartService.submitOrder($scope.order).success(function (response) {
            alert(response.message);
            if(response.success){
                //如果是在线支付
                if($scope.order.paymentType == "1"){
                    window.location.href = "pay.html";
                }else{
                    window.location.href = "paysuccess.html";
                }
            }
        })
    }

    //加入我的關注
    $scope.addGoodsToMyFavorite=function (itemId) {
        cartService.addGoodsToMyFavorite(itemId).success(function (response) {
                alert(response.message);
        })
    }


});