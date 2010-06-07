package contextawaremodel.agents.behaviours;

import contextawaremodel.agents.CIAgent;
import jade.core.behaviours.TickerBehaviour;

public class RecordingCIABehaviour extends TickerBehaviour {

    private CIAgent ag;

    // constructor takes CIAgent reference and recording time interval in miliseconds
    public RecordingCIABehaviour(CIAgent ag, int mlsRecTimeInterval) {
        super(ag, mlsRecTimeInterval);
        this.ag = ag;
    }

    @Override
    protected void onTick() {
        // Create the directory path for recording
        /*
        DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        java.util.Date date = new java.util.Date();
        String pathName = "./logs/" + dateFormat.format(date);
        new File(pathName).mkdirs();

        // Save the file to the correct path
        String fileName = pathName + "/ontology.owl";
        ArrayList errors = new ArrayList();
        ((JenaOWLModel) this.ag.getEnvironmentOwlModel()).save(new File(fileName).toURI(), FileUtils.langXMLAbbrev, errors);
        */
     //   System.out.println("[CIA] A new log was saved to \"" + pathName + "\".");
    }
}
