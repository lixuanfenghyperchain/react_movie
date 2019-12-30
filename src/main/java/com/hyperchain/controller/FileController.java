/**
 * Copyright (C), 2016-2019, 趣链科技有限有限公司
 * FileName: FIleController
 * Author:   lixuanfeng
 * Date:     2019/12/5 下午10:14
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.hyperchain.controller;

import com.hyperchain.response.BaseResult;
import com.hyperchain.response.Code;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author lixuanfeng
 * @create 2019/12/5
 * @since 1.0.0
 */
@RequestMapping(value = "/file")
@Controller
public class FileController {
    @Value("${file.upload.location}")
    private String uploadPath;
    @Value("${PIC_ACCESS_URL}")
    private String PIC_ACCESS_URL;


    @PostMapping(value = "/upload")
    @ResponseBody
    public BaseResult upload(MultipartFile file) {
        if (file.isEmpty()) {

        }
        String fileName = file.getOriginalFilename();
        //使用uuid随机生成一个文件名
        String fileNameUUID = UUID.randomUUID().toString().replace("-", "").toUpperCase();
        fileName = fileNameUUID + fileName;
        File dest = new File(uploadPath + fileName);
        try {
            file.transferTo(dest);
            Map fileMap = new HashMap();
            fileMap.put("name", fileName);
            fileMap.put("url", PIC_ACCESS_URL + fileName);
            return BaseResult.response("0", "文件上传成功", fileMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("文件上次失败");
    }

    @GetMapping(value = "/delete")
    @ResponseBody
    public BaseResult removePic(String name) {
        File file = new File(uploadPath + name);
        if (file.exists()) {
            //文件存在，删除
            file.delete();
            return BaseResult.response(Code.RETURN_SUCCESS);
        } else {
            return BaseResult.response("0", "文件不存在", null);
        }
    }

}