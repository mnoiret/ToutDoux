package tout.doux.app;

import android.animation.TimeInterpolator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import tout.doux.app.comparator.ComparatorByAlphabeticalOrder;
import tout.doux.app.model.StorageHelper;
import tout.doux.app.model.Todo;

public class MainActivity extends Activity {
    public static StorageHelper helper;
    public static List<Todo> todoList;
    public static TodoAdapter adapter;
    static boolean Alphabetical = false;
    Todo t;


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        helper = new StorageHelper(this);
        todoList = helper.getAll();
        adapter = new TodoAdapter(this,
                todoList);
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
        if (id == R.id.action_sort) {
            //on utilise un comparator pour trier la liste
            //Collections.sort(todoList, new ComparatorByAlphabeticalOrder());
            Alphabetical = true;
            reloadData();
            return true;
        }
        /*else if(id == R.id.action_clear){
            //on clear la liste
            reloadData();
            return true;
        }*/
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

            menu.add("Editer");
            menu.add("Supprimer");
        }
    }

    @Override
    public boolean onContextItemSelected(final MenuItem item)
    {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();


        try
        {
            if(item.getTitle()=="Editer")
            {

                final EditText input = new EditText(this);
                input.setId(1);
                input.setText(t.getTitle());
                new AlertDialog.Builder(this).setTitle("Modification d'un élément")
                        .setView(input)
                        .setPositiveButton("Enregistrer", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                                t.setTitle(input.getText().toString());
                                helper.updateTodo(t);
                                reloadData();
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
        else if(item.getTitle()=="Supprimer")
            {
                new AlertDialog.Builder(this).setTitle("Confirmer Suppression")
                        .setMessage("Etes-vous sur de vouloir supprimer cet élément ?")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                                Log.d("delete_Menu",t.todo_id+" - "+t.getTitle());
                                helper.deleteTodo(t);
                                reloadData();
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
            else
            {return false;}
            return true;
        }
        catch(Exception e)
        {
            return true;
        }

    }

    public static void reloadData() {
        todoList.clear();

        if(Alphabetical==true)
            todoList.addAll(helper.getAllAlphabetical());
        else
            todoList.addAll(helper.getAll());

        adapter.notifyDataSetChanged();

    }


/**
         * A placeholder fragment containing a simple view.
         */
    public static class PlaceholderFragment extends Fragment {

        ArrayList<String> dataList;

        EditText ed;
        boolean search;

        public PlaceholderFragment() {

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {

            final View rootView = inflater.inflate(R.layout.fragment_main, container, false);


            ed = (EditText) rootView.findViewById(R.id.editText);
            Button btn_add = (Button) rootView.findViewById(R.id.ok_button  );
            final ImageButton btn_garbage = (ImageButton) rootView.findViewById(R.id.btn_garbage  );
            ImageButton btn_search = (ImageButton) rootView.findViewById(R.id.btn_search  );
            final EditText search_edit = (EditText) rootView.findViewById(R.id.edSearch);
            final ImageButton resetSearch = (ImageButton) rootView.findViewById(R.id.button);
            search=false;


            final ListView listView = (ListView) rootView.findViewById(R.id.listView);
            final Activity act = this.getActivity();


            dataList = new ArrayList<String>();

            listView.setAdapter(adapter);
            registerForContextMenu(listView);

            btn_add.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String title = ed.getText().toString();
                    String content = "";
                    Todo t = new Todo(title, content);
                    ArrayList<String> data = new ArrayList<String>();
                    data.add(title);
                    adapter.insert(t);
                    helper.addTodo(title, content);
                    reloadData();
                    ed.setText("");
                }
            });

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int index,
                                        long arg3) {
                    Todo t = (Todo) listView.getAdapter().getItem(index);
                    TextView tv = (TextView) arg1.findViewById(R.id.todoItemTitle);
                    if (t.isDone() == true) {
                        t.setDone(false);
                        tv.setPaintFlags(tv.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                    } else {
                        t.setDone(true);
                        tv.setPaintFlags(tv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    }
                }
            });

            resetSearch.setOnClickListener(new Button.OnClickListener(){
                @Override
                public void onClick(View v){
                    if(search == true){
                        search=false;
                        search_edit.setText("");
                        btn_garbage.setVisibility(View.VISIBLE);
                        search_edit.setVisibility(View.GONE);
                        resetSearch.setVisibility(View.GONE);
                        reloadData();
                    }
                }
            });


            btn_search.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(search==true){
                        if(search_edit.getText().toString().matches("")){
                            search=false;
                            search_edit.setText("");
                            btn_garbage.setVisibility(View.VISIBLE);
                            search_edit.setVisibility(View.GONE);
                            resetSearch.setVisibility(View.GONE);
                        }
                        else{
                            List<Todo> resSearch;
                            resSearch = helper.searchTodo(search_edit.getText().toString());

                            todoList.clear();
                            todoList.addAll(resSearch);
                            adapter.notifyDataSetChanged();
                        }


                    }else{
                        search=true;
                        search_edit.setText("");
                        btn_garbage.setVisibility(View.GONE);
                        search_edit.setVisibility(View.VISIBLE);
                        resetSearch.setVisibility(View.VISIBLE);
                    }
                }
            });


            btn_garbage.setOnClickListener(new Button.OnClickListener() {

                @Override
                public void onClick(View view) {
                    todoList.clear();
                    helper.deleteAllTodo();
                    reloadData();
                }
            });


            return rootView;
        }

    }
}
