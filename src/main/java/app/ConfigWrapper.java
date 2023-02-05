package app;

import app.wads.Wad;
import app.wads.WadDir;
import app.wads.WadPack;

import java.util.ArrayList;

public class ConfigWrapper {
    private ArrayList<WadDir> wadDirs;
    private ArrayList<WadPack> wadPacks;
    private String gzdoomDir;
    private Wad defaultIWAD;

    public ConfigWrapper(ArrayList<WadDir> wadDirs, ArrayList<WadPack> wadPacks, Wad defaultIWAD, String gzdoomDir) {
        this.wadDirs = wadDirs;
        this.wadPacks = wadPacks;
        this.defaultIWAD = defaultIWAD;
        this.gzdoomDir = gzdoomDir;
    }

    public ArrayList<WadDir> getWadDirs() {
        return wadDirs;
    }

    public void setWadDirs(ArrayList<WadDir> wadDirs) {
        this.wadDirs = wadDirs;
    }

    public ArrayList<WadPack> getWadPacks() {
        return wadPacks;
    }

    public void setWadPacks(ArrayList<WadPack> wadPacks) {
        this.wadPacks = wadPacks;
    }

    public Wad getDefaultIWAD() {
        return defaultIWAD;
    }

    public String getGzdoomDir() {
        return gzdoomDir;
    }
}
