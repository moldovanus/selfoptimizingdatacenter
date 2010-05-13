package contextawaremodel.worldInterface.datacenterInterface.proxies.impl;

import contextawaremodel.worldInterface.datacenterInterface.xmlParsers.ServerInfoSAXHandler;
import contextawaremodel.worldInterface.dtos.ServerDto;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: May 8, 2010
 * Time: 11:06:39 AM
 * To change this template use File | Settings | File Templates.
 */
public class HyperVServerManagementProxy extends ServerManagementProxy {

    public HyperVServerManagementProxy(String hostName) {
        super(hostName);
    }

    public static void main(String[] args) {
        ServerManagementProxy serverManagementProxy = new HyperVServerManagementProxy("192.168.2.123");
        ServerManagementProxy.DEBUG = true;
        //serverManagementProxy.getServerInfo();
        serverManagementProxy.moveSourceActions("\\\\192.168.2.123\\VirtualMachines\\myVM", "TestMachine");
        ServerManagementProxy serverManagementProxy1 = new HyperVServerManagementProxy("192.168.2.101");
        serverManagementProxy1.DEBUG = true;
        serverManagementProxy1.getServerInfo();
        serverManagementProxy1.moveDestinationActions("\\\\192.168.2.123\\VirtualMachines\\myVM", "\\\\192.168.2.101\\VirtualMachines\\myVM", "TestMachine");
        //serverManagementProxy.sendServerToSleep();
        System.out.println("End");

    }

    public ServerDto getServerInfo() {

        ServerDto serverDto = null;
        try {
            URL url = new URL("http://" + hostName + "/ServerManagement/Service1.asmx/GetServerInfo");
            URLConnection connection = url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            //Send header
            BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "utf-8"));
            wr.write("POST /ServerManagement/Service1.asmx/GetServerInfo HTTP/1.1\r\n");
            wr.write("Host: " + hostName + "\r\n");
            wr.write("Content-Type: application/x-www-form-urlencoded\r\n");
            wr.write("Content-Length: " + 0 + "\r\n");
            wr.write("\r\n");

            wr.flush();

            BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String content = "";
            // Response
            String line;
            while ((line = rd.readLine()) != null) {
                if (DEBUG) {
                    System.out.println(line);
                }
                if (line.length() > 0 && line.charAt(1) != '?') {
                    content += "\n" + line;
                }
            }

