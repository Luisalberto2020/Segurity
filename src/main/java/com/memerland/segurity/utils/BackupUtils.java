package com.memerland.segurity.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.memerland.segurity.Segurity;

public class BackupUtils {
    
    private static final File serverFolder  = Segurity.instance.getServer().getPluginsFolder().getParentFile();
    private static final File backupFolder = new File(serverFolder, "backup");
    public static void createFolderBackup() {
    
        if(!backupFolder.exists()) {
            backupFolder.mkdir();
        }
    


    }


    public static void createBackup() throws Exception {
 
      
        ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(new File(backupFolder,LocalDateTime.now().toString()+".zip" ))));
        zipFolder(Segurity.instance.getServer().getWorldContainer(), zos);
        zos.close();
       



    }
    public static void deleteBackupDaysAgo(){

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -2);
        long twoDaysAgo = calendar.getTimeInMillis();


        File[] files = Segurity.instance.getServer().getWorldContainer().listFiles();
        for (File file : files) {
            if (file.getName().endsWith(".zip")) {
                if (file.lastModified() < twoDaysAgo) {
                    file.delete();
                }
            }
        }
    }


    private static void zipFolder(File sourceFolder, ZipOutputStream zos) throws Exception {
        File[] files = sourceFolder.listFiles();
        byte[] buffer = new byte[1024];

        for (File file : files) {
            if(!file.getName().equals("backup")){
            if (file.isDirectory()) {
                zipFolder(file, zos);
            } else {
                FileInputStream fis = new FileInputStream(file);
                zos.putNextEntry(new ZipEntry(file.getPath()));

                int length;
                while ((length = fis.read(buffer)) > 0) {
                    zos.write(buffer, 0, length);
                }

                zos.closeEntry();
                fis.close();
            }
        }
    }
    
    }
}

