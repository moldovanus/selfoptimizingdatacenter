/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package contextawaremodel.agents;

import jade.core.Agent;
import org.web3d.x3d.sai.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import contextawaremodel.GlobalVars;
import contextawaremodel.agents.behaviours.BasicX3DBehaviour;

/**
 * @author Administrator
 */

public class X3DAgent extends Agent {

    private JFrame frame;

    private X3DScene mainScene;
    private final Agent selfReference = this;
    private ArrayList<X3DNode> taskLabels;
    private Map<String, X3DNode> tasks;
    private Map<String, X3DNode> powerMeterLabels;
    private ArrayList<String> wiresIndexes;
    static int server = 1;
    private float[] activeWireColor = new float[]{0, 0.6f, 0};
    private Timer changeWiresBackTimer;

    @Override
    protected void takeDown() {
        super.takeDown();    //To change body of overridden methods use File | Settings | File Templates.
        frame.setVisible(false);
        System.exit(1);
    }

    @Override
    protected void setup() {

        Object[] args = getArguments();
        if (args == null) {
            System.out.println("[X3D] X3D Agent failed, owlModel arguments are null!");
            //return;
        }


        // this.policyConversionModel = (OntModel) args[0];
        System.out.println("[X3DAgent] : Hellooo ! ");
        frame = new JFrame("X3D vizualization");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container contentPane = frame.getContentPane();

        // Setup browser parameters
        HashMap requestedParameters = new HashMap();
        requestedParameters.put("Xj3D_ShowConsole", Boolean.FALSE);
        requestedParameters.put("Xj3D_FPSShown", Boolean.TRUE);
        requestedParameters.put("Xj3D_NavbarShown", Boolean.TRUE);

        // Create an SAI component
        X3DComponent x3dComp = BrowserFactory.createX3DComponent(requestedParameters);

        taskLabels = new ArrayList<X3DNode>();
        powerMeterLabels = new HashMap<String, X3DNode>();
        tasks = new HashMap<String, X3DNode>();
        wiresIndexes = new ArrayList<String>();

        frame.setSize(500, 500);
        frame.setVisible(true);

        // Add the component to the UI
        JComponent x3dPanel = (JComponent) x3dComp.getImplementation();
        contentPane.add(x3dPanel, BorderLayout.CENTER);

        // Get an external browser
        ExternalBrowser x3dBrowser = x3dComp.getBrowser();

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
        ActionListener actionListener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                X3DNode tube = mainScene.getNamedNode("Server_0_tube_XFORM");
                X3DNode upperTubeOne = mainScene.getNamedNode("Server_0_tube_01_XFORM");
                X3DNode upperTubeTwo = mainScene.getNamedNode("Server_0_tube_02_XFORM");

                SFRotation rotation = (SFRotation) tube.getField("rotation");
                SFRotation rotation_1 = (SFRotation) upperTubeOne.getField("rotation");
                SFRotation rotation_2 = (SFRotation) upperTubeTwo.getField("rotation");
                float[] values = new float[4];
                rotation.getValue(values);
                values[3] += 0.1;

                float[] newValues = new float[]{1, 0, 0, 0};
                rotation_1.getValue(newValues);
                newValues[3] += 0.1;
                rotation.setValue(values);
                rotation_1.setValue(newValues);
                newValues[3] *= -1;
                rotation_2.setValue(newValues);
            }
        };

        Timer timer = new Timer(50, actionListener);
        timer.start();

        for (int i = 1; i <= 5; i++) {
            addLabelToPowerMeters("" + 1 * 10, "PowerMeterGroup_0" + i);
        }


        ActionListener simulationListener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                for (X3DNode entry : taskLabels) {
                    mainScene.removeRootNode(entry);
                }

                for (Map.Entry<String, X3DNode> entry : powerMeterLabels.entrySet()) {
                    mainScene.removeRootNode(entry.getValue());
                }

                if (server == 5) {
                    server = 1;
                }

                Random random = new Random();
                int index_1 = random.nextInt(5) + 1;
                int index_2 = random.nextInt(5) + 1;
                int index_3 = random.nextInt(5) + 1;

                addTask("Task_1", "Server_" + index_1, 1);

                mainScene.removeRootNode(powerMeterLabels.get("PowerMeterGroup_0" + index_1));
                mainScene.removeRootNode(powerMeterLabels.get("PowerMeterGroup_0" + index_1 + "_Inverse"));
                addLabelToPowerMeters("10", "PowerMeterGroup_0" + index_1);
                if (index_2 == index_1) {
                    addTask("Task_2", "Server_" + index_1, 0);
                    mainScene.removeRootNode(powerMeterLabels.get("PowerMeterGroup_0" + index_1));
                    mainScene.removeRootNode(powerMeterLabels.get("PowerMeterGroup_0" + index_1 + "_Inverse"));
                    addLabelToPowerMeters("20", "PowerMeterGroup_0" + index_1);

                    if (index_3 == index_1) {
                        mainScene.removeRootNode(powerMeterLabels.get("PowerMeterGroup_0" + index_1));
                        mainScene.removeRootNode(powerMeterLabels.get("PowerMeterGroup_0" + index_1 + "_Inverse"));
                        addLabelToPowerMeters("30", "PowerMeterGroup_0" + index_1);
                        addTask("Task_3", "Server_" + index_1, 2);
                    }
                } else {
                    mainScene.removeRootNode(powerMeterLabels.get("PowerMeterGroup_0" + index_2));
                    mainScene.removeRootNode(powerMeterLabels.get("PowerMeterGroup_0" + index_2 + "_Inverse"));
                    addLabelToPowerMeters("10", "PowerMeterGroup_0" + index_2);
                    addTask("Task_2", "Server_" + index_2, 1);
                }

                if (index_3 == index_2) {
                    mainScene.removeRootNode(powerMeterLabels.get("PowerMeterGroup_0" + index_2));
                    mainScene.removeRootNode(powerMeterLabels.get("PowerMeterGroup_0" + index_2 + "_Inverse"));
                    addLabelToPowerMeters("20", "PowerMeterGroup_0" + index_2);
                    addTask("Task_3", "Server_" + index_2, 2);
                } else {
                    mainScene.removeRootNode(powerMeterLabels.get("PowerMeterGroup_0" + index_3));
                    mainScene.removeRootNode(powerMeterLabels.get("PowerMeterGroup_0" + index_3 + "_Inverse"));
                    addLabelToPowerMeters("10", "PowerMeterGroup_0" + index_3);
                    addTask("Task_3", "Server_" + index_3, 1);
                }

                for (int i = 1; i <= 5; i++) {
                    if (i == index_1 || i == index_2 || i == index_3) {
                        continue;
                    }
                    addLabelToPowerMeters("" + 0, "PowerMeterGroup_0" + i);
                }

                //x3dBrowser.replaceWorld(mainScene);

            }
        };
        Timer simulationTimer = new Timer(1000, simulationListener);

        //simulationTimer.start();
        //addLabelToPowerMeters();

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

    private void setWireColor(String serverName, float[] color) {
        String[] elements = serverName.split("_");
        final X3DNode material = mainScene.getNamedNode("Wire_0" + Integer.parseInt(elements[1]) + "_MAT");
        SFColor diffuseColor = (SFColor) material.getField("diffuseColor");
        diffuseColor.setValue(color);
    }

    private void addInverseLabelToPowerMeters(String power, String powerMeterName) {
        //for ( int i = 1; i <= 5; i++){
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

        diffuseColor.setValue(new float[]{0, 0, 1});
        ambientIntensity.setValue(1);

        SFNode appearanceMaterial = (SFNode) appearance.getField("material");
        appearanceMaterial.setValue(material);

        SFNode shapeAppearance = (SFNode) shape.getField("appearance");
        shapeAppearance.setValue(appearance);

        MFString string = (MFString) label.getField("string");
        string.clear();
        string.insertValue(0, "Watts: " + power);

        SFNode shapeGeometry = (SFNode) shape.getField("geometry");
        shapeGeometry.setValue(label);

        MFNode newTransformChildren = (MFNode) transform.getField("children");
        newTransformChildren.append(shape);

        SFVec3f serverTranslation = (SFVec3f) transform.getField("translation");
        float[] translationValues = new float[3];

        X3DNode powerMeter = mainScene.getNamedNode(powerMeterName + "_XFORM");
        SFVec3f powerMeterTranslation = (SFVec3f) powerMeter.getField("translation");
        powerMeterTranslation.getValue(translationValues);
        translationValues[1] += 0.2;
        translationValues[0] -= 0.4;

        serverTranslation.setValue(translationValues);

        SFRotation serverRotation = (SFRotation) transform.getField("rotation");
        float[] rotationValues = new float[]{0, 1, 0, 0f};
        serverRotation.setValue(rotationValues);
        mainScene.addRootNode(transform);
        powerMeterLabels.put(powerMeterName + "_Inverse", transform);
        //}
    }

    public void addLabelToPowerMeters(String power, String powerMeterName) {

        removePowerMeterLabel(powerMeterName);

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

        diffuseColor.setValue(new float[]{0, 0, 1});
        ambientIntensity.setValue(1);

        SFNode appearanceMaterial = (SFNode) appearance.getField("material");
        appearanceMaterial.setValue(material);

        SFNode shapeAppearance = (SFNode) shape.getField("appearance");
        shapeAppearance.setValue(appearance);

        MFString string = (MFString) label.getField("string");
        string.clear();
        string.insertValue(0, "Watts: " + power);

        SFNode shapeGeometry = (SFNode) shape.getField("geometry");
        shapeGeometry.setValue(label);

        MFNode newTransformChildren = (MFNode) transform.getField("children");
        newTransformChildren.append(shape);

        SFVec3f serverTranslation = (SFVec3f) transform.getField("translation");
        float[] translationValues = new float[3];

        X3DNode powerMeter = mainScene.getNamedNode(powerMeterName + "_XFORM");
        SFVec3f powerMeterTranslation = (SFVec3f) powerMeter.getField("translation");
        powerMeterTranslation.getValue(translationValues);
        translationValues[1] += 0.2;
        serverTranslation.setValue(translationValues);

        SFRotation serverRotation = (SFRotation) transform.getField("rotation");
        float[] rotationValues = new float[]{0, 1, 0, 3f};
        serverRotation.setValue(rotationValues);

        mainScene.addRootNode(transform);
        powerMeterLabels.put(powerMeterName, transform);
        addInverseLabelToPowerMeters(power, powerMeterName);

    }

    public void removePowerMeterLabel(String powerMeterName) {
        X3DNode label = powerMeterLabels.remove(powerMeterName);
        if (label == null) {
            return;
        }
        X3DNode inverseLabel = powerMeterLabels.remove(powerMeterName + "_Inverse");
        mainScene.removeRootNode(label);
        mainScene.removeRootNode(inverseLabel);
    }

    private void addLabelToTask(String taskName, X3DNode taskTransform) {

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

        diffuseColor.setValue(new float[]{0, 1, 0});
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

        //SFVec3f powerMeterTranslation = (SFVec3f) taskTransform.getField("translation");
        //powerMeterTranslation.getValue(translationValues);
        //translationValues[1] += 0.2;
        translationValues[1] += 0.1;
        translationValues[0] += 0.15;
        translationValues[2] -= 0.15;
        serverTranslation.setValue(translationValues);

        MFNode taskTransformChildren = (MFNode) taskTransform.getField("children");
        taskTransformChildren.append(transform);
        mainScene.addRootNode(taskTransform);

        taskLabels.add(taskTransform);
    }

    public void addTask(String taskName, String serverName, int taskNumber) {

        wiresIndexes.add(serverName);
        setWireColor(serverName, activeWireColor);
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

        //SFVec3f scale = (SFVec3f) newTransform.getField("scale");
        //scale.setValue(new float[]{1.0f, 1.0f, 1.0f});

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

        //runningTasks.put(taskName, newTransform);

        addLabelToTask(taskName, newTransform);
        tasks.put(taskName, newTransform);
    }

    public void removeTask(String taskName) {
        X3DNode task = tasks.remove(taskName);
        mainScene.removeRootNode(task);
    }

    public void sendServerToLowPower(String serverName) {
            //TODO : metode de server colouwinr pe state
    }

    public void wakeUpServer(String serverName) {

    }
}