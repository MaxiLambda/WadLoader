package app;

import app.gui.WadLoaderGui;
import com.formdev.flatlaf.FlatDarculaLaf;


public class Main {

    public static void main(String[] args) {
        FlatDarculaLaf.setup();
        new WadLoaderGui();
    }
}
