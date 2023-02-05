package app.gui.Tabs;

import app.Config;
import app.wads.WadDir;
import com.codepoetics.protonpack.StreamUtils;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class ConfigTab extends JPanel implements Tab {
    //TODO configure Layout with GridBagLayout for better scaling
    //TODO add apply changes now button
    private final ArrayList<JTextField> textFields = new ArrayList<>();
    private final JButton addWadDirButton = new JButton("add Wad dir");
    private final JButton saveButton = new JButton("Save");
    private final JButton changeGzdoomDirButton = new JButton("Change GZDoom dir");

    public ConfigTab() {

        setLayout(new GridLayout(0, 3));

        addWadDirButton.addActionListener(e -> {
            String dirName = JOptionPane.showInputDialog(this, "Enter the name of the new dir");
            if (dirName != null || !dirName.equals("")) Config.newWadDir(dirName);
        });

        saveButton.addActionListener(e ->
                //create WadDirs by zipping the contents of the textfields with the WadDir Names from the config
                Config.setWadDirs((ArrayList<WadDir>) StreamUtils.
                        zip(textFields.stream(), Config.getWadDirs().stream(), (textField, wadDir) ->
                                wadDir.setPathReturnThis(textField.getText())).
                        collect(Collectors.toList()))
        );

        changeGzdoomDirButton.addActionListener(e -> {
            JFileChooser fileChooser = createJFileChooser("Choose dir with the GZDoom.exe file", "");
            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                Config.setGzdoomDir(fileChooser.getSelectedFile().getAbsolutePath());
            }
        });
        updateUIContent();
    }

    private JFileChooser createJFileChooser(String dialogTitle, String path) {
        JFileChooser fileChooser = new JFileChooser();

        fileChooser.setCurrentDirectory(new File(path));
        fileChooser.setDialogTitle(dialogTitle);
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setAcceptAllFileFilterUsed(false);
        return fileChooser;
    }

    @Override
    public void updateUIContent() {
        removeAll();

        for (WadDir wadDir : Config.getWadDirs()) {
            JFileChooser fileChooser = createJFileChooser("Choose " + wadDir.getName() + "-Dir", wadDir.getPath());
            JLabel label = new JLabel(wadDir.getName());
            JButton button = new JButton("Choose dir");
            JTextField textField = new JTextField();

            button.addActionListener(e -> {
                if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                    textField.setText(fileChooser.getSelectedFile().getAbsolutePath());
                }
            });
            label.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
            textField.setEditable(false);
            textField.setText(wadDir.getPath());
            //TODO maybe put components in JFrames/ Labels to prevent Awkward Scaling
            add(label);
            add(textField);
            add(button);
            textFields.add(textField);
        }
        //todo maybe put buttons in extra JFrame
        add(addWadDirButton);
        add(saveButton);
        add(changeGzdoomDirButton);
    }
}
