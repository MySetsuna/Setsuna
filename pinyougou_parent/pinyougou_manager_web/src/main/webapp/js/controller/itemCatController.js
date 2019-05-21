 //控制层 
app.controller('itemCatController' ,function($scope,$controller,itemCatService){	
	
	$controller('baseController',{$scope:$scope});//继承
	
    //读取列表数据绑定到表单中  
	$scope.findAll=function(){
		itemCatService.findAll().success(
			function(response){
				$scope.list=response;
			}			
		);
	}    
	
	//分页
	$scope.findPage=function(page,rows){			
		itemCatService.findPage(page,rows).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	
	//查询实体 
	$scope.findOne=function(id){				
		itemCatService.findOne(id).success(
			function(response){
				$scope.entity= response;					
			}
		);				
	}
	
	//保存 
	$scope.save=function(){				
		var serviceObject;//服务层对象  				
		if($scope.entity.id!=null){//如果有ID
			serviceObject=itemCatService.update( $scope.entity ); //修改  
		}else{
			serviceObject=itemCatService.add( $scope.entity  );//增加 
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
		itemCatService.dele( $scope.selectIds ).success(
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
		itemCatService.search(page,rows,$scope.searchEntity).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}

	//定义一个变量用来记录上级ID
	$scope.parentId = 0;

	//跟据父节点id查询子节点列表
	$scope.findByParentId=function (parentId) {

		//记录上级id
		$scope.parentId = parentId;

		itemCatService.findByParentId(parentId).success(function (response) {
            $scope.list = response;
        })
    }

    //用于记录页面当前所在分类的级别：1,2,3
    $scope.grade=1;
	//修改当前分类级别
	$scope.setGrade=function (value) {
        $scope.grade = value;
    }

    //分级查询
	$scope.selectList=function (p_entity) {
		if($scope.grade == 1){
            $scope.entity_1=null;
            $scope.entity_2=null;
        }else if($scope.grade == 2){
            $scope.entity_1=p_entity;
            $scope.entity_2=null;
		}else{
            $scope.entity_2=p_entity;
		}
		//刷新数据
		$scope.findByParentId(p_entity.id);
    }

	//分类状态
	$scope.status=['未审核','已审核','审核未通过','关闭'];

	//审核分类
	$scope.updateStatus=function (status) {
		itemCatService.updateStatus($scope.selectIds,status).success(function (response) {
			alert(response.message);
			if(response.success){
				$scope.findByParentId($scope.parentId);
				// $scope.reloadList();
				//清空审核列表
				$scope.selectIds = [];
			}
		})
	}

    $scope.uploadFile=function () {
        itemCatService.uploadFile().success(function (response) {
            alert(response.message);
        })
    }

});	
