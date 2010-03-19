/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package actionselection.command;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;

import jade.core.Agent;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import selfHealingOntology.Sensor;
import selfHealingOntology.SelfHealingProtegeFactory;
import contextawaremodel.GlobalVars;
import com.hp.hpl.jena.ontology.OntModel;

/**
 * @author Me
 */

public class SetCommand extends SelfHealingCommand {

    private int newValue = 1;
    private int previousValue = 1;
    private String targetSensor;

    public SetCommand(SelfHealingProtegeFactory protegeFactory, String targetSensor, int newValue) {
        super(protegeFactory);
        this.targetSensor = targetSensor;
        this.newValue = newValue;
    }

    public int getNewValue() {
        return newValue;
    }

    public void setNewValue(int newValue) {
        this.newValue = newValue;
    }

    @Override
    public void execute(OntModel model) {
        Sensor sensor = protegeFactory.getSensor(targetSensor);
        previousValue = sensor.getValueOfService();
        sensor.setValueOfService(newValue,model);
    }

    @Override
    public void rewind(OntModel model)  {

       Sensor sensor = protegeFactory.getSensor(targetSensor);
       sensor.setValueOfService(previousValue,model);

    }

    @Override
    public String toString() {
        return "Set " + targetSensor + " to " + newValue;
    }

    @Override
    public void executeOnWebService() {

        Sensor sensor = protegeFactory.getSensor(targetSensor);

        int value = sensor.getValueOfService();

        String xmldata =
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
                        "<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                        "<soap12:Body>" +
                        "<SetSensorValue xmlns=\"http://tempuri.org/\">\n<value>" + Integer.toString(value) + "</value>\n</SetSensorValue> \n" +
                        "</soap12:Body>\n" +
                        "</soap12:Envelope>";

        try {

            String sensorURL = sensor.getWebServiceURI();
            if (sensorURL != null) {

                //Parse URL and create socket
                String[] uriDetails = sensorURL.split("[:/]+");

                String hostname = uriDetails[1];
                int port = Integer.valueOf(uriDetails[2]);

                InetAddress addr = InetAddress.getByName(hostname);
                Socket sock = new Socket(addr, port);
                String path = uriDetails[3];
                for (int i = 4; i < uriDetails.length; i++) {
                    path += "/" + uriDetails[i];
                }


                //Send header
                BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream(), "utf-8"));
                wr.write("POST /" + path + " HTTP/1.1\r\n");
                wr.write("Host: " + hostname + "\r\n");
                wr.write("Content-Type: text/xml; charset=\"utf-8 \" \r\n");
                wr.write("Content-Length: " + xmldata.length() + "\r\n");
                wr.write("SOAPAction: http://tempuri.org/SetSensorValue \r\n");
                wr.write("\r\n");

                //Send data
                wr.write(xmldata);// System.out.println(xmldata);
                wr.flush();
                BufferedReader rd = new BufferedReader(new InputStreamReader(sock.getInputStream()));
                // Response
                //String line;
                //while ((line = rd.readLine()) != null) {
                //     System.out.println(line);
                // }


                sock.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();

        }
    }

    @Override
    public String[] toStringArray() {
        String[] array = new String[3];
        array[0] = "Set";
        array[1] = targetSensor;
        array[2] = "" + newValue;
        return array;
    }

    public void executeOnX3D(Agent agent) {
         Sensor sensor = protegeFactory.getSensor(targetSensor);
        String actionName = ( sensor.getName().contains("Temperature"))? "setTemperature" : "setHumidity" ;
        ACLMessage message = new ACLMessage(ACLMessage.INFORM);
        try {
            message.setContentObject(new Object[]{actionName, sensor.getValueOfService()});
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        message.addReceiver(new AID(GlobalVars.X3DAGENT_NAME + "@" + agent.getContainerController().getPlatformName()));
        message.setLanguage("JavaSerialization");
        agent.send(message);
    }

    public void rewindOnX3D(Agent agent) {
         Sensor sensor = protegeFactory.getSensor(targetSensor);
        String actionName =( sensor.getName().contains("Temperature"))? "setTemperature" : "setHumidity" ;
        ACLMessage message = new ACLMessage(ACLMessage.INFORM);
        try {
            message.setContentObject(new Object[]{ actionName, sensor.getValueOfService()});
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        message.addReceiver(new AID(GlobalVars.X3DAGENT_NAME + "@" + agent.getContainerController().getPlatformName()));
        message.setLanguage("JavaSerialization");
        agent.send(message);
    }
}
