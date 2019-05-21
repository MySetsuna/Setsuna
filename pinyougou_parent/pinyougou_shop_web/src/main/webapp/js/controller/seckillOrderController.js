 //控制层 
app.controller('seckillOrderController' ,function($scope,$controller,seckillOrderService){	
	
	$controller('baseController',{$scope:$scope});//继承
	
    //读取列表数据绑定到表单中  
	$scope.findAll=function(){
		seckillOrderService.findAll().success(
			function(response){
				$scope.list1=response;
			}			
		);
	}    
	
	//分页
	$scope.findPage=function(page,rows){			
		seckillOrderService.findPage(page,rows).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	
	//查询实体 
	$scope.findOne=function(id){				
		seckillOrderService.findOne(id).success(
			function(response){
				$scope.entity= response;					
			}
		);				
	}
	
	//保存 
	$scope.save=function(){				
		var serviceObject;//服务层对象  				
		if($scope.entity.id!=null){//如果有ID
			serviceObject=seckillOrderService.update( $scope.entity ); //修改  
		}else{
			serviceObject=seckillOrderService.add( $scope.entity  );//增加 
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
		seckillOrderService.dele( $scope.selectIds ).success(
			function(response){
				if(response.success){
					$scope.reloadList();//刷新列表
				}						
			}		
		);				
	}
    //批量删除
    $scope.pretendDeleting=function(){
        confirm('此操作将从本页面上移除选中订单信息且不可主动恢复，如需恢复订单信息需联系平台管理员，请慎重操作！');
		alert("开发人员表示还要去后台数据库加‘商家显示’的字段，平台管理员表示还要管你商家爱不爱看到订单，就你事儿多，反正你不爱看这订单数据铁定也没法删要留根的，所以滚，干脆不给删！");
    }
	
	$scope.searchEntity={};//定义搜索对象 
	
	//搜索
	$scope.search=function(page,rows){			
		seckillOrderService.search(page,rows,$scope.searchEntity).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}

	$scope.orderStatus=['未发货','已发货','审核未通过','关闭'];
    
});	
