package tout.doux.app;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;



import java.util.List;

import tout.doux.app.model.Todo;

public class TodoAdapter extends BaseAdapter {

    List<Todo> data;
    Context context;

    public TodoAdapter(Context _context, List<Todo> _data) {
        context = _context;
        data    = _data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int pos) {
        return data.get(pos);
    }


    public void insert(Todo t){
        data.add(t);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    static class ViewHolder {
        TextView titleView;
        TextView contentView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if(convertView == null) {
            LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.todo_item, parent, false);
            holder = new ViewHolder();
            holder.titleView    = (TextView) convertView.findViewById(R.id.todoItemTitle);
            holder.contentView  = (TextView) convertView.findViewById(R.id.todoItemContent);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Todo todo = data.get(position);
        holder.titleView.setText(todo.getTitle());
        holder.contentView.setText(todo.getContent());
        return convertView;
    }

}
