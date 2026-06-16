import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

public class NoteController implements Serializable {

    private static NoteController noteController;
    private int idCounter;
    private final HashMap<String, Note> notes;

    public NoteController() {
        idCounter = 0;
        notes = new HashMap<>();
    }

    public static NoteController getInstance() {
        try {
            noteController = Database.load();
            if (noteController == null) noteController = new NoteController();
        } catch (Exception e) {
            noteController = new NoteController();
        }
        return noteController;
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (true) commandProcessor(scanner);
    }

    private void commandProcessor(Scanner scanner) {
        sayInfo();
        String command = scanner.nextLine().trim().toLowerCase();
        switch (command) {
            case "add":
            case "1": {
                try {
                    addNote(scanner);
                } catch (InvalidInputException | IOException e) {
                    System.out.println("ERROR: " + e.getMessage());
                }
                break;
            }
            case "remove":
            case "2": {
                try {
                    removeNote(scanner);
                } catch (IOException | NoteNotFoundException e) {
                    System.out.println("ERROR: " + e.getMessage());
                }
                break;
            }
            case "notes":
            case "3": {
                try {
                    showNotes(scanner);
                } catch (NoteNotFoundException e) {
                    System.out.println("ERROR: " + e.getMessage());
                }
                break;
            }
            case "export":
            case "4": {
                try {
                    exportNote(scanner);
                } catch (IOException | NoteNotFoundException e) {
                    System.out.println("ERROR: " + e.getMessage());
                }
                break;
            }
            case "exit":
            case "5":
                System.exit(0);
            default:
                System.out.println("input is invalid.");
        }
    }

    private void sayInfo() {
        System.out.println("1- Add");
        System.out.println("2- Remove");
        System.out.println("3- Notes");
        System.out.println("4- Export");
        System.out.println("5- Exit");
    }

    private void addNote(Scanner scanner) throws InvalidInputException, IOException {
        System.out.println("please write the title:");
        String title = scanner.nextLine().trim();

        if (title.isEmpty())
            throw new InvalidInputException("title is empty");

        if (notes.containsKey(title.toLowerCase()))
            throw new InvalidInputException("title is exist.");

        System.out.println("please write the note: (end with # in new line)");
        String text = parseText(scanner);

        if (text.isEmpty())
            throw new InvalidInputException("text is empty.");

        notes.put(title.toLowerCase(), new Note(title, text, ++idCounter));
        Database.save(this);
    }

    private String parseText(Scanner scanner) {
        StringBuilder text = new StringBuilder();
        String line;
        while (true) {
            line = scanner.nextLine();
            if (line.trim().equals("#")) {
                break;
            }
            text.append(line).append(System.lineSeparator());
        }
        return text.toString().trim();
    }

    private void showNotes(Scanner scanner) throws NoteNotFoundException {
        showList();
        String id = scanner.nextLine().trim();
        Note note = getNoteByIdOrTitle(id);
        System.out.println(note);
    }

    private void showList() throws NoteNotFoundException {
        if (notes.isEmpty()) {
            System.out.println("no notes.");
            throw new NoteNotFoundException("not found.");
        }
        System.out.println("please choose a note.");
        for (Note note : notes.values().stream().toList().reversed()) {
            System.out.printf("%d- %-5.20s\t%s\n",
                    note.getId(), note.getTitle(), note.getCreatedDateTime());
        }
    }

    private Note findNoteById(int id) throws NoteNotFoundException {
        for (Note n : notes.values()) {
            if (n.getId() == id) return n;
        }
        throw new NoteNotFoundException("id not found.");
    }

    private Note getNoteByIdOrTitle(String input) throws NoteNotFoundException {
        try {
            int numId = Integer.parseInt(input);
            return findNoteById(numId);
        } catch (NumberFormatException e) {
            Note note = notes.get(input.toLowerCase());
            if (note == null)
                throw new NoteNotFoundException("note title not found.");
            return note;
        }
    }

    private void removeNote(Scanner scanner) throws IOException, NoteNotFoundException {
        showList();
        String id = scanner.nextLine().trim();
        Note noteToRemove = getNoteByIdOrTitle(id);
        notes.remove(noteToRemove.getTitle().toLowerCase());
        Database.save(this);
    }

    private void exportNote(Scanner scanner) throws IOException, NoteNotFoundException {
        showList();
        String id = scanner.nextLine().trim();
        Note note = getNoteByIdOrTitle(id);
        Database.exportNote(note);
    }
}

class InvalidInputException extends Exception {
    public InvalidInputException(String msg) {
        super(msg);
    }
}

class NoteNotFoundException extends Exception {
    public NoteNotFoundException(String msg) {
        super(msg);
    }
}