//控制层
app.controller('orderController', function ($scope, $controller, orderService) {
    $controller('baseController', {$scope: $scope});//继承

    //读取列表数据到表单中
    // $scope.findByParentId=function () {
    //     orderService.findByParentId().success(function (response) {
    //         $scope.list=response;
    //     });
    //
    // }

    //读取列表数据绑定到表单中
    $scope.findByParentId = function () {
        orderService.findByParentId().success(
            function (response) {
                $scope.list = response;
            }
        );
    }
    //
    $scope.findByOrderId = function (orderIdStr) {
        orderService.findByOrderId(orderIdStr).success(
            function (response) {
                $scope.orderItemList = response;
            }
        );
    }
    //修改
    $scope.modification = function (OrderId) {
        orderService.findByParentId(OrderId).success(
            function (response) {
                $scope.modificationorderid = response;
            }
        )
    }

    //查询
    $scope.findByQueryId = function (orderId) {
        orderService.findByQueryId(orderId).success(
            function (response) {
                $scope.findByQueryId = response;
            }
        )
    }


    /**
     * 显示状态 已未付款','已付款','未发货','已发货','交易成功','交易关闭','待评价'的状态
     */
    // $scope.status = ['未付款', '已付款', '未发货', '已发货', '交易成功', '交易关闭', '待评价'];//商品状态


    //默认显示
    /* var arrTime = ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'];
     var arrSale = [10000];
     var dom = document.getElementById("container");
     var myChart = echarts.init(dom);
     option = {
         xAxis: {
             type: 'category',
             data: arrTime//每天的时间
         },
         yAxis: {
             type: 'value'
         },
         series: [{
             data: arrSale,//日销售额数据
             type: 'line',
         }]
     };
     if (option && typeof option === "object") {
         myChart.setOption(option, true);
     }
 */

    //折线图查找日销售额
    $scope.searchDaySale = function () {
        orderService.searchDaySale($scope.startTime, $scope.endTime).success(
            function (response) {
                //清空元素
                arrTime = [];
                arrSale = [];
                var dayAndSaleMap = response
                for (var key in dayAndSaleMap) {
                    arrTime.push(key)
                    arrSale.push(dayAndSaleMap[key])
                }
                var dom = document.getElementById("container");
                var myChart = echarts.init(dom);
                option = {
                    xAxis: {
                        type: 'category',
                        data: arrTime//每天的时间
                    },
                    yAxis: {
                        type: 'value'
                    },
                    series: [{
                        data: arrSale,//日销售额数据
                        type: 'line',
                        // smooth: true
                        // areaStyle: {}
                    }]
                };
                if (option && typeof option === "object") {
                    myChart.setOption(option, true);
                }
            }
        )
    }


    //读取列表数据绑定到表单中
    $scope.findAll = function () {
        orderService.findAll().success(
            function (response) {
                $scope.list = response;
            }
        );
    }


    //订单状态
    $scope.status = ['角标占位', '未付款', '已付款', '未发货', '已发货', '交易成功', '交易关闭', '待评价'];

    //订单发货
    $scope.updateStatus = function (status) {
        orderService.updateStatus($scope.selectIds, status).success(function (response) {
            alert(response.message);
            if (response.success) {
                $scope.reloadList();
                //清空发货列表
                $scope.selectIds = [];
            }
        })
    }

    $scope.searchEntity = {};//定义搜索对象

    //搜索
    $scope.search = function (page, rows) {
        orderService.search(page, rows, $scope.searchEntity).success(
            function (response) {
                $scope.list = response.rows;
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    }


    //查找各商品销售额
    $scope.searchDayGoodsSale = function () {
        orderService.searchDayGoodsSale($scope.startTime, $scope.endTime).success(
            function (response) {
                $scope.itemList = response;
            }
        );
    }
})