 //控制层 
app.controller('itemCatController' ,function($scope,$controller,itemCatService,typeTemplateService){
	
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
            var flag = isExist();
            if (flag) {
                alert("抱歉您已申请过该品牌，请勿重复申请！");
            }else {
                $scope.entity.parentId = $scope.parentId;
                serviceObject = itemCatService.add($scope.entity);//增加
            }
		}				
		serviceObject.success(
			function(response){
				if(response.success){
					//重新查询
                    $scope.findByParentId($scope.parentId);
				}else{
					alert(response.message);
				}
			}		
		);				
	}



    //判断是否该商家已经在同个分类目录下申请过这个分类
    isExist=function(){
        var isExist = false;
        for (var i = 0 ; i < $scope.list.length ; i ++ ) {
        	//相同的条件判断：同分类目录下，同名且同模板类型才算重复，同名不同模板类型，同名不同上级分类都算一个新分类？

            if ( $scope.entity.name ==$scope.list[i].name && $scope.parentId == $scope.list[i].parentId && $scope.entity.typeId == $scope.list[i].typeId ) {
                isExist = true;
            }
        }
        return isExist;
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

    //分类的审核状态
    $scope.catStatus=['未审核','已通过','驳回'];

    $scope.typeTemplateList=[];
    //加载所有规格列表
    $scope.findTypeTemplateList=function () {
        typeTemplateService.findAll().success(function (response) {
            // //删除多余属性
            // for(var i = 0; i < response.length; i++){
            //     delete response[i]["brandIds"];
            //     delete response[i]["specIds"];
            //     delete response[i]["customAttributeItems"];
            //     delete response[i]["status"];
            //     delete response[i]["sellerId"];
            // }
            $scope.typeTemplateList= response;
        })
    }
})

