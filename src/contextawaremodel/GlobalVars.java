package contextawaremodel;

import java.util.HashMap;
import java.util.Map;

public class GlobalVars {

    public static final String x3dScene = "x3d/datacenter.x3d";
    public static final String XML_ELEMENT = "int";
    public static final String CONTEXT_INSTANCE_MODIFIED = "Context instance modified";
    public static final String baseDataCenter = "http://www.owl-ontologies.com/Ontology1230214892.owl";
    public static final String base = "http://www.owl-ontologies.com/Ontology1230214892.owl";
    public static final String CIAGENT_NAME = "CIAgent";
    public static final String CMAGENT_NAME = "CMAAgent";
    public static final String RPAGENT_NAME = "RPAgent";
    public static final String EMAGENT_NAME = "EMAgent";
    public static final String GUIAGENT_NAME = "GUIAgent";
    public static final String RLAGENT_NAME = "ReinforcementLearningAgent";
    public static final String X3DAGENT_NAME = "X3DAgent";
    public static final String TMAGENT_NAME = "TaskManagementAgent";
    public static final String PHYSICAL_RESOURCE_VALUE_NAME = "valueOfService";
    public static final String PHYSICAL_RESOURCE_NAME_NAME = "nameOfService";
    public static final String AGENT_REQUEST = "request";
    public static final String WORLD_FILE = "ontology/worldFile.xml";
    public static final String WORLD_FILE_SCHEMA = "ontology/worldFile.xsd";
    public static final String ONTOLOGY_CLASS_NAME = "ontology-class-name";
    public static final String ONTOLOGY_INDIVIDUAL_NAME = "ontology-individual-name";
    public static final String X3D_SCENE_FILE = "x3d/datacenter.x3d";
    public static final String DATA_NAME = "data-name";
    public static final String DATA_VALUE = "data-value";
    public static final String CONTEXT_ELEMENT = "context-element";
    public static final String ONTOLOGY_ENVIROMENT_FILE = "ontology/contextOntology.owl";
    public static final String ONTOLOGY_DATACENTER_FILE = "ontology/Datacenter.owl";
    public static final String POLICIES_FILE = "ontology/TestPolicies.xml";
    public static final String MEMORY_FILE = "memory/memorySelfHealing.dat";
    public static final String MEMORY_SELFOPTIMIZING_FILE = "memory/memorySelfOptimizing.dat";
    public static final String FUZZY_LOGIC_CONTROL_FILE = "fuzzy/negotiator.fcl";

    public static final int MAX_NAME_LENGTH = 10;
    public static final int MAX_TASK_LIFE_IN_MINUTES = 5;

    //public static final String MEMORY_DATACENTER_FILE = "memory/memory.dat";
    public static final int INDIVIDUAL_DELETED = 0;
    public static final int INDIVIDUAL_CREATED = 1;
    public static final int INDIVIDUAL_MODIFIED = 2;
    public static final Map<String, String> brokenResources = new HashMap<String, String>();
    //public static final Map<String, String> validResources = new HashMap<String, String>();
    public static Map<String, Map<String, String>> valueMapping = new HashMap<String, Map<String, String>>();
    private static String x3DPlatformName = "acasa-25f3f1aa5:1099/JADE";
    private static String x3DPlatformAddress = "http://acasa-25f3f1aa5:7778/acc";

    //ok because the x3DPlatformAddress will be  changed only once per run
    public static String getX3DPlatformAddress() {
        return x3DPlatformAddress;
    }

    public synchronized static void setX3DPlatformAddress(String x3DPlatformAddress) {
        GlobalVars.x3DPlatformAddress = x3DPlatformAddress;
    }

    public static String getX3DPlatformName() {
        return x3DPlatformName;
    }

    public synchronized static void setX3DPlatformName(String x3DPlatformName) {
        GlobalVars.x3DPlatformName = x3DPlatformName;
    }
}
