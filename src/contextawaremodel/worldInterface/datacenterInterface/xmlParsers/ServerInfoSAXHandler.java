package contextawaremodel.worldInterface.datacenterInterface.xmlParsers;

import contextawaremodel.GlobalVars;
import contextawaremodel.worldInterface.dtos.ServerDto;
import contextawaremodel.worldInterface.dtos.StorageDto;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: May 8, 2010
 * Time: 12:32:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class ServerInfoSAXHandler extends DefaultHandler {
    /*
   <ServerInfo xmlns="http://www.SelfOptimizingDatacenter.edu/">
<TotalCPU>int</TotalCPU>
<CoreCount>int</CoreCount>
<FreeCPU>
<int>int</int>
<int>int</int>
</FreeCPU>
<Storage>
<Storage>
<Name>string</Name>
<Size>int</Size>
<FreeSpace>int</FreeSpace>
</Storage>
<Storage>
<Name>string</Name>
<Size>int</Size>
<FreeSpace>int</FreeSpace>
</Storage>
</Storage>
<TotalMemory>int</TotalMemory>
<FreeMemory>int</FreeMemory>
</ServerInfo>
    */
    // private static final String  SERVER_INFO = "ServerInfo";
    private static final String TOTAL_CPU = "TotalCPU";
    private static final String CORE_COUNT = "CoreCount";

    private static final String FREE_CPU = "FreeCPU";
    private static final String FREE_CPU_VAL = "int";

    private static final String STORAGE = "Storage";
    private static final String STORAGE_NAME = "Name";
    private static final String STORAGE_SIZE = "Size";
    private static final String STORAGE_FREE_SPACE = "FreeSpace";

    private static final String TOTAL_MEMORY = "TotalMemory";
    private static final String FREE_MEMORY = "FreeMemory";

    private boolean inTotalCPU = false;
    private boolean inCoreCount = false;
    private boolean inFreeCPU = false;
    private boolean inFreeCPUVal = false;

    private boolean inStorage = false;
    private boolean inStorageName = false;
    private boolean inStorageSize = false;
    private boolean inStorageFreeSpace = false;

    private boolean inTotalMemory = false;
    private boolean inFreeMemory = false;
    private boolean inStorageList = false;

    private String text;

    private final NumberFormat numberFormat = NumberFormat.getIntegerInstance();

    ServerDto serverDto;
    List<Integer> freeCPUValues;
    List<StorageDto> storageInfo;
    StorageDto currentStorage;

    public ServerInfoSAXHandler() {
        serverDto = new ServerDto();
        freeCPUValues = new ArrayList<Integer>();
        storageInfo = new ArrayList<StorageDto>();
        text = "";
    }

    public ServerDto getServerDto() {
        return serverDto;
    }

    @Override
    public void startElement(String uri, String localName, String qName, org.xml.sax.Attributes attributes) throws SAXException {

        if (localName.equals(TOTAL_CPU)) {
            inTotalCPU = true;
        } else if (localName.equals(CORE_COUNT)) {
            inCoreCount = true;
        } else if (localName.equals(FREE_CPU)) {
            inFreeCPU = true;
        } else if (localName.equals(STORAGE)) {
            inStorage = true;
            if (!inStorageList) {
                inStorageList = true;
                currentStorage = new StorageDto();
            }
        } else if (localName.equals(TOTAL_MEMORY)) {
            inTotalMemory = true;
        } else if (localName.equals(FREE_MEMORY)) {
            inFreeMemory = true;
        } else if (localName.equals(FREE_CPU_VAL)) {
            inFreeCPUVal = true;
        } else if (localName.equals(STORAGE_NAME)) {
            inStorageName = true;
        } else if (localName.equals(STORAGE_SIZE)) {
            inStorageSize = true;
        } else if (localName.equals(STORAGE_FREE_SPACE)) {
            inStorageFreeSpace = true;
        }
    }

    public void endElement(String namespaceURI, String localName, String qualifiedName) throws SAXException {

        if (localName.equals(TOTAL_CPU)) {
            try {
                serverDto.setTotalCPU(numberFormat.parse(text).intValue());
            } catch (ParseException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            inTotalCPU = false;
        } else if (localName.equals(CORE_COUNT)) {
            try {
                serverDto.setCoreCount(numberFormat.parse(text).intValue());
            } catch (ParseException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            inCoreCount = false;
        } else if (localName.equals(FREE_CPU)) {
            inFreeCPU = false;
        } else if (localName.equals(FREE_CPU_VAL)) {
            try {
                freeCPUValues.add(numberFormat.parse(text).intValue());
            } catch (ParseException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            inFreeCPUVal = false;
        } else if (localName.equals(STORAGE)) {
            inStorage = false;
        } else if (localName.equals(TOTAL_MEMORY)) {
            try {
                serverDto.setTotalMemory(numberFormat.parse(text).intValue());
            } catch (ParseException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            inTotalMemory = false;
        } else if (localName.equals(FREE_MEMORY)) {
            try {
                serverDto.setFreeMemory(numberFormat.parse(text).intValue());
            } catch (ParseException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            inFreeMemory = false;
        } else if (localName.equals(STORAGE_NAME)) {
            inStorageName = false;
            currentStorage.setName(text);
        } else if (localName.equals(STORAGE_SIZE)) {

            try {
                currentStorage.setSize(numberFormat.parse(text).intValue());
            } catch (ParseException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            inStorageSize = false;
        } else if (localName.equals(STORAGE_FREE_SPACE)) {
            try {
                currentStorage.setFreeSpace(numberFormat.parse(text).intValue());
            } catch (ParseException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            inStorageFreeSpace = false;
        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException {

        if (inTotalCPU || inCoreCount
                || inFreeCPUVal || inTotalMemory || inFreeMemory
                || inStorageName || inStorageFreeSpace || inStorageSize) {
            text = new String(ch, start, length);
        }
    }
}
