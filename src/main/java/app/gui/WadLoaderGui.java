package app.gui;

import app.gui.Tabs.ConfigTab;
import app.gui.Tabs.StartWadsTab;
import app.gui.Tabs.Tab;
import app.gui.Tabs.WadPacksTab;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;


public class WadLoaderGui extends JFrame {
    private static HashMap<String, Tab> tabs = new HashMap<>();

    public WadLoaderGui() {
        setTitle("Wad Loader");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        add(createTabbedPane());
        setSize(600, 600);
        setVisible(true);
    }

    public static Map<String, Tab> getTabs() {
        return tabs;
    }

    private JTabbedPane createTabbedPane() {
        JTabbedPane jTabbedPane = new JTabbedPane();
        tabs.put("Start Game", new StartWadsTab());
        tabs.put("Wad Packs", new WadPacksTab());
        tabs.put("Config", new ConfigTab());

        jTabbedPane.addTab("Start Game", null, (Component) tabs.get("Start Game"), "To start Wads and WadPacks");
        jTabbedPane.addTab("Wad Packs", null, (Component) tabs.get("Wad Packs"), "For managing Wad-Packs");
        jTabbedPane.addTab("Config", null, (Component) tabs.get("Config"), "The configuration Settings for the WadLoader");

        return jTabbedPane;
    }

}
