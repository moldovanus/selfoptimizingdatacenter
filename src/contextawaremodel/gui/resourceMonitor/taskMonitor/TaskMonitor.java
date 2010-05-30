package contextawaremodel.gui.resourceMonitor.taskMonitor;

import contextawaremodel.gui.resourceMonitor.IMonitor;
import contextawaremodel.gui.resourceMonitor.resourceMonitorPlotter.impl.TaskResourceMonitor;
import greenContextOntology.ReceivedTaskInfo;
import greenContextOntology.RequestedTaskInfo;
import greenContextOntology.Task;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: Me
 * Date: May 24, 2010
 * Time: 11:48:19 AM
 * To change this template use File | Settings | File Templates.
 */
public class TaskMonitor implements IMonitor {

    private JPanel taskPanel;
    private JFrame standaloneWindow;

    private TaskResourceMonitor taskCPUMonitor;
    private TaskResourceMonitor taskMemoryMonitor;
    private TaskResourceMonitor taskStorageMonitor;

    private Task task;

    private int refreshRate = 1000;
    private Timer refreshInfoTimer;

    public TaskMonitor(Task task) {
        this.task = task;
        setup();
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

    private void refreshData() {
        ReceivedTaskInfo receivedTaskInfo = task.getReceivedInfo();
        if (receivedTaskInfo != null) {
            taskCPUMonitor.setCurrentValue(receivedTaskInfo.getCpuReceived());
            taskMemoryMonitor.setCurrentValue(receivedTaskInfo.getMemoryReceived());
            taskStorageMonitor.setCurrentValue(receivedTaskInfo.getStorageReceived());
        }
    }

    private void setup() {
        taskPanel = new JPanel();
        taskPanel.setBackground(new Color(255, 255, 255));

        JPanel centerPanel = new JPanel();

        centerPanel.setLayout(new GridLayout(3, 1));
        taskPanel.setLayout(new BorderLayout());

        RequestedTaskInfo requestedTaskInfo = task.getRequestedInfo();
        ReceivedTaskInfo receivedTaskInfo = task.getReceivedInfo();

        taskCPUMonitor = new TaskResourceMonitor("CPU", requestedTaskInfo.getCpuMinAcceptableValue(), requestedTaskInfo.getCpuMaxAcceptableValue());
        taskMemoryMonitor = new TaskResourceMonitor("Memory", requestedTaskInfo.getMemoryMinAcceptableValue(), requestedTaskInfo.getMemoryMaxAcceptableValue());
        taskStorageMonitor = new TaskResourceMonitor("Storage", requestedTaskInfo.getStorageMinAcceptableValue(), requestedTaskInfo.getStorageMaxAcceptableValue());

        taskCPUMonitor.setCurrentValue(receivedTaskInfo.getCpuReceived());
        taskMemoryMonitor.setCurrentValue(receivedTaskInfo.getMemoryReceived());
        taskStorageMonitor.setCurrentValue(receivedTaskInfo.getStorageReceived());

        JPanel cpuPanel = new JPanel();
                         cpuPanel.setLayout(new BorderLayout());
        cpuPanel.add(new JLabel("Cores " +  task.getRequestedInfo().getCores()), BorderLayout.NORTH);
        cpuPanel.add(taskCPUMonitor.getGraphPanel(),BorderLayout.CENTER);

        centerPanel.add(cpuPanel);
        centerPanel.add(taskMemoryMonitor.getGraphPanel());
        centerPanel.add(taskStorageMonitor.getGraphPanel());
        centerPanel.setBorder(BorderFactory.createLineBorder(Color.RED, 2));

        taskPanel.add(new JLabel(task.getTaskName() + "(" + task.getLocalName() + ")"), BorderLayout.NORTH);
        taskPanel.add(centerPanel, BorderLayout.CENTER);

    }

    public void executeStandaloneWindow() {
        standaloneWindow = new JFrame(task.getTaskName() + "(" + task.getLocalName() + ")" + " Monitor");
        standaloneWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        standaloneWindow.setLayout(new BorderLayout());
        standaloneWindow.add(taskPanel, "Center");
        standaloneWindow.setSize(300, 500);
        standaloneWindow.setVisible(true);
    }

    public JPanel getMonitorPanel() {
        return taskPanel;
    }

    public void destroyStandaloneWindow() {
        if (standaloneWindow != null) {
            standaloneWindow.dispose();
        }
    }
}