            try {
                XMLReader reader = XMLReaderFactory.createXMLReader();
                ServerInfoSAXHandler handler = new ServerInfoSAXHandler();
                reader.setContentHandler(handler);
                ByteArrayInputStream in = new ByteArrayInputStream(content.getBytes());
                InputSource source = new InputSource(in);
                reader.parse(source);
                serverDto = handler.getServerDto();
                if (DEBUG) {
                    // System.out.println("Handler ret " + handler.ret);
                }
                // res = Double.valueOf(handler.ret).doubleValue();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return serverDto;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void moveDestinationActions(String path1, String path2, String vmName) {
        try {
            //Socket sock = new Socket(hostName, 80);
            URL url = new URL("http://" + hostName + "/ServerManagement/Service1.asmx/MoveDestinationActions");
            URLConnection connection = url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);

            //Send header
            String data = "path1=" + path1 + "&path2=" + path2 + "&vmName=" + vmName;
            BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "utf-8"));
            wr.write("POST /ServerManagement/Service1.asmx/MoveDestinationActions HTTP/1.1\r\n");
            wr.write("Host: " + hostName + "\r\n");
            wr.write("Content-Type: application/x-www-form-urlencoded\r\n");
            wr.write("Content-Length: " + data.length() + "\r\n");
            wr.write("\r\n");
            wr.write(data);
            wr.write("\r\n");
            wr.write(data);
            wr.write("\r\n");

            wr.flush();
            if (DEBUG) {
                BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                // Response
                String line;
                while ((line = rd.readLine()) != null) {
                    System.out.println(line);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void moveSourceActions(String path, String vmName) {
        try {
            //Socket sock = new Socket(hostName, 80);
            URL url = new URL("http://" + hostName + "/ServerManagement/Service1.asmx/MoveSourceActions");
            URLConnection connection = url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);

            //Send header
            String data = "path=" + path + "&vmName=" + vmName;
            BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "utf-8"));

            wr.write("POST /ServerManagement/Service1.asmx/MoveSourceActions HTTP/1.1\r\n");
            wr.write("Host: " + hostName + "\r\n");
            wr.write("Content-Type: application/x-www-form-urlencoded\r\n");
            wr.write("Content-Length: " + data.length() + "\r\n");
            wr.write("\r\n");
            wr.write(data);
            wr.write("\r\n");
            wr.write(data);
            wr.write("\r\n");

            wr.flush();
            if (DEBUG) {
                BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                // Response
                String line;
                while ((line = rd.readLine()) != null) {

                    System.out.println(line);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deployVirtualMachine(String from, String to, String vmName) {
        try {
            //Socket sock = new Socket(hostName, 80);
            URL url = new URL("http://" + hostName + "/ServerManagement/Service1.asmx/DeployVirtualMachine ");
            URLConnection connection = url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);

            //Send header
            String data = "from=" + from + "&to=" + to + "&vmName=" + vmName;
            BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "utf-8"));
            wr.write("POST /ServerManagement/Service1.asmx/DeployVirtualMachine HTTP/1.1\r\n");
            wr.write("Host: " + hostName + "\r\n");
            wr.write("Content-Type: application/x-www-form-urlencoded\r\n");
            wr.write("Content-Length: " + data.length() + "\r\n");
            wr.write("\r\n");
            wr.write(data);
            wr.write("\r\n");

            wr.write(data);
            wr.write("\r\n");

            wr.flush();
            if (DEBUG) {
                BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                // Response
                String line;
                while ((line = rd.readLine()) != null) {

                    System.out.println(line);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void startVirtualMachine(String vmName) {
        try {
            //Socket sock = new Socket(hostName, 80);
            URL url = new URL("http://" + hostName + "/ServerManagement/Service1.asmx/StartVirtualMachine");
            URLConnection connection = url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);

            //Send header
            String data = "vmName=" + vmName;
            BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "utf-8"));
            wr.write("POST /ServerManagement/Service1.asmx/StartVirtualMachine HTTP/1.1\r\n");
            wr.write("Host: " + hostName + "\r\n");
            wr.write("Content-Type: application/x-www-form-urlencoded\r\n");
            wr.write("Content-Length: " + data.length() + "\r\n");
            wr.write("\r\n");
            wr.write(data);
            wr.write("\r\n");

            wr.flush();
            if (DEBUG) {
                BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                // Response
                String line;
                while ((line = rd.readLine()) != null) {

                    System.out.println(line);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopVirtualMachine(String vmName) {
        try {
            //Socket sock = new Socket(hostName, 80);
            URL url = new URL("http://" + hostName + "/ServerManagement/Service1.asmx/StopVirtualMachine");
            URLConnection connection = url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);

            //Send header
            String data = "vmName=" + vmName;
            BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "utf-8"));
            wr.write("POST /ServerManagement/Service1.asmx/StopVirtualMachine HTTP/1.1\r\n");
            wr.write("Host: " + hostName + "\r\n");
            wr.write("Content-Type: application/x-www-form-urlencoded\r\n");
            wr.write("Content-Length: " + data.length() + "\r\n");
            wr.write("\r\n");

            wr.write(data);
            wr.write("\r\n");

            wr.flush();
            if (DEBUG) {
                BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                // Response
                String line;
                while ((line = rd.readLine()) != null) {

                    System.out.println(line);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteVirtualMachine(String vmName) {
        try {
            //Socket sock = new Socket(hostName, 80);
            URL url = new URL("http://" + hostName + "/ServerManagement/Service1.asmx/DeleteVirtualMachine");
            URLConnection connection = url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);

            //Send header
            String data = "vmName=" + vmName;
            BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "utf-8"));
            wr.write("POST /ServerManagement/Service1.asmx/DeleteVirtualMachine HTTP/1.1\r\n");
            wr.write("Host: " + hostName + "\r\n");
            wr.write("Content-Type: application/x-www-form-urlencoded\r\n");
            wr.write("Content-Length: " + data.length() + "\r\n");
            wr.write("\r\n");
            wr.write(data);
            wr.write("\r\n");

            wr.flush();
            if (DEBUG) {
                BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                // Response
                String line;
                while ((line = rd.readLine()) != null) {

                    System.out.println(line);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void wakeUpServer(String mac, String ipAddress, int port) {

        try {
            //Socket sock = new Socket(hostName, 80);
            URL url = new URL("http://" + hostName + "/ServerManagement/Service1.asmx/WakeUpServer ");
            URLConnection connection = url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);

            //Send header
            String data = "";
            data += "mac=" + mac + "&ipAddress=" + ipAddress + "&port=" + port;

            System.out.println(data);
            BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "utf-8"));
            wr.write("POST /ServerManagement/Service1.asmx/WakeUpServer HTTP/1.1\r\n");
            wr.write("Host: " + hostName + "\r\n");
            wr.write("Content-Type: application/x-www-form-urlencoded\r\n");
            wr.write("Content-Length: " + data.length() + "\r\n");
            wr.write("\r\n");
            wr.write(data);
            wr.write("\r\n");

            wr.flush();
            if (DEBUG) {
                BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                // Response
                String line;
                while ((line = rd.readLine()) != null) {

                    System.out.println(line);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendServerToSleep() {

        try {
            //Socket sock = new Socket(hostName, 80);
            URL url = new URL("http://" + hostName + "/ServerManagement/Service1.asmx/SendServerToSleep");
            URLConnection connection = url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "utf-8"));
            wr.write("POST /ServerManagement/Service1.asmx/SendServerToSleep HTTP/1.1\r\n");
            wr.write("Host: " + hostName + "\r\n");
            wr.write("Content-Type: application/x-www-form-urlencoded\r\n");
            wr.write("Content-Length: " + 0 + "\r\n");
            wr.write("\r\n");

            wr.flush();

            //connection hangs until server wakes up. Good because this invokes Restart so it waiths until server goes to sleep :D 
            if (DEBUG) {
                BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                // Response
                String line;
                while ((line = rd.readLine()) != null) {

                    System.out.println(line);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
