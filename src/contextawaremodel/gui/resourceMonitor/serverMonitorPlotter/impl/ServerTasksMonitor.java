package contextawaremodel.gui.resourceMonitor.serverMonitorPlotter.impl;

import contextawaremodel.gui.resourceMonitor.taskMonitor.TasksQueueMonitor;
import contextawaremodel.gui.resourceMonitor.taskMonitor.TaskMonitor;
import greenContextOntology.Server;
import greenContextOntology.ProtegeFactory;
import greenContextOntology.Task;

import javax.swing.*;
import java.util.Collection;
import java.util.Random;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Me
 * Date: May 21, 2010
 * Time: 12:49:25 AM
 * To change this template use File | Settings | File Templates.
 */
public class ServerTasksMonitor extends TasksQueueMonitor {
    private Server server;

    public ServerTasksMonitor(Server server) {
        super(null);
        this.server = server;
    }

    protected void refreshData() {
        Collection<Task> tasks = server.getRunningTasks();
        taskNamesPanel.removeAll();
        taskDetailsPanel.removeAll();
        int tasksNo = tasks.size();
        taskNamesPanel.setLayout(new GridLayout(tasksNo, 1));
        taskDetailsPanel.setLayout(new GridLayout(tasksNo, 1));

        for (Task task : tasks) {
            JLabel label = new JLabel(task.getTaskName() + "(" + task.getLocalName() + ")");
            JPanel panel = new JPanel();
            panel.setLayout(new BorderLayout());
            if (generatedColors.containsKey(label.getText())) {
                panel.setBackground(generatedColors.get(label.getText()));
            } else {
                Color color = new Color(new Random().nextInt(200) + 50, new Random().nextInt(200) + 50, new Random().nextInt(200) + 50);
                panel.setBackground(color);
                generatedColors.put(label.getText(), color);
            }

            panel.add(label, BorderLayout.CENTER);
            TaskMonitor monitor = new TaskMonitor(task);
            monitor.getMonitorPanel().setBackground(generatedColors.get(label.getText()));
            monitor.getMonitorPanel().setBorder(BorderFactory.createLineBorder(Color.BLACK));
            taskNamesPanel.add(panel);
            taskDetailsPanel.add(monitor.getMonitorPanel());
        }

        monitorPanel.repaint();
    }

/* private JScrollPane tasksScrollPanel;
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
*/

}
