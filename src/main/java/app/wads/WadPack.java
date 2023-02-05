package app.wads;

import app.Config;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

public class WadPack {

    private Wad iWAD;
    private ArrayList<Wad> wads;
    private String name;

    public WadPack(Wad iWAD, String name, ArrayList<Wad> wads) {
        this.iWAD = iWAD;
        this.wads = wads;
        this.name = name;
        if (iWAD != null) Config.setDefaultIWAD(iWAD);
    }

    public WadPack(Wad iWAD, String name) {
        this.iWAD = iWAD;
        this.name = name;
        wads = new ArrayList<>();
        if (iWAD != null) Config.setDefaultIWAD(iWAD);
    }

    public static WadPack createNewPack() {
        return new WadPack(null, "create new WadPack");
    }

    public static void startWadPack(WadPack wadPackToStart) {
        if (!wadPackToStart.getIWAD().isIWAD()) {
            JOptionPane.showMessageDialog(null, "The wad could not be started because it is not an IWAD. Your IWAD dir may be bad.", "Not an IWAD Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String paths = wadPackToStart.getWads().stream().sorted(Wad::compareToByLoadOrder).map(Wad::getPath).map(p -> "\"" + p + "\"").collect(Collectors.joining(" "));
        String command = String.format("\"%s\\gzdoom.exe\" -iwad \"%s\" -file %s", Config.getGzdoomDir(), wadPackToStart.getIWAD().getPath(), paths);
        System.out.println(command);
        try {
            Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addWad(Wad wad) {
        wads.add(wad);
    }

    public ArrayList<Wad> getWads() {
        return wads;
    }

    public void setWads(ArrayList<Wad> wads) {
        this.wads = wads;
    }

    public Wad getIWAD() {
        return iWAD;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setiWAD(Wad iWAD) {
        if (iWAD.isIWAD()) this.iWAD = iWAD;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WadPack wadPack = (WadPack) o;
        return Objects.equals(iWAD, wadPack.iWAD) && Objects.equals(wads, wadPack.wads) && Objects.equals(name, wadPack.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(iWAD, wads, name);
    }
}
