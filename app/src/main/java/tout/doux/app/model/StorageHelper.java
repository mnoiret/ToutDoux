package tout.doux.app.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marie on 23/05/14.
 */

public class StorageHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "FeedReader.db";

    private static final String SQL_UP_0 =
            "CREATE TABLE Todo (todo_id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT,content TEXT,done BOOLEAN);";

    private static final String SQL_DOWN_0 = "DROP TABLE IF EXISTS Todo";

    private static final String SQL_UP_1 =
            "ALTER Table Todo ADD date TEXT";

    private static final String SQL_DOWN_1 = "BEGIN TRANSACTION;" +
            "CREATE TEMPORARY TABLE todo_backup(todo_id,title,content);\n" +
            "INSERT INTO todo_backup SELECT todo_id,title,content FROM todo;\n" +
            "DROP TABLE todo;\n" +
            SQL_UP_0 +
            "INSERT INTO todo SELECT todo_id,title,content FROM todo_backup;\n" +
            "DROP TABLE todo_backup;\n" +
            "COMMIT;";

    private static final String SQL_UP_2 =
            "ALTER Table Todo ADD date_done TEXT";

    private static final String SQL_DOWN_2 = "BEGIN TRANSACTION;" +
            "CREATE TEMPORARY TABLE todo_backup(todo_id,title,content,date);\n" +
            "INSERT INTO todo_backup SELECT todo_id,title,content,date FROM todo;\n" +
            "DROP TABLE todo;\n" +
            SQL_UP_0 +
            "INSERT INTO todo SELECT todo_id,title,content,date FROM todo_backup;\n" +
            "DROP TABLE todo_backup;\n" +
            "COMMIT;";

    public StorageHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_UP_0);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        int currentVersion = oldVersion;
        if(currentVersion == 0 && newVersion > currentVersion) {
            db.execSQL(SQL_UP_1);
            currentVersion = 1;
        }
        if(currentVersion == 1 && newVersion > currentVersion) {
            db.execSQL(SQL_UP_2);
            currentVersion = 2;
        }
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        int currentVersion = oldVersion;
        if(currentVersion == 2 && newVersion < currentVersion) {
            db.execSQL(SQL_DOWN_2);
            currentVersion = 1;
        }
        if(currentVersion == 1 && newVersion < currentVersion) {
            db.execSQL(SQL_DOWN_1);
            currentVersion = 0;
        }
    }

    public void addTodo(String title, String content) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("content", content);
        values.put("title", title);
        db.insert("Todo", null, values);
        db.close();
    }

    public Todo getTodo(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query("Todo",                  //table
                new String[] { "todo_id", "title", "content","done"}, // columns
                "todo_id" + "=?",                               // WHERE clause
                new String[] { String.valueOf(id) },            // WHERE arguments
                null,                                           // GROUP BY
                null,                                           // HAVING
                null,                                           // ORDER BY
                null);                                          // LIMIT
        if (cursor != null)
            cursor.moveToFirst();

        Todo todo = new Todo(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2),Boolean.parseBoolean(cursor.getString(3)));
        return todo;
    }


    public List<Todo> getAll() {
        List<Todo> todoList = new ArrayList<Todo>();
        // Select All Query
        String selectQuery = "SELECT  * FROM Todo";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Todo todo = new Todo(Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1), cursor.getString(2),Boolean.parseBoolean(cursor.getString(3)));

                todoList.add(todo);
            } while (cursor.moveToNext());
        }

        return todoList;
    }

    public int count() {
        String countQuery = "SELECT  * FROM Todo";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        return cursor.getCount();
    }

    /*public int updateTodo(Todo todo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("title", todo.title);
        values.put("content", todo.content);

        return db.update("Todo",
                values,
                "todo_id" + " = ?",
                new String[] { String.valueOf(todo.todo_id) });
    }*/

    public void deleteTodo(Todo todo) {
        Log.d("delete_BDD",todo.todo_id + " - "+todo.getTitle());
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("Todo", "todo_id" + " = ?",
                new String[] { String.valueOf(todo.todo_id) });
        db.close();
    }
}
