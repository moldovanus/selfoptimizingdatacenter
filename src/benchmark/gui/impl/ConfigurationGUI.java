package benchmark.gui.impl;

import benchmark.fileIO.ConfigurationFileIO;
import benchmark.gui.AbstractConfigurator;
import greenContextOntology.ProtegeFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Me
 * Date: Jun 18, 2010
 * Time: 6:59:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class ConfigurationGUI {
    private final String ADD_ROW_TOOLTIP = "Inserts an empty row in the table";
    private final String SAVE_TASKS_CONFIGURATION_TOOLTIP = "Saves the tasks configuration information from the tasks table in a user specified file";
    private final String SAVE_SERVES_CONFIGURATION_TOOLTIP = "Saves the servers configuration information from the servers table in a user specified file";
    private final String LOAD_TASKS_CONFIGURATION_TOOLTIP = "<html>Loads a previousely saved tasks configuration.<br><b>WARNING:</b>Replaces the existing configuration</br></html>";
    private final String LOAD_SERVERS_CONFIGURATION_TOOLTIP = "<html>Loads a previousely saved servers configuration.<br><b>WARNING:</b>Replaces the existing configuration</br></html>";
    private final String REMOVE_ROW_TOOLTIP = "Deletes the selected row from the table";
    private final String CREATE_VIRTUAL_MACHINES_TOOLTIP = "<html>Creates virtual machines based on an existing virtual machine with the specified resource requirements." +
            "<br><b>WARNING:</b>It will erase all previous virtual machines from the context model and generate new ones.</br></html>";
    private final String CREATE_SERVER_INSTANCES = "<html>Creates server instances in the context model based on the provided specifications" +
            "<br><b>WARNING:</b>It will erase all servers from the context model and generate new ones.</br></html>";

    private JFrame configurationFrame;


    private JPanel configurationPanel;
    private JPanel taskConfigurationPanel;
    private JPanel serverConfigurationPanel;

    private AbstractConfigurator taskConfigurator;
    private AbstractConfigurator serverConfigurator;

    private JButton addTaskRowButton;
    private JButton removeTaskRowButton;
    private JButton generateVirtualMachinesButton;

    private JButton addServerRowButton;
    private JButton removeServerRowButton;
    private JButton generateServersButton;

    private ProtegeFactory factory;

    private JFileChooser fileChooser;

    private JMenuBar menuBar;
    private JMenu menu;

    private AbstractAction saveTasksConfiguration;
    private AbstractAction saveServersConfiguration;
    private AbstractAction loadTasksConfiguration;
    private AbstractAction loadServersConfiguration;
    private AbstractAction exitAction;


    public ConfigurationGUI(ProtegeFactory factory) {
        this.factory = factory;

        fileChooser = new JFileChooser();

        configurationFrame = new JFrame("Configuration Window");
        configurationFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        taskConfigurationPanel = new JPanel();
        serverConfigurationPanel = new JPanel();
        configurationPanel = new JPanel();

        menuBar = new JMenuBar();
        menu = new JMenu("File");

        addTaskRowButton = new JButton("Add row");
        removeTaskRowButton = new JButton("Remove selected");
        generateVirtualMachinesButton = new JButton("Create Virtual Machines");

        addServerRowButton = new JButton("Add row");
        removeServerRowButton = new JButton("Remove selected");
        generateServersButton = new JButton("Create Server Instances");

        addTaskRowButton.setToolTipText(ADD_ROW_TOOLTIP);
        addServerRowButton.setToolTipText(ADD_ROW_TOOLTIP);

        removeTaskRowButton.setToolTipText(REMOVE_ROW_TOOLTIP);
        removeServerRowButton.setToolTipText(REMOVE_ROW_TOOLTIP);

        generateVirtualMachinesButton.setToolTipText(CREATE_VIRTUAL_MACHINES_TOOLTIP);
        generateServersButton.setToolTipText(CREATE_SERVER_INSTANCES);

        addTaskRowButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                taskConfigurator.insertEmptyRow();
            }
        });

        addServerRowButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                serverConfigurator.insertEmptyRow();
            }
        });

        removeTaskRowButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                taskConfigurator.removeRow();
            }
        });

        removeServerRowButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                serverConfigurator.removeRow();
            }
        });

        final ProtegeFactory pf = factory;
        generateVirtualMachinesButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                taskConfigurator.createEntities(pf);
            }
        });

        generateServersButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                serverConfigurator.createEntities(pf);
            }
        });

        saveTasksConfiguration = new AbstractAction("Save tasks configuration") {

            public void actionPerformed(ActionEvent e) {
                int userOption = fileChooser.showSaveDialog(null);
                if (userOption == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    try {
                        ConfigurationFileIO.saveConfiguration(taskConfigurator.getTableData(), new File(file.getPath() + ".tasks_config"));
                    } catch (IOException e1) {
                        JOptionPane.showMessageDialog(null, "File save error", "Save error", JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        };

        saveServersConfiguration = new AbstractAction("Save servers configuration") {

            public void actionPerformed(ActionEvent e) {
                int userOption = fileChooser.showSaveDialog(null);
                if (userOption == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    try {
                        ConfigurationFileIO.saveConfiguration(serverConfigurator.getTableData(), new File(file.getPath() + ".servers_config"));
                    } catch (IOException e1) {
                        JOptionPane.showMessageDialog(null, "File save error", "Save error", JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        };

        loadTasksConfiguration = new AbstractAction("Load tasks configuration") {

            public void actionPerformed(ActionEvent e) {
                int userOption = fileChooser.showOpenDialog(null);
                if (userOption == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    taskConfigurationPanel.repaint();
                    try {
                        java.util.List<String[]> data = ConfigurationFileIO.loadConfiguration(file);
                        taskConfigurator.setTableData(data);
                    } catch (Exception e1) {
                        JOptionPane.showMessageDialog(null, "Invalid file", "Load error", JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        };

        loadServersConfiguration = new AbstractAction("Load servers configuration") {

            public void actionPerformed(ActionEvent e) {
                int userOption = fileChooser.showOpenDialog(null);
                if (userOption == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    serverConfigurationPanel.repaint();
                    try {
                        java.util.List<String[]> data = ConfigurationFileIO.loadConfiguration(file);
                        serverConfigurator.setTableData(data);
                    } catch (Exception e1) {
                        JOptionPane.showMessageDialog(null, "Invalid file", "Load error", JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        };

        exitAction = new AbstractAction("Close window") {
            public void actionPerformed(ActionEvent e) {
                configurationFrame.dispose();
            }
        };


        configurationFrame.setJMenuBar(menuBar);
        menuBar.add(menu);

        saveTasksConfiguration.putValue(AbstractAction.SHORT_DESCRIPTION, SAVE_TASKS_CONFIGURATION_TOOLTIP);
        saveServersConfiguration.putValue(AbstractAction.SHORT_DESCRIPTION, SAVE_SERVES_CONFIGURATION_TOOLTIP);
        loadTasksConfiguration.putValue(AbstractAction.SHORT_DESCRIPTION, LOAD_TASKS_CONFIGURATION_TOOLTIP);
        loadServersConfiguration.putValue(AbstractAction.SHORT_DESCRIPTION, LOAD_SERVERS_CONFIGURATION_TOOLTIP);

        menu.add(saveTasksConfiguration);
        menu.add(saveServersConfiguration);
        menu.add(loadTasksConfiguration);
        menu.add(loadServersConfiguration);
        menu.add(exitAction);

        taskConfigurator = new TaskConfigurator();
        serverConfigurator = new ServerConfigurator();

        configurationFrame.setSize(1200, 700);
        configurationFrame.setLayout(new BorderLayout());


        taskConfigurationPanel.setLayout(new BorderLayout());
        serverConfigurationPanel.setLayout(new BorderLayout());
        configurationPanel.setLayout(new GridLayout(2, 1));

        configurationPanel.add(taskConfigurationPanel);
        configurationPanel.add(serverConfigurationPanel);

        configurationFrame.add(configurationPanel, BorderLayout.CENTER);
        configurationFrame.add(new JLabel("<html><b>Note:</b> For help just move the mouse over any buton or column entry (NOT column name)." +
                "<br> For info about columns if tale is empty insert and empty row first.</br></html>"), BorderLayout.SOUTH);

        JPanel taskConfigurationButtonsPanel = new JPanel();
        JPanel serverConfigurationButtonsPanel = new JPanel();

        taskConfigurationButtonsPanel.setLayout(new GridBagLayout());
        serverConfigurationButtonsPanel.setLayout(new GridBagLayout());

        taskConfigurationPanel.setBorder(BorderFactory.createLoweredBevelBorder());
        serverConfigurationPanel.setBorder(BorderFactory.createLoweredBevelBorder());

        taskConfigurationPanel.add(new JLabel("Workload configuration: virtual machines resources requirements"), BorderLayout.NORTH);
        serverConfigurationPanel.add(new JLabel("Datacenter configuration: servers information and optimum load ranges"), BorderLayout.NORTH);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 1;
        constraints.weighty = 1;

        constraints.gridwidth = 1;
        constraints.gridheight = 1;

        taskConfigurationButtonsPanel.add(addTaskRowButton, constraints);

        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;

        taskConfigurationButtonsPanel.add(removeTaskRowButton, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 2;
        constraints.gridheight = 1;

        taskConfigurationButtonsPanel.add(generateVirtualMachinesButton, constraints);

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;

        serverConfigurationButtonsPanel.add(addServerRowButton, constraints);

        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;

        serverConfigurationButtonsPanel.add(removeServerRowButton, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 2;
        constraints.gridheight = 1;

        serverConfigurationButtonsPanel.add(generateServersButton, constraints);

        taskConfigurationPanel.add(taskConfigurator.getTablePane(), BorderLayout.CENTER);
        taskConfigurationPanel.add(taskConfigurationButtonsPanel, BorderLayout.SOUTH);

        serverConfigurationPanel.add(serverConfigurator.getTablePane(), BorderLayout.CENTER);
        serverConfigurationPanel.add(serverConfigurationButtonsPanel, BorderLayout.SOUTH);

    }

    public void setVisible(boolean visible) {
        configurationFrame.setVisible(visible);
    }

    public static void main(String[] args) {
        new ConfigurationGUI(null).setVisible(true);
    }
}
