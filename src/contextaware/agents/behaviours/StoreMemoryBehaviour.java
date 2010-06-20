/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package contextaware.agents.behaviours;

import contextaware.GlobalVars;
import contextaware.agents.ReinforcementLearningAgent;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import utils.context.DatacenterMemory;
import utils.context.EnvironmentMemory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

/**
 * @author Me
 */
public class StoreMemoryBehaviour extends TickerBehaviour {

    private EnvironmentMemory environmentMemory;
    private DatacenterMemory datacenterMemory;


    public StoreMemoryBehaviour(Agent a, long period, EnvironmentMemory environmentMemory, DatacenterMemory datacenterMemory) {
        super(a, period);
        this.environmentMemory = environmentMemory;
        this.datacenterMemory = datacenterMemory;

    }

    @Override
    protected void onTick() {


        ObjectOutputStream selfOptimizingMemoryOutputStream = null;
        ObjectOutputStream selfHealingMemoryOutputStream = null;

        try {
            File selfHealingMemoryFile = new File(GlobalVars.MEMORY_SELFHEALING_FILE);
            File selfOptimizingmemoryFile = new File(GlobalVars.MEMORY_SELFOPTIMIZING_FILE);

            selfOptimizingMemoryOutputStream = new ObjectOutputStream(new FileOutputStream(selfOptimizingmemoryFile));
            selfHealingMemoryOutputStream = new ObjectOutputStream(new FileOutputStream(selfHealingMemoryFile));

            selfHealingMemoryOutputStream.writeObject(environmentMemory);
            selfOptimizingMemoryOutputStream.writeObject(datacenterMemory);
            
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(" Environment Memory has been saved to \"" + GlobalVars.MEMORY_SELFHEALING_FILE + "\".");
        System.out.println("Datacenter Memory has been saved to \"" + GlobalVars.MEMORY_SELFOPTIMIZING_FILE + "\".");


    }
}
