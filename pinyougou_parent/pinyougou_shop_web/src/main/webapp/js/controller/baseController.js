app.controller("baseController", function ($scope, $http) {

    //获取用户信息
    $scope.getUserInfo = function () {
        $http.get("../login/name.do").success(function (response) {
            $scope.loginName = response.loginName;
        })
    }
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

    //重新加载数据
    $scope.reloadList = function () {
        //$scope.findPage($scope.paginationConf.currentPage, $scope.paginationConf.itemsPerPage);
        $scope.search($scope.paginationConf.currentPage, $scope.paginationConf.itemsPerPage);
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

    /**
     * 搜索一个数组中某个属性是否等于某个值
     * @param list 数组
     * @param key 查找的属性名
     * @param value 对比的值
     * @return 查找到的结果，null,代表查找不到
     */
    $scope.searchObjectByKey = function (list, key, value) {
        for (var i = 0; i < list.length; i++) {
            if (list[i][key] == value) {
                return list[i];
            }
        }
        return null;
    }

})