package com.javatest.yunpan.controller;

import com.javatest.yunpan.dto.ResponseResult;
import com.javatest.yunpan.dto.ResponseResultFactory;
import com.javatest.yunpan.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@RestController
public class FileController {

    @SuppressWarnings("all")
    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private FileService fileService;

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public ResponseResult fileUpload(@RequestParam("file")MultipartFile multipartFile) {
        String md5 = httpServletRequest.getParameter("md5");
        long size = Long.parseLong(httpServletRequest.getParameter("size"));
        if (httpServletRequest.getParameter("chunks") != null) {
            int chunk = Integer.parseInt(httpServletRequest.getParameter("chunk"));
            try {
                fileService.writeChunkToFileByMD5(md5, multipartFile.getInputStream(), chunk);
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseResultFactory.getResponseResult(false, "Fail to receive chunk:" + chunk);
            }
        } else {
            try {
                fileService.saveFileByMD5(md5,multipartFile.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseResultFactory.getResponseResult(false, "Fail to receive file");
            }
        }
        return ResponseResultFactory.getResponseResult(true, "文件上传成功");
    }

    @RequestMapping(value = "/upload/{md5}", method = RequestMethod.POST)
    public ResponseResult finishFileUpload(@PathVariable String md5) {
        HttpSession httpSession = httpServletRequest.getSession();
        System.out.println(httpSession.getId());
        fileService.finishFileUpload(md5);
        return ResponseResultFactory.getResponseResult(true, "文件上传完成");
    }

    @RequestMapping(value = "/fileMD5/{md5}", method = RequestMethod.GET)
    public ResponseResult checkFileExits(@PathVariable String md5) {
        System.out.println(md5);
        return ResponseResultFactory.getResponseResult(true, md5);
    }

    @RequestMapping(value = "/chunk/{chunk}", method = RequestMethod.GET)
    public ResponseResult checkChunkExits(@RequestParam("md5") String md5, @PathVariable("chunk") int id) {
        if (!fileService.checkChunkExits(md5,id)) {
            return ResponseResultFactory.getResponseResult(true, "Have not received this chunk");
        }
        return ResponseResultFactory.getResponseResult(false, "This chunk has be received");
    }
}
