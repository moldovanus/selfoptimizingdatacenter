package contextaware.gui.resourceMonitor;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Me
 * Date: May 24, 2010
 * Time: 1:52:14 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractMonitor implements IMonitor {

    protected JFrame standaloneWindow;

    protected int refreshRate = 1000;

    protected JPanel monitorPanel;
    private Timer refreshInfoTimer;

    protected AbstractMonitor() {


        refreshInfoTimer = new Timer(refreshRate, new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                Thread thread = new Thread() {

                    public void run() {
                        refreshData();
                    }
                };
                thread.start();

            }
        });
        refreshInfoTimer.start();

    }

    protected abstract void setup();

    protected abstract void refreshData();


    public abstract void executeStandaloneWindow();

    /**
     * @return information refresh rate in milliseconds
     */
    public int getRefreshRate() {
        return refreshRate;
    }

    /**
     * @param refreshRate information refresh rate in milliseconds
     */
    public void setRefreshRate(int refreshRate) {
        this.refreshRate = refreshRate;
    }


    public JPanel getMonitorPanel() {
        return monitorPanel;
    }

    public void destroyStandaloneWindow() {
        if (standaloneWindow != null) {
            standaloneWindow.dispose();
        }
    }

}
