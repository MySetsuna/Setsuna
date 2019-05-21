app.service("uploadService",function ($http) {

    //上传图片
    this.uploadFile=function () {
        //FormData是H5提交的对象，用于封装表单元素
        var formData = new FormData();
        //追加表单元素，第二个参数file为页面表单元素的id，取第一个
        //append(表单input元素的type,表单元素(第二个file是表单元素的id))
        formData.append("file",file.files[0]);

        return $http({
            method:'POST',
            //上传的url
            url:"../upload.do",
            //表单内容
            data: formData,
            //anjularjs对于post和get请求默认的Content-Type header 是application/json。
            //通过设置‘Content-Type’: undefined，
            // 这样浏览器会帮我们把Content-Type 设置为 multipart/form-data.
            headers: {'Content-Type':undefined},
            //通过设置 transformRequest: angular.identity ，
            // anjularjs transformRequest function 将序列化我们的formdata object.
            transformRequest: angular.identity
        });
    }
});
