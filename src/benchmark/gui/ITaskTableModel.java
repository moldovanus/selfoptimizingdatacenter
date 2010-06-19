package benchmark.gui;

import greenContextOntology.ProtegeFactory;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Me
 * Date: Jun 18, 2010
 * Time: 4:11:27 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ITaskTableModel {
    String TASK_NAME = "Task Name";
    String TASK_REQUESTED_CORES_NO = "Requested Cores";
    String TASK_CPU_WEIGHT = "CPU Weight";
    String TASK_STORAGE_WEIGHT = "Storage Weight";
    String TASK_MEMORY_WEIGHT = "Memory Weight";
    String TASK_MIN_CPU = "Min CPU";
    String TASK_MAX_CPU = "Max CPU";
    String TASK_MIN_MEMORY = "Min Memory";
    String TASK_MAX_MEMORY = "Max Memory";
    String TASK_MIN_STORAGE = "Min Storage";
    String TASK_MAX_STORAGE = "Max Storage";

    void createTaskEntities(ProtegeFactory factory);

    public List<String[]> getTableData();

    public void setTableData(List<String[]> data);
}
