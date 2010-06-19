package benchmark.gui.impl;

import greenContextOntology.Task;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: Me
 * Date: Jun 17, 2010
 * Time: 1:11:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class WorkLoadScheduler {
    private JFrame mainFrame;
    private JPanel upperCenterPanel;
    private JPanel lowerCenterPanel;
    private JPanel centerPanel;

    private JList existingTasksList;
    private JList newSequenceList;
    private JList sequencesList;

    private JPanel existingTasksPanel;
    private JPanel newSequencePanel;
    private JPanel sequencesPanel;


    private JButton addToSequenceButton;
    private JButton scheduleSequenceButton;
    private JButton rescheduleSequenceButton;
    private JButton rescheduleTaskLifeButton;
    private JButton activateSequenceButton;

    private Collection<Task> existingVirtualMachines;


    public static void main(String[] args) {
        new WorkLoadScheduler(null);
    }

    public WorkLoadScheduler(Collection<Task> existingVirtualMachines) {
        this.existingVirtualMachines = existingVirtualMachines;
        setup();
    }

    private void setup() {
        mainFrame = new JFrame("Work Load Scheduler");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        existingTasksList = new JList();
        newSequenceList = new JList();
        sequencesList = new JList();

        upperCenterPanel = new JPanel();
        lowerCenterPanel = new JPanel();
        centerPanel = new JPanel();

        existingTasksPanel = new JPanel();
        newSequencePanel = new JPanel();
        sequencesPanel = new JPanel();

        centerPanel.setLayout(new GridLayout(2,1));
        upperCenterPanel.setLayout(new FlowLayout());
        lowerCenterPanel.setLayout(new FlowLayout());

        existingTasksPanel.setLayout(new BorderLayout());
        newSequencePanel.setLayout(new BorderLayout());
        sequencesPanel.setLayout(new BorderLayout());

        existingTasksPanel.setBorder(BorderFactory.createRaisedBevelBorder());
        newSequencePanel.setBorder(BorderFactory.createRaisedBevelBorder());
        sequencesPanel.setBorder(BorderFactory.createRaisedBevelBorder());

        existingTasksPanel.add(existingTasksList, "Center");
        newSequencePanel.add(newSequenceList, "Center");
        sequencesPanel.add(sequencesList, "Center");

        existingTasksList.add("DDD",new JLabel("fggggggggggggfff"));
        newSequenceList.add(new JLabel("ffff"));
        sequencesList.add(new JLabel("ffff"));

        addToSequenceButton = new JButton("Add");
        scheduleSequenceButton = new JButton("Schedule");
        rescheduleSequenceButton = new JButton("Reschedule Sequence");
        rescheduleTaskLifeButton = new JButton("Set Life Time");
        activateSequenceButton = new JButton("Activate");

        upperCenterPanel.add(existingTasksPanel);
        upperCenterPanel.add(newSequencePanel);
        upperCenterPanel.add(rescheduleTaskLifeButton);

        lowerCenterPanel.add(sequencesPanel);
        lowerCenterPanel.add(rescheduleSequenceButton);

        centerPanel.add(upperCenterPanel);
        centerPanel.add(lowerCenterPanel);

        mainFrame.setLayout(new BorderLayout());
        mainFrame.setSize(1000, 700);


        mainFrame.add(centerPanel, "Center");

        mainFrame.add(activateSequenceButton, "South");

        //mainFrame.pack();
        mainFrame.setVisible(true);

    }


}
