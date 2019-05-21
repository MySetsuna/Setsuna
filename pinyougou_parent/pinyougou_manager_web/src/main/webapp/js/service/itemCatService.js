//服务层
app.service('itemCatService',function($http){
	    	
	//读取列表数据绑定到表单中
	this.findAll=function(){
		return $http.get('../itemCat/findAll.do');		
	}
	//分页 
	this.findPage=function(page,rows){
		return $http.get('../itemCat/findPage.do?page='+page+'&rows='+rows);
	}
	//查询实体
	this.findOne=function(id){
		return $http.get('../itemCat/findOne.do?id='+id);
	}
	//增加 
	this.add=function(entity){
		return  $http.post('../itemCat/add.do',entity );
	}
	//修改 
	this.update=function(entity){
		return  $http.post('../itemCat/update.do',entity );
	}
	//删除
	this.dele=function(ids){
		return $http.get('../itemCat/delete.do?ids='+ids);
	}
	//搜索
	this.search=function(page,rows,searchEntity){
		return $http.post('../itemCat/search.do?page='+page+"&rows="+rows, searchEntity);
	}

	this.findByParentId=function (parentId) {
        return $http.get("../itemCat/findByParentId.do?parentId=" + parentId);
    }

	//更改状态
	this.updateStatus=function(ids,status){
		return $http.get('../itemCat/updateStatus.do?ids='+ids+"&status="+status);
	}
    //上传EXCEL表格
    this.uploadFile=function () {
        //FormData是H5提交的对象，用于封装表单元素
        var formData = new FormData();
        //追加表单元素，第二个参数file为页面表单元素的id，取第一个
        //append(表单input元素的type,表单元素(第二个file是表单元素的id))
        formData.append("file",file.files[0]);

        return $http({
            method:'POST',
            //上传的url
            url:"../itemCat/upload.do",
            //表单内容
            data: formData,
            //anjularjs对于post和get请求默认的Content-Type header 是application/json。
            //通过设置‘Content-Type’: undefined，
            // 这样浏览器会帮我们把Content-Type 设置为 multipart/form-data.
            headers: {'Content-Type':undefined},
            //通过设置 transformRequest: angular.identity ，
            // anjularjs transformRequest function 将序列化我们的formdata object.
            transformRequest: angular.identity
        });
    }
});
