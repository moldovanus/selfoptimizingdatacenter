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
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InstantiationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IllegalAccessException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        serverTasksMonitor = new ServerTasksMonitor(server);
        xyMonitor = new ServerMonitorXYPlotter(server, proxy, refreshRate);
        pieMonitor = new ServerMonitorPiePlotter(server, proxy, refreshRate);

        serverMonitorWindow = new JDesktopPane();
        serverMonitorWindow.setBackground(new Color(100, 149, 237));
        serverMonitorPanel = new JPanel();
        serverMonitorPanel.setSize(800, 500);
        toolBar = new JToolBar();

        JInternalFrame taskQueueFrame = new JInternalFrame("Task queue");
        JInternalFrame totalResourcesUsageFrame = new JInternalFrame("Total resources usage");
        JInternalFrame taskResourcesUsageFrame = new JInternalFrame("Tasks resources usage");

        showTaskQueueButton = new JButton("Task queue");
        showTotalResourcesUsageButton = new JButton("Total resources usage");
        showTasksResourcesUsageButton = new JButton("Tasks resources usage");

        showTaskQueueButton.setBackground(new Color(140, 189, 255));
        showTotalResourcesUsageButton.setBackground(new Color(140, 189, 255));
        showTasksResourcesUsageButton.setBackground(new Color(140, 189, 255));

        taskQueueFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        taskQueueFrame.setBounds(10, 50, 100, 500);
        taskQueueFrame.setClosable(true);
        taskQueueFrame.setMaximizable(true);
        taskQueueFrame.setResizable(true);

        totalResourcesUsageFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        totalResourcesUsageFrame.setBounds(120, 50, 500, 500);
        totalResourcesUsageFrame.setClosable(true);
        totalResourcesUsageFrame.setMaximizable(true);
        totalResourcesUsageFrame.setResizable(true);

        taskResourcesUsageFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        taskResourcesUsageFrame.setBounds(620, 40, 500, 500);
        taskResourcesUsageFrame.setClosable(true);
        taskResourcesUsageFrame.setMaximizable(true);
        taskResourcesUsageFrame.setResizable(true);

        totalResourcesUsageFrame.add(xyMonitor.getServerMonitorPanel(), BorderLayout.CENTER);
        taskResourcesUsageFrame.add(pieMonitor.getServerMonitorPanel(), BorderLayout.CENTER);
        taskQueueFrame.add(serverTasksMonitor.getTasksScrollPanel(), BorderLayout.CENTER);

        serverMonitorWindow.setLayout(null);
        serverMonitorPanel.setLayout(new BorderLayout());

        taskQueueFrame.setVisible(true);
        totalResourcesUsageFrame.setVisible(true);
        taskResourcesUsageFrame.setVisible(true);


        serverMonitorWindow.add(taskQueueFrame);
        serverMonitorWindow.add(totalResourcesUsageFrame);
        serverMonitorWindow.add(taskResourcesUsageFrame);

        serverMonitorPanel.add(serverMonitorWindow, BorderLayout.CENTER);
        serverMonitorPanel.add(toolBar, BorderLayout.SOUTH);


        showTaskQueueButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JInternalFrame taskQueueFrame = new JInternalFrame("Task queue");
                taskQueueFrame.add(serverTasksMonitor.getTasksScrollPanel(), BorderLayout.CENTER);
                taskQueueFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                taskQueueFrame.setBounds(0, 50, 100, 500);
                taskQueueFrame.setClosable(true);
                taskQueueFrame.setMaximizable(true);
                taskQueueFrame.setResizable(true);
                taskQueueFrame.setVisible(true);
  
                serverMonitorWindow.add(taskQueueFrame);
            }
        });

        showTotalResourcesUsageButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JInternalFrame totalResourcesUsageFrame = new JInternalFrame("Total resources usage");
                totalResourcesUsageFrame.add(xyMonitor.getServerMonitorPanel(), BorderLayout.CENTER);
                totalResourcesUsageFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                totalResourcesUsageFrame.setBounds(120, 50, 500, 500);
                totalResourcesUsageFrame.setClosable(true);
                totalResourcesUsageFrame.setMaximizable(true);
                totalResourcesUsageFrame.setResizable(true);
                totalResourcesUsageFrame.setVisible(true);
                xyMonitor.startGatheringData();
                serverMonitorWindow.add(totalResourcesUsageFrame);
            }
        });

        showTasksResourcesUsageButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JInternalFrame taskResourcesUsageFrame = new JInternalFrame("Tasks resources usage");
                taskResourcesUsageFrame.add(pieMonitor.getServerMonitorPanel(), BorderLayout.CENTER);
                taskResourcesUsageFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                taskResourcesUsageFrame.setBounds(620, 50, 500, 500);
                taskResourcesUsageFrame.setClosable(true);
                taskResourcesUsageFrame.setMaximizable(true);
                taskResourcesUsageFrame.setResizable(true);
                taskResourcesUsageFrame.setVisible(true);
                pieMonitor.startGatheringData();
                serverMonitorWindow.add(taskResourcesUsageFrame);

            }
        });

        toolBar.add(showTaskQueueButton);
        toolBar.addSeparator();
        toolBar.add(showTotalResourcesUsageButton);
        toolBar.addSeparator();
        toolBar.add(showTasksResourcesUsageButton);

    }
    //TODO : add toolbar and coe stuff


    public void executeStandaloneWindow() {
        JFrame frame = new JFrame(server.getLocalName() + " Monitor");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(serverMonitorPanel, "Center");
        frame.setSize(1200, 700);
        frame.setVisible(true);
    }

    public JPanel getServerMonitorPanel() {
        return serverMonitorPanel;
    }

  
}
