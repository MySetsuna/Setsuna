//控制层
app.controller('orderController', function ($scope, $controller, orderService, itemCatService, orderItemService) {

    $controller('baseController', {$scope: $scope});//继承

    //读取列表数据绑定到表单中  
    $scope.findAll = function () {
        orderService.findAll().success(
            function (response) {
                $scope.list = response;
            }
        );
    }

    $scope.findOrderItem = function (orderId) {
        orderItemService.findByOrderId(orderId).success(function (response) {
            $scope.orderItemList = response;
        })
    }

    //分页
    $scope.findPage = function (page, rows) {
        orderService.findPage(page, rows).success(
            function (response) {
                $scope.list = response.rows;
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    }

    //查询实体
    $scope.findOne = function (id) {
        orderService.findOne(id).success(
            function (response) {
                $scope.entity = response;
            }
        );
    }

    //保存
    $scope.save = function () {
        var serviceObject;//服务层对象
        if ($scope.entity.id != null) {//如果有ID
            serviceObject = orderService.update($scope.entity); //修改
        } else {
            serviceObject = orderService.add($scope.entity);//增加
        }
        serviceObject.success(
            function (response) {
                if (response.success) {
                    //重新查询
                    $scope.reloadList();//重新加载
                } else {
                    alert(response.message);
                }
            }
        );
    }


    //批量删除
    $scope.dele = function () {
        //获取选中的复选框
        orderService.dele($scope.selectIds).success(
            function (response) {
                if (response.success) {
                    $scope.reloadList();//刷新列表
                }
            }
        );
    }

    $scope.status = ['', '未付款', '已付款', '未发货', '已发货', '交易成功', '交易关闭', '待评价'];
    $scope.payType = ['', '在线支付', '货到付款'];
    $scope.sourceType = ['', 'app端', 'pc端', 'M端', '微信端', '手机qq端']

    $scope.searchEntity = {propertyMap: {categoryIdList: []}};//定义搜索对象
    //搜索
    $scope.search = function (page, rows) {
        orderService.search(page, rows, $scope.searchEntity).success(
            function (response) {
                $scope.list = response.rows;
                $scope.paginationConf.totalItems = response.total;//更新总记录数

            }
        );
    }
    //统计
    $scope.searchCount = function () {
        // alert(JSON.stringify($scope.searchEntity))
        orderService.searchCount($scope.searchEntity).success(
            function (response) {
                $scope.list = response;
                alert("有销量SKU" + response.length + "个");
                for (var i = 0; i <= $scope.list.length - 1; i++) {
                    $scope.list[i].sumNum = 0;
                    $scope.list[i].sumFee = 0;
                    for (var j = 0; j <= $scope.list[i].tbOrderItemList.length - 1; j++) {
                        $scope.list[i].sumNum += $scope.list[i].tbOrderItemList[j].num;
                        $scope.list[i].sumFee += $scope.list[i].tbOrderItemList[j].totalFee;
                    }

                }

            })
    };
    //生成饼状图
    $scope.brandSumNum = [];
    $scope.brandSumFee = [];
    $scope.itemCat3List = null;
    $scope.createPie = function () {
        if ($scope.itemCat3List == null) {
            alert("请选择分类");
        } else {
            $scope.brandList = [];
            $scope.brandSumNum = [];
            $scope.brandSumFee = [];
            $scope.myChart1 = echarts.init(document.getElementById('main1'));
            $scope.myChart = echarts.init(document.getElementById('main'));
            orderService.createPie($scope.searchEntity).success(
                function (response) {
                    $scope.list = response;

                    for (var i = 0; i < $scope.list.length; i++) {
                        //标题表
                        if ($scope.brandList.lastIndexOf($scope.list[i].brand) >= 0) {
                            continue;
                        } else {
                            $scope.brandList.push($scope.list[i].brand)
                            $scope.brandSumNum.push({value: 0, name: $scope.list[i].brand});
                            $scope.brandSumFee.push({value: 0, name: $scope.list[i].brand});

                        }
                        //$scope.brandSumNum[i].name=$scope.list[i].brand;
                        //$scope.brandSumFee[i].name=$scope.list[i].brand
                    }

                    for (var i = 0; i < $scope.list.length; i++) {
                        //生成数据
                        $scope.list[i].sumNum = 0;
                        $scope.list[i].sumFee = 0;
                        for (var j = 0; j <= $scope.list[i].tbOrderItemList.length - 1; j++) {
                            $scope.list[i].sumNum += $scope.list[i].tbOrderItemList[j].num;
                            $scope.list[i].sumFee += $scope.list[i].tbOrderItemList[j].totalFee;

                        }
                    }


                    // 使用刚指定的配置项和数据显示图表。
                    // //销量图
                    for (var k = 0; k < $scope.brandSumNum.length; k++) {
                        for (var i = 0; i < $scope.list.length; i++) {

                            if ($scope.list[i].brand === $scope.brandSumNum[k].name) {
                                $scope.brandSumNum[k].value += $scope.list[i].sumNum;
                            }
                        }
                    }


                    $scope.option1 = {
                        title: {
                            text: '销量分析(件)',
                            subtext: '同类商品不同品牌',
                            x: 'center'
                        },
                        tooltip: {
                            trigger: 'item',
                            formatter: "{a} <br/>{b} : {c} ({d}%)"
                        },
                        legend: {
                            orient: 'vertical',
                            left: 'left',
                            data: $scope.brandList
                        },
                        series: [
                            {
                                name: '访问来源',
                                type: 'pie',
                                radius: '55%',
                                center: ['50%', '60%'],
                                data: $scope.brandSumNum,
                                itemStyle: {
                                    emphasis: {
                                        shadowBlur: 10,
                                        shadowOffsetX: 0,
                                        shadowColor: 'rgba(0, 0, 0, 0.5)'
                                    }
                                }
                            }
                        ]
                    };
                    $scope.myChart.setOption($scope.option1);

                    //销售额
                    for (var k = 0; k < $scope.brandSumFee.length; k++) {
                        for (var i = 0; i < $scope.list.length; i++) {

                            if ($scope.list[i].brand === $scope.brandSumFee[k].name) {
                                $scope.brandSumFee[k].value += $scope.list[i].sumFee;
                            }
                        }
                        $scope.brandSumFee[k].value = $scope.brandSumFee[k].value.toFixed(2)
                    }


                    $scope.option2 = {
                        color: ['rgb(254,67,101)', 'rgb(252,157,154)', 'rgb(249,205,173)', 'rgb(200,200,169)', 'rgb(131,175,155)'],
                        title: {
                            text: '销售额分析(元)',
                            subtext: '同类商品不同品牌',
                            x: 'center'
                        },
                        tooltip: {
                            trigger: 'item',
                            formatter: "{a} <br/>{b} : {c} ({d}%)"
                        },
                        legend: {
                            orient: 'vertical',
                            left: 'left',
                            data: $scope.brandList
                        },
                        series: [
                            {
                                name: '访问来源',
                                type: 'pie',
                                radius: '55%',
                                center: ['50%', '60%'],
                                data: $scope.brandSumFee,
                                itemStyle: {
                                    emphasis: {
                                        shadowBlur: 10,
                                        shadowOffsetX: 0,
                                        shadowColor: 'rgba(1, 8, 4, 0.5)'
                                    }
                                }
                            }
                        ]
                    };
                    $scope.myChart1.setOption($scope.option2);

                });


        }

    };

    //加载商品分类一级目录
    $scope.selectItemCat1List = function () {
        itemCatService.findByParentId(0).success(function (response) {
            $scope.itemCat1List = response;

        })
        //二级分类加载
        //$watch(监听的变量名，函数(新的值,原来的值))
        $scope.$watch("category1Id", function (newValue, oldValue) {
            itemCatService.findByParentId(newValue).success(function (response) {
                $scope.itemCat2List = response;
                //$scope.entity.goods.category2Id = -1;

                // $scope.itemCat3List = [];
            })
        });
        //三级分类加载
        //$watch(监听的变量名，函数(新的值,原来的值))
        $scope.$watch("category2Id", function (newValue, oldValue) {
            $scope.searchEntity.propertyMap.categoryIdList = [];
            itemCatService.findByParentId(newValue).success(function (response) {
                $scope.itemCat3List = response;
                $scope.searchEntity.propertyMap.categoryIdList.push($scope.category2Id)
                for (var i = 0; i < $scope.itemCat3List.length; i++) {
                    $scope.searchEntity.propertyMap.categoryIdList.push($scope.itemCat3List[i].id);
                }
            })
        });
    }

    $scope.outPutAsXlsx = function (putAll) {
        if (putAll == 1) {
            var order = JSON.stringify($scope.searchEntity).replace(/(\")/g, '\'');

        } else {
            var order = null;
        }
        var options = {
            url: '../userOrder/findOrderAndOrderItem.do',
            data: {Order: order},
            method: 'post'
        };


        DownLoadFile(options)
    }
    DownLoadFile = function (options) {
        var config = $.extend(true, {method: 'post'}, options);
        var $iframe = $('<iframe id="down-file-iframe" />');
        var $form = $('<form target="down-file-iframe" method="' + config.method + '" />');
        $form.attr('action', config.url);
        for (var key in config.data) {
            $form.append('<input type="hidden" name="' + key + '" value="' + config.data[key] + '" />');
        }
        $iframe.append($form);
        $(document.body).append($iframe);
        $form[0].submit();
        $iframe.remove();
    }


    //条件展开
    $scope.openTime = '展开';
    $scope.openField = '展开';

    //
    $scope.propertyTypeName = ['商品名', '商品SKU', '商品分类', '品牌', '商家ID'];
    $scope.timeTypeName = ['年度统计', '历年季度', '历年月份', '自定义区间'];

    $scope.isThree = function (three) {
        three = parseInt(three);

        if (three === 3) {
            return true;
        } else {
            return false;
        }
    }
    $scope.isNotThree = function (three) {
        return !($scope.isThree(three))
    }


});
