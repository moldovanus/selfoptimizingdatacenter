package contextawaremodel.gui.resourceMonitor.serverMonitorPlotter.impl;

import contextawaremodel.gui.resourceMonitor.IServerMonitor;
import contextawaremodel.gui.resourceMonitor.ServerMonitor;
import contextawaremodel.worldInterface.datacenterInterface.proxies.ServerManagementProxyInterface;
import greenContextOntology.Server;

import javax.swing.*;
import javax.swing.event.InternalFrameListener;
import javax.swing.event.InternalFrameEvent;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: May 16, 2010
 * Time: 2:25:43 PM
 * To change this template use File | Settings | File Templates.
 * This class created a tabbed panel containing on one tab a ServerMonitorXYPlotter and on the other a ServerMonitorPiePlotter
 */
public class FullServerMonitor implements IServerMonitor {

    private int refreshRate = 1000;
    private JDesktopPane serverMonitorWindow;
    private JToolBar toolBar;
    private JPanel serverMonitorPanel;
    private JButton showTaskQueueButton;
    private JButton showTotalResourcesUsageButton;
    private JButton showTasksResourcesUsageButton;
    private ServerTasksMonitor serverTasksMonitor;
    private ServerMonitor xyMonitor;
    private ServerMonitor pieMonitor;

    private Server server;
    private ServerManagementProxyInterface proxy;

    private InternalFrameListener closeTaskQueueListener = new InternalFrameListener() {

        public void internalFrameOpened(InternalFrameEvent e) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        public void internalFrameClosing(InternalFrameEvent e) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        public void internalFrameClosed(InternalFrameEvent e) {
            serverTasksMonitor.stopGatheringData();
        }

        public void internalFrameIconified(InternalFrameEvent e) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        public void internalFrameDeiconified(InternalFrameEvent e) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        public void internalFrameActivated(InternalFrameEvent e) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        public void internalFrameDeactivated(InternalFrameEvent e) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

    };

    private InternalFrameListener closeTotalResourcesListener = new InternalFrameListener() {
        public void internalFrameOpened(InternalFrameEvent e) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        public void internalFrameClosing(InternalFrameEvent e) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        public void internalFrameClosed(InternalFrameEvent e) {
            xyMonitor.stopGatheringData();
        }

        public void internalFrameIconified(InternalFrameEvent e) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        public void internalFrameDeiconified(InternalFrameEvent e) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        public void internalFrameActivated(InternalFrameEvent e) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        public void internalFrameDeactivated(InternalFrameEvent e) {
            //To change body of implemented methods use File | Settings | File Templates.
        }
    };

    private InternalFrameListener closeTasksResourcesListener = new InternalFrameListener() {
        public void internalFrameOpened(InternalFrameEvent e) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        public void internalFrameClosing(InternalFrameEvent e) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        public void internalFrameClosed(InternalFrameEvent e) {
            pieMonitor.stopGatheringData();
        }

        public void internalFrameIconified(InternalFrameEvent e) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        public void internalFrameDeiconified(InternalFrameEvent e) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        public void internalFrameActivated(InternalFrameEvent e) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        public void internalFrameDeactivated(InternalFrameEvent e) {
            //To change body of implemented methods use File | Settings | File Templates.
        }


    };

    public FullServerMonitor(Server server, ServerManagementProxyInterface proxy, int refreshRate) {
        this.refreshRate = refreshRate;
        this.server = server;
        this.proxy = proxy;
        setup();
    }

    public FullServerMonitor(Server server, ServerManagementProxyInterface proxy) {
        this.server = server;
        this.proxy = proxy;
        setup();
    }

