app.controller("seckillGoodsController",function ($scope,$location,$interval,seckillGoodsService) {
    $scope.findList = function () {
        seckillGoodsService.findList().success(function (response) {
            $scope.goodsList = response;
        })
    };
    
    $scope.findOne=function () {
        var id = $location.search()["id"];
        seckillGoodsService.findOne(id).success(function (response) {
            $scope.entity = response;

            //计算时间差
            //当前时间
            var now = new Date().getTime();
            //结束时间表
            var endTime = new Date($scope.entity.endTime).getTime();
            //结束时间与当前时间的时差值(秒)
            var allSecond = Math.floor((endTime - now) / 1000);

            $scope.allSecondStr = "";
            //$interval(执行的函数,间隔的毫秒数,运行次数);
            timer = $interval(function () {

                allSecond--;

                //把时间转换成天：时：分：秒的格式
                $scope.allSecondStr = convertTimeString(allSecond);
                if($scope.allSecond == 0){
                    //停止倒计时
                    $interval.cancel(timer);
                }
            },1000);
        })
    }

    /*$scope.allSecond = 10;
    //$interval(执行的函数,间隔的毫秒数,运行次数);
    timer = $interval(function () {
        $scope.allSecond--;
        if($scope.allSecond == 0){
            //停止倒计时
            $interval.cancel(timer);
        }
    },1000);*/

    //把秒转换为 天小时分钟秒格式  XXX天 10:22:33
    convertTimeString=function(allsecond){
        var days= Math.floor( allsecond/(60*60*24));//天数
        var hours= Math.floor( (allsecond-days*60*60*24)/(60*60) );//小时数
        var minutes= Math.floor(  (allsecond -days*60*60*24 - hours*60*60)/60    );//分钟数
        var seconds= allsecond -days*60*60*24 - hours*60*60 -minutes*60; //秒数
        var timeString="";
        if(days>0){
            timeString=days+"天 ";
        }
        if(hours < 10){
            hours = "0" + hours;
        }
        if(minutes < 10){
            minutes = "0" + minutes;
        }
        if(seconds < 10){
            seconds = "0" + seconds;
        }
        return timeString+hours+":"+minutes+":"+seconds;
    }

    $scope.submitOrder=function(){
        seckillGoodsService.submitOrder($scope.entity.id).success(
            function(response){
                alert(response.message);
                if(response.success){
                    window.location.href="pay.html";
                }
            }
        );
    }


})