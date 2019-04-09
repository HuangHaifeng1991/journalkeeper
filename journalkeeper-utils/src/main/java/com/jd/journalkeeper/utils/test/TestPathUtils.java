package com.jd.journalkeeper.utils.test;


import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

/**
 * @author liyue25
 * Date: 2018/8/29
 */
public class TestPathUtils {
    public static Path prepareBaseDir(String basePath) throws IOException {
        String property = "java.io.tmpdir";
        String tempDir = System.getProperty(property);
        File tempDirFile = new File(tempDir);
        assert tempDirFile.exists() && tempDirFile.isDirectory() && tempDirFile.canWrite();

        File base = new File(tempDirFile, basePath);
        if (base.exists()){
            if(base.isDirectory()) deleteFolder(base); else base.delete();
        }
        base.mkdirs();
        return base.toPath();
    }

    public static Path prepareBaseDir() throws IOException {
        return prepareBaseDir( "journalkeeper");
    }

    public static void destroyBaseDir() {
        String property = "java.io.tmpdir";
        String tempDir = System.getProperty(property);

        destroyBaseDir(new File(tempDir + File.separator + "journalkeeper"));
    }

    public static void destroyBaseDir(File base) {
        if (base.exists()){
            if(base.isDirectory()) deleteFolder(base); else base.delete();
        }

    }


    private static void deleteFolder(File folder) {
        File[] files = folder.listFiles();
        if(files!=null) { //some JVMs return null for empty dirs
            for(File f: files) {
                if(f.isDirectory()) {
                    deleteFolder(f);
                } else {
                    f.delete();
                }
            }
        }
        folder.delete();
    }
}
