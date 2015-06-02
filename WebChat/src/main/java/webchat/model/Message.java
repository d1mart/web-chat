package webchat.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Message {
    private String id;
    private String user;
    private String text;
    private String date;

    public Message(String id, String name, String text, String date) {
        this.id = id;
        this.user = name;
        this.text = text;
        this.date = getNewDate();
    }

    public String getNewDate(){
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy hh:mm");
        return format.format(date);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    public String toString() {
        return "{\"id\":\"" + this.id + "\",\"user\":\"" + this.user + "\",\"text\":\"" + this.text + "\"}";
    }
}
