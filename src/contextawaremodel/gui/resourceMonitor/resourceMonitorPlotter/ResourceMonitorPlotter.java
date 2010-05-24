package contextawaremodel.gui.resourceMonitor.resourceMonitorPlotter;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: May 16, 2010
 * Time: 12:34:17 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class ResourceMonitorPlotter {
    protected String resourceName;

    protected int snapshotCount = 0;
    protected int minTimeRange = 0;
    protected int maxTimeRange = 60;
    protected int snapshotIncrement = 1;

    protected static final Font LABEL_FONT = new Font("sherif", Font.BOLD, 15);

    protected JPanel graphPanel;

    protected ResourceMonitorPlotter(String resourceName) {
        this.resourceName = resourceName;
        graphPanel = new JPanel();
    }


    public abstract void setCurrentValue(Object currentValue);

    protected abstract void setup(int minimumValue, int maximumValue);

    public int getMinTimeRange() {
        return minTimeRange;
    }

    public void setMinTimeRange(int minTimeRange) {
        this.minTimeRange = minTimeRange;
    }

    public int getMaxTimeRange() {
        return maxTimeRange;
    }

    public void setMaxTimeRange(int maxTimeRange) {
        this.maxTimeRange = maxTimeRange;
    }

    public JPanel getGraphPanel() {
        return graphPanel;
    }

    public int getSnapshotIncrement() {
        return snapshotIncrement;
    }

    public void setSnapshotIncrement(int snapshotIncrement) {
        this.snapshotIncrement = snapshotIncrement;
    }

    public void setDisplayTimeRange(int minim, int max) {
        minTimeRange = minim;
        maxTimeRange = max;
    }

}
