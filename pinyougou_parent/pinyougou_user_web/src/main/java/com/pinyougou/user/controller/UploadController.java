package com.pinyougou.user.controller;

import com.pinyougou.utils.FastDFSClient;
import entity.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Steven
 * @version 1.0
 * @description com.pinyougou.shop.controller
 * @date 2019-4-14
 */
@RestController
public class UploadController {

    @Value("${FAST_DFS_SERVICE_URL}")
    private String FAST_DFS_SERVICE_URL;

    @RequestMapping("upload")
    public Result upload(MultipartFile file){
        try {
            //1、解析后缀名
            String oldName = file.getOriginalFilename();  //原来文件名
            String extName = oldName.substring(oldName.lastIndexOf(".") + 1);   //文件后缀名
            //把文件上传到FastDfs
            FastDFSClient fastDFSClient = new FastDFSClient("classpath:fdfs_client.conf");
            //开始上传到FastDfs中
            String fileId = fastDFSClient.uploadFile(file.getBytes(), extName, null);

            //拼接图片的url
            String url = FAST_DFS_SERVICE_URL+ fileId;
            //把图片url返回到message中
            return new Result(true, url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(false, "文件上传失败");
    }
}
