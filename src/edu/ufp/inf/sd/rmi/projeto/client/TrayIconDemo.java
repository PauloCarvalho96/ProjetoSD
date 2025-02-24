package edu.ufp.inf.sd.rmi.projeto.client;

import java.awt.*;
import java.awt.TrayIcon.MessageType;

public class TrayIconDemo {


    public TrayIconDemo(String taskName) throws AWTException {
        if (SystemTray.isSupported()) {
            this.displayTray(taskName);
        } else {
            System.err.println("System tray not supported!");
        }
    }

    public void displayTray(String taskName) throws AWTException {
        //Obtain only one instance of the SystemTray object
        SystemTray tray = SystemTray.getSystemTray();

        //If the icon is a file
        Image image = Toolkit.getDefaultToolkit().createImage("logo/logo.png");
        //Alternative (if the icon is on the classpath):
        //Image image = Toolkit.getDefaultToolkit().createImage(getClass().getResource("icon.png"));

        TrayIcon trayIcon = new TrayIcon(image, "Tray Demo");
        //Let the system resize the image if needed
        trayIcon.setImageAutoSize(true);
        //Set tooltip text for the tray icon
        trayIcon.setToolTip("System tray icon demo");
        tray.add(trayIcon);

        trayIcon.displayMessage("HashCracker", taskName + " is completed!!", MessageType.INFO);
    }
}