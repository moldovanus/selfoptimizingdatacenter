package contextawaremodel.gui.resourceMonitor.serverMonitorPlotter.impl;

import contextawaremodel.gui.resourceMonitor.taskMonitor.TaskMonitor;
import greenContextOntology.Server;
import greenContextOntology.Task;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: Me
 * Date: May 21, 2010
 * Time: 12:49:25 AM
 * To change this template use File | Settings | File Templates.
 */
public class ServerTasksMonitor  {
    private JScrollPane tasksScrollPanel;
    private JPanel tasksPanel;
    private Server server;

    private int refreshRate = 1000;

    private Timer refreshInfoTimer;

    public ServerTasksMonitor(Server server) {
        this.server = server;
        setup();
        refreshInfoTimer = new Timer(refreshRate, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                refreshData();
            }
        });
        refreshInfoTimer.start();
    }

    public ServerTasksMonitor(int refreshRate) {
        this.refreshRate = refreshRate;
        setup();
        refreshInfoTimer = new Timer(refreshRate, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                refreshData();
            }
        });
        refreshInfoTimer.start();
    }

    private void setup() {
        tasksPanel = new JPanel();
        tasksScrollPanel = new JScrollPane(tasksPanel);
        tasksScrollPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        tasksPanel.setLayout(new FlowLayout());
    }

    public void refreshData() {
        tasksPanel.removeAll();
        Collection<Task> tasks = server.getRunningTasks();
        tasksPanel.setLayout(new GridLayout(tasks.size(),1));
        for (Task task :tasks ) {
            //JLabel label = new JLabel(task.getTaskName());
            //label.setToolTipText(task.toString());
            TaskMonitor taskMonitor = new TaskMonitor(task);
            tasksPanel.add(taskMonitor.getMonitorPanel());
        }
    }

    public JScrollPane getTasksScrollPanel() {
        return tasksScrollPanel;
    }


}
