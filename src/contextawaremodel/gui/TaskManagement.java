/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * TaskManagement.java
 *
 * Created on Mar 23, 2010, 7:40:56 PM
 */

package contextawaremodel.gui;

import greenContextOntology.ProtegeFactory;
import greenContextOntology.Task;
import greenContextOntology.TaskInfo;
import greenContextOntology.Policy;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.ArrayList;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import edu.stanford.smi.protegex.owl.swrl.model.SWRLFactory;
import edu.stanford.smi.protegex.owl.swrl.exceptions.SWRLFactoryException;
import com.hp.hpl.jena.ontology.OntModel;
import jade.core.Agent;
import actionselection.command.RemoveTaskFromServerCommand;
import actionselection.command.Command;
import actionselection.command.DeleteOWLIndividualCommand;

/**
 * @author Moldovanus
 */
public class TaskManagement extends javax.swing.JFrame {

    /**
     * Creates new form TaskManagement
     *
     * @param protegeFactory
     * @param swrlFactory
     * @param ontModel
     * @param agent
     */

    private Collection<Command> commands;
    private int selectedIndex = 0;
    private boolean clearForAdding;
    private boolean addingTask;

    public TaskManagement(ProtegeFactory protegeFactory, SWRLFactory swrlFactory, OntModel ontModel, Agent agent) {
        super("Task Management");
        this.protegeFactory = protegeFactory;
        this.swrlFactory = swrlFactory;
        this.ontModel = ontModel;
        this.agent = agent;
        initComponents();
        commands = new ArrayList<Command>();
    }

