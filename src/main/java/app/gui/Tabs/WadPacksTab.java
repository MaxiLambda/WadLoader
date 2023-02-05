package app.gui.Tabs;

import app.Config;
import app.gui.swingExtension.GridBagConstraintsFixedColumnCount;
import app.gui.swingExtension.JListCheckBox;
import app.wads.Wad;
import app.wads.WadPack;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


public class WadPacksTab extends JPanel implements Tab {
    private final DefaultListModel<Wad> allWadsModel = new DefaultListModel<>();
    private final DefaultListModel<Wad> allChosenWadsModel = new DefaultListModel<>();
    private final DefaultListModel<WadPack> allWadPacksModel = new DefaultListModel<>();
    private WadPack selectedWadPack = null;

    public WadPacksTab() {
        setLayout(new GridBagLayout());
        GridBagConstraintsFixedColumnCount c = new GridBagConstraintsFixedColumnCount(3);

        updateUIContent();

        JListCheckBox<Wad> allWads = new JListCheckBox<>(allWadsModel);
        JListCheckBox<Wad> allChosenWads = new JListCheckBox<>(allChosenWadsModel);
        JListCheckBox<WadPack> allWadPacks = new JListCheckBox<>(allWadPacksModel);

        allWadPacks.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        allWadPacks.addListSelectionListener(e -> {
            selectedWadPack = allWadPacks.getSelectedValue();
            if (allWadPacks.getSelectedValue() == null) return;
            if (allWadPacks.getSelectedValue().equals(WadPack.createNewPack())) {
                WadPack wadPackToAdd = createNewWadPackDialogue();
                if (wadPackToAdd != null) {
                    allWadPacksModel.add(allWadPacksModel.size(), wadPackToAdd);
                    allWadPacks.setSelectedIndex(allWadPacksModel.size() - 1);
                }
            } else {
                ArrayList<Wad> wadPackWads = (ArrayList<Wad>) allWadPacks.getSelectedValue().getWads().clone();
                wadPackWads.add(allWadPacks.getSelectedValue().getIWAD());
                allChosenWadsModel.removeAllElements();
                wadPackWads.sort(Wad::compareTo);
                allChosenWadsModel.addAll(wadPackWads);
            }

        });

        JScrollPane allWadsPane = new JScrollPane(allWads);
        JScrollPane allChosenWadsPane = new JScrollPane(allChosenWads);
        JScrollPane allWadPacksPane = new JScrollPane(allWadPacks);

        JPanel allWadsButtons = new JPanel();
        JPanel allChosenWadsButtons = new JPanel();
        JPanel allWadPacksButtons = new JPanel();

        JButton addButton = new JButton("add");
        JButton clearButton = new JButton("clear");
        JButton removeButton = new JButton("remove");
        JButton saveButton = new JButton("save");
        JButton deleteButton = new JButton("delete");
        JButton renameButton = new JButton("rename");

        addButton.addActionListener(e -> {
            if (selectedWadPack == null) return;
            List<Wad> chosenWadsList = Stream.concat(
                    //contains all the selected values in the allWads List
                    Arrays.stream(allWads.getSelectedIndices()).mapToObj(allWadsModel::get),
                    IntStream.range(0, allChosenWadsModel.size()).mapToObj(allChosenWadsModel::get)
            ).distinct().sorted(Wad::compareTo).collect(Collectors.toList());
            allChosenWadsModel.removeAllElements();
            allChosenWadsModel.addAll(chosenWadsList);
        });

        clearButton.addActionListener(e -> allWads.clearSelection());

        removeButton.addActionListener(e -> {
            if (selectedWadPack == null) return;
            if (Arrays.stream(allChosenWads.getSelectedIndices()).parallel().mapToObj(allChosenWadsModel::get).filter(Wad::isIWAD).count() < IntStream.range(0, allChosenWadsModel.size()).parallel().mapToObj(allChosenWadsModel::get).filter(Wad::isIWAD).count()) {
                Arrays.stream(allChosenWads.getSelectedIndices()).parallel().
                        mapToObj(allChosenWadsModel::get).
                        forEach(allChosenWadsModel::removeElement);
            }

        });

        saveButton.addActionListener(e -> {
            if (allChosenWadsModel.isEmpty() || selectedWadPack == null) return;
            ArrayList<Wad> newWadList;

            if (JOptionPane.showConfirmDialog(this, "Do you want to change/ set the load-order of the wads?", "Change load order", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                newWadList = (ArrayList<Wad>) IntStream.range(1, allChosenWadsModel.size()).
                        mapToObj(allChosenWadsModel::get).peek(Wad::setLoadOrderDialog).
                        collect(Collectors.toList());
            } else {
                newWadList = (ArrayList<Wad>) IntStream.range(1, allChosenWadsModel.size()).
                        mapToObj(allChosenWadsModel::get).
                        collect(Collectors.toList());
            }

            if (!allChosenWadsModel.contains(selectedWadPack.getIWAD())) {
                List<Wad> iWads = IntStream.range(0, allChosenWadsModel.size()).mapToObj(allChosenWadsModel::get).filter(Wad::isIWAD).collect(Collectors.toList());
                Config.updateWadPackIWAD(selectedWadPack, iWads.get(0));
            }

            //updates/ writes WadPack
            if (Config.existsWadPack(selectedWadPack)) {
                Config.updateWadPack(selectedWadPack, newWadList);
            } else {
                selectedWadPack.setWads(newWadList);
                Config.addWadPack(selectedWadPack);
            }
        });

        deleteButton.addActionListener(e -> {
            if (allWadPacks.getSelectedIndices().length == 0) return;
            Arrays.stream(allWadPacks.getSelectedIndices()).parallel().
                    filter(i -> i > 0).
                    mapToObj(allWadPacksModel::get).
                    peek(Config::removeWadPack).
                    forEach(allWadPacksModel::removeElement);
            Config.save();
            allChosenWadsModel.removeAllElements();
        });

        renameButton.addActionListener(e -> {
            if (selectedWadPack != null && !selectedWadPack.equals(WadPack.createNewPack())) {
                String newName = JOptionPane.showInputDialog(this, "Enter the new name of the WadPack", selectedWadPack.getName());
                if (newName != null) Config.updateWadPack(selectedWadPack, newName);
                updateUI();
            }
        });

        allWadsButtons.add(addButton);
        allWadsButtons.add(clearButton);
        allChosenWadsButtons.add(removeButton);
        allChosenWadsButtons.add(saveButton);
        allWadPacksButtons.add(deleteButton);
        allWadPacksButtons.add(renameButton);


        //Adding all the components to the tab
        add(new JLabel("Available Wads"), c.getNextConstraints(0.1));
        add(new JLabel("Wads in WadPack"), c.getNextConstraints());
        add(new JLabel("WadPacks"), c.getNextConstraints());

        add(allWadsPane, c.getNextConstraints(1.0));
        add(allChosenWadsPane, c.getNextConstraints());
        add(allWadPacksPane, c.getNextConstraints());

        add(allWadsButtons, c.getNextConstraints(0.1));
        add(allChosenWadsButtons, c.getNextConstraints());
        add(allWadPacksButtons, c.getNextConstraints());
    }

    private Collection<Wad> getWads() {
        return Config.getWadDirs().stream().map(dir -> {
                    try {
                        return Files.list(Paths.get(dir.getPath())).
                                filter(Files::isRegularFile).
                                map(path -> new Wad(path.toString(), dir.getName())).
                                collect(Collectors.toList());
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                }).filter(Objects::nonNull).
                flatMap(Collection::stream).
                collect(Collectors.toList());
    }

    private Collection<WadPack> getWadPacks() {
        ArrayList<WadPack> createNew = new ArrayList<>();

        //the pack that opens Dialogs for new Pack creation
        createNew.add(WadPack.createNewPack());

        createNew.addAll(Config.getWadPacks());
        return createNew;
    }

    @Override
    public void updateUIContent() {
        allWadsModel.removeAllElements();
        allWadsModel.addAll(getWads());

        allWadPacksModel.removeAllElements();
        allWadPacksModel.addAll(getWadPacks());

        allChosenWadsModel.removeAllElements();
    }

    private WadPack createNewWadPackDialogue() {
        Wad[] iWADS = Wad.getIWADs().toArray(Wad[]::new);
        allChosenWadsModel.removeAllElements();
        if (iWADS.length == 0) {
            JOptionPane.showMessageDialog(this, "Your IWAD directory is empty. Select another directory in the Config-Tab.", "Emtpy IWAD dir", JOptionPane.ERROR_MESSAGE);
            return null;
        } else {
            String nameNewWadPack = JOptionPane.showInputDialog(this, "Enter the name of the new WadPack.");
            if (nameNewWadPack != null && nameNewWadPack.matches("[a-zA-Z\\-_ \\d]+")) {
                Wad iWAD = (Wad) JOptionPane.showInputDialog(this, "Choose an IWAD.", "IWAD Selection", JOptionPane.QUESTION_MESSAGE, null, iWADS, Config.getDefaultIWAD() != null ? Config.getDefaultIWAD() : iWADS[0]);
                if (iWAD != null) {
                    return new WadPack(iWAD, nameNewWadPack);
                } else {
                    System.out.println("Bad Wad input or canceled.");
                    return null;
                }
            } else {
                System.out.println("Canceled Name input or bad input");
                return null;
            }
        }
    }
}
