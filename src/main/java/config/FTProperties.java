package config;

/**
 * ftp基础信息
 *
 * @author chenchuancheng
 * @since 2022/9/26 15:09
 */

//@ConfigurationProperties(prefix = "ftp")
public class FTProperties {
    /**
     * 主机地址
     */
    private String ftpHost;

    /**
     * 用户名
     */
    private String ftpUsername;

    /**
     * 密码
     */
    private String ftpPassword;

    /**
     * 端口
     */
    private int ftpPort;

    public String getFtpHost() {
        return ftpHost;
    }

    public void setFtpHost(String ftpHost) {
        this.ftpHost = ftpHost;
    }

    public String getFtpUsername() {
        return ftpUsername;
    }

    public void setFtpUsername(String ftpUsername) {
        this.ftpUsername = ftpUsername;
    }

    public String getFtpPassword() {
        return ftpPassword;
    }

    public void setFtpPassword(String ftpPassword) {
        this.ftpPassword = ftpPassword;
    }

    public int getFtpPort() {
        return ftpPort;
    }

    public void setFtpPort(int ftpPort) {
        this.ftpPort = ftpPort;
    }
}
