package com.memerland.segurity.schedulers;

import org.bukkit.scheduler.BukkitRunnable;

import com.memerland.segurity.Segurity;
import com.memerland.segurity.utils.BackupUtils;

public class BackupSchedule extends BukkitRunnable {

    @Override
    public void run() {
        
        try{
            BackupUtils.createBackup();
            

        }catch (Exception e){
            Segurity.instance.getLogger().warning("Error creating backup" + e.getMessage());
        }
        try {
            BackupUtils.deleteBackupDaysAgo();
        } catch (Exception e) {
            Segurity.instance.getLogger().warning("Error deleting backup" + e.getMessage());
        }
    }
    
}
