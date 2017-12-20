package com.horizonpvp.astra;

import com.horizonpvp.astra.cheat.CheatManager;
import com.horizonpvp.astra.user.ACUserManager;
import org.bukkit.plugin.java.JavaPlugin;

public class AntiCheat extends JavaPlugin {

    private CheatManager cheatManager;
    private ACUserManager userManager;

    public void onEnable() {
        saveDefaultConfig();
        this.cheatManager = new CheatManager(this);
        this.userManager = new ACUserManager(this);
    }

    public CheatManager getCheatManager() {
        return cheatManager;
    }

    public ACUserManager getUserManager() {
        return userManager;
    }

}
