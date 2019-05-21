 //控制层 
app.controller('goodsController' ,function($scope,$controller,$location,goodsService,
										   uploadService,itemCatService,typeTemplateService){
	
	$controller('baseController',{$scope:$scope});//继承
	
    //读取列表数据绑定到表单中  
	$scope.findAll=function(){
		goodsService.findAll().success(
			function(response){
				$scope.list=response;
			}			
		);
	}    
	
	//分页
	$scope.findPage=function(page,rows){			
		goodsService.findPage(page,rows).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	
	//查询实体 
	$scope.findOne=function(){
        var id = $location.search()["id"];
		goodsService.findOne(id).success(
			function(response){
				//alert(JSON.stringify(response));
				$scope.entity= response;
				//填充富文本
                editor.html($scope.entity.goodsDesc.introduction);

                //把图片列表的json串转换成对象
                $scope.entity.goodsDesc.itemImages = JSON.parse($scope.entity.goodsDesc.itemImages);
                //把扩展属性列表的json串转换成对象
                $scope.entity.goodsDesc.customAttributeItems = JSON.parse($scope.entity.goodsDesc.customAttributeItems);
                //把规格信息列表的json串转换成对象
                $scope.entity.goodsDesc.specificationItems = JSON.parse($scope.entity.goodsDesc.specificationItems);

                //把商品sku信息中的spec属性转换成对象
				for(var i = 0; i < response.itemList.length; i++){
                    response.itemList[i].spec = JSON.parse(response.itemList[i].spec);
				}
			}
		);
	}
	
	//保存 
	$scope.save=function(){
        //读取富文本内容，保存到对象中
        $scope.entity.goodsDesc.introduction = editor.html();
		var serviceObject;//服务层对象 			
		if($scope.entity.goods.id!=null){//如果有ID
			serviceObject=goodsService.update( $scope.entity ); //修改  
		}else{
			serviceObject=goodsService.add( $scope.entity  );//增加 
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
        $scope.entity.goodsDesc.introduction = editor.html();
        goodsService.add($scope.entity).success(function (response) {
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
		goodsService.dele( $scope.selectIds ).success(
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
		goodsService.search(page,rows,$scope.searchEntity).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}

    $scope.image_entity = {};
	//文件上传
	$scope.uploadFile=function () {
		uploadService.uploadFile().success(function (response) {
			if(response.success){
				//记录图片地址
                $scope.image_entity.url = response.message;
			}else{
                alert(response.message);
			}
        })
    }

    //定义页面实体结构
	//{goods:{},goodsDesc:{itemImages:图片列表,specificationItems:规格列表}}
    $scope.entity={goods:{},goodsDesc:{itemImages:[],specificationItems:[]}};
	//添加图片
	$scope.add_image_entity=function () {
        $scope.entity.goodsDesc.itemImages.push($scope.image_entity);
    }

    //删除图片
    $scope.delete_image_entity=function (index) {
        $scope.entity.goodsDesc.itemImages.splice(index,1);
    }

    // //加载商品分类一级目录
    // $scope.selectItemCat1List=function () {
	// 	itemCatService.findByParentId(0).success(function (response) {
    //         $scope.itemCat1List = response;
    //
    //     })
    // }
    //
    // //二级分类加载
	// //$watch(监听的变量名，函数(新的值,原来的值))
    // $scope.$watch("entity.goods.category1Id",function (newValue,oldValue) {
    //     itemCatService.findByParentId(newValue).success(function (response) {
    //         $scope.itemCat2List = response;
    //         //$scope.entity.goods.category2Id = -1;
    //
    //         $scope.itemCat3List = [];
    //     })
    // });
    //
    // //三级分类加载
    // //$watch(监听的变量名，函数(新的值,原来的值))
    // $scope.$watch("entity.goods.category2Id",function (newValue,oldValue) {
    //     itemCatService.findByParentId(newValue).success(function (response) {
    //         $scope.itemCat3List = response;
    //     })
    // });
    //
    // //模板id加载
    // //$watch(监听的变量名，函数(新的值,原来的值))
    // $scope.$watch("entity.goods.category3Id",function (newValue,oldValue) {
	// 	itemCatService.findOne(newValue).success(function (response) {
	// 		$scope.entity.goods.typeTemplateId = response.typeId;
    //     })
    // });
    //
    // //品牌列表、扩展属性加载
    // //$watch(监听的变量名，函数(新的值,原来的值))
    // $scope.$watch("entity.goods.typeTemplateId",function (newValue,oldValue) {
    //     typeTemplateService.findOne(newValue).success(function (response) {
    //    //把品牌json串转换为品牌列表
    //         response.brandIds = JSON.parse(response.brandIds);
    //         //把扩展属性json串转换为列表
    //         var id = $location.search()["id"];
    //         if(id == null){
    //             $scope.entity.goodsDesc.customAttributeItems = JSON.parse(response.customAttributeItems);
	// 		}
    //         //读取规格与选项列表
	// 		typeTemplateService.findSpecList(newValue).success(function (response) {
	// 			//alert(JSON.stringify(response));
    //             $scope.specList = response;
    //         });
    //         $scope.typeTemplate = response;
    //     })
    // });
    //查询一级分类
    $scope.selectItemCat1List=function () {
        itemCatService.findByParentId(0).success(function (response) {
            $scope.itemCat1List=response;

        })
    }

    //跟据一级类目，更新二级类目
    //$watch方法用于监控某个变量的值，当被监控的值发生变化，就自动执行相应的函数
    $scope.$watch("entity.goods.category1Id",function (newValue,oldValue) {
        itemCatService.findByParentId(newValue).success(function (response) {
            $scope.itemCat2List=response;
            //点选1级列表请空第3级列表和模板ID
            if($location.search()['id']==null) {
                $scope.itemCat3List={};
                $scope.entity.goods.typeTemplateId="";
                //点选1级列表请空扩展属性列表
                $scope.entity.goodsDesc.customAttributeItems = {};
                //点选1级列表请空规格选项和品牌
                $scope.specList={};
                $scope.typeTemplate.brandIds=[];
            }
        })
    })

    //跟据二级类目，更新三级类目
    //$watch方法用于监控某个变量的值，当被监控的值发生变化，就自动执行相应的函数
    $scope.$watch("entity.goods.category2Id",function (newValue,oldValue) {
        itemCatService.findByParentId(newValue).success(function (response) {
            $scope.itemCat3List=response;
            //点选1级列表请空第3级列表和模板ID
            if($location.search()['id']==null) {
                $scope.entity.goods.typeTemplateId="";
                //点选1级列表请空扩展属性列表
                $scope.entity.goodsDesc.customAttributeItems = {};
                //点选1级列表请空规格选项和品牌
                $scope.specList={};
                $scope.typeTemplate.brandIds=[];
            }
        })
    })

    //选择三级类目后，更新模板id
    $scope.$watch("entity.goods.category3Id",function (newValue, oldValue) {
        itemCatService.findOne(newValue).success(function (response) {

            $scope.entity.goods.typeTemplateId =response.typeId;
        })
    })

    //加载品牌列表
    $scope.$watch("entity.goods.typeTemplateId",function (newValue, oldValue) {
        typeTemplateService.findOne(newValue).success(function (response) {
            $scope.typeTemplate =response;
            //品牌
            $scope.typeTemplate.brandIds = JSON.parse(response.brandIds);
            //拓展属性列表
            //如果没有ID，则加载模板中的扩展数据
            if($location.search()['id']==null) {
                $scope.entity.goodsDesc.customAttributeItems = JSON.parse(response.customAttributeItems);
            }
        });
        //加载规格选项列表
        typeTemplateService.findSpecList(newValue).success(function (response) {
            $scope.specList=response;
        })
    })

    /**
	 * 页面规格checkbox的点击事件
     * @param $event 整个checkbox本身
     * @param specName 规格名称
     * @param optionName 选项名称
     */
    $scope.updateSpecAttribute=function ($event,specName,optionName) {
		//检查我们的规格名称有没有补点击过
		var obj = $scope.searchObjectByKey($scope.entity.goodsDesc.specificationItems,'attributeName',specName);
		if(obj == null){
			//规格列表追加一个元素
            $scope.entity.goodsDesc.specificationItems.push({
                "attributeName": specName,
                "attributeValue": [
                    optionName
                ]
            });
		}else{
			//检查checkbox是否是选中状态
			if($event.target.checked){
				//追加规格选项元素
                obj.attributeValue.push(optionName);
			}else {
				//删除规格选项元素
                var optionIndex = obj.attributeValue.indexOf(optionName);
                obj.attributeValue.splice(optionIndex, 1);
                //如果取消勾选后，选项列表已经没有了
				if(obj.attributeValue.length < 1){
                    var specIndex = $scope.entity.goodsDesc.specificationItems.indexOf(obj);
                    $scope.entity.goodsDesc.specificationItems.splice(specIndex, 1);
				}
			}
		}
		//刷新表格
        $scope.createItemList();
    }

    // 1.创建$scope.createItemList方法，同时创建一条有基本数据，不带规格的初始数据
	$scope.createItemList=function () {
        // 参考: $scope.entity.itemList=[{spec:{},price:0,num:99999,status:'0',isDefault:'0' }]
        $scope.entity.itemList = [{spec: {}, price: 0, num: 99999, status: '0', isDefault: '0'}];
        // 2.查找遍历所有已选择的规格列表，后续会重复使用它，所以我们可以抽取出个变量items
		var items = $scope.entity.goodsDesc.specificationItems;
		for(var i = 0; i < items.length; i++){
            // 9.回到createItemList方法中，在循环中调用addColumn方法，并让itemList重新指向返回结果;
            $scope.entity.itemList = addColumn($scope.entity.itemList,items[i].attributeName,items[i].attributeValue);
		}
    }
    // 3.抽取addColumn(当前的表格，列名称，列的值列表)方法，用于每次循环时追加列
    addColumn = function (list,specName,optionValue) {
        // 4.编写addColumn逻辑，当前方法要返回添加所有列后的表格，定义新表格变量newList
		var newList = [];
        // 5.在addColumn添加两重嵌套循环，一重遍历之前表格的列表，二重遍历新列值列表
		for(var i = 0; i < list.length; i++){
			for(var j = 0; j < optionValue.length; j++){
                // 6.在第二重循环中，使用深克隆技巧，把之前表格的一行记录copy所有属性，
				// 用到var newRow = JSON.parse(JSON.stringify(之前表格的一行记录));
                var newRow = JSON.parse(JSON.stringify(list[i]));
                // 7.接着第6步，向newRow里追加一列
                newRow.spec[specName] = optionValue[j];
                // 8.把新生成的行记录，push到newList中
                newList.push(newRow);
			}
		}
        return newList;
    }

	//所有的商品分类列表，下标就是我们的商品分类id，类似Map结构
    $scope.itemCatList=[];
	//加载所有商品分类
    $scope.findItemCatList=function () {
        itemCatService.findAll().success(function (response) {
			for(var i = 0; i < response.length; i++){
				$scope.itemCatList[response[i].id] = response[i].name;
			}
        })
    }

    //商品审核状态
    $scope.status=['未审核','已审核','审核未通过','关闭'];
    /**
	 * 识别checkbox是否要勾中
     * @param specName 当前后规格名称
     * @param optionName 当前的选项名称
     */
    $scope.checkAttributeValue=function (specName,optionName) {
        var obj = $scope.searchObjectByKey($scope.entity.goodsDesc.specificationItems, 'attributeName', specName);
        if(obj != null){
        	//如果找到相应的规格选项
        	if(obj.attributeValue.indexOf(optionName) > -1){
                return true;
			}
		}
        return false;
    };

    //商品上下架状态数组
    $scope.marketableStatus=['已下架', '已上架'];
    /**
     * 商品上下架
     * @param marketStatus 上下架状态
     */
    $scope.updateMarketStatus=function (marketStatus) {
        //筛选审核通过的商品
        for (i=0; i<$scope.selectAuditStatus.length; i++){
            if ($scope.selectAuditStatus[i] != "1"){
                alert("您的选择中含有未审核通过的产品，请重新选择");
                return;
            }
        }
        //筛选是否同时勾选上架和未上架的商品
        for (i=0; i<$scope.selectIsMarketable.length; i++){
            if ($scope.selectIsMarketable[i] != $scope.selectIsMarketable[0]){
                alert("您的选择中同时含有上架和未上架的商品，请重新选择");
                return;
            }
        }
        //进行商品的上下架
        goodsService.updateMarketStatus($scope.selectIds, marketStatus).success(function (response) {
            alert(response.message);
            if (response.success){
                $scope.reloadList();
                //清空审核列表
                $scope.selectIds = [];
                $scope.selectAuditStatus = [];
                $scope.selectIsMarketable = [];
            }
        })
    };

    //选中审核状态列表
    $scope.selectAuditStatus = [];
    //选中上下架状态列表
    $scope.selectIsMarketable = [];
    /**
     * //搜集商品的审核状态和上下架状态数组
     * @param $event
     * @param auditStatus 审核状态
     * @param isMarketable 上下架状态
     */
    $scope.checkSelectStatus=function ($event, auditStatus, isMarketable) {
        //如果是被选中,则增加到数组
        if ($event.target.checked) {
            $scope.selectAuditStatus.push(auditStatus);
            $scope.selectIsMarketable.push(isMarketable);
        } else {
            //如果没被选中则删除数据
            $scope.selectAuditStatus.splice( $scope.selectAuditStatus.indexOf(auditStatus), 1);
            $scope.selectIsMarketable.splice( $scope.selectIsMarketable.indexOf(isMarketable), 1);
        }
    };

});