    /**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        taskManagementPane = new javax.swing.JPanel();
        tasksListPane = new javax.swing.JScrollPane();
        tasksList = new javax.swing.JList();
        requestedInfoPanel = new javax.swing.JPanel();
        requestedLabel = new javax.swing.JLabel();
        requestedCoresLabel = new javax.swing.JLabel();
        requestedCoresField = new javax.swing.JTextField();
        requestedCpuField = new javax.swing.JTextField();
        requestedMemoryField = new javax.swing.JTextField();
        requestedStorageField = new javax.swing.JTextField();
        requestedCpuLabel = new javax.swing.JLabel();
        requestedMemoryLabel = new javax.swing.JLabel();
        requestedStorageLabel = new javax.swing.JLabel();
        applyTaskChanges = new javax.swing.JButton();
        receivedInfoPane = new javax.swing.JPanel();
        receivedLabel = new javax.swing.JLabel();
        receivedCoresLabel = new javax.swing.JLabel();
        receivedCoresField = new javax.swing.JTextField();
        receivedCpuField = new javax.swing.JTextField();
        receivedCpuLabel = new javax.swing.JLabel();
        receivedMemoryLabel = new javax.swing.JLabel();
        receivedMemoryField = new javax.swing.JTextField();
        receivedStorageField = new javax.swing.JTextField();
        receivedStorageLabel = new javax.swing.JLabel();
        deleteTaskButton = new javax.swing.JButton();
        addTaskPanel = new javax.swing.JPanel();
        addTaskInfoPanel1 = new javax.swing.JPanel();
        addTaskLabel = new javax.swing.JLabel();
        addTaskCoresNo = new javax.swing.JLabel();
        addTaskCoresField = new javax.swing.JTextField();
        addTaskCpuField = new javax.swing.JTextField();
        addTaskMemoryField = new javax.swing.JTextField();
        addTaskStorageField = new javax.swing.JTextField();
        addTaskCpuLabel = new javax.swing.JLabel();
        addTaskMemoryLabel = new javax.swing.JLabel();
        addTaskStorageLabel = new javax.swing.JLabel();
        addTaskButton = new javax.swing.JButton();
        addTaskNameField = new javax.swing.JTextField();
        addTaskNameLabel = new JLabel("Name");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);


        requestedInfoPanel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        requestedLabel.setFont(new java.awt.Font("Tahoma", 1, 14));
        requestedLabel.setText("Requested");

        requestedCoresLabel.setText("Cores No");

        requestedCoresField.setMinimumSize(new java.awt.Dimension(0, 0));

        requestedCpuField.setMinimumSize(new java.awt.Dimension(0, 0));

        requestedMemoryField.setMinimumSize(new java.awt.Dimension(0, 0));

        requestedStorageField.setMinimumSize(new java.awt.Dimension(0, 0));

        requestedCpuLabel.setText("CPU");

        requestedMemoryLabel.setText("Memory");

        requestedStorageLabel.setText("Storage");

        applyTaskChanges.setText("Apply changes");

        javax.swing.GroupLayout requestedInfoPanelLayout = new javax.swing.GroupLayout(requestedInfoPanel);
        requestedInfoPanel.setLayout(requestedInfoPanelLayout);
        requestedInfoPanelLayout.setHorizontalGroup(
                requestedInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(requestedInfoPanelLayout.createSequentialGroup()
                                .addGroup(requestedInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(requestedInfoPanelLayout.createSequentialGroup()
                                                .addContainerGap()
                                                .addGroup(requestedInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(requestedCoresLabel)
                                                        .addComponent(requestedCpuLabel)
                                                        .addComponent(requestedMemoryLabel)
                                                        .addComponent(requestedStorageLabel))
                                                .addGap(18, 18, 18)
                                                .addGroup(requestedInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addComponent(requestedStorageField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(requestedMemoryField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(requestedCpuField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(requestedCoresField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGroup(requestedInfoPanelLayout.createSequentialGroup()
                                        .addGap(48, 48, 48)
                                        .addComponent(requestedLabel)))
                                .addContainerGap(52, Short.MAX_VALUE))
                        .addComponent(applyTaskChanges, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 182, Short.MAX_VALUE)
        );
        requestedInfoPanelLayout.setVerticalGroup(
                requestedInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(requestedInfoPanelLayout.createSequentialGroup()
                        .addComponent(requestedLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(requestedInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(requestedCoresLabel)
                                .addComponent(requestedCoresField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(requestedInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(requestedCpuLabel)
                                .addComponent(requestedCpuField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(requestedInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(requestedMemoryLabel)
                                .addComponent(requestedMemoryField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(13, 13, 13)
                        .addGroup(requestedInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(requestedStorageLabel)
                                .addComponent(requestedStorageField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(applyTaskChanges))
        );

        receivedInfoPane.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        receivedLabel.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        receivedLabel.setText("Received");

        receivedCoresLabel.setText("Cores No");

        receivedCoresField.setEditable(false);
        receivedCoresField.setMinimumSize(new java.awt.Dimension(0, 0));

        receivedCpuField.setEditable(false);
        receivedCpuField.setMinimumSize(new java.awt.Dimension(0, 0));

        receivedCpuLabel.setText("CPU");

        receivedMemoryLabel.setText("Memory");

        receivedMemoryField.setEditable(false);
        receivedMemoryField.setMinimumSize(new java.awt.Dimension(0, 0));


        receivedStorageField.setEditable(false);
        receivedStorageField.setMinimumSize(new java.awt.Dimension(0, 0));

        receivedStorageLabel.setText("Storage");

        javax.swing.GroupLayout receivedInfoPaneLayout = new javax.swing.GroupLayout(receivedInfoPane);
        receivedInfoPane.setLayout(receivedInfoPaneLayout);
        receivedInfoPaneLayout.setHorizontalGroup(
                receivedInfoPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(receivedInfoPaneLayout.createSequentialGroup()
                                .addGap(54, 54, 54)
                                .addComponent(receivedLabel)
                                .addContainerGap(67, Short.MAX_VALUE))
                        .addGroup(receivedInfoPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(receivedInfoPaneLayout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addGroup(receivedInfoPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(receivedCoresLabel)
                                .addComponent(receivedCpuLabel)
                                .addComponent(receivedMemoryLabel)
                                .addComponent(receivedStorageLabel))
                        .addGap(18, 18, 18)
                        .addGroup(receivedInfoPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(receivedStorageField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(receivedMemoryField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(receivedCpuField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(receivedCoresField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(35, Short.MAX_VALUE)))
        );
        receivedInfoPaneLayout.setVerticalGroup(
                receivedInfoPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(receivedInfoPaneLayout.createSequentialGroup()
                                .addComponent(receivedLabel)
                                .addContainerGap(132, Short.MAX_VALUE))
                        .addGroup(receivedInfoPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(receivedInfoPaneLayout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addGroup(receivedInfoPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(receivedCoresLabel)
                                .addComponent(receivedCoresField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(receivedInfoPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(receivedCpuLabel)
                                .addComponent(receivedCpuField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(receivedInfoPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(receivedMemoryLabel)
                                .addComponent(receivedMemoryField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(13, 13, 13)
                        .addGroup(receivedInfoPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(receivedStorageLabel)
                                .addComponent(receivedStorageField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(17, Short.MAX_VALUE)))
        );

        deleteTaskButton.setText("Delete task");


        javax.swing.GroupLayout taskManagementPaneLayout = new javax.swing.GroupLayout(taskManagementPane);
        taskManagementPane.setLayout(taskManagementPaneLayout);
        taskManagementPaneLayout.setHorizontalGroup(
                taskManagementPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(taskManagementPaneLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(taskManagementPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(requestedInfoPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(receivedInfoPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(tasksListPane)
                                .addComponent(deleteTaskButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap(30, Short.MAX_VALUE))
        );
        taskManagementPaneLayout.setVerticalGroup(
                taskManagementPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, taskManagementPaneLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(tasksListPane, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(deleteTaskButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(requestedInfoPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(receivedInfoPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
        );

        jTabbedPane1.addTab("Task Management", taskManagementPane);

        addTaskInfoPanel1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        addTaskLabel.setFont(new java.awt.Font("Tahoma", 1, 14));
        addTaskLabel.setText("Add task");

        addTaskCoresNo.setText("Cores No");

        addTaskCoresField.setMinimumSize(new java.awt.Dimension(0, 0));

        addTaskCpuField.setMinimumSize(new java.awt.Dimension(0, 0));

        addTaskMemoryField.setMinimumSize(new java.awt.Dimension(0, 0));


        addTaskStorageField.setMinimumSize(new java.awt.Dimension(0, 0));

        addTaskCpuLabel.setText("CPU");

        addTaskMemoryLabel.setText("Memory");

        addTaskStorageLabel.setText("Storage");

        addTaskButton.setText("Add task");

        javax.swing.GroupLayout addTaskInfoPanel1Layout = new javax.swing.GroupLayout(addTaskInfoPanel1);
        addTaskInfoPanel1.setLayout(addTaskInfoPanel1Layout);
        addTaskInfoPanel1Layout.setHorizontalGroup(
                addTaskInfoPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(addTaskInfoPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(addTaskInfoPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(addTaskInfoPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(addTaskLabel)
                                        .addGroup(addTaskInfoPanel1Layout.createSequentialGroup()
                                        .addGroup(addTaskInfoPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(addTaskNameLabel)
                                                .addComponent(addTaskCoresNo)
                                                .addComponent(addTaskCpuLabel)
                                                .addComponent(addTaskMemoryLabel)
                                                .addComponent(addTaskStorageLabel))
                                        .addGap(18, 18, 18)
                                        .addGroup(addTaskInfoPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(addTaskNameField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(addTaskStorageField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(addTaskMemoryField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(addTaskCpuField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(addTaskCoresField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addComponent(addTaskButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 202, Short.MAX_VALUE))
                        .addContainerGap())
        );
        addTaskInfoPanel1Layout.setVerticalGroup(
                addTaskInfoPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(addTaskInfoPanel1Layout.createSequentialGroup()
                        .addComponent(addTaskLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(addTaskInfoPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(addTaskNameLabel)
                                .addComponent(addTaskNameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(addTaskInfoPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(addTaskCoresNo)
                                .addComponent(addTaskCoresField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(addTaskInfoPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(addTaskCpuLabel)
                                .addComponent(addTaskCpuField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(addTaskInfoPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(addTaskMemoryLabel)
                                .addComponent(addTaskMemoryField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(13, 13, 13)
                        .addGroup(addTaskInfoPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(addTaskStorageLabel)
                                .addComponent(addTaskStorageField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(addTaskButton)
                        .addContainerGap(475, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout addTaskPanelLayout = new javax.swing.GroupLayout(addTaskPanel);
        addTaskPanel.setLayout(addTaskPanelLayout);
        addTaskPanelLayout.setHorizontalGroup(
                addTaskPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 228, Short.MAX_VALUE)
                        .addGroup(addTaskPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(addTaskPanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(addTaskInfoPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
        );
        addTaskPanelLayout.setVerticalGroup(
                addTaskPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 653, Short.MAX_VALUE)
                        .addGroup(addTaskPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(addTaskPanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(addTaskInfoPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
        );

        jTabbedPane1.addTab("Add Task", addTaskPanel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 678, Short.MAX_VALUE)
        );


        pack();

        tasksList.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        tasksList.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                selectedIndex = tasksList.getSelectedIndex();

                selectedTaskName = (String) tasksList.getModel().getElementAt(selectedIndex);
                //aSystem.out.println("!!!!!!!!!!!!!!!!!!!! @@@@@@@@ Selecteeed : " + selectedTaskName);

                //hack to avoid inconsistencies. list selection fires not ok
                if (selectedTaskName == null) {
                    return;
                }

                selectedTask = protegeFactory.getTask(selectedTaskName.split(" ")[0]);

                TaskInfo requested = selectedTask.getRequestedInfo();
                TaskInfo received = selectedTask.getReceivedInfo();

                requestedCoresField.setText("" + requested.getCores());
                requestedCpuField.setText("" + requested.getCpu());
                requestedStorageField.setText("" + requested.getStorage());
                requestedMemoryField.setText("" + requested.getMemory());

                receivedCoresField.setText("" + received.getCores());
                receivedCpuField.setText("" + received.getCpu());
                receivedStorageField.setText("" + received.getStorage());
                receivedMemoryField.setText("" + received.getMemory());
            }
        });


        deleteTaskButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                //try {
                System.out.println("Deleting instance " + selectedTask);

                RemoveTaskFromServerCommand command = new RemoveTaskFromServerCommand(protegeFactory, selectedTask.getName(), selectedTask.getAssociatedServer().getName());
                //command.execute(ontModel);
                //command.executeOnX3D(agent);
                //selectedTask.deleteInstance(ontModel, swrlFactory);

                DeleteOWLIndividualCommand deleteOWLIndividualCommand = new DeleteOWLIndividualCommand(selectedTask);

                commands.add(command);
                commands.add(deleteOWLIndividualCommand);

                //System.out.println("Instance deleted");
                //} catch (SWRLFactoryException e1) {
                //   e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                //}

                requestedCoresField.setText("");
                requestedCpuField.setText("");
                requestedStorageField.setText("");
                requestedMemoryField.setText("");

                receivedCoresField.setText("");
                receivedCpuField.setText("");
                receivedStorageField.setText("");
                receivedMemoryField.setText("");

                tasksList.disable();
                tasksList.repaint();
                //tasksList.remove(selectedIndex);
                //setTasks(protegeFactory.getAllTaskInstances());

            }
        });

        addTaskButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                //wait in order to avoid ontology inconsistencies
                synchronized (this) {
                    if (!clearForAdding) {
                        try {
                            wait(5000);
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                            return;
                        }
                    }
                }

                addingTask = true;

                String taskName = addTaskNameField.getText();
                Task task = protegeFactory.createTask(taskName);
                Policy policy = protegeFactory.createQoSPolicy(taskName + "Policy");

                TaskInfo requestedInfo = protegeFactory.createTaskInfo(taskName + "RequestedInfo");
                TaskInfo receivedInfo = protegeFactory.createTaskInfo(taskName + "ReceivedInfo");


                requestedInfo.setCores(Integer.parseInt(addTaskCoresField.getText()), ontModel);
                requestedInfo.setCpu(Integer.parseInt(addTaskCpuField.getText()), ontModel);
                requestedInfo.setMemory(Integer.parseInt(addTaskMemoryField.getText()), ontModel);
                requestedInfo.setStorage(Integer.parseInt(addTaskStorageField.getText()), ontModel);

                receivedInfo.setCores(0, ontModel);
                receivedInfo.setCpu(0, ontModel);
                receivedInfo.setMemory(0, ontModel);
                receivedInfo.setStorage(0, ontModel);

                task.setReceivedInfo(receivedInfo, ontModel);
                task.setRequestedInfo(requestedInfo, ontModel);
                policy.setReferenced(task);
                policy.setPriority(1);

                //todo:add means of specifying the priority from the user interface
                task.setCpuWeight(0.1f);
                task.setMemoryWeight(0.1f);
                task.setStorageWeight(0.1f);

                synchronized (this) {
                    addingTask = false;
                    //notify ReinforcementLearningDatacenterBehavior that task has been created
                    notifyAll();
                }
                //task.createSWRLRule(swrlFactory);
            }
        });

    }// </editor-fold>//GEN-END:initComponents

    public void setClearForAdding(boolean clearForAdding) {
        this.clearForAdding = clearForAdding;
    }


    public boolean executeCommands() {

        for (Command command : commands) {
            command.execute(ontModel);
            command.executeOnX3D(agent);
        }
        int size = commands.size();
        commands.clear();
        return size != 0;

    }

    /* public void addDeleteTaskListener(ActionListener listener) {
        deleteTaskButton.addActionListener(listener);
    }

    public void addAddTaskListener(ActionListener listener) {
        addTaskButton.addActionListener(listener);
    }

    public void addApplyChangesListener(ActionListener listener) {
        applyTaskChanges.addActionListener(listener);
    }


    public int getRequestedCoreNo() throws ParseException {
        return numberFormat.parse(this.addTaskCoresField.getText()).intValue();
    }

    public int getRequestedCPU() throws ParseException {
        return numberFormat.parse(this.addTaskCpuField.getText()).intValue();
    }

    public int getRequestedMemory() throws ParseException {
        return numberFormat.parse(this.addTaskMemoryField.getText()).intValue();
    }

    public int getRequestedStorage() throws ParseException {
        return numberFormat.parse(this.addTaskStorageField.getText()).intValue();
    }

    public int getUpdatedRequestedCoreNo() throws ParseException {
        return numberFormat.parse(this.requestedCoresField.getText()).intValue();
    }

    public int getUpdatedRequestedCPU() throws ParseException {
        return numberFormat.parse(this.requestedCpuField.getText()).intValue();
    }

    public int getUpdatedRequestedMemory() throws ParseException {
        return numberFormat.parse(this.requestedMemoryField.getText()).intValue();
    }

    public int getUpdatedRequestedStorage() throws ParseException {
        return numberFormat.parse(this.requestedStorageField.getText()).intValue();
    }

    public void setRequestedCoresField(int value) {
        requestedCoresField.setText("" + value);
    }

    public void setRequestedCpuField(int value) {
        requestedCpuField.setText("" + value);
    }

    public void setRequestedMemoryField(int value) {
        requestedMemoryField.setText("" + value);
    }

    public void setRequestedStorageField(int value) {
        requestedStorageField.setText("" + value);
    }


    public void setReceivedCoresField(int value) {
        receivedCoresField.setText("" + value);
    }

    public void setReceivedCpuField(int value) {
        receivedCpuField.setText("" + value);
    }

    public void setReceivedMemoryField(int value) {
        receivedMemoryField.setText("" + value);
    }

    public void setReceivedStorageField(int value) {
        receivedStorageField.setText("" + value);
    }*/


