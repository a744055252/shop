package com.juan.shop.file;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import utils.Checker;

/**
 * @author liguanhuan
 */
@Controller
@RequestMapping("file")
@Slf4j
public class FileController {


    @Autowired
    private IFileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<Long> upload(@RequestParam("file") MultipartFile file) {

        Checker.isTrue(file.isEmpty(), "上传的文件不能为空！");

        long fileId = fileService.upload(file);

        return ResponseEntity.ok(fileId);
    }

}
