app.controller("itemController",function ($scope,$http) {
    $scope.num = 1;
    //修改数量
    $scope.addNum=function (x) {
        if(x < 1){
            x = 1;
        }
        $scope.num = x;
    }

    //记录用户选择的规格
    $scope.specificationItems={};
    /**
     * 页面规格点击事件
     * @param specName 规格名称
     * @param optionName 选项名称
     */
    $scope.selectSpecification=function (specName,optionName) {
        $scope.specificationItems[specName] = optionName;

        //更新sku信息
        searchSku();
    }

    //加载默认SKU
    $scope.loadSku=function(){
        $scope.sku=skuList[0];
        //注意这里一定要用深克隆
        $scope.specificationItems= JSON.parse(JSON.stringify($scope.sku.spec)) ;
    }


    //匹配两个对象
    matchObject=function(map1,map2){
        for(var k in map1){
            if(map1[k]!=map2[k]){
                return false;
            }
        }
        for(var k in map2){
            if(map2[k]!=map1[k]){
                return false;
            }
        }
        return true;
    }

    //查询SKU
    searchSku=function(){
        for(var i=0;i<skuList.length;i++ ){
            if( matchObject(skuList[i].spec ,$scope.specificationItems ) ){
                $scope.sku=skuList[i];
                return ;
            }
        }
        $scope.sku={id:0,title:'--------',price:0};//如果没有匹配的
    }

    //添加购物车
    $scope.addCart = function () {
        //alert($scope.sku.id + ":" + $scope.num);
        //window.location.href = "http://localhost:8088/cart/addGoodsToCartList.do?itemId="+ $scope.sku.id+"&num=" + $scope.num;
        $http.get("http://localhost:8088/cart/addGoodsToCartList.do?itemId=" +
            + $scope.sku.id + "&num=" + $scope.num,{'withCredentials':true}).success(function (response) {
            if(response.success){
                window.location.href = "http://localhost:8088/cart.html";
            }else {
                alert(response.message);
            }
        })
    };


});