 //控制层 
app.controller('typeTemplateController' ,function($scope,$controller,typeTemplateService,
												  brandService,specificationService){
	
	$controller('baseController',{$scope:$scope});//继承
	
    //读取列表数据绑定到表单中  
	$scope.findAll=function(){
		typeTemplateService.findAll().success(
			function(response){
				$scope.list=response;
			}			
		);
	}    
	
	//分页
	$scope.findPage=function(page,rows){			
		typeTemplateService.findPage(page,rows).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	
	//查询实体 
	$scope.findOne=function(id){				
		typeTemplateService.findOne(id).success(
			function(response){
				//把品牌字符串转换为json对象
                response.brandIds = JSON.parse(response.brandIds);
				//把规格字符串转换为json对象
                response.specIds = JSON.parse(response.specIds);
				//把扩展属性字符串转换为json对象
                response.customAttributeItems = JSON.parse(response.customAttributeItems);
				$scope.entity= response;					
			}
		);				
	}
	
	//保存 
	$scope.save=function(){	
		var serviceObject;//服务层对象  				
		if($scope.entity.id!=null){//如果有ID
			serviceObject=typeTemplateService.update( $scope.entity ); //修改  
		}else{
			serviceObject=typeTemplateService.add( $scope.entity  );//增加 
		}				
		serviceObject.success(
			function(response){
				if(response.success){
					//重新查询 
		        	$scope.reloadList();//重新加载
				}else{
					alert(response.message);
				}
			}		
		);				
	}
	
	 
	//批量删除 
	$scope.dele=function(){			
		//获取选中的复选框			
		typeTemplateService.dele( $scope.selectIds ).success(
			function(response){
				if(response.success){
					$scope.reloadList();//刷新列表
				}						
			}		
		);				
	}
	
	$scope.searchEntity={};//定义搜索对象 
	
	//搜索
	$scope.search=function(page,rows){			
		typeTemplateService.search(page,rows,$scope.searchEntity).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}

    //品牌列表
    //$scope.brandList={data:[{id:1,text:'联想'},{id:2,text:'vivo'},{id:3,text:'华为'}]};
    $scope.brandList={data:[]};
	//加载所有品牌列表
	$scope.findBrandList=function () {
		brandService.findAll().success(function (response) {
			//删除多余属性
			for(var i = 0; i < response.length; i++){
                delete response[i]["firstChar"];
                delete response[i]["name"];
			}
            $scope.brandList.data = response;
        })
    }

    $scope.specList={data:[]};
    //加载所有规格列表
    $scope.findSpecList=function () {
        specificationService.findAll().success(function (response) {
            //删除多余属性
            for(var i = 0; i < response.length; i++){
                delete response[i]["specName"];
            }
            $scope.specList.data = response;
        })
    }

    //声明模板对象,customAttributeItems:扩展属性列表
    $scope.entity = {customAttributeItems: []};

    //表格行添加
    $scope.addTableRow=function () {
        $scope.entity.customAttributeItems.push({});
    }

    //表格行删除
    $scope.deleteTableRow=function (index) {
        $scope.entity.customAttributeItems.splice(index, 1);
    }


	//模板状态
	$scope.status=['未审核','已审核','审核未通过','关闭'];

	//审核模板
	$scope.updateStatus=function (status) {
		typeTemplateService.updateStatus($scope.selectIds,status).success(function (response) {
			alert(response.message);
			if(response.success){
				$scope.reloadList();
				//清空审核列表
				$scope.selectIds = [];
			}
		})
	}

    $scope.uploadFile=function () {
        typeTemplateService.uploadFile().success(function (response) {
            alert(response.message);
        })
    }




});	
