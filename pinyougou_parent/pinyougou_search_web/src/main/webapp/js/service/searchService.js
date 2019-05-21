//搜索服务层
app.service("searchService",function($http){
    this.search=function(searchMap){
        return $http.post('itemsearch/search.do',searchMap);
    }
    this.addGoodsToFootmark=function(goodsId){
        return $http.get('itemsearch/addGoodsToFootmark.do?goodsId='+goodsId);
    }
});
