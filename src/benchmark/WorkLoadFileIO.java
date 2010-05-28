package benchmark;

import java.io.*;

/**
 * Created by IntelliJ IDEA.
 * User: Me
 * Date: May 27, 2010
 * Time: 3:04:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class WorkLoadFileIO {
    private WorkLoadFileIO() {
    }

    public static void writeWorkLoadToFile(WorkLoadLoader generator, String filePath) throws IOException {
        ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(new File(filePath)));
        outputStream.writeObject(generator);
        outputStream.flush();
        outputStream.close();
    }

    public static WorkLoadLoader loadWorkLoadFromFile(String fileName) throws IOException, ClassNotFoundException {
        ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(new File(fileName)));
        WorkLoadLoader generator = (WorkLoadLoader) inputStream.readObject();
        inputStream.close();
        return generator;
    }
}
