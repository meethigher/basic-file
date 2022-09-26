package service;

import java.io.InputStream;

/**
 * 最初我的做法，第一个方法多加了一个远程保存路径参数，第二个方法参数是全路径，这是不合规范的。
 * 抽象应该考虑易用性、方便性，直白如话，不需要使用的人去过多了解这个方法如何用。
 * 像具体的参数应该放到具体实现里面，我之前考虑是将公共的参数，提取到抽象方法里，但这无疑会增加使用者的负担。
 * 抽象，只取必要即可，能简则简。
 * 如果抽象带了具象化的东西，也就没多大意义了，灵活性也会受限制。
 * 下面是经过前辈修订后的。
 */
public interface BasicFileService {

    /**
     * 将字节流写入到对应的文件中
     *
     * @param inputStream 需要写入的输入流
     * @param fileName    写入的文件名称
     * @return true为成功
     */
    boolean write(InputStream inputStream, String fileName);

    /**
     * 将指定的文件读取到本地存储，返回存储在本地完整的路径地址
     *
     * @param fileName 文件名称
     * @return 文件地址
     */
    String read(String fileName);
}
