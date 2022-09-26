package utils;

/**
 * 处理文件路径的工具类
 *
 * @author chenchuancheng
 * @since 2022/9/26 13:55
 */
public class FilePathUtils {

    /**
     * 获取文件地址
     *
     * @param fileName 文件名
     * @param baseDir  文件路径
     * @return 文件完整路径
     */
    public static String getFilePath(String baseDir, String fileName) {
        if (baseDir == null || fileName == null) {
            throw new IllegalArgumentException("参数不可为空");
        }
        if (baseDir.endsWith("/")) {
            return String.format("%s%s", baseDir, fileName);
        } else {
            return String.format("%s/%s", baseDir, fileName);
        }
    }

    /**
     * 截取文件名
     *
     * @param filePath 文件地址
     * @return 文件名
     */
    public static String getFileName(String filePath) {
        if (filePath == null) {
            throw new IllegalArgumentException("参数不可为空");
        }
        int i = filePath.lastIndexOf("/");
        return filePath.substring(i + 1);
    }
}
