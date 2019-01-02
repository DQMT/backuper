package xyz.tincat.backuper.util;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @ Date       ：Created in 16:13 2019/1/2
 * @ Modified By：
 * @ Version:     0.1
 */
@Slf4j
public class ZipUtil {

    public static void pack(List<String> fileList, String targetZip) {
        FileOutputStream fos;
        ZipOutputStream zos = null;
        FileInputStream fis = null;
        File file = null;
        try{
            fos = new FileOutputStream(targetZip);
            zos = new ZipOutputStream(fos);
            for (int i = 0; i < fileList.size(); i++) {
                file = new File(fileList.get(i));
                if (file.isFile()) {
                    fis = new FileInputStream(file);
                    ZipEntry ze = new ZipEntry(file.getName());
                    zos.putNextEntry(ze);
                    byte[] content = new byte[1024];
                    int len;
                    while ((len = fis.read(content)) != -1) {
                        zos.write(content, 0, len);
                        zos.flush();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (zos != null) {
                    zos.close();
                }
            } catch (IOException e) {
                log.error("创建ZIP文件失败", e);
            }
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                log.error("创建ZIP文件失败", e);
            }
        }
    }

    public static void createZip(String sourcePath, String zipPath) {
        FileOutputStream fos;
        ZipOutputStream zos = null;
        try {
            fos = new FileOutputStream(zipPath);
            zos = new ZipOutputStream(fos);
            writeZip(new File(sourcePath), "", zos);
        } catch (FileNotFoundException e) {
            log.error("创建ZIP文件失败", e);
        } finally {
            try {
                if (zos != null) {
                    zos.close();
                }
            } catch (IOException e) {
                log.error("创建ZIP文件失败", e);
            }
        }
    }

    private static void writeZip(File file, String parentPath, ZipOutputStream zos) {
        if (file.exists()) {
            if (file.isDirectory()) {//处理文件夹
                parentPath += file.getName() + File.separator;
                File[] files = file.listFiles();
                if (files.length != 0) {
                    for (File f : files) {
                        writeZip(f, parentPath, zos);
                    }
                } else { //空目录则创建当前目录
                    try {
                        zos.putNextEntry(new ZipEntry(parentPath));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(file);
                    ZipEntry ze = new ZipEntry(parentPath + file.getName());
                    zos.putNextEntry(ze);
                    byte[] content = new byte[1024];
                    int len;
                    while ((len = fis.read(content)) != -1) {
                        zos.write(content, 0, len);
                        zos.flush();
                    }

                } catch (FileNotFoundException e) {
                    log.error("创建ZIP文件失败", e);
                } catch (IOException e) {
                    log.error("创建ZIP文件失败", e);
                } finally {
                    try {
                        if (fis != null) {
                            fis.close();
                        }
                    } catch (IOException e) {
                        log.error("创建ZIP文件失败", e);
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        ZipUtil.createZip("D:\\zpack", "D:\\zpack" + ".zip");
        List<String> fileList = new ArrayList<>();
        fileList.add("D:\\app.properties");
        fileList.add("D:\\task.txt");
        ZipUtil.pack(fileList, "D:\\pack.zip");
    }
}