package app.wads;

import app.Config;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

//Full conversions can also be .bat Files
//Full conversions can't be opened with other wads
public class Wad implements Comparable<Wad> {
    //path of the wad
    private String path;
    //order in which to load the wad in packs
    private int loadOrder;
    //name of the wad
    private String name;
    //??? can be removed?
    private String folder = "";

    public Wad(String path, int loadOrder) {
        this.path = path;
        this.loadOrder = loadOrder;
        setName();
    }

    public Wad(String path, String folder) {
        this.path = path;
        this.loadOrder = 0;
        this.folder = folder;
        setName();
    }

    public Wad(String path, String folder, int loadOrder, String name) {
        this.path = path;
        this.loadOrder = loadOrder;
        this.folder = folder;
        this.name = name;
    }

    public Wad(String path) {
        this.path = path;
        this.loadOrder = 0;
        setName();
    }

    public static List<Wad> getIWADs() {
        return Config.getWadDirs().stream().filter(wadDir -> wadDir.getName().equals("IWAD")).
                map(dir -> {
                    try {
                        return Files.list(Paths.get(dir.getPath())).
                                filter(Files::isRegularFile).
                                map(Path::toString).
                                filter(f -> f.matches(".*?(\\.(pk3|wad)$)")).
                                map(path -> new Wad(path, dir.getName()))
                                .collect(Collectors.toList());
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                }).filter(Objects::nonNull).flatMap(Collection::stream).collect(Collectors.toList());
    }

    public static List<Wad> getNotIWADs() {
        return Config.getWadDirs().stream().filter(wadDir -> !wadDir.getName().equals("IWAD")).
                map(dir -> {
                    try {
                        return Files.list(Paths.get(dir.getPath())).
                                filter(Files::isRegularFile).
                                map(Path::toString).
                                filter(f -> f.matches(".*?(\\.(pk3|wad)$)")).
                                map(path -> new Wad(path, dir.getName())).
                                collect(Collectors.toList());
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                }).filter(Objects::nonNull).flatMap(Collection::stream).collect(Collectors.toList());
    }

    public static void startWad(Wad iWADtoStart) {
        if (!iWADtoStart.isIWAD()) {
            JOptionPane.showMessageDialog(null, "The wad could not be started because it is not an IWAD. Your IWAD dir may be bad.", "Not an IWAD Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String command = String.format("\"%s\\gzdoom.exe\" -iwad \"%s\"", Config.getGzdoomDir(), iWADtoStart.getPath());
        try {
            Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void startWad(Wad wadToStart, Wad iWADToStartWith) {
        if (!iWADToStartWith.isIWAD()) {
            JOptionPane.showMessageDialog(null, "The wad could not be started because it is not an IWAD. Your IWAD dir may be bad.", "Not an IWAD Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String command = String.format("\"%s\\gzdoom.exe\" -iwad \"%s\" -file \"%s\"", Config.getGzdoomDir(), iWADToStartWith.getPath(), wadToStart.getPath());
        try {
            Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void startWad(Collection<Wad> wadsToStart, Wad iWADToStartWith) {
        if (!iWADToStartWith.isIWAD()) {
            JOptionPane.showMessageDialog(null, "The wad could not be started because it is not an IWAD. Your IWAD dir may be bad.", "Not an IWAD Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String paths = wadsToStart.stream().parallel().map(Wad::getPath).map(p -> "\"" + p + "\"").collect(Collectors.joining(" "));
        String command = String.format("\"%s\\gzdoom.exe\" -iwad \"%s\" -file %s", Config.getGzdoomDir(), iWADToStartWith.getPath(), paths);
        System.out.println(command);
        try {
            Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Path getPath() {
        return Paths.get(path);
    }

    public int getLoadOrder() {
        return loadOrder;
    }

    public void setLoadOrder(int loadOrder) {
        this.loadOrder = loadOrder;
    }

    private void setName() {
        //remove the extension and just Return the filename
        name = Paths.get(path).getFileName().toString().replaceFirst(".[^.]+$", "");
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public void setLoadOrderDialog() {
        try {
            this.loadOrder = Integer.parseInt((String) JOptionPane.showInputDialog(null,
                    String.format("Enter the load-order of the %s-Wad", name),
                    "Set load-order",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    null,
                    0));
        } catch (NumberFormatException ex) {
            System.out.println("Canceled or invalid input");
        }

    }

    //returns just the filename without the extension
    @Override
    public String toString() {
        if (!folder.equals("")) {
            return String.format("%s - %s", name, folder);
        }
        return name;

    }

    public boolean isIWAD() {
        //path is not set after serialaization
        return Config.getWadDirs().stream().filter(wadDir -> wadDir.getName().
                        equals("IWAD")).
                //if multiple Dirs are called IWAD
                        anyMatch(wadDir -> {
                    return wadDir.getPath().equals(getPath().getParent().toString());
                });
    }

    @Override
    public int compareTo(Wad o) {
        if (isIWAD() ^ o.isIWAD()) return isIWAD() ? -1 : 1;
        return (path + name).compareTo(o.path + o.name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Wad wad = (Wad) o;
        return Objects.equals(path, wad.path) && Objects.equals(name, wad.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, loadOrder, name);
    }

    public int compareToByLoadOrder(Wad o) {
        return loadOrder - o.loadOrder;
    }
}
