package contextawaremodel.gui.resourceMonitor.taskMonitor;

import contextawaremodel.gui.resourceMonitor.AbstractMonitor;
import greenContextOntology.ProtegeFactory;
import greenContextOntology.Task;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: Me
 * Date: May 24, 2010
 * Time: 1:38:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class TasksQueueMonitor extends AbstractMonitor {

    private ProtegeFactory protegeFactory;
    protected JPanel taskNamesPanel;
    protected JPanel taskDetailsPanel;

    protected Map<String, Color> generatedColors;

    public TasksQueueMonitor(ProtegeFactory protegeFactory) {
        this.protegeFactory = protegeFactory;
        generatedColors = new HashMap<String, Color>();
        setup();
    }

    protected void setup() {

        monitorPanel = new JPanel();
        taskNamesPanel = new JPanel();
        taskDetailsPanel = new JPanel();

        JTabbedPane tabbedPane = new JTabbedPane();
        JScrollPane taskNamesScrollPane = new JScrollPane(taskNamesPanel);
        JScrollPane taskDetailsScrollPane = new JScrollPane(taskDetailsPanel);

//        taskNamesScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
//        taskDetailsScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        monitorPanel.setLayout(new BorderLayout());


        tabbedPane.add("Names", taskNamesScrollPane);
        tabbedPane.add("Details", taskDetailsScrollPane);

        monitorPanel.add(tabbedPane, BorderLayout.CENTER);
    }

    protected void refreshData() {
        Collection<Task> tasks = protegeFactory.getAllTaskInstances();
        taskNamesPanel.removeAll();
        taskDetailsPanel.removeAll();
        int tasksNo = tasks.size();
        taskNamesPanel.setLayout(new GridLayout(tasksNo, 1));
        taskDetailsPanel.setLayout(new GridLayout(tasksNo, 1));

        for (Task task : tasks) {
            if (task.isRunning()) {
                continue;
            }

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

    public void executeStandaloneWindow() {
        standaloneWindow = new JFrame("Tasks Queue Monitor");
        standaloneWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        standaloneWindow.setLayout(new BorderLayout());
        standaloneWindow.add(monitorPanel, "Center");
        standaloneWindow.setSize(400, 600);
        standaloneWindow.setVisible(true);
    }




}
