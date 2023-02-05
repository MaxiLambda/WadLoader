package app;

import app.gui.Tabs.Tab;
import app.gui.WadLoaderGui;
import app.wads.Wad;
import app.wads.WadDir;
import app.wads.WadPack;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Config {
    //public static final Path configPath = Paths.get("src\\main\\resources\\config.json");
    public static final Path configDir = Paths.get(System.getenv("appdata")+"\\WadLoader");
    public static final Path configPath = Paths.get(System.getenv("appdata")+"\\WadLoader\\config.json");
    public static final String[] DEFAULT_WAD_FOLDERS = {"IWAD", "Full Conversions", "Other", "Deathmatch"};
    private static final Gson gson = new Gson();
    private static ArrayList<WadDir> wadDirs = new ArrayList<WadDir>();
    private static ArrayList<WadPack> wadPacks = new ArrayList<WadPack>();
    private static String gzdoomDir;
    private static Wad defaultIWAD;

    static {
        if (Files.notExists(configPath)) {
            Config.createNewConfigFile();
        } else {
            try (BufferedReader br = Files.newBufferedReader(configPath)) {
                ConfigWrapper config = gson.fromJson(br.lines().collect(Collectors.joining()), ConfigWrapper.class);
                setWadPacks(config.getWadPacks());
                setWadDirs(config.getWadDirs());
                setDefaultIWAD(config.getDefaultIWAD());
                setGzdoomDir(config.getGzdoomDir());
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (NullPointerException ex) {
                System.out.println(ex.getMessage());
                ex.printStackTrace();
                Config.createNewConfigFile();
            }
        }
    }


    public static ArrayList<WadDir> getWadDirs() {
        return wadDirs;
    }

    public static void setWadDirs(ArrayList<WadDir> newWadDirs) {
        //TODO check if all wads in packs can still be accessed
        if (newWadDirs == null) throw new NullPointerException("newWadDirs was null");
        wadDirs = newWadDirs;
        save();
    }

    public static ArrayList<WadPack> getWadPacks() {
        return wadPacks;
    }

    public static void setWadPacks(ArrayList<WadPack> newWadPacks) {
        if (newWadPacks == null) throw new NullPointerException("newWadPacks was null");
        wadPacks = newWadPacks;
        save();
    }

    public static void save() {
        try (BufferedWriter br = Files.newBufferedWriter(configPath)) {
            br.write(gson.toJson(new ConfigWrapper(wadDirs, wadPacks, defaultIWAD, gzdoomDir)));
            System.out.println("Config updated.");
            WadLoaderGui.getTabs().keySet().stream().
                    map(WadLoaderGui.getTabs()::get).
                    forEach(Tab::updateUIContent);
            System.out.println("Tabs updated");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    public static void addWadPack(WadPack wadPack) {
        wadPacks.add(wadPack);
        save();
    }

    public static boolean existsWadPack(WadPack wadPack) {
        if (wadPack == null) return false;
        return wadPacks.stream().anyMatch(wadPack::equals);
    }

    public static void updateWadPack(WadPack wadPack, ArrayList<Wad> newWadList) {
        wadPacks.stream().filter(wadPack::equals).forEach(wadPack1 -> wadPack1.setWads(newWadList));
        save();
    }

    public static void updateWadPack(WadPack wadPack, String name) {
        if (name.matches("[a-zA-Z\\-_ \\d]+")) wadPack.setName(name);
        save();
    }

    public static void updateWadPackIWAD(WadPack wadPack, Wad newIWAD) {
        wadPacks.stream().filter(wadPack::equals).forEach(wadPack1 -> wadPack1.setiWAD(newIWAD));
    }

    private static void createNewConfigFile() {
        //Create config File
        if (Files.notExists(configDir)) {
            try {
                Files.createDirectory(configDir);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("unable to create config dir");
                return;
            }
        }else if(Files.notExists(configPath)){
            try {
                Files.createFile(configPath);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("unable to create config file");
                return;
            }
        }
        wadDirs = (ArrayList<WadDir>) Arrays.stream(DEFAULT_WAD_FOLDERS).map(WadDir::new).collect(Collectors.toList());
        wadPacks = new ArrayList<>();
        gzdoomDir = "D:\\GZDoom";
        Config.save();
        System.out.println("New Config File created");
    }

    public static void removeWadPack(WadPack wadPack) {
        wadPacks.remove(wadPack);
    }

    public static Wad getDefaultIWAD() {
        return defaultIWAD;
    }

    public static void setDefaultIWAD(Wad defaultIWAD) {
        Config.defaultIWAD = defaultIWAD;
        save();
    }

    public static String getGzdoomDir() {
        return gzdoomDir;
    }

    public static void setGzdoomDir(String gzdoomDir) {
        Config.gzdoomDir = gzdoomDir;
        save();
    }

    public static void newWadDir(String name) {
        wadDirs.add(new WadDir(name));
        save();
    }
}
