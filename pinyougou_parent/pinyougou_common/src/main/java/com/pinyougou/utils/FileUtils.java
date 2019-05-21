package com.pinyougou.utils;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Base64;

/**
 * @author Lan
 * @date 2019/5/17 7:19
 */
public class FileUtils {
    /**
     * 下载文件时，针对不同浏览器，进行附件名的编码
     *
     * @param filename 下载文件名
     * @param agent    客户端浏览器
     * @return 编码后的下载附件名
     * @throws IOException
     */
    public static String encodeDownloadFilename(String filename, String agent)
            throws IOException {
        if (agent.contains("Firefox")) { // 火狐浏览器
            //new BASE64Encoder().encode(filename.getBytes("utf-8")) jdk8之前
            filename = "=?UTF-8?B?"
                    + Base64.getEncoder().encodeToString(filename.getBytes("utf-8"))
                    + "?=";
            filename = filename.replaceAll("\r\n", "");
        } else { // IE及其他浏览器
            filename = URLEncoder.encode(filename, "utf-8");
            filename = filename.replace("+", " ");
        }
        return filename;
    }


}
