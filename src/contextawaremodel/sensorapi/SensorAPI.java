package contextawaremodel.sensorapi;

import greenContextOntology.*;

import java.util.Hashtable;

public class SensorAPI {

    private static Hashtable<String, SensorWSReader> sensorReaders =
            new Hashtable<String, SensorWSReader>();
    private static Hashtable<String, ServerInfoReader> serverReaders =
            new Hashtable<String, ServerInfoReader>();

    public static double getSensorValue(String wsURL) {
        WSRequestBuilder wsrb = new WSRequestBuilder(wsURL);
        return wsrb.getSensorValue();
    }

    public static void addSensorListener(String url, SensorListener listener, int timeInterval) {
        if (sensorReaders.containsKey(url)) {
            SensorWSReader reader = sensorReaders.get(url);
            reader.setInterval(Math.min(timeInterval, reader.getInterval()));
            reader.addListener(listener);
        } else {
            SensorWSReader reader = new SensorWSReader(url, timeInterval);
            reader.addListener(listener);
        }
    }

    public static void addServerListener(Server server, ProtegeFactory protegeFactory, int interval) {
        ServerInfoReader reader = new ServerInfoReader(server, protegeFactory, interval);
        serverReaders.put(server.getServerIPAddress(), reader);
    }

    public static void addSensorListener(String url, SensorListener listener) {
        addSensorListener(url, listener, 3000);
    }

    public static void addServerListener(Server server, ProtegeFactory protegeFactory) {
        ServerInfoReader reader = new ServerInfoReader(server, protegeFactory, 3000);
        serverReaders.put(server.getServerIPAddress(), reader);
    }

}
