package redfoxclassic.hehe.model;

public class NoteModel {

    private String title;
    private String content;
    private int id;
    private String date;
//
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return " Title : " + getTitle() + " , " + " Content : " + getContent() + " , _id :" + String.valueOf(id) +
                " , date : " + getDate() + "\n";
    }
}
