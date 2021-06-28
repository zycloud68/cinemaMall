package com.stylefeng.guns.order.common.utils;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import sun.net.ftp.FtpClient;

import java.io.InputStreamReader;


@Slf4j
@Data
@Configuration
@ConfigurationProperties(prefix = "ftp")
public class FTPUtils {
    // 地址,端口,名称
    private String hostName;
    private Integer port;
    private String userName;
    private String password;

    private FTPClient ftpClient =null;
    private void initFTPClient(){
        try{
            ftpClient = new FTPClient();
            ftpClient.setControlEncoding("utf-8");
        }catch (Exception e){
            log.error("初始化FTP失败",e);
            // new InputStreamReader(ftpClient.retrieveFileStream(fileAddrees));
        }
    }

    public static void main(String[] args) {
        FTPUtils ftpUtils = new FTPUtils();
        // String fileStrAddress = ftpUtils.getFileStrByAddress("seats/cgs.json");
        // System.out.println(fileStrAddress);
    }
}
