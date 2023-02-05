package app.gui.Tabs;

import app.Config;
import app.gui.swingExtension.GridBagConstraintsFixedColumnCount;
import app.gui.swingExtension.JListCheckBox;
import app.wads.Wad;
import app.wads.WadPack;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StartWadsTab extends JPanel implements Tab {

    private final DefaultListModel<WadPack> wadPackModel = new DefaultListModel<>();
    private final DefaultListModel<Wad> wadsModel = new DefaultListModel<>();
    private final DefaultListModel<Wad> iWadsModel = new DefaultListModel<>();

    public StartWadsTab() {
        setLayout(new GridBagLayout());
        GridBagConstraintsFixedColumnCount gridBagConstraintsFixedColumnCount = new GridBagConstraintsFixedColumnCount(3);

        updateUIContent();

        JListCheckBox<WadPack> wadPacks = new JListCheckBox<>(wadPackModel);
        JListCheckBox<Wad> wads = new JListCheckBox<>(wadsModel);
        JListCheckBox<Wad> iWADs = new JListCheckBox<>(iWadsModel);

        wadPacks.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        iWADs.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane wadPacksPane = new JScrollPane(wadPacks);
        JScrollPane wadsPane = new JScrollPane(wads);
        JScrollPane iWADsPane = new JScrollPane(iWADs);

        //create Buttons and assign action Listeners to start the Wad/ WadPack onclick
        JButton startWadPack = new JButton("Start");
        JButton startWad = new JButton("Start");
        JButton startIWAD = new JButton("Start");

        startWadPack.addActionListener(e -> {
            WadPack wadPackToStart = wadPacks.getSelectedValue();
            if (wadPackToStart != null) {
                WadPack.startWadPack(wadPackToStart);
            }
        });

        startWad.addActionListener(e -> {
            List<Wad> wadsToStart = Arrays.stream(wads.getSelectedIndices()).mapToObj(wadsModel::get).collect(Collectors.toList());
            Wad iWadToStartWith = iWADs.getSelectedValue();
            if (wadsToStart.size() != 0) {
                if (iWadToStartWith != null) {
                    Wad.startWad(wadsToStart, iWadToStartWith);
                } else {
                    JOptionPane.showMessageDialog(this, "Select an IWAD.", "No IWAD selected", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        startIWAD.addActionListener(e -> {
            Wad iWadToStart = iWADs.getSelectedValue();
            if (iWadToStart != null) {
                Wad.startWad(iWadToStart);
            }
        });

        //Adding all the Components
        add(new Label("Wad Packs"), gridBagConstraintsFixedColumnCount.getNextConstraints(0.1));
        add(new Label("Wads"), gridBagConstraintsFixedColumnCount.getNextConstraints());
        add(new Label("IWADs"), gridBagConstraintsFixedColumnCount.getNextConstraints());

        add(wadPacksPane, gridBagConstraintsFixedColumnCount.getNextConstraints(1.0));
        add(wadsPane, gridBagConstraintsFixedColumnCount.getNextConstraints());
        add(iWADsPane, gridBagConstraintsFixedColumnCount.getNextConstraints());

        add(startWadPack, gridBagConstraintsFixedColumnCount.getNextConstraints(0.1));
        add(startWad, gridBagConstraintsFixedColumnCount.getNextConstraints());
        add(startIWAD, gridBagConstraintsFixedColumnCount.getNextConstraints());
    }

    @Override
    public void updateUIContent() {
        wadPackModel.removeAllElements();
        wadPackModel.addAll(Config.getWadPacks());

        wadsModel.removeAllElements();
        wadsModel.addAll(Wad.getNotIWADs());

        iWadsModel.removeAllElements();
        iWadsModel.addAll(Wad.getIWADs());
    }
}
