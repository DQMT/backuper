package xyz.tincat.backuper.task;

import lombok.extern.slf4j.Slf4j;
import xyz.tincat.backuper.util.ZipUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @ Date       ：Created in 14:22 2019/1/2
 * @ Modified By：
 * @ Version:     0.1
 */
@Slf4j
public class FilePacker {

    private static String PATH_SEPARATOR = ";";
    private List<String> files;
    private List<String> folders;

    public FilePacker(String filesAndFolders) {
        files = new ArrayList<>();
        folders = new ArrayList<>();
        String[] strings = filesAndFolders.split(PATH_SEPARATOR);
        for (int i = 0; i < strings.length; i++) {
            File file = new File(strings[i]);
            if (file.exists()) {
                if (file.isDirectory()) {
                    folders.add(strings[i]);
                } else if (file.isFile()) {
                    files.add(strings[i]);
                }
            }
        }
        log.info("files = {},folders = {}", files, folders);
    }

    public void pack(String targetFile) {
        List<String> fileList = new ArrayList<>();
        fileList.addAll(files);
        folders.forEach(f -> {
            ZipUtil.createZip(f, f + ".zip");
            fileList.add(f + ".zip");
        });
        ZipUtil.pack(fileList, targetFile);
        log.info("pack success. create target file : {}", targetFile);
    }

    public static void main(String[] args) {
        FilePacker fp = new FilePacker("D:\\app.properties;D:\\zpack;D:\\task.txt");
        fp.pack("D:\\pack.zip");
    }

}