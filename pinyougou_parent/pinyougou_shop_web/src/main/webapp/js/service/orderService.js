//服务层
app.service('orderService',function ($http) {
    /**
     * 商家商品订单查询
     * @returns {*}
     */
    //读取数据到当前数据
    this.findByParentId=function () {
        return $http.get("../form/search.do");
    }

    this.findByQueryId=function (orderIdStr) {
        return $http.get("../from/findByQueryId.do？orderIdStr="+orderIdStr);
    }

    //时间段每日销售额折线图展现
    this.searchDaySale=function (startTime,endTime) {
        return $http.get("../form/searchDaySale.do?start="+startTime+"&end="+endTime);
    }

    //读取列表数据绑定到表单中
    this.findAll=function(){
        return $http.get('../form/findAll.do');
    }

    //更改订单状态
    this.updateStatus=function(ids,status){
        return $http.get('../form/updateStatus.do?ids='+ids+"&status="+status);
    }
    //搜索分页
    this.search=function(page,rows,searchEntity){
        return $http.post('../form/searchAndPaging.do?page='+page+"&rows="+rows, searchEntity);
    }

    //各商品销售额时间段统计
    this.searchDayGoodsSale=function (startTime,endTime) {
        return $http.get("../form/searchDayGoodsSale.do?start="+startTime+"&end="+endTime);
    }
})