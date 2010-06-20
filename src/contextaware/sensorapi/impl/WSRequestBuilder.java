package contextaware.sensorapi.impl;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class WSRequestBuilder {

    private String wsURL;
    private static final boolean DEBUG = false;

    public void setURL(String wsURL) {
        this.wsURL = wsURL;
    }

    public WSRequestBuilder(String wsURL) {
        this.wsURL = wsURL;
    }

    public synchronized double getSensorValue() {

        double res = -1.1;

        String xmldata =
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
                "<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                "<soap12:Body>" +
                "<GetSensorValue xmlns=\"http://tempuri.org/\" />" +
                "</soap12:Body>" +
                "</soap12:Envelope>";

        try {
            //Parse URL and create socket
            String[] uriDetails = wsURL.split("[:/]+");

            String hostname = uriDetails[1];
            int port = Integer.valueOf(uriDetails[2]);

            InetAddress addr = InetAddress.getByName(hostname);
            Socket sock = new Socket(addr, port);
            String path = uriDetails[3];
            for (int i = 4; i < uriDetails.length; i++) {
                path += "/" + uriDetails[i];
            }

            if (DEBUG) {
                System.out.println("d " + hostname + "\n" + path);
            }

            //Send header
            BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream(), "utf-8"));
            wr.write("POST /" + path + " HTTP/1.1\r\n");
            wr.write("Host: " + hostname + "\r\n");
            wr.write("Content-Type: text/xml; charset=\"utf-8\"\r\n");
            wr.write("Content-Length: " + xmldata.length() + "\r\n");
            wr.write("SOAPAction: http://tempuri.org/GetSensorValue\r\n");
            wr.write("\r\n");

            //Send data
            wr.write(xmldata);// System.out.println(xmldata);
            wr.flush();
            BufferedReader rd = new BufferedReader(new InputStreamReader(sock.getInputStream()));

            // Response
            String line;
            while ((line = rd.readLine()) != null) {
                if (DEBUG) {
                    System.out.println(line);
                }
                if (line.length() > 0 && line.charAt(0) == '<' && line.charAt(1)!='?') {
                    //this respose line contains the XML Soap response
                    try {
                        XMLReader reader = XMLReaderFactory.createXMLReader();
                        SensorValueXMLHandler handler = new SensorValueXMLHandler();
                        reader.setContentHandler(handler);
                        ByteArrayInputStream in = new ByteArrayInputStream(line.getBytes());
                        InputSource source = new InputSource(in);
                        reader.parse(source);
                        if (DEBUG) {
                            System.out.println("Handler ret " + handler.ret);
                        }
                        res = Double.valueOf(handler.ret).doubleValue();

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
            sock.close();
            return res;

        } catch (IOException ex) {
            ex.printStackTrace();
            return -1.0;
        }

    }
}

