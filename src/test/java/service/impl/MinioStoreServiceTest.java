package service.impl;


import config.MinioProperties;
import org.junit.Before;
import org.junit.Test;
import service.BasicFileService;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Minio对象存储单测
 *
 * @author chenchuancheng
 * @since 2022/9/26 14:34
 */
public class MinioStoreServiceTest {

    private BasicFileService service;

    private static final String fileName = "minio-test.xlsx";


    @Before
    public void init() {
        MinioProperties properties = new MinioProperties();
        //{"url":"http://192.168.110.138:9000","accessKey":"kao77NONUTdDzJtc","secretKey":"r6BfnXCkyx8PwrQUjU0FvCHjZksepA5z","api":"s3v4","path":"auto"}
        properties.setEndpoint("http://192.168.110.138:9000");
        properties.setBucket("test");
        properties.setAccessKey("kao77NONUTdDzJtc");
        properties.setSecretKey("r6BfnXCkyx8PwrQUjU0FvCHjZksepA5z");
        service = new MinioStoreService(properties, "/", "D:/");
    }

    @Test
    public void test01Write() throws Exception {
        String path = MinioStoreServiceTest.class.getClassLoader().getResource("excel-import-service.xlsx").getPath();
        InputStream is = new FileInputStream(path);
        boolean result = service.write(is, fileName);
        assertTrue(result);
    }

    @Test
    public void test02Read() {
        String filePath = service.read(fileName);
        System.out.println(filePath);
        assertNotNull(filePath);
        File file = new File(filePath);
        assertTrue(file.exists());
    }
}
