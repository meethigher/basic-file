package service.impl;


import config.MinioProperties;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.UploadObjectArgs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.BasicFileService;
import utils.FilePathUtils;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Minio对象存储
 *
 * @author chenchuancheng
 * @since 2022/9/26 13:44
 */
public class MinioStoreService implements BasicFileService {

    private final Logger log = LoggerFactory.getLogger(MinioStoreService.class);


    /**
     * minio存储路径
     */
    private final String remoteBaseDir;

    /**
     * 本地存储路径
     */
    private final String localBaseDir;

    /**
     * 存储桶名
     */
    private final String bucket;

    /**
     * minio对象
     */
    private final MinioClient minioClient;

    public MinioStoreService(MinioProperties properties, String remoteBaseDir, String localBaseDir) {
        this.remoteBaseDir = remoteBaseDir;
        this.localBaseDir = localBaseDir;
        this.minioClient = MinioClient.builder().endpoint(properties.getEndpoint()).credentials(properties.getAccessKey(), properties.getSecretKey()).build();
        this.bucket = properties.getBucket();
    }

    public MinioStoreService(MinioProperties properties) {
        this(properties, "/meethigher", "/meethigher");
    }


    @Override
    public boolean write(InputStream inputStream, String fileName) {
        String localFilePath = FilePathUtils.getFilePath(localBaseDir, fileName);

        //uploadObject需要一个文件对象计算一系列属性、类型等。该操作也可以自己通过putObject直接传入流实现，只不过需要自己计算。
        byte[] bytes = new byte[1024];
        int len;
        try (OutputStream os = new FileOutputStream(localFilePath)) {
            while ((len = inputStream.read(bytes)) != -1) {
                os.write(bytes, 0, len);
            }
            os.flush();
            UploadObjectArgs args = UploadObjectArgs.builder()
                    .bucket(bucket)
                    .object(FilePathUtils.getFilePath(remoteBaseDir, fileName))
                    .filename(localFilePath).build();
            minioClient.uploadObject(args);
            return true;
        } catch (Exception e) {
            log.error("Minio write error:{}", e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public String read(String fileName) {
        String filePath = FilePathUtils.getFilePath(remoteBaseDir, fileName);
        GetObjectArgs args = GetObjectArgs.builder().bucket(bucket).object(filePath).build();
        String localFilePath = FilePathUtils.getFilePath(localBaseDir, fileName);

        byte[] bytes = new byte[1024];
        int len;
        try (InputStream is = minioClient.getObject(args); OutputStream os = new FileOutputStream(localFilePath)) {
            while ((len = is.read(bytes)) != -1) {
                os.write(bytes, 0, len);
            }
            os.flush();
            return localFilePath;
        } catch (Exception e) {
            log.error("Minio read error:{}", e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
