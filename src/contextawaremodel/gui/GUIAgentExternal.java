package contextawaremodel.gui;

public interface GUIAgentExternal {

     void shutdownPlatform();
     void showMemoryMonitor();
     void startJADEGUI();
     void addIndividual(String name);
     void startRealTimePlot(long interval);
}
