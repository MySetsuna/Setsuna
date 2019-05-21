//购物车服务层
app.service('cartService',function($http){
    //购物车列表
    this.findCartList=function(){
        return $http.get('cart/findCartList.do');
    }

    //添加商品到购物车
    this.addGoodsToCartList=function(itemId,num){
        return $http.get('cart/addGoodsToCartList.do?itemId='+itemId+'&num='+num);
    }

    //获取地址列表
    this.findAddressList=function(){
        return $http.get('address/findListByLoginUser.do');
    }

    this.submitOrder=function (order) {
        return $http.post("order/add.do",order);
    }

    this.addGoodsToMyFavorite=function (itemId) {
        return $http.post("cart/addGoodsToMyFavorite.do?itemId="+itemId);
    }

});
