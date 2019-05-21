app.controller("baseController", function ($scope) {
    //分页控件属性配置
    $scope.paginationConf = {
        //当前页
        currentPage: 1,
        //总记录数
        totalItems: 10,
        //每页查询的记录数
        itemsPerPage: 10,
        //分页选项，用于选择每页显示多少条记录
        perPageOptions: [10, 20, 30, 40, 50],
        //当页码变更后触发的函数
        onChange: function () {
            $scope.reloadList();//重新加载
        }
    };
    //1:指向查询页面 2:指向统计页面 3.指向饼状图页面
    $scope.searchType = 0;

    //重新加载数据

    $scope.reloadList = function () {
        //$scope.findPage($scope.paginationConf.currentPage, $scope.paginationConf.itemsPerPage);
        if ($scope.searchType === 0) {
            $scope.search($scope.paginationConf.currentPage, $scope.paginationConf.itemsPerPage);
        }
        if ($scope.searchType === 1) {
            $scope.searchCount();
            //$scope.searchEntity = {dateMap: {timeType: '3', byYear: '='}, propertyMap: {propertyType: '0'}};
            // searchEntity.dateMap.timeType='3';searchEntity.dateMap.byYear='';searchEntity.propertyMap.propertyType='0';
        }
    }

    //选中的id列表
    $scope.selectIds = [];

    //复选更新选中列表
    $scope.updateSelection = function ($event, id) {
        //如果是被选中,则增加到数组
        if ($event.target.checked) {
            $scope.selectIds.push(id);
        } else {
            //查找当前id的下标
            var idx = $scope.selectIds.indexOf(id);
            //删除数据
            $scope.selectIds.splice(idx, 1);
        }
    }

    /**
     * 格式化提取JSON串
     * @param jsonString 要提取的json字符
     * @param key 提取的属性
     */
    $scope.jsonToString = function (jsonString, key) {
        var obj = JSON.parse(jsonString);
        var result = "";
        for (var i = 0; i < obj.length; i++) {
            if (i > 0) {
                result += ",";
            }
            result += obj[i][key];
        }
        return result;
    }


})