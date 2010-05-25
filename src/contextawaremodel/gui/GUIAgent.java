package contextawaremodel.gui;

import actionselection.gui.ActionsOutputFrame;
import contextawaremodel.GlobalVars;
import contextawaremodel.agents.X3DAgent;
import contextawaremodel.gui.resourceMonitor.IMonitor;
import contextawaremodel.gui.resourceMonitor.serverMonitorPlotter.impl.FullServerMonitor;
import contextawaremodel.gui.resourceMonitor.taskMonitor.TasksQueueMonitor;
import contextawaremodel.worldInterface.datacenterInterface.proxies.impl.HyperVServerManagementProxy;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import greenContextOntology.ProtegeFactory;
import greenContextOntology.Server;
import jade.core.Agent;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import logger.LoggerGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GUIAgent extends Agent {

    private ActionsOutputFrame enviromentMonitor;
    private JMenuBar menuBar;
    private LoggerGUI enviromentLogger;
    private LoggerGUI datacenterLogger;

    private AgentController x3DController;
    private AgentContainer container;

    private OWLModel datacenterOwlModel;

    private ProtegeFactory protegeFactory;
    private List<IMonitor> serverMonitors;
    private IMonitor tasksQueueMonitor;

    @Override
    protected void setup() {


        System.out.println("[GUIAgent] Hello!");

        Object[] args = getArguments();
        if (args != null && args.length > 0) {
            datacenterOwlModel = (OWLModel) args[0];
            protegeFactory = new ProtegeFactory(datacenterOwlModel);
        } else {
            System.out.println("[GUIAgent] No OWLModel provided.");
            this.doDelete();
            return;
        }
        serverMonitors = new ArrayList<IMonitor>();
        tasksQueueMonitor = new TasksQueueMonitor(protegeFactory);
        container = getContainerController();

        enviromentMonitor = new ActionsOutputFrame("Enviroment");
        enviromentLogger = new LoggerGUI("EnviromentManagementLog");
        datacenterLogger = new LoggerGUI("DatacenterManagementLog");

        enviromentLogger.setLogPath("Logs/");
        datacenterLogger.setLogPath("Logs/");

        JFrame frame = new JFrame("System Control Unit");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        menuBar = new JMenuBar();


        frame.setJMenuBar(menuBar);

        JMenu fileMenu = new JMenu("File");
        JMenu windowMenu = new JMenu("Window");

        JMenu enviromentalControlMenu = new JMenu("Enviromental Control");
        JMenu datacenterControlMenu = new JMenu("Datacenter Control");

        AbstractAction exitAction = new AbstractAction("Exit") {

            public void actionPerformed(ActionEvent e) {
                shutdownPlatform();
            }
        };

        AbstractAction showSelfHealingLogMenuItem = new AbstractAction("Log") {

            public void actionPerformed(ActionEvent e) {
                enviromentLogger.setVisible(true);
            }
        };

        AbstractAction showDatacenterLogMenuItem = new AbstractAction("Log") {

            public void actionPerformed(ActionEvent e) {
                datacenterLogger.setVisible(true);
            }
        };

        AbstractAction showEnviromentMonitorWindowMenuItem = new AbstractAction("Context Monitor") {

            public void actionPerformed(ActionEvent e) {
                //  shutdownPlatform();
                showEnviromentMonitor();
            }
        };

        AbstractAction showServerMonitorsMenuItem = new AbstractAction("Server Monitors") {

            public void actionPerformed(ActionEvent e) {

                for (IMonitor monitor : serverMonitors) {
                    monitor.destroyStandaloneWindow();
                }

                serverMonitors.clear();

                Collection<Server> servers = protegeFactory.getAllServerInstances();
                for (Server server : servers) {
                    IMonitor serverMonitor = new FullServerMonitor(server, new HyperVServerManagementProxy(server.getServerIPAddress()));
                    serverMonitor.executeStandaloneWindow();
                    serverMonitors.add(serverMonitor);
                }
            }
        };

        AbstractAction showTasksQueueMenuItem = new AbstractAction("Tasks Queue") {

            public void actionPerformed(ActionEvent e) {

                tasksQueueMonitor.destroyStandaloneWindow();
                tasksQueueMonitor.executeStandaloneWindow();
            }
        };

        AbstractAction showDatacenterSimulationWindowMenuItem = new AbstractAction("X3D  Representation") {

            public void actionPerformed(ActionEvent e) {

                try {
                    x3DController = container.createNewAgent(GlobalVars.X3DAGENT_NAME, X3DAgent.class.getName(), null);
                    x3DController.start();
                } catch (StaleProxyException e1) {
                    e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }

            }
        };

        fileMenu.add(exitAction);

        windowMenu.add(enviromentalControlMenu);
        windowMenu.add(datacenterControlMenu);

        enviromentalControlMenu.add(showEnviromentMonitorWindowMenuItem);
        enviromentalControlMenu.add(showSelfHealingLogMenuItem);

        datacenterControlMenu.add(showDatacenterLogMenuItem);
        datacenterControlMenu.add(showTasksQueueMenuItem);
        datacenterControlMenu.add(showServerMonitorsMenuItem);
        datacenterControlMenu.add(showDatacenterSimulationWindowMenuItem);

        menuBar.add(fileMenu);
        menuBar.add(windowMenu);

        frame.pack();
        frame.setVisible(true);

        this.addBehaviour(new ReceiveChangesGUIBehaviour(this));

    }

    public void shutdownPlatform() {
        System.out.println("[GUIAgent] Shutting down platform ... ");
        try {
            this.getContainerController().getPlatformController().kill();
            System.exit(0);
        } catch (Exception e) {
        }
    }

    private void showEnviromentMonitor() {

        enviromentMonitor.setVisible(true);
    }

    public void setEnviromentMonitorActionsList(ArrayList<String[]> list) {
        enviromentMonitor.setActionsList(list);
    }

    public void setEnviromentMonitorBrokenStatesList(ArrayList<String[]> list) {
        enviromentMonitor.setBrokenStatesList(list);
    }

    public void setEnviromentMonitorBrokenPoliciesList(ArrayList<String> list) {
        enviromentMonitor.setBrokenPoliciesList(list);
    }

    public void logEnviromentManagementInformation(Color color, String header, ArrayList message) {
        enviromentLogger.log(color, header, message);
    }

    public void logDatacenterManagementInformation(Color color, String header, ArrayList message) {
        datacenterLogger.log(color, header, message);
    }

}