    public void setTasks(final Collection collection) {
        tasksList.removeAll();

        tasksList.setModel(new javax.swing.AbstractListModel() {
            Object[] objects = collection.toArray();

            public int getSize() {
                return objects.length;
            }

            public Object getElementAt(int i) {
                Task task = null;
                String name = null;
                try {
                    task = ((Task) objects[i]);
                    name = "" + task.getName().split("#")[1] + "  -  ";
                    name += task.isRunning() ? "deployed" : "waiting";
                } catch (Exception e) {
                    System.err.println("Array index out of bounds exception eaten");
                }


                return name;
            }
        });
        tasksListPane.setViewportView(tasksList);
        tasksList.enable();
        tasksList.repaint();
        /*
        tasksList.removeAll();
        for (Object o : collection) {
            tasksList.add(o.toString(), new JLabel(o.toString()));
        }*/
    }


    public String getSelectedTaskName() {
        return selectedTaskName;
    }


    private NumberFormat numberFormat = NumberFormat.getIntegerInstance();

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addTaskButton;
    private javax.swing.JTextField addTaskCoresField;
    private javax.swing.JLabel addTaskCoresNo;
    private javax.swing.JTextField addTaskCpuField;
    private javax.swing.JLabel addTaskCpuLabel;
    private javax.swing.JPanel addTaskInfoPanel1;
    private javax.swing.JLabel addTaskLabel;
    private javax.swing.JTextField addTaskMemoryField;
    private javax.swing.JLabel addTaskMemoryLabel;
    private javax.swing.JPanel addTaskPanel;
    private javax.swing.JTextField addTaskStorageField;
    private javax.swing.JLabel addTaskStorageLabel;
    private javax.swing.JButton applyTaskChanges;
    private javax.swing.JButton deleteTaskButton;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField receivedCoresField;
    private javax.swing.JLabel receivedCoresLabel;
    private javax.swing.JTextField receivedCpuField;
    private javax.swing.JLabel receivedCpuLabel;
    private javax.swing.JPanel receivedInfoPane;
    private javax.swing.JLabel receivedLabel;
    private javax.swing.JTextField receivedMemoryField;
    private javax.swing.JLabel receivedMemoryLabel;
    private javax.swing.JTextField receivedStorageField;
    private javax.swing.JLabel receivedStorageLabel;
    private javax.swing.JTextField requestedCoresField;
    private javax.swing.JLabel requestedCoresLabel;
    private javax.swing.JTextField requestedCpuField;
    private javax.swing.JLabel requestedCpuLabel;
    private javax.swing.JPanel requestedInfoPanel;
    private javax.swing.JLabel requestedLabel;
    private javax.swing.JTextField requestedMemoryField;
    private javax.swing.JLabel requestedMemoryLabel;

    private javax.swing.JTextField requestedStorageField;
    private javax.swing.JLabel requestedStorageLabel;
    private javax.swing.JPanel taskManagementPane;
    private javax.swing.JList tasksList;
    private javax.swing.JScrollPane tasksListPane;
    private javax.swing.JTextField addTaskNameField;
    private javax.swing.JLabel addTaskNameLabel;

    private String selectedTaskName;

    private ProtegeFactory protegeFactory;
    private OntModel ontModel;
    private SWRLFactory swrlFactory;
    private Task selectedTask;

    private Agent agent;
    // End of variables declaration//GEN-END:variables


}
