package top.ingxx.shop.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import top.ingxx.untils.entity.PygResult;
import top.ingxx.untils.until.FastDFSClient;

@RestController
public class UploadController {

    @Value("${FILE_SERVER_URL}")
    private String file_server_url;

    @RequestMapping("/upload")
    public PygResult upload(MultipartFile file) {
        String filename = file.getOriginalFilename();
        String extName = filename.substring(filename.lastIndexOf(".") + 1);//获得扩展名
        try {
            FastDFSClient client = new FastDFSClient("classpath:config/fdfs_client.conf");
            String fileId = client.uploadFile(file.getBytes(), extName);
            String url = file_server_url + fileId;//图片完整地址
            return new PygResult(true,url);
        } catch (Exception e) {
            e.printStackTrace();
            return new PygResult(false,"上传失败");
        }
    }
}
