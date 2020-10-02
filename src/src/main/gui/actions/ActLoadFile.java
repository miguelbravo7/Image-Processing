package main.gui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import main.gui.Menu;

public class ActLoadFile implements ActionListener {
	private static final Logger LOGGER = Logger.getLogger(ActLoadFile.class.getName());

    public void actionPerformed(ActionEvent evt) {
        JFileChooser chooser = new JFileChooser(".");
        String filepath = "";
        chooser.setFileFilter(new FileNameExtensionFilter("JPG & PNG Images", "jpg", "png"));
        int returnVal = chooser.showOpenDialog(Menu.MENU_FRAME);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            filepath = chooser.getSelectedFile().getAbsolutePath();
            LOGGER.log(Level.FINE, "You chose to open this file: {0}", filepath);
            Menu.openImage(filepath);
        }
    }
}
