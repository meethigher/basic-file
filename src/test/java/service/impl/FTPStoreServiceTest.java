package service.impl;


import config.FTProperties;
import org.junit.Before;
import org.junit.Test;
import service.BasicFileService;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * FTP存储服务单测
 *
 * @author chenchuancheng
 * @since 2022/9/26 15:35
 */
public class FTPStoreServiceTest {

    private BasicFileService service;

    private static final String fileName = "ftp-test.xlsx";

    @Before
    public void setUp() throws Exception {
        FTProperties properties = new FTProperties();
        properties.setFtpHost("192.168.110.70");
        properties.setFtpPort(21);
        properties.setFtpUsername("ftpadmin");
        properties.setFtpPassword("ftpadmin");
        service = new FTPStoreService("/test", "D:/", properties);
    }

    @Test
    public void test01Write() throws Exception {
        String path = MinioStoreServiceTest.class.getClassLoader().getResource("excel-import-service.xlsx").getPath();
        InputStream is = new FileInputStream(path);
        boolean result = service.write(is, fileName);
        assertTrue(result);
    }

    @Test
    public void test02Read() throws Exception {
        String filePath = service.read(fileName);
        System.out.println(filePath);
        assertNotNull(filePath);
        File file = new File(filePath);
        assertTrue(file.exists());
    }
}
