package com.tennetcn.free.file.message;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author chfree
 * @email chfree001@gmail.com
 * @create 2020-11-21 20:20
 * @comment
 */

@Data
public class FileChunkView {
    /**
     * 当前文件块，从1开始
     */
    private Integer chunkNumber;

    /**
     * 分块大小
     */
    private Long chunkSize;

    /**
     * 当前分块大小
     */
    private Long currentChunkSize;

    /**
     * 总大小
     */
    private Long totalSize;

    /**
     * 文件标识
     */
    private String identifier;

    /**
     * 文件名
     */
    private String filename;

    /**
     * 相对路径
     */
    private String relativePath;

    /**
     * 总块数
     */
    private Integer totalChunks;

    /**
     * 文件类型
     */
    private String type;

    /**
     * 要上传的文件
     */
    private MultipartFile file;
}
