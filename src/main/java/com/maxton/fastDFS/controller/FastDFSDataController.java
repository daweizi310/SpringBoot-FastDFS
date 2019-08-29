package com.maxton.fastDFS.controller;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.domain.proto.storage.DownloadByteArray;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.maxton.fastDFS.util.FastDFSClient;
import com.maxton.fastDFS.util.QRCodeUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;

import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @Description
 * @Author maxton.zhang
 * @Date 2019/8/1 9:47
 * @Version 1.0
 */
@Api(tags = "FastDFS文件上传下载删除")
@RestController
@RequestMapping("/fastDFS")
public class FastDFSDataController {

    @Autowired
    private FastFileStorageClient storageClient;

    @Autowired
    private FastDFSClient fastDFSClient;

    @Autowired
    private QRCodeUtils qrCodeUtils;

    @Autowired
    private Environment env;

    @Autowired
    JdbcTemplate jdbcTemplate;

    /**
     * 文件上传
     *
     * @param myFile
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "本地文件上传")
    @PostMapping("/upload")
    public String upload(MultipartFile myFile) throws Exception {
        try {
            // myFile.getOriginalFilename():取到文件的名字
            String fileOriginalFilename = myFile.getOriginalFilename();
            // myFile.getInputStream():指这个文件中的输入流
            InputStream is = myFile.getInputStream();
            // myFile.getSize():文件的大小
            long fileSize = myFile.getSize();
            // FilenameUtils.getExtension(""):取到一个文件的后缀名
            String extension = FilenameUtils.getExtension(fileOriginalFilename);
            // group1:指storage服务器的组名
            // 这一行是通过storageClient将文件传到storage容器
            StorePath uploadFile = storageClient.uploadFile("group1", is, fileSize, extension);
            // 上传数据库
            String sql = "insert into fastdfs_data(fileName,groupName,path) values(?,?,?)";
            jdbcTemplate.update(sql, myFile.getOriginalFilename(), uploadFile.getGroup(), uploadFile.getPath());
            System.out.println(uploadFile.getFullPath());
            // 关闭流
            is.close();
            // 返回它在storage容器的的路径
            return uploadFile.getFullPath();
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    /**
     * 生成二维码上传
     *
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "生成二维码上传")
    @PostMapping("/uploadUtils")
    public String uploadUtils() throws Exception {
        try {
            // 获取本地文件路劲 Linux是改成Linux存放路径
            String url = env.getProperty("hcat.tmpdir");
            String fileName = UUID.randomUUID() + ".png";
            System.out.println(fileName);
            // 生成文件全路径
            String qrcodePath = url + fileName;
            // 内容
            String content = "这里是二维码的内容";
            // 生成二维码
            qrCodeUtils.createQRCode(qrcodePath, content);
            File file = new File(qrcodePath);
            // 上传
            String uploadQRCode = fastDFSClient.uploadFile(file);
            // 上传数据库
            String group = uploadQRCode.substring(0, uploadQRCode.indexOf("/"));
            String path = uploadQRCode.substring(uploadQRCode.indexOf("/") + 1);
            String sql = "insert into fastdfs_data(fileName,groupName,path) values(?,?,?)";
            jdbcTemplate.update(sql, fileName, group, path);
            System.out.println(uploadQRCode);
            return uploadQRCode;
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    /**
     * 文件下载
     *
     * @param id
     * @param response
     * @throws Exception
     */
    @ApiOperation(value = "文件下载")
    @GetMapping("/download/{id}")
    public void download(@PathVariable String id, HttpServletResponse response) throws Exception {
        try {
            // 根据id查询一条数据
            List query = jdbcTemplate.query("select * from fastdfs_data where id = " + id, new ColumnMapRowMapper());
            // 转数据
            Map map = (Map) query.get(0);
            String fileName = URLEncoder.encode(map.get("fileName").toString(), "utf-8"); // 解决中文文件名下载后乱码的问题
            // 告诉浏览器 下载的文件名
            response.setHeader("Content-Disposition", "attachment; fileName=" + fileName + "");
            String groupName = map.get("groupName").toString();
            String filepath = map.get("path").toString();
            // 将文件的内容输出到浏览器 fastdfs
            byte[] downloadFile = storageClient.downloadFile(groupName, filepath, new DownloadByteArray());
            // http://localhost:8989/fastDFS/download/1
            // 返回的地址在浏览器中访问可以直接下载到本地
            response.getOutputStream().write(downloadFile);
        } catch (DataAccessException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ApiOperation(value = "文件删除")
    @DeleteMapping("delete/{id}")
    public void delete(@PathVariable String id) {
        // 根据id查询一条数据
        List query = jdbcTemplate.query("select * from fastdfs_data where id = " + id, new ColumnMapRowMapper());
        // 转数据
        Map map = (Map) query.get(0);
        String groupName = map.get("groupName").toString();
        String filepath = map.get("path").toString();
        // 删除文件
        storageClient.deleteFile(groupName, filepath);
        Object args[] = new Object[]{id};
        // 删除数据
        int temp = jdbcTemplate.update("delete from fastdfs_data where id = " + id, args);
        if (temp > 0) {
            System.out.println("删除成功");
        } else {
            System.out.println("删除失败");
        }
    }

}
