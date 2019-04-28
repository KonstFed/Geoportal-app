package com.example.biologic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import java.util.ArrayList;

public class NotesAdapter extends BaseAdapter {
    private Context contextm;
    private ArrayList<Note> notes;

    public NotesAdapter(Context contextm, ArrayList<Note> notes) {
        this.contextm = contextm;
        this.notes = notes;
    }

    @Override
    public int getCount() {
        return notes.size();
    }

    @Override
    public Object getItem(int position) {
        return notes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        //use convertView recycle
        if(convertView==null){
            holder=new ViewHolder(null);
            convertView = LayoutInflater.from(contextm).inflate(R.layout.fieldedit, parent, false);
            holder.button= (Button) convertView.findViewById(R.id.note);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        //set text and url
//        holder.textView.setText(mList.get(position).getText());
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Перейти в активность с редактом записи
            }
        });
        return convertView;

    }
    class ViewHolder
    {
        Button button;

        public ViewHolder(Button button) {
            this.button = button;
        }
    }
}
