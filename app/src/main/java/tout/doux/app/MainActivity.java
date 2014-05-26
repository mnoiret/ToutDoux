package tout.doux.app;

import android.app.Activity;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import tout.doux.app.model.StorageHelper;
import tout.doux.app.model.Todo;

public class MainActivity extends Activity {
    StorageHelper helper;
    Todo t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }
    // Log.d("main", "value: " + prefs.getInt("value", 0));


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.sort, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Create and return an example alert dialog with an edit text box.
     */
    private Dialog createEditDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Hello User");
        builder.setMessage("What is your name:");

        // Use an EditText view to get user input.
        final EditText input = new EditText(this);
        input.setId(1);
        builder.setView(input);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString();
                return;
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });

        return builder.create();
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.listView) {
            ListView lv = (ListView) v;
            AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) menuInfo;
            t = (Todo) lv.getItemAtPosition(acmi.position);

            menu.add("Edit");
            menu.add("Suppr");
        }
    }

    @Override
    public boolean onContextItemSelected(final MenuItem item)
    {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();


        try
        {
            if(item.getTitle()=="Edit")
            {
                new AlertDialog.Builder(this).setTitle("Confirmer Suppression")
                        .setMessage("Etes-vous sur de vouloir supprimer cet élément ?")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                                //helper.deleteTodo(t);
                            }
                        })
                        .setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                       })
                        .show();
            }
            else if(item.getTitle()=="Suppr")
            {
                new AlertDialog.Builder(this).setTitle("Confirmer Suppression")
                        .setMessage("Etes-vous sur de vouloir supprimer cet élément ?")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                                helper.deleteTodo(t);
                            }
                        })
                        .show();
            }
            else
            {return false;}
            return true;
        }
        catch(Exception e)
        {
            return true;
        }
    }




/**
         * A placeholder fragment containing a simple view.
         */
    public static class PlaceholderFragment extends Fragment {

        StorageHelper helper;
        ArrayList<String> dataList;
        List<Todo> todoList;
        TodoAdapter adapter;
        EditText ed;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);


            helper = new StorageHelper(this.getActivity());

            ed = (EditText) rootView.findViewById(R.id.editText);
            Button btn_add = (Button) rootView.findViewById(R.id.ok_button  );
            final ListView listView = (ListView) rootView.findViewById(R.id.listView);
            final Activity act = this.getActivity();

            todoList = helper.getAll();
            dataList = new ArrayList<String>();

            final TodoAdapter adapter =
                    new TodoAdapter(this.getActivity(),
                            todoList);
            listView.setAdapter(adapter);
            registerForContextMenu(listView);

            btn_add.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String title = ed.getText().toString();
                    String content = "";
                    Todo t = new Todo(title,content);
                    ArrayList<String> data = new ArrayList<String>();
                    data.add(title);
                    adapter.insert(t);
                    helper.addTodo(title, content);
                    reloadData();


                }
            });

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int index,
                                        long arg3) {
                    Todo t = (Todo)listView.getAdapter().getItem(index);
                    TextView tv = (TextView) arg1.findViewById(R.id.todoItemTitle);
                    if(t.isDone()==true){
                        t.setDone(false);
                        tv.setTextColor(Color.BLACK);
                    }
                    else{
                        t.setDone(true);
                        tv.setTextColor(Color.RED);
                    }

                }


            });




            return rootView;
        }

        public void reloadData() {

            todoList.clear();

            todoList = helper.getAll();

            ed.setText("");
        }
    }
}