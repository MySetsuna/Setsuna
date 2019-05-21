 //控制层 
app.controller('addressController' ,function($scope,$controller,addressService){	
	
	$controller('baseController',{$scope:$scope});//继承
	
    //读取列表数据绑定到表单中  
	$scope.findAll=function(){
		addressService.findAll().success(
			function(response){
				$scope.list=response;
			}			
		);
	}    
	
	//分页
	$scope.findPage=function(page,rows){			
		addressService.findPage(page,rows).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	
	//查询实体 
	$scope.findOne=function(id){				
		addressService.findOne(id).success(
			function(response){
				$scope.entity= response;
				fun1(response.provinceId,response.cityId,response.townId)
				/*$('#distpicker1').distpicker({
					province: response.provinceId,
					city: response.cityId,
					district:response.townId,
				});*/
			}
		);				
	}
	
	//保存 
	$scope.save=function(){
		$scope.entity.provinceId = $('#provinceId').val();
		$scope.entity.cityId = $('#cityId').val();
		$scope.entity.townId = $('#townId').val();
		var serviceObject;//服务层对象
		if($scope.entity.id!=null){//如果有ID
			serviceObject=addressService.update( $scope.entity ); //修改  
		}else{
			serviceObject=addressService.add( $scope.entity  );//增加 
		}				
		serviceObject.success(
			function(response){
				if(response.success){
					//重新查询
					$scope.entity = '';
		        	$scope.findListByLoginUser();//重新加载
				}else{
					alert(response.message);
				}
			}		
		);				
	}
	
	 
	//批量删除 
	$scope.dele=function(){			
		//获取选中的复选框			
		addressService.dele( $scope.selectIds ).success(
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
		addressService.search(page,rows,$scope.searchEntity).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	//查询地址
	$scope.findListByLoginUser=function(){
		addressService.findListByLoginUser().success(
			function(response){
				$scope.addressList=response;
			}
		);
	}

	//删除单个地址
	$scope.deleteOne=function(id){
		addressService.deleteOne(id).success(
			function(response){
				if (response.success) {
					$scope.findListByLoginUser();//重新加载
				}
			}
		);
	}


});	