    private void setup() {
        serverTasksMonitor = new ServerTasksMonitor(server);
        xyMonitor = new ServerMonitorXYPlotter(server, proxy, refreshRate);
        pieMonitor = new ServerMonitorPiePlotter(server, proxy, refreshRate);

        serverMonitorWindow = new JDesktopPane();
        serverMonitorPanel = new JPanel();
        toolBar = new JToolBar();

        JInternalFrame taskQueueFrame = new JInternalFrame("Task queue");
        JInternalFrame totalResourcesUsageFrame = new JInternalFrame("Total resources usage");
        JInternalFrame taskResourcesUsageFrame = new JInternalFrame("Tasks resources usage");

        showTaskQueueButton = new JButton("Task queue");
        showTotalResourcesUsageButton = new JButton("Total resources usage");
        showTasksResourcesUsageButton = new JButton("Tasks resources usage");

        toolBar.add(showTaskQueueButton);
        toolBar.add(showTotalResourcesUsageButton);
        toolBar.add(showTasksResourcesUsageButton);

        taskQueueFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        taskQueueFrame.addInternalFrameListener(closeTaskQueueListener);

        totalResourcesUsageFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        totalResourcesUsageFrame.addInternalFrameListener(closeTotalResourcesListener);

        taskResourcesUsageFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        taskResourcesUsageFrame.addInternalFrameListener(closeTasksResourcesListener);


        totalResourcesUsageFrame.add(xyMonitor.getServerMonitorPanel(), BorderLayout.CENTER);
        taskResourcesUsageFrame.add(pieMonitor.getServerMonitorPanel(), BorderLayout.CENTER);
        taskQueueFrame.add(serverTasksMonitor.getTasksScrollPanel(), BorderLayout.CENTER);

        serverMonitorWindow.add(taskQueueFrame);
        serverMonitorWindow.add(totalResourcesUsageFrame);
        serverMonitorWindow.add(taskResourcesUsageFrame);
        serverMonitorWindow.add(toolBar, BorderLayout.SOUTH);

        showTaskQueueButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JInternalFrame taskQueueFrame = new JInternalFrame("Task queue");
                taskQueueFrame.add(serverTasksMonitor.getTasksScrollPanel(), BorderLayout.CENTER);
                taskQueueFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                taskQueueFrame.addInternalFrameListener(closeTaskQueueListener);
                serverTasksMonitor.startGatheringData();
                serverMonitorWindow.add(taskQueueFrame);
            }
        });

        showTotalResourcesUsageButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JInternalFrame totalResourcesUsageFrame = new JInternalFrame("Total resources usage");
                totalResourcesUsageFrame.add(xyMonitor.getServerMonitorPanel(), BorderLayout.CENTER);
                totalResourcesUsageFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                totalResourcesUsageFrame.addInternalFrameListener(closeTotalResourcesListener);
                xyMonitor.startGatheringData();
                serverMonitorWindow.add(totalResourcesUsageFrame);
            }
        });

        showTasksResourcesUsageButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JInternalFrame taskResourcesUsageFrame = new JInternalFrame("Tasks resources usage");
                taskResourcesUsageFrame.add(pieMonitor.getServerMonitorPanel(), BorderLayout.CENTER);
                taskResourcesUsageFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                taskResourcesUsageFrame.addInternalFrameListener(closeTasksResourcesListener);
                pieMonitor.startGatheringData();
                serverMonitorWindow.add(taskResourcesUsageFrame);

            }
        });

        totalResourcesUsageFrame.setVisible(true);
        taskResourcesUsageFrame.setVisible(true);
        taskQueueFrame.setVisible(true);

        serverMonitorWindow.setVisible(true);

    }
    //TODO : add toolbar and coe stuff


    public void executeStandaloneWindow() {
       /* JFrame frame = new JFrame(server.getLocalName() + " Monitor");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(serverMonitorPanel, "Center");
        frame.setSize(400, 600);
        frame.setVisible(true);*/
    }

    public JPanel getServerMonitorPanel() {
        return serverMonitorPanel;
    }

    public void stopGatheringData() {
        serverTasksMonitor.stopGatheringData();
        xyMonitor.stopGatheringData();
        pieMonitor.stopGatheringData();
    }

    public void startGatheringData() {
        serverTasksMonitor.startGatheringData();
        xyMonitor.startGatheringData();
        pieMonitor.startGatheringData();
    }
}
