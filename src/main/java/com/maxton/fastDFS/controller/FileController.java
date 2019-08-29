package com.maxton.fastDFS.controller;

import com.maxton.fastDFS.util.SystemConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;

/**
 * @Description
 * @Author maxton.zhang
 * @Date 2019/8/1 15:17
 * @Version 1.0
 */
@Api(tags = "管理文件上传下载删除")
@RestController
@RequestMapping("/file")
public class FileController {

    @Resource
    private SystemConfig systemConfig;

    public static void uploadFile(byte[] file, String filePath, String fileName) throws Exception {
        File targetFile = new File(filePath);
        if (targetFile.exists()) {
            targetFile.mkdirs();
        }
        FileOutputStream out = new FileOutputStream(filePath + fileName);
        out.write(file);
        out.flush();
        out.close();
    }


    @ApiOperation(value = "上传")
    @GetMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
        System.out.println(file);
        if (file.isEmpty()) {
            return "请选择图片";
        }
        String contentType = file.getContentType();
        System.out.println(contentType);//图片文件类型
        String fileName = file.getOriginalFilename();  //图片名字
        System.out.println(fileName);
        //String filePath=request.getSession().getServletContext().getRealPath("/");//当前系统的路径
        //filePath=filePath+"/upload/";//文件保存的路径
        String filePath = "E:/images/";//文件存的路径
        String path = filePath + fileName;
        File fileImage = new File(path);
        if (!fileImage.getParentFile().exists()) {
            fileImage.getParentFile().mkdirs();
        }
        try {
            uploadFile(file.getBytes(), path, fileName);
            return "上传成功 ";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "上传失败";
    }


}
