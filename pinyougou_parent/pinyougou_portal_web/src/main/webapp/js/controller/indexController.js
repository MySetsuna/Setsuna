app.controller("indexController",function ($scope,contentService) {

    //声明广告数据:[分类id:[数据列表]]
    $scope.contentList=[];

    //加载所有广告
    $scope.findAllContent=function () {
        //加载轮播图广告
        contentService.findByCategoryId(1).success(function (response) {
            $scope.contentList[1] = response;
        })
        //加载轮播图广告
        contentService.findByCategoryId(2).success(function (response) {
            $scope.contentList[2] = response;
        })
        //加载轮播图广告
        contentService.findByCategoryId(3).success(function (response) {
            $scope.contentList[3] = response;
        })
    }

    $scope.keywords = "";

    $scope.search=function () {
        if($scope.keywords == ""){
            alert("请先输入搜索关键字");
            return;
        }
        window.location.href = "http://localhost:8084/search.html#?keywords="+$scope.keywords;
    }
    //点击分类跳转到搜索页
    $scope.catClick=function (keywords) {
        window.location.href = "http://localhost:8084/search.html#?keywords="+keywords;
    }

    $scope.findAllCats=function () {
        contentService.findAllCats().success(function (response) {
            $scope.cats = response;
        })
    }




});