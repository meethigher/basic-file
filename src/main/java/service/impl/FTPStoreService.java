package service.impl;


import config.FTProperties;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.BasicFileService;
import utils.FilePathUtils;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * FTP存储服务
 *
 * @author chenchuancheng
 * @since 2022/9/26 15:27
 */
public class FTPStoreService implements BasicFileService {

    private final Logger log = LoggerFactory.getLogger(FTPStoreService.class);

    /**
     * FTP客户端
     */
    private final FTPClient client = new FTPClient();

    /**
     * FTP存储路径
     */
    private final String remoteBaseDir;

    /**
     * 本地存储路径
     */
    private final String localBaseDir;

    /**
     * FTP基础信息
     */
    private final FTProperties properties;

    public FTPStoreService(String remoteBaseDir, String localBaseDir, FTProperties properties) {
        this.remoteBaseDir = remoteBaseDir;
        this.localBaseDir = localBaseDir;
        this.properties = properties;
    }

    public FTPStoreService(FTProperties properties) {
        this("/meethigher", "/meethigher", properties);
    }

    /**
     * 获取Client
     *
     * @return FTPClient
     */
    private FTPClient getClient() {
        try {
            client.connect(properties.getFtpHost(), properties.getFtpPort());
            boolean login = client.login(properties.getFtpUsername(), properties.getFtpPassword());
            if (login) {
                //这三个参数，在登录成功后使用才会生效
                //[FTPClient上传文件内容为空/损坏/缺失_WYXXXXXXXXX的博客-CSDN博客_ftp上传后是空文件](https://blog.csdn.net/WYXXXXXXXXX/article/details/120767254)
                client.setCharset(StandardCharsets.UTF_8);
                client.setControlEncoding("utf-8");
                client.setFileType(FTP.BINARY_FILE_TYPE);
                return client;
            }
        } catch (Exception e) {
            log.error("getClient error:{}", e.getMessage());
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public boolean write(InputStream inputStream, String fileName) {
        FTPClient client = getClient();
        if (client == null) {
            return false;
        }
        try {
            client.makeDirectory(remoteBaseDir);
            return client.storeFile(FilePathUtils.getFilePath(remoteBaseDir, fileName), inputStream);
        } catch (Exception e) {
            log.error("FTP write error:{}", e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public String read(String fileName) {
        FTPClient client = getClient();
        if (client == null) {
            return null;
        }
        String localFilePath = FilePathUtils.getFilePath(localBaseDir, fileName);
        byte[] bytes = new byte[1024];
        int len;
        try (InputStream is = client.retrieveFileStream(FilePathUtils.getFilePath(remoteBaseDir, fileName));
             OutputStream os = new FileOutputStream(localFilePath)) {
            while ((len = is.read(bytes)) != -1) {
                os.write(bytes, 0, len);
            }
            os.flush();
            return localFilePath;
        } catch (Exception e) {
            log.error("FTP read error:{}", e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
