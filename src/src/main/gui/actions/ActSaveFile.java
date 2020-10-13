package main.gui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFileChooser;

import main.gui.Menu;

public class ActSaveFile implements ActionListener {
    private static final Logger LOGGER = Logger.getLogger(ActSaveFile.class.getName());

    public void actionPerformed(ActionEvent evt) {
        JFileChooser chooser = new JFileChooser(".");
        String filepath = "";
        chooser.setControlButtonsAreShown(true);
        chooser.setDialogTitle("Specify a file to save");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnVal = chooser.showSaveDialog(Menu.MENU_FRAME);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            filepath = chooser.getSelectedFile().getPath();
            LOGGER.log(Level.FINE, "You chose to save on this folder: {0}", filepath);
            Menu.saveImage(Menu.currentImage(), new File(filepath));
        }
    }
}
