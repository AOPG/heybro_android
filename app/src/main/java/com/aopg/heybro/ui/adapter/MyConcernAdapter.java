package com.aopg.heybro.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.aopg.heybro.R;
import com.aopg.heybro.entity.Concern;
import com.aopg.heybro.ui.activity.MyConcernActivity;
import com.aopg.heybro.ui.activity.SingleChartActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by 王伟健 on 2018-06-12.
 */

public class MyConcernAdapter extends BaseAdapter {

    private Context context;
    private List<Concern> concerns;
  //  private Map<String,String> message;

    public MyConcernAdapter(Context context,List<Concern> concerns){
        this.context = context;
        this.concerns = concerns;
    }

    @Override
    public int getCount() {
        return concerns.size();
    }

    @Override
    public Object getItem(int position) {
        return concerns.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null){
            convertView = View.inflate(context, R.layout.friend_item2, null);
        }
        TextView noteTv = convertView.findViewById(R.id.note);
        final String note = concerns.get(position).getUserNote();
        final String userConcernCode = concerns.get(position).getUserConcernCode();
        noteTv.setText(note);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(context,SingleChartActivity.class);
                intent.putExtra("note",note);
                intent.putExtra("userConcernCode",userConcernCode);
                context.startActivity(intent);
            }
        });
        return convertView;
    }
}
