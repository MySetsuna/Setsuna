 //控制层 
app.controller('specificationController' ,function($scope,$controller,specificationService){	
	
	$controller('baseController',{$scope:$scope});//继承
	
    //读取列表数据绑定到表单中  
	$scope.findAll=function(){
        $scope.getUserInfo();
		specificationService.findAll($scope.loginName).success(
			function(response){
				$scope.list=response;
			}			
		);
	}    
	
	//分页
	$scope.findPage=function(page,rows){			
		specificationService.findPage(page,rows).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	
	//查询实体 
	$scope.findOne=function(id){				
		specificationService.findOne(id).success(
			function(response){
				$scope.entity= response;					
			}
		);				
	}
	
	//保存 
	$scope.save=function(){				
		var serviceObject;//服务层对象  				
		if($scope.entity.specification.id!=null){//如果有ID
			serviceObject=specificationService.update( $scope.entity ); //修改
		}else{
            var flag = isExist();
            if (flag) {
                alert("抱歉您已申请过该规格，请勿重复申请！");
            }else {
                serviceObject = specificationService.add($scope.entity,$scope.loginName );//增加
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

    //判断是否该商家已经申请过这个规格(单品牌单商家算一条数据库记录，因此需防止过度重复)
    isExist=function(){
        var isExist = false;
        for (var i = 0 ; i < $scope.list.length ; i ++ ) {
            if ( $scope.entity.specification.specName==$scope.list[i].specName ) {
                isExist = true;
            }
        }
        return isExist;
    }
	
	 
	//批量删除 
	$scope.dele=function(){			
		//获取选中的复选框			
		specificationService.dele( $scope.selectIds ).success(
			function(response){
				if(response.success){
					$scope.reloadList();//刷新列表
				}						
			}		
		);				
	}
	
	$scope.searchEntity={};//定义搜索对象 

    //商家自家的规格搜索
	$scope.search=function(page,rows){
		specificationService.search($scope.loginName,page,rows,$scope.searchEntity).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}

    //声明规格项列表，这里写$scope.entity.specificationOptionList=[]，当调用数组的push方法时会报错
	//entity完整的内容为$scope.entity = {specification:{},specificationOptionList:[]}
    $scope.entity = {specificationOptionList:[]};

	//表格行添加
	$scope.addTableRow=function () {
        $scope.entity.specificationOptionList.push({});
    }

    //表格行删除
    $scope.deleteTableRow=function (index) {
        $scope.entity.specificationOptionList.splice(index, 1);
    }

    //规格的审核状态
    $scope.specStatus=['未审核','已通过','驳回'];

//end node.
});	
