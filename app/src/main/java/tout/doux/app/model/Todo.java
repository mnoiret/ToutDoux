package tout.doux.app.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Marie on 23/05/14.
 */

public class Todo {
    public int    todo_id;
    public String title;
    public String content;
    public boolean done;

    public Todo(int _todo_id, String _title, String _content,boolean _done) {
        todo_id = _todo_id;
        title   = _title;
        content = _content;
        done = _done;
    }

    public Todo( String _title, String _content) {
        title   = _title;
        content = _content;
    }

    public int getTodo_id() {
        return todo_id;
    }

    public void setTodo_id(int todo_id) {
        this.todo_id = todo_id;
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

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

