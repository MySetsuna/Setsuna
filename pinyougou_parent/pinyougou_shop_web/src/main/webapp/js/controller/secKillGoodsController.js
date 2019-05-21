 //控制层 
app.controller('secKillGoodsController' ,function($scope,$controller,$location,secKillGoodsService, uploadService){
	
	$controller('baseController',{$scope:$scope});//继承
	
    //读取列表数据绑定到表单中  
	$scope.findAll=function(){
		secKillGoodsService.findAll().success(
			function(response){
				$scope.list=response;
			}			
		);
	}    
	
	//分页
	$scope.findPage=function(page,rows){			
		secKillGoodsService.findPage(page,rows).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	
	//查询实体 
	$scope.findOne=function(){
        var id = $location.search()["id"];
		secKillGoodsService.findOne(id).success(
			function(response){
				//alert(JSON.stringify(response));
				$scope.entity= response;
				//填充富文本
                editor.html($scope.entity.introduction);

                //把图片列表的json串转换成对象（这里只给传一张图的）
                $scope.entity.smallPic = JSON.parse($scope.entity.smallPic);
                //把扩展属性列表的json串转换成对象
                // $scope.entity.goodsDesc.customAttributeItems = JSON.parse($scope.entity.goodsDesc.customAttributeItems);
                //把规格信息列表的json串转换成对象
                // $scope.entity.goodsDesc.specificationItems = JSON.parse($scope.entity.goodsDesc.specificationItems);

                //把商品sku信息中的spec属性转换成对象
				// for(var i = 0; i < response.itemList.length; i++){
                //     response.itemList[i].spec = JSON.parse(response.itemList[i].spec);
				// }
			}
		);
	}
	
	//保存 
	$scope.save=function(){
        //读取富文本内容，保存到对象中
        $scope.entity.introduction = editor.html();
		var serviceObject;//服务层对象 			
		if($scope.entity.id!=null){//如果有ID
			serviceObject=secKillGoodsService.update( $scope.entity ); //修改  
		}else{
			serviceObject=secKillGoodsService.add( $scope.entity  );//增加 
		}				
		serviceObject.success(
			function(response){
                alert(response.message);
                if(response.success){
                   window.location.href="goods.html";
                }
			}		
		);				
	}

	//录入商品
	$scope.add=function () {
		//读取富文本内容，保存到对象中
        // $scope.entity.goodsDesc.introduction = editor.html();
        $scope.entity.introduction = editor.html();
        secKillGoodsService.add($scope.entity).success(function (response) {
            alert(response.message);
            if(response.success){
           //清空页面
           $scope.entity = {};
                editor.html("");
			}
        })
    }
	
	 
	//批量删除 
	$scope.dele=function(){			
		//获取选中的复选框			
		secKillGoodsService.dele( $scope.selectIds ).success(
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
		secKillGoodsService.search(page,rows,$scope.searchEntity).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}

    $scope.image_entity ="";//只给传一张图（后台数据库的表格决定的）
	//文件上传
	$scope.uploadFile=function () {
		//不用了，因为在新建那里加了点击之后清空image_entity
	    // if ($scope.image_entity == "") {
            uploadService.uploadFile().success(function (response) {
                if(response.success){
                    //记录图片地址
                    $scope.image_entity = response.message;
                }else{
                    alert(response.message);
                }
            })
        // }else{
	    //     alert("抱歉，秒杀商品只允许上传一张图片！不合理你咬我啊……");
        // }
    }

    //定义页面实体结构
	//{goods:{},goodsDesc:{itemImages:图片列表,specificationItems:规格列表}}
    // $scope.entity={goods:{},goodsDesc:{itemImages:[],specificationItems:[]}};
    $scope.entity={};
	//添加图片
	$scope.add_image_entity=function () {
        $scope.entity.smallPic=$scope.image_entity;
    }

    //删除图片
    $scope.delete_image_entity=function () {
        // $scope.entity.goodsDesc.itemImages.splice(index,1);
        $scope.entity.smallPic = "";
        $scope.image_entity = "";
    }



});
