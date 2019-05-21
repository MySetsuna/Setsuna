//服务层
app.service('brandService',function($http){
    // //给模板模块查询所有的品牌列表调用
    // this.getAll=function(){
    //     return $http.get('../brand/findAll.do');
    // }
	//读取列表数据绑定到表单中
	this.findAll=function(loginName){
		return $http.get('../brand/findAll.do?loginName='+loginName);
	}
	//分页 
	this.findPage=function(page,rows){
		return $http.get('../brand/findPage.do?page='+page+'&rows='+rows);
	}
	//查询实体
	this.findOne=function(id){
		return $http.get('../brand/findOne.do?id='+id);
	}
	//增加 
	this.add=function(entity,loginName){
		return  $http.post('../brand/sellerAdd.do?loginName='+loginName,entity );
	}
	//修改 
	this.update=function(entity){
		return  $http.post('../brand/update.do',entity );
	}
	//删除
	this.dele=function(ids){
		return $http.get('../brand/delete.do?ids='+ids);
	}

    //商家自家的品牌搜索
	this.search=function(sellerId,page,rows,searchEntity){
        return $http.post('../brand/sellerSearch.do?sellerId='+sellerId+'&page='+page+"&rows="+rows, searchEntity);
    }
});
