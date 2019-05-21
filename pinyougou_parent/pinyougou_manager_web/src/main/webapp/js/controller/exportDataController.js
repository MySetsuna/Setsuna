app.controller("exportDataController",function ($scope,exportDataService) {
    $scope.exportData=function () {
        exportDataService.exportData().success(function (response) {
            if (response.success) {

            }
        })
    }
})