package contextawaremodel.gui.resourceMonitor.serverMonitorPlotter.impl;

import contextawaremodel.GlobalVars;
import contextawaremodel.gui.resourceMonitor.IMonitor;
import contextawaremodel.gui.resourceMonitor.taskMonitor.TasksQueueMonitor;
import contextawaremodel.worldInterface.datacenterInterface.proxies.ServerManagementProxyInterface;
import edu.stanford.smi.protege.exception.OntologyLoadException;
import edu.stanford.smi.protegex.owl.ProtegeOWL;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import greenContextOntology.ProtegeFactory;
import greenContextOntology.Server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: May 16, 2010
 * Time: 2:25:43 PM
 * To change this template use File | Settings | File Templates.
 * This class created a tabbed panel containing on one tab a ServerMonitorXYPlotter and on the other a ServerMonitorPiePlotter
 */
public class FullServerMonitor implements IMonitor {

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

    private JFrame standaloneWindow;

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

        JInternalFrame taskQueueFrame = new JInternalFrame("Running tasks");
        JInternalFrame totalResourcesUsageFrame = new JInternalFrame("Total resources usage");
        JInternalFrame taskResourcesUsageFrame = new JInternalFrame("Tasks resources usage");

        /*8888888888888888888888888*/

        showTaskQueueButton = new JButton("Task queue");
        showTotalResourcesUsageButton = new JButton("Total resources usage");
        showTasksResourcesUsageButton = new JButton("Tasks resources usage");

        showTaskQueueButton.setBackground(new Color(140, 189, 255));
        showTotalResourcesUsageButton.setBackground(new Color(140, 189, 255));
        showTasksResourcesUsageButton.setBackground(new Color(140, 189, 255));

        taskQueueFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        taskQueueFrame.setBounds(10, 50, 200, 500);
        taskQueueFrame.setClosable(true);
        taskQueueFrame.setMaximizable(true);
        taskQueueFrame.setResizable(true);

        totalResourcesUsageFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        totalResourcesUsageFrame.setBounds(220, 50, 500, 500);
        totalResourcesUsageFrame.setClosable(true);
        totalResourcesUsageFrame.setMaximizable(true);
        totalResourcesUsageFrame.setResizable(true);

        taskResourcesUsageFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        taskResourcesUsageFrame.setBounds(720, 40, 500, 500);
        taskResourcesUsageFrame.setClosable(true);
        taskResourcesUsageFrame.setMaximizable(true);
        taskResourcesUsageFrame.setResizable(true);

        totalResourcesUsageFrame.add(xyMonitor.getMonitorPanel(), BorderLayout.CENTER);
        taskResourcesUsageFrame.add(pieMonitor.getMonitorPanel(), BorderLayout.CENTER);
        taskQueueFrame.add(serverTasksMonitor.getMonitorPanel(), BorderLayout.CENTER);

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
                taskQueueFrame.add(serverTasksMonitor.getMonitorPanel(), BorderLayout.CENTER);
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
                totalResourcesUsageFrame.add(xyMonitor.getMonitorPanel(), BorderLayout.CENTER);
                totalResourcesUsageFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                totalResourcesUsageFrame.setBounds(120, 50, 500, 500);
                totalResourcesUsageFrame.setClosable(true);
                totalResourcesUsageFrame.setMaximizable(true);
                totalResourcesUsageFrame.setResizable(true);
                totalResourcesUsageFrame.setVisible(true);
                serverMonitorWindow.add(totalResourcesUsageFrame);
            }
        });

        showTasksResourcesUsageButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JInternalFrame taskResourcesUsageFrame = new JInternalFrame("Tasks resources usage");
                taskResourcesUsageFrame.add(pieMonitor.getMonitorPanel(), BorderLayout.CENTER);
                taskResourcesUsageFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                taskResourcesUsageFrame.setBounds(620, 50, 500, 500);
                taskResourcesUsageFrame.setClosable(true);
                taskResourcesUsageFrame.setMaximizable(true);
                taskResourcesUsageFrame.setResizable(true);
                taskResourcesUsageFrame.setVisible(true);
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
        standaloneWindow = new JFrame(server.getLocalName() + " Monitor");
        standaloneWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        standaloneWindow.setLayout(new BorderLayout());
        standaloneWindow.add(serverMonitorPanel, "Center");
        standaloneWindow.setSize(1200, 800);
        standaloneWindow.setVisible(true);
    }

    public JPanel getMonitorPanel() {
        return serverMonitorPanel;
    }


    public void destroyStandaloneWindow() {
        if (standaloneWindow != null) {
            standaloneWindow.dispose();
        }
    }


}
