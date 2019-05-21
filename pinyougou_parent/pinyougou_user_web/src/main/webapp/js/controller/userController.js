 //控制层 
app.controller('userController' ,function($scope,$controller,userService,uploadService){
	
	/*$controller('baseController',{$scope:$scope});//继承*/
	
    //读取列表数据绑定到表单中  
	$scope.findAll=function(){
		userService.findAll().success(
			function(response){
				$scope.list=response;
			}			
		);
	}    
	
	//分页
	$scope.findPage=function(page,rows){			
		userService.findPage(page,rows).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	
	//查询实体 
	$scope.findOne=function(id){				
		userService.findOne(id).success(
			function(response){
				$scope.entity= response;					
			}
		);				
	}
	
	//保存 
	$scope.save=function(){				
		var serviceObject;//服务层对象
		$scope.year = $('#select_year2').val();
		$scope.month = $('#select_month2').val();
		$scope.day = $('#select_day2').val();
		var time = new Date();
		time.setFullYear($scope.year);
		time.setMonth($scope.month);
		time.setDate($scope.day);
		$scope.entity.birthday = time;
		if($scope.entity.id!=null){//如果有ID
			serviceObject=userService.update( $scope.entity ); //修改  
		}else{
			serviceObject=userService.add( $scope.entity  );//增加 
		}				
		serviceObject.success(
			function(response){
				if(response.success){
					//重新查询 
		        	$scope.findUserByUserId();//重新加载
				}else{
					alert(response.message);
				}
			}		
		);				
	}

	$scope.entity={phone:'',password:''};
	$scope.code="";

	//注册
	$scope.reg=function(){
		//确认密码
		if($scope.entity.password != $scope.password){
			alert("再次密码输入不致，请重新输入！");
			return;
		}
		if($scope.code == ""){
            alert("请先输入验证码！");
            return;
        }
		userService.add($scope.entity,$scope.code).success(
			function(response){
				alert(response.message);
				if(response.success){
                    $scope.entity={phone:'',password:''};
                }
			}
		);
	}

	//发送短信认证码
	$scope.sendCode=function(){
		if($scope.entity.phone == ''){
			alert("请先输入手机号！");
			return;
		}
        userService.sendCode($scope.entity.phone).success(function (response) {
            alert(response.message);
        });
	}
	
	 
	//批量删除 
	$scope.dele=function(){			
		//获取选中的复选框			
		userService.dele( $scope.selectIds ).success(
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
		userService.search(page,rows,$scope.searchEntity).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}

	//查询实体
	$scope.findUserByUserId=function(){
		userService.findUserByUserId().success(
			function(response){
				$scope.year = new Date(response.birthday).getFullYear();
				$scope.month = new Date(response.birthday).getMonth()+1;
				$scope.day = new Date(response.birthday).getDate();
				$scope.entity= response;
				$scope.year = new Date(response.birthday).getFullYear();
				$scope.month = new Date(response.birthday).getMonth()+1;
				$scope.day = new Date(response.birthday).getDate();
				fun1($scope.year,$scope.month,$scope.day)
			}
		);
	}
	//上传图片
	$scope.uploadFile=function () {
		uploadService.uploadFile().success(function (response) {
			//如果上传成功,绑定url到表单
			if(response.success){
				$scope.entity.headPic=response.message;
			}else{
				alert(response.message);
			}
		}).error(function() {
			alert("上传发生错误");
		});
	}

	//查询用户收藏
	$scope.findUserFavoriteByUserId=function(){
		userService.findUserFavoriteByUserId().success(
			function(response){
				$scope.list=response;
			}
		);
	}
	//查询用户足迹
	$scope.findPersonFootmark=function(){
		userService.findPersonFootmark().success(
			function(response){
				$scope.list=response;
			}
		);
	}

});
