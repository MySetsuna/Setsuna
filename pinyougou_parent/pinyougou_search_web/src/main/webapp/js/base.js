//声明模块
var app = angular.module("pinyougou",[]);

//html信任过滤器
app.filter("trustHtml",['$sce',function ($sce) {
    //data为被转换的内容
    return function (data) {
        //返回转换后的内容
        return $sce.trustAsHtml(data);
    }
}]);
