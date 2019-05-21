app.controller("searchController",function ($scope,$location,searchService) {
    //$scope.searchMap = {keywords:""};
    /**
     * 搜索对象
     * @param {{keywords: 关键字, category: 商品分类, brand: 品牌, spec: {'网络'：'移动4G','机身内存':'64G'}
     * ,price:价格区间,,'pageNo':当前页,'pageSize':每页查询的记录数，'sortField':排序的域,'sort':排序方式asc|desc }}
     */
    $scope.searchMap={'keywords':'','category':'','brand':'','spec':{},'price':'',
        'pageNo':1,'pageSize':40,'sortField':'','sort':'' };

    $scope.resultMap={brandIds:[]};

    //初始化数据
    $scope.initMap = function () {
        $scope.searchMap.category = '';
        $scope.searchMap.brand = '';
        $scope.searchMap.spec = {};
        $scope.searchMap.price = '';
        $scope.searchMap.pageNo = 1;
        $scope.searchMap.pageSize = 40;
    }
    //搜索商品
    $scope.search=function () {
        searchService.search($scope.searchMap).success(function (response) {
            $scope.resultMap = response;

            //刷新分页标签
            buildPageLabel();
        })
    }

    /**
     * 页码跳转事件
     * @param page 当前要跳转到的页码
     */
    $scope.queryByPage=function (page) {
        //这一步非常关键
        page = parseInt(page);
        if(page < 1 || page > $scope.resultMap.totalPages){
            alert("请输入正确页码！");
            return;
        }
        $scope.searchMap.pageNo = page;
        //刷新数据
        $scope.search();
    }

    /**
     * 构建分页标签
     */
    buildPageLabel=function () {
        //记录总共有多少页
        $scope.pageLable=[];
        var firstPage = 1;  //开始页码
        var endPage = $scope.resultMap.totalPages;  //结束页码

        $scope.firstDot=true;//前面有点
        $scope.lastDot=true;//后边有点

        //控制标签输出结果
        if($scope.resultMap.totalPages > 5){
            //如果当前页码 <= 3
            if($scope.searchMap.pageNo <= 3){
                //显示前5页
                endPage = 5;
                $scope.firstDot=false;//前面有点
            //100  96 97 98 99 100
            //如果当前页在后3页,如果当前页码 >= (总页数-2)
            }else if($scope.searchMap.pageNo >= ($scope.resultMap.totalPages - 2)){
                //显示后5页
                firstPage = $scope.resultMap.totalPages - 4;
                $scope.lastDot=false;//后边有点
            }else{  //正常跳页
                //当前页码为中心
                firstPage = $scope.searchMap.pageNo - 2;
                endPage = $scope.searchMap.pageNo + 2;

                $scope.firstDot=true;//前面有点
                $scope.lastDot=true;//后边有点
            }
        }else{
            $scope.firstDot=false;//前面有点
            $scope.lastDot=false;//后边有点
        }
        for(var i = firstPage; i <= endPage; i++){
            $scope.pageLable.push(i);
        }
    }


    /**
     * 添加搜索项
     * @param key 操作的属性名
     * @param value 操作的属性值
     */
    $scope.addSearchItem=function (key,value) {
        //添加品牌与分类
        if(key == "category" || key == "brand" || key == 'price'){
            $scope.searchMap[key] = value;
        }else{
            $scope.searchMap.spec[key] = value;
        }
        //刷新数据
        $scope.search();
    }
    /**
     * 删除搜索项
     * @param key
     */
    $scope.removeSearchItem=function (key) {
        //添加品牌与分类
        if(key == "category" || key == "brand" || key == 'price'){
            $scope.searchMap[key] = "";
        }else{
            //删除属性
            delete $scope.searchMap.spec[key];
        }
        //刷新数据
        $scope.search();
    }

    /**
     * 排序查询
     * @param sortField 排序的域
     * @param sort 排序的方式asc|desc
     */
    $scope.sortSearch=function (sortField,sort) {
        $scope.searchMap.sortField = sortField;
        $scope.searchMap.sort = sort;
        //刷新数据
        $scope.search();
    }



    /**
     * 识别关键字是否包含品牌
     */
    $scope.keywordsIsBrand=function () {
        for(var i = 0; i < $scope.resultMap.brandIds.length; i++){
            if($scope.searchMap.keywords == $scope.resultMap.brandIds[i].text){
                return true;
            }
        }
        return false;
    }

    /**
     * 接收其它页面跳转过来的查询请求
     */
    $scope.loadKeywords=function () {
        var keywords = $location.search()["keywords"];
        if (keywords != null) {
            $scope.searchMap.keywords = keywords;
            $scope.search();
        }
    }

    $scope.addGoodsToFootmark=function (goodsId) {
        searchService.addGoodsToFootmark(goodsId);
    }
})