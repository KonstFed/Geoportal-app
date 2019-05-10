package com.example.biologic;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

public class NotesAdapter extends BaseAdapter {

    private Context contextm;
    private Table table;

    public NotesAdapter(Context contextm, Table table) {
        this.contextm = contextm;
        this.table = table;
    }

    @Override
    public int getCount() {
        return table.notes.size();
    }

    @Override
    public Object getItem(int position) {
        return table.notes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        //use convertView recycle
        if(convertView==null){
            holder=new ViewHolder();
            convertView = LayoutInflater.from(contextm).inflate(R.layout.fieldedit, parent, false);
            holder.button= (Button) convertView.findViewById(R.id.note);
            holder.button.setText(table.notes.get(position).name);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        //set text and url
//        holder.textView.setText(mList.get(position).getText());
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(contextm,NoteRedactorActivity.class);
                String tmp = table.notes.get(position).name+":"+table.struct+":"+table.notes.get(position).data;
                intent.putExtra("Note",tmp);
                contextm.startActivity(intent);
            }
        });
        return convertView;

    }
    class ViewHolder
    {
        Button button;

//        public ViewHolder(Button button) {
//            this.button = button;
//        }
    }
}
