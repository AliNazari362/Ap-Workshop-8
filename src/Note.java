import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Note implements Serializable {

    private final int id;
    private final String title;
    private final String text;
    private final LocalDateTime createdDateTime;

    public Note(String title, String text, int idCounter) {
        this.id = idCounter;
        this.title = title;
        this.text = text;
        this.createdDateTime = LocalDateTime.now();
    }

    public String getTitle() {
        return title;
    }

    public LocalDate getCreatedDateTime() {
        return createdDateTime.toLocalDate();
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "-------------------- " + title
                + " --------------------" + System.lineSeparator() + text;
    }
}
