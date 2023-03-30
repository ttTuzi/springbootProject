package com.wei.controller;

import com.wei.common.R;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

/**
 * @Description: TODO
 * @author: Wei Liang
 * @date: 2023年03月21日 8:42 PM
 */
@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {

    @Value("${reggie.path}")
    private String basePath;


    /**
     * file upload
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){
        //file is a temp file, needs to save, else it will be deleted after finish.
        log.info(file.toString());

        //original File name
        String originalFileName = file.getOriginalFilename();
        String suffix = originalFileName.substring(originalFileName.lastIndexOf("."));

        //use UUID instead originalFileName, because if name same, previous one will be replaced
        String filename = UUID.randomUUID().toString();

        //directory obj, if not exist, create one
        File dir = new File(basePath);
        if(!dir.exists()){
            dir.mkdirs();
        }
        try {
            log.info(suffix);
            //save temp file to point location
            file.transferTo(new File(basePath+filename+suffix));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.success(filename+suffix);
    }

    /**
     * file download
     * @param name
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){
        //input to read file
        try {
            FileInputStream fis = new FileInputStream(new File(basePath + name));
            ServletOutputStream os = response.getOutputStream();
            response.setContentType("image/jpeg");
            int len = 0;
            byte[] bytes = new byte[1024];
            while((len = fis.read(bytes))!=-1){
                os.write(bytes,0,len);
                os.flush();
            }

            os.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //output to write file
    }
}
