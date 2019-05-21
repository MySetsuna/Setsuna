//服务层
app.service('typeTemplateService',function($http){
	    	
	//读取列表数据绑定到表单中
	this.findAll=function(){
		return $http.get('../typeTemplate/findAll.do');		
	}
	//分页 
	this.findPage=function(page,rows){
		return $http.get('../typeTemplate/findPage.do?page='+page+'&rows='+rows);
	}
	//查询实体
	this.findOne=function(id){
		return $http.get('../typeTemplate/findOne.do?id='+id);
	}
	//增加 
	this.add=function(entity){
		return  $http.post('../typeTemplate/add.do',entity );
	}
	//修改 
	this.update=function(entity){
		return  $http.post('../typeTemplate/update.do',entity );
	}
	//删除
	this.dele=function(ids){
		return $http.get('../typeTemplate/delete.do?ids='+ids);
	}
	//搜索
	this.search=function(page,rows,searchEntity){
		return $http.post('../typeTemplate/search.do?page='+page+"&rows="+rows, searchEntity);
	}
	//更改状态
	this.updateStatus=function(ids,status){
		return $http.get('../typeTemplate/updateStatus.do?ids='+ids+"&status="+status);
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
            url:"../typeTemplate/upload.do",
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
