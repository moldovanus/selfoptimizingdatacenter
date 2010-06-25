package contextaware.worldInterface.datacenterInterface.proxies.impl;

import contextaware.worldInterface.datacenterInterface.xmlParsers.ServerInfoSAXHandler;
import contextaware.worldInterface.dtos.ServerDto;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

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

    private void waitUntilTargetIsAlive(String ip) {
        String pingCmd = "ping " + ip;
        boolean ok = false;
        while (!ok) {
            try {
                Runtime r = Runtime.getRuntime();
                Process p = r.exec(pingCmd);

                BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    System.out.println(inputLine);

                    if (!inputLine.contains("unreachable") && !inputLine.equals("") &&
                            inputLine.contains("Reply")) {
                        ok = true;
                        break;
                    }

                }
                in.close();

            }
            catch (IOException e) {
                System.out.println(e);
            }
        }
    }

    public ServerDto getServerInfo() {

        ServerDto serverDto = null;
        try {
            URL url = new URL("http://" + hostName + "/ServerManagement/Service1.asmx/GetServerInfo");

            URLConnection connection = url.openConnection();
            connection.setDoInput(true);

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


            wr.flush();
            BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            if (DEBUG) {

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
            wr.write(data);
            wr.write("\r\n");
            wr.write("\r\n");

            wr.flush();
            BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            if (DEBUG) {

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

    public void deployVirtualMachine(String from, String to, String vmName, String newName) {
        try {
            //TODO: remove the hardcoded vmName when multiple reference vm's can be defined
            URL url = new URL("http://" + hostName + "/ServerManagement/Service1.asmx/DeployVirtualMachine?from="
                    + from + "&to=" + to + "&vmName=" + "VM_1" + "&vmCopyName=" + newName + "");
            URLConnection connection = url.openConnection();
            connection.setDoInput(true);

            BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            if (DEBUG) {

                // Response
                String line;
                while ((line = rd.readLine()) != null) {

                    System.out.println(line);
                }
            }
            startVirtualMachine(newName);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void startVirtualMachine(String vmName) {
        try {
            URL url = new URL("http://" + hostName + "/ServerManagement/Service1.asmx/StartVirtualMachine?vmName=" + vmName);
            URLConnection connection = url.openConnection();
            connection.setDoInput(true);

            BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            if (DEBUG) {

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
            URL url = new URL("http://" + hostName + "/ServerManagement/Service1.asmx/StopVirtualMachine?vmName=" + vmName);
            URLConnection connection = url.openConnection();
            connection.setDoInput(true);

            BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            if (DEBUG) {

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
            URL url = new URL("http://" + hostName + "/ServerManagement/Service1.asmx/DeleteVirtualMachine?vmName=" + vmName);
            URLConnection connection = url.openConnection();
            connection.setDoInput(true);

            BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            if (DEBUG) {

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

    public void modifyVirtualMachine(String vmName, int memory, int processorPercentage, int cores) {
        try {
            URL url = new URL("http://" + hostName + "/ServerManagement/Service1.asmx/WakeUpServer?"
                    + "vmName=" + vmName + "&memory=" + memory + "&procSpeed=" + processorPercentage +
                    "&cores=" + cores);
            URLConnection connection = url.openConnection();
            connection.setDoInput(true);
            BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            if (DEBUG) {
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

    public void deployVirtualMachineWithCustomResources(String from, String to,
                                                        String vmName, String vmCopyName,
                                                        int memory, int processorPercentage, int nrCores) {
        try {
            //TODO: remove the hardcoded vmName when multiple reference vm's can be defined
            URL url = new URL("http://" + hostName + "/ServerManagement/Service1.asmx/DeployVirtualMachineWithModify?from="
                    + from + "&to=" + to + "&vmName=" + "VM_1" + "&vmCopyName=" + vmCopyName
                    + "&memory=" + memory + "&procSpeed=" + processorPercentage + "&nrCores=" + nrCores);
            URLConnection connection = url.openConnection();
            connection.setDoInput(true);

            BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            if (DEBUG) {
                // Response
                String line;
                while ((line = rd.readLine()) != null) {

                    System.out.println(line);
                }
            }
            startVirtualMachine(vmCopyName);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void wakeUpServer(String mac, String ipAddress, int port) {

        try {
            URL url = new URL("http://" + hostName + "/ServerManagement/Service1.asmx/WakeUpServer?"
                    + "mac=" + mac + "&ipAddress=" + ipAddress + "&port=" + port);
            URLConnection connection = url.openConnection();
            connection.setDoInput(true);
            BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            if (DEBUG) {
                // Response
                String line;
                while ((line = rd.readLine()) != null) {

                    System.out.println(line);
                }
            }
            waitUntilTargetIsAlive(ipAddress);
            //Thread.sleep(60000);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendServerToSleep() {

        try {
            URL url = new URL("http://" + hostName + "/ServerManagement/Service1.asmx/SendServerToSleep");
            URLConnection connection = url.openConnection();
            connection.setDoInput(true);

            BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            if (DEBUG) {
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
