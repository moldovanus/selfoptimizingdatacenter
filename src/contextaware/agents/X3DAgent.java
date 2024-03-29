/*
 * Agent used for creating 3D context representations
 */
package contextaware.agents;

import contextaware.GlobalVars;
import contextaware.agents.behaviours.BasicX3DBehaviour;
import jade.core.Agent;
import org.web3d.x3d.sai.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class X3DAgent extends Agent {

    public static final String ADD_TASK_COMMAND = "addTask";
    public static final String REMOVE_TASK_COMMAND = "removeTask";
    public static final String MOVE_TASK_COMMAND = "moveTask";
    public static final String SEND_SERVER_TO_LOW_POWER_COMMAND = "sendToLowPower";
    public static final String WAKE_UP_SERVER_COMMAND = "wakeUpServer";
    public static final String SET_TEMPERATURE_COMMAND = "setTemperature";
    public static final String SET_HUMIDITY_COMMAND = "setHumidity";

    public static final float POWER_METER_LABEL_TRANSLATION = 0.7f;
    public static final float SENSOR_LABEL_TRANSLATION = 0.4f;

    public static final float[] SENSOR_LABEL_COLOR = new float[]{0.5f, 0, 0};
    public static final float[] TASK_LABEL_COLOR = new float[]{0, 1, 0};
    public static final float[] POWER_LABEL_COLOR = new float[]{0, 0, 0.6f};
    public static final float[] ACTIVE_WIRE_COLOR = new float[]{0, 0.6f, 0};
    public static final float[] ACTIVE_SERVER_COLOR = new float[]{0.3451f, 0.7804f, 0.8824f};
    public static final float[] INACTIVE_SERVER_COLOR = new float[]{0.5f, 0.5f, 0.5f};

    private X3DAgent selfReference;

    private JFrame frame;

    private X3DScene mainScene;
    private ArrayList<X3DNode> taskLabels;
    private Map<String, X3DNode> tasks;
    private Map<String, MFString> objectLabels;
    private ArrayList<String> wiresIndexes;

    private Timer changeWiresBackTimer;
    private Timer fanAnimationTimer;
    private Timer sensorAnimationTimer;
    private float fanIncrement = 0.1f;
    private ExternalBrowser x3dBrowser;
    private int activeServersNo = 6;

    private X3DNode attentionArow;
    private X3DNode inverseAttentionArow;
    private boolean frameClosed = true;

    private ActionListener sensorsAnimationListener = new ActionListener() {

        public void actionPerformed(ActionEvent e) {
            X3DNode sensor_1 = mainScene.getNamedNode("SensorTube_01_XFORM");
            X3DNode sensor_2 = mainScene.getNamedNode("SensorTube_02_XFORM");
            SFRotation rotation_1 = (SFRotation) sensor_1.getField("rotation");
            SFRotation rotation_2 = (SFRotation) sensor_2.getField("rotation");
            float[] values = new float[4];
            rotation_1.getValue(values);
            values[3] += 0.1f;
            rotation_1.setValue(values);
            float temp = values[3] * -1;
            rotation_2.getValue(values);
            values[3] = temp;
            rotation_2.setValue(values);
        }
    };


    private ActionListener fanAnimationListener = new ActionListener() {

        public void actionPerformed(ActionEvent e) {
            X3DNode fan = mainScene.getNamedNode("Fan_XFORM");
            SFRotation rotation = (SFRotation) fan.getField("rotation");
            float[] values = new float[4];
            rotation.getValue(values);
            values[3] += fanIncrement;
            rotation.setValue(values);
        }
    };

    public void setFanSpeed(float speed) {
        fanIncrement = speed;
    }


    @Override
    protected void takeDown() {
        frame.setVisible(false);
        frame.dispose();
        super.takeDown();
    }

    /**
     * Method called when the agent is created.
     */
    @Override
    protected void setup() {
        selfReference = this;
        frameClosed = false;

        System.out.println("[X3DAgent] : Hellooo ! ");
        frame = new JFrame("X3D vizualization");
        frame.addWindowListener(new WindowListener() {

            public void windowOpened(WindowEvent e) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            public void windowClosing(WindowEvent e) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            public void windowClosed(WindowEvent e) {
               selfReference.takeDown();
               selfReference.doDelete();
               frameClosed= true;
            }

            public void windowIconified(WindowEvent e) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            public void windowDeiconified(WindowEvent e) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            public void windowActivated(WindowEvent e) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            public void windowDeactivated(WindowEvent e) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });
        Container contentPane = frame.getContentPane();

        // Setup browser parameters
        HashMap requestedParameters = new HashMap();
        requestedParameters.put("Xj3D_ShowConsole", Boolean.FALSE);
        requestedParameters.put("Xj3D_FPSShown", Boolean.TRUE);
        requestedParameters.put("Xj3D_NavbarShown", Boolean.TRUE);

        // Create an SAI component
        X3DComponent x3dComp = BrowserFactory.createX3DComponent(requestedParameters);

        taskLabels = new ArrayList<X3DNode>();
        objectLabels = new HashMap<String, MFString>();
        tasks = new HashMap<String, X3DNode>();
        wiresIndexes = new ArrayList<String>();

        frame.setSize(500, 500);
        frame.setVisible(true);

        // Add the component to the UI
        JComponent x3dPanel = (JComponent) x3dComp.getImplementation();
        contentPane.add(x3dPanel, BorderLayout.CENTER);

        // Get an external browser
        x3dBrowser = x3dComp.getBrowser();

        boolean useXj3D = true;

        if (x3dBrowser.getName().indexOf("Xj3D") < 0) {
            System.out.println("Not running on Xj3D, extended functions disabled");
            useXj3D = false;
        }

        // Create an X3D scene by loading a file
        mainScene = x3dBrowser.createX3DFromURL(new String[]{GlobalVars.X3D_SCENE_FILE});

        if (mainScene == null) {
            System.err.println("X3D file " + GlobalVars.X3D_SCENE_FILE + " not found ");
            return;
        }

        // Replace the current world with the new one
        x3dBrowser.replaceWorld(mainScene);

        if (!useXj3D) {
            return;
        }

        attentionArow = mainScene.getNamedNode("AttentionArrow_XFORM");
        inverseAttentionArow = mainScene.getNamedNode("InverseAttentionArrow_XFORM");

        //spin the UTCN crest
        ActionListener actionListener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                X3DNode sigla = mainScene.getNamedNode("Sigla_UTCN_XFORM");
                X3DNode tube = mainScene.getNamedNode("Server_0_tube_XFORM");

                SFRotation rotation = (SFRotation) sigla.getField("rotation");
                SFRotation rotationTube = (SFRotation) tube.getField("rotation");

                float[] values = new float[4];
                rotation.getValue(values);
                values[3] += 0.1;

                rotation.setValue(values);
                rotationTube.setValue(values);
            }
        };


        Timer timer = new Timer(50, actionListener);
        timer.start();

        for (int i = 1; i <= 5; i++) {
            addObjectLabel("Watts: " + 1 * 10, "PowerMeterGroup_0" + i,
                    POWER_LABEL_COLOR, POWER_METER_LABEL_TRANSLATION);
        }


        fanAnimationTimer = new Timer(100, fanAnimationListener);

        sensorAnimationTimer = new Timer(50, sensorsAnimationListener);
        sensorAnimationTimer.start();

        addObjectLabel("Temperature : 32", "SensorSphere_01",
                SENSOR_LABEL_COLOR, SENSOR_LABEL_TRANSLATION);
        addObjectLabel("Humidity : 21", "SensorSphere_02",
                SENSOR_LABEL_COLOR, SENSOR_LABEL_TRANSLATION);

        final float[] initialWireColor = new float[]{0.8784f, 0.5608f, 0.3412f};

        ActionListener setWiresColorBack = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                for (String index : wiresIndexes) {
                    setWireColor(index, initialWireColor);
                }
            }
        };

        changeWiresBackTimer = new Timer(400, setWiresColorBack);
        changeWiresBackTimer.start();

        addBehaviour(new BasicX3DBehaviour(this));

    }

    /**
     * Sets the color of the wire associated to server serverName to
     * the specified color
     *
     * @param serverName the server the wire is connected to
     * @param color      the new color of the wire
     */
    private void setWireColor(String serverName, float[] color) {
        String[] elements = serverName.split("_");
        X3DNode material =
                mainScene.getNamedNode("Wire_0" + Integer.parseInt(elements[1]) + "_MAT");
        SFColor diffuseColor = (SFColor) material.getField("diffuseColor");
        diffuseColor.setValue(color);
    }

    /**
     * Adds a text label to the other side of the object. The side initially not visible
     * when the scene is loaded.
     *
     * @param textLabel   the label of the object
     * @param objectName  the name of the X3D node to which the label is applied
     * @param color       the color of the label
     * @param translation the translation of the text relative to the object
     */
    private void addInverseObjectLabel(String textLabel, String objectName,
                                       float[] color, float translation) {

        if (!objectLabels.containsKey(objectName + "_Inverse")) {

            X3DNode transform = mainScene.createNode("Transform");

            X3DNode shape = mainScene.createNode("Shape");
            X3DNode label = mainScene.createNode("Text");
            X3DNode appearance = mainScene.createNode("Appearance");
            X3DNode material = mainScene.createNode("Material");
            X3DNode fontStyle = mainScene.createNode("FontStyle");

            MFString justify = (MFString) fontStyle.getField("justify");
            SFFloat size = (SFFloat) fontStyle.getField("size");

            justify.set1Value(0, "MIDDLE");
            justify.set1Value(1, "MIDDLE");
            size.setValue(0.15f);
            SFNode fontStyleAttribute = (SFNode) label.getField("fontStyle");
            fontStyleAttribute.setValue(fontStyle);

            SFColor diffuseColor = (SFColor) material.getField("diffuseColor");
            SFFloat ambientIntensity = (SFFloat) material.getField("ambientIntensity");

            diffuseColor.setValue(color);
            ambientIntensity.setValue(1);

            SFNode appearanceMaterial = (SFNode) appearance.getField("material");
            appearanceMaterial.setValue(material);

            SFNode shapeAppearance = (SFNode) shape.getField("appearance");
            shapeAppearance.setValue(appearance);

            MFString string = (MFString) label.getField("string");
            string.clear();
            string.insertValue(0, textLabel);

            SFNode shapeGeometry = (SFNode) shape.getField("geometry");
            shapeGeometry.setValue(label);

            MFNode newTransformChildren = (MFNode) transform.getField("children");
            newTransformChildren.append(shape);

            SFVec3f serverTranslation = (SFVec3f) transform.getField("translation");
            float[] translationValues = new float[3];

            X3DNode powerMeter = mainScene.getNamedNode(objectName + "_XFORM");
            SFVec3f powerMeterTranslation = (SFVec3f) powerMeter.getField("translation");
            powerMeterTranslation.getValue(translationValues);
            translationValues[1] += 0.2;
            translationValues[0] -= translation;

            serverTranslation.setValue(translationValues);

            SFRotation serverRotation = (SFRotation) transform.getField("rotation");
            float[] rotationValues = new float[]{0, 1, 0, 0f};
            serverRotation.setValue(rotationValues);
            mainScene.addRootNode(transform);
            objectLabels.put(objectName + "_Inverse", string);
        } else {
            MFString string = objectLabels.get(objectName + "_Inverse");
            //string.clear();
            if (string.size() == 0) {
                string.insertValue(0, textLabel);
            } else {
                string.set1Value(0, textLabel);
            }
        }
    }

    /**
     * Adds a text label to the visible side of the object. The side initially visible
     * when the scene is loaded.
     *
     * @param textLabel   the label of the object
     * @param objectName  the name of the X3D node to which the label is applied
     * @param color       the color of the label
     * @param translation the translation of the text relative to the object
     */
    public void addObjectLabel(String textLabel, String objectName,
                               float[] color, float translation) {

        if (!objectLabels.containsKey(objectName)) {
            X3DNode transform = mainScene.createNode("Transform");
            X3DNode shape = mainScene.createNode("Shape");
            X3DNode label = mainScene.createNode("Text");
            X3DNode appearance = mainScene.createNode("Appearance");
            X3DNode material = mainScene.createNode("Material");
            X3DNode fontStyle = mainScene.createNode("FontStyle");

            MFString justify = (MFString) fontStyle.getField("justify");
            SFFloat size = (SFFloat) fontStyle.getField("size");

            justify.set1Value(0, "MIDDLE");
            justify.set1Value(1, "MIDDLE");
            size.setValue(0.15f);
            SFNode fontStyleAttribute = (SFNode) label.getField("fontStyle");
            fontStyleAttribute.setValue(fontStyle);

            SFColor diffuseColor = (SFColor) material.getField("diffuseColor");
            SFFloat ambientIntensity = (SFFloat) material.getField("ambientIntensity");

            diffuseColor.setValue(color);
            ambientIntensity.setValue(1);

            SFNode appearanceMaterial = (SFNode) appearance.getField("material");
            appearanceMaterial.setValue(material);

            SFNode shapeAppearance = (SFNode) shape.getField("appearance");
            shapeAppearance.setValue(appearance);

            MFString string = (MFString) label.getField("string");
            string.clear();
            string.insertValue(0, textLabel);

            SFNode shapeGeometry = (SFNode) shape.getField("geometry");
            shapeGeometry.setValue(label);

            MFNode newTransformChildren = (MFNode) transform.getField("children");
            newTransformChildren.append(shape);

            SFVec3f serverTranslation = (SFVec3f) transform.getField("translation");
            float[] translationValues = new float[3];

            X3DNode powerMeter = mainScene.getNamedNode(objectName + "_XFORM");
            SFVec3f powerMeterTranslation = (SFVec3f) powerMeter.getField("translation");
            powerMeterTranslation.getValue(translationValues);
            translationValues[1] += 0.2;
            translationValues[0] += translation;

            serverTranslation.setValue(translationValues);

            SFRotation serverRotation = (SFRotation) transform.getField("rotation");
            float[] rotationValues = new float[]{0, 1, 0, 3f};
            serverRotation.setValue(rotationValues);

            mainScene.addRootNode(transform);
            objectLabels.put(objectName, string);
            addInverseObjectLabel(textLabel, objectName, color, translation);
        } else {
            MFString string = objectLabels.get(objectName);
            if (string.size() == 0) {
                string.insertValue(0, textLabel);
            } else {
                string.set1Value(0, textLabel);
            }

            addInverseObjectLabel(textLabel, objectName, color, translation);
        }
    }

    /**
     * @param taskName      the name of the task to be used as label
     * @param taskTransform the transform of the task. Each task is placed above the
     *                      previous task so it is necesary to know the transform off the target task
     * @param color         the color of the label
     */
    private void addLabelToTask(String taskName, X3DNode taskTransform, float[] color) {

        X3DNode transform = mainScene.createNode("Transform");
        X3DNode shape = mainScene.createNode("Shape");
        X3DNode label = mainScene.createNode("Text");
        X3DNode appearance = mainScene.createNode("Appearance");
        X3DNode material = mainScene.createNode("Material");
        X3DNode fontStyle = mainScene.createNode("FontStyle");

        MFString justify = (MFString) fontStyle.getField("justify");
        SFFloat size = (SFFloat) fontStyle.getField("size");

        justify.set1Value(0, "MIDDLE");
        justify.set1Value(1, "MIDDLE");
        size.setValue(0.15f);
        SFNode fontStyleAttribute = (SFNode) label.getField("fontStyle");
        fontStyleAttribute.setValue(fontStyle);

        SFColor diffuseColor = (SFColor) material.getField("diffuseColor");
        SFFloat ambientIntensity = (SFFloat) material.getField("ambientIntensity");

        diffuseColor.setValue(color);
        ambientIntensity.setValue(1);

        SFNode appearanceMaterial = (SFNode) appearance.getField("material");
        appearanceMaterial.setValue(material);

        SFNode shapeAppearance = (SFNode) shape.getField("appearance");
        shapeAppearance.setValue(appearance);

        MFString string = (MFString) label.getField("string");
        string.clear();
        string.insertValue(0, taskName);

        SFNode shapeGeometry = (SFNode) shape.getField("geometry");
        shapeGeometry.setValue(label);

        MFNode newTransformChildren = (MFNode) transform.getField("children");
        newTransformChildren.append(shape);


        SFRotation serverRotation = (SFRotation) transform.getField("rotation");
        float[] rotationValues = new float[]{0, 1, 0, 3};
        serverRotation.setValue(rotationValues);

        SFVec3f serverTranslation = (SFVec3f) transform.getField("translation");
        float[] translationValues = new float[3];

        translationValues[1] += 0.1;
        translationValues[0] += 0.15;
        translationValues[2] -= 0.15;
        serverTranslation.setValue(translationValues);

        MFNode taskTransformChildren = (MFNode) taskTransform.getField("children");
        taskTransformChildren.append(transform);
        mainScene.addRootNode(taskTransform);
        taskLabels.add(taskTransform);
    }

    /**
     * Creates a new task representation and adds it to the 3D scene
     *
     * @param taskName   the name of the new task to be used as label
     * @param serverName
     * @param taskNumber
     */
    public void addTask(String taskName, String serverName, int taskNumber) {

        wiresIndexes.add(serverName);
        setWireColor(serverName, ACTIVE_WIRE_COLOR);
        changeWiresBackTimer.restart();

        X3DNode newShape = mainScene.createNode("Shape");
        X3DNode newBox = mainScene.createNode("Box");
        X3DNode newTransform = mainScene.createNode("Transform");
        X3DNode appearance = mainScene.createNode("Appearance");
        X3DNode material = mainScene.createNode("Material");

        SFNode appearanceField = (SFNode) newShape.getField("appearance");
        SFVec3f boxSize = (SFVec3f) newBox.getField("size");
        boxSize.setValue(new float[]{0.2699f, 0.08436f, 0.2801f});

        SFColor diffuseColor = (SFColor) material.getField("diffuseColor");
        SFFloat ambientIntensity = (SFFloat) material.getField("ambientIntensity");

        diffuseColor.setValue(new float[]{0.5529f, 0.02745f, 0.2275f});
        ambientIntensity.setValue(1);

        SFNode appearanceMaterial = (SFNode) appearance.getField("material");
        appearanceMaterial.setValue(material);
        appearanceField.setValue(appearance);

        SFNode shape_geometry = (SFNode) (newShape.getField("geometry"));
        shape_geometry.setValue(newBox);
        MFNode newTransformChildren = (MFNode) newTransform.getField("children");
        newTransformChildren.append(newShape);

        SFVec3f translation = (SFVec3f) newTransform.getField("translation");

        X3DNode serverTransform = mainScene.getNamedNode(serverName + "_XFORM");
        SFVec3f serverTranslation = (SFVec3f) serverTransform.getField("translation");
        float[] translationValues = new float[3];

        serverTranslation.getValue(translationValues);
        float heightIndex = -0.2f;
        heightIndex += 0.1f * (taskNumber - 1);

        translationValues[1] += heightIndex;
        translation.setValue(translationValues);

        SFRotation newTransformRotation = (SFRotation) newTransform.getField("rotation");
        SFRotation serverRotation = (SFRotation) serverTransform.getField("rotation");
        float[] rotationValues = new float[]{0, 1, 0, 3};
        serverRotation.getValue(rotationValues);
        newTransformRotation.getValue(rotationValues);

        addLabelToTask(taskName, newTransform, TASK_LABEL_COLOR);
        tasks.put(taskName, newTransform);

        //place attention arrow
        String[] elements = serverName.split("_");
        X3DNode planeTransform = mainScene.getNamedNode("Server_" + elements[1] + "_XFORM");
        addAttentionArrow(planeTransform);
    }

    /**
     * Removes the representation for task taskName from the scene
     *
     * @param taskName the name of the task to be removed from the scene
     */
    public void removeTask(String taskName) {
        X3DNode task = tasks.remove(taskName);
        addInverseAttentionArrow(task);
        mainScene.removeRootNode(task);
    }

    /**
     * Places an attention arow above the server and changes the color of the server
     * platform to gray
     *
     * @param serverName the name of the target server
     */
    public void sendServerToLowPower(String serverName) {
        String[] elements = serverName.split("_");
        X3DNode material = mainScene.getNamedNode("ServerPlane_0" + elements[1] + "_MAT");
        SFColor diffuseColor = (SFColor) material.getField("diffuseColor");
        diffuseColor.setValue(INACTIVE_SERVER_COLOR);
        activeServersNo--;
        fanAnimationTimer.setDelay(100 / activeServersNo);
        fanAnimationTimer.restart();

        //place attention arrow
        X3DNode planeTransform = mainScene.getNamedNode("Server_" + elements[1] + "_XFORM");
        addAttentionArrow(planeTransform);

    }

    /**
     * Places an attention arow above the server and changes the color of the server
     * platform to blue
     *
     * @param serverName the name of the target server
     */
    public void wakeUpServer(String serverName) {
        String[] elements = serverName.split("_");
        X3DNode material = mainScene.getNamedNode("ServerPlane_0" + elements[1] + "_MAT");
        SFColor diffuseColor = (SFColor) material.getField("diffuseColor");
        diffuseColor.setValue(ACTIVE_SERVER_COLOR);
        activeServersNo++;
        fanAnimationTimer.setDelay(100 / activeServersNo);
        fanAnimationTimer.restart();

        //place attention arrow
        X3DNode planeTransform = mainScene.getNamedNode("Server_" + elements[1] + "_XFORM");
        addAttentionArrow(planeTransform);
    }

    /**
     * Adds an attention arow pointing down for a certain time above the specified node
     *
     * @param targetNode the node above which an attention arrow will appear
     */
    private void addAttentionArrow(X3DNode targetNode) {
        //place attention arrow
        SFRotation planeRotation = (SFRotation) targetNode.getField("rotation");
        SFRotation arrowRotation = (SFRotation) attentionArow.getField("rotation");
        SFVec3f planeTranslation = (SFVec3f) targetNode.getField("translation");
        SFVec3f arrowTranslation = (SFVec3f) attentionArow.getField("translation");
        SFVec3f arrowScale = (SFVec3f) attentionArow.getField("scale");
        arrowScale.setValue(new float[]{1, 1, 1});
        float[] arrowTranslationValues = new float[4];
        float[] arrowRotationValues = new float[4];
        planeTranslation.getValue(arrowTranslationValues);
        planeRotation.getValue(arrowRotationValues);
        arrowTranslationValues[1] += 0.5;
        arrowRotation.setValue(arrowRotationValues);
        arrowTranslation.setValue(arrowTranslationValues);

        Timer timer = new Timer(2000, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                SFVec3f arrowScale = (SFVec3f) attentionArow.getField("scale");
                arrowScale.setValue(new float[]{0, 0, 0});
            }
        });

        timer.setRepeats(false);
        timer.start();
    }

    /**
     * Adds an attention arow pointing up for a certain time above the specified node
     *
     * @param targetNode the node above which an attention arrow will appear
     */
    private void addInverseAttentionArrow(X3DNode targetNode) {
        //place attention arrow
        SFRotation planeRotation = (SFRotation) targetNode.getField("rotation");
        SFRotation arrowRotation = (SFRotation) inverseAttentionArow.getField("rotation");
        SFVec3f planeTranslation = (SFVec3f) targetNode.getField("translation");
        SFVec3f arrowTranslation = (SFVec3f) inverseAttentionArow.getField("translation");
        SFVec3f arrowScale = (SFVec3f) inverseAttentionArow.getField("scale");
        arrowScale.setValue(new float[]{1, 1, 1});
        float[] arrowTranslationValues = new float[4];
        float[] arrowRotationValues = new float[4];
        planeTranslation.getValue(arrowTranslationValues);
        planeRotation.getValue(arrowRotationValues);
        arrowTranslationValues[1] += 1;
        arrowRotation.setValue(arrowRotationValues);
        arrowTranslation.setValue(arrowTranslationValues);

        Timer timer = new Timer(2000, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                SFVec3f arrowScale = (SFVec3f) inverseAttentionArow.getField("scale");
                arrowScale.setValue(new float[]{0, 0, 0});
            }
        });

        timer.setRepeats(false);
        timer.start();
    }

    /**
     * Rotates the circle located around of the sensor sphere representation
     *
     * @param sensorTubeName name of the sensor to be animated
     */
    public void animateSensor(String sensorTubeName) {
        X3DNode node = mainScene.getNamedNode(sensorTubeName);
        final SFRotation arrowRotation = (SFRotation) node.getField("rotation");
        final float[] values = new float[4];
        arrowRotation.getValue(values);
        values[0] = 1;
        values[1] = 0;
        arrowRotation.setValue(values);

        Timer timer = new Timer(2000, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                values[0] = 0;
                values[1] = 1;
                arrowRotation.setValue(values);
            }
        });

        timer.setRepeats(false);
        timer.start();

    }

    public boolean isFrameClosed() {
        return frameClosed;
    }
}