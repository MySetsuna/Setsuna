 //控制层 
app.controller('brandController' ,function($scope,$controller,brandService){	
	
	$controller('baseController',{$scope:$scope});//继承

    //读取列表数据绑定到表单中  
	$scope.findAll=function(){
		//ajax执行顺序的缘故，并不先执行登录名初始化，所以废的，后端对接方法已添加后端获取登录用户名功能，这里保留这些无用代码纯粹是因为懒得改，前端传不传用户名过来后端都会覆盖掉
        $scope.getUserInfo();
		brandService.findAll($scope.loginName).success(
			function(response){
				$scope.list=response;
			}			
		);
	}    
	
	//分页
	$scope.findPage=function(page,rows){			
		brandService.findPage(page,rows).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	
	//查询实体 
	$scope.findOne=function(id){				
		brandService.findOne(id).success(
			function(response){
				$scope.entity= response;					
			}
		);				
	}
	
	//保存 
	$scope.save=function(){
		var serviceObject;//服务层对象  				
		if($scope.entity.id!=null){//如果有ID

			serviceObject=brandService.update( $scope.entity); //修改
		}else{
            var flag = isExist();
            if (flag) {
            	alert("抱歉您已申请过该品牌，请勿重复申请！");
			}else{
                serviceObject=brandService.add( $scope.entity,$scope.loginName  );//增加
			}
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
	//判断是否该商家已经申请过这个品牌(单品牌单商家算一条数据库记录，因此需防止过度重复)
	isExist=function(){
		var isExist = false;
        for (var i = 0 ; i < $scope.list.length ; i ++ ) {
           if ( $scope.entity.name ==$scope.list[i].name ) {
               isExist = true;
		   }
		}
		return isExist;
	}
	
	 
	//批量删除 
	$scope.dele=function(){			
		//获取选中的复选框			
		brandService.dele( $scope.selectIds ).success(
			function(response){
				if(response.success){
					$scope.reloadList();//刷新列表
				}						
			}		
		);				
	}
	
	$scope.searchEntity={};//定义搜索对象 

    //商家自家的品牌搜索
    $scope.search=function(page,rows){
        brandService.search($scope.loginName,page,rows,$scope.searchEntity).success(
            function(response){
                $scope.list=response.rows;
                $scope.paginationConf.totalItems=response.total;//更新总记录数
            }
        );
    }
    //品牌的审核状态
	$scope.brandStatus=['未审核','已通过','驳回'];
    
});	
