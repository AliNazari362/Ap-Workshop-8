import java.io.*;

public class Database {

    private static final String FILE_NAME = "notes.ser";

    public static void save(NoteController noteController) throws IOException {
        FileOutputStream fos = new FileOutputStream(FILE_NAME, false);
        ObjectOutputStream objOs = new ObjectOutputStream(fos);
        objOs.writeObject(noteController);
        System.out.println("saved.");
        objOs.close();
        fos.close();
    }

    public static NoteController load() throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(FILE_NAME);
        ObjectInputStream objIs = new ObjectInputStream(fis);
        NoteController noteController = (NoteController) objIs.readObject();
        System.out.println("loaded.");
        objIs.close();
        fis.close();
        return noteController;
    }

    public static void exportNote(Note note) throws IOException {
        String root = "./exports/" + note.getTitle() + ".txt";
        File file = new File(root);
        FileWriter writer = new FileWriter(file);
        writer.write(note.toString());
        System.out.println("exported at " + root + ".");
        writer.close();
    }
}
