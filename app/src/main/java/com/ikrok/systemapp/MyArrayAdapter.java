package com.ikrok.systemapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MyArrayAdapter extends ArrayAdapter<AppInformation> {

    @InjectView(R.id.imageView_icon)
    ImageView appIcon;
    @InjectView(R.id.textView_appName)
    TextView appName;
    @InjectView(R.id.textView2_appPackage)
    TextView packageName;
    private final Context context;
    private final ArrayList<AppInformation> list;


    public MyArrayAdapter(Context context, ArrayList<AppInformation> list) {
        super(context,0, list);
        this.context = context;
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_layout, parent, false);
        ButterKnife.inject(this, rowView);
        AppInformation currentPos = list.get(position);
        appIcon.setImageDrawable(currentPos.getIcon());
        appName.setText(currentPos.getAppName());
        packageName.setText(currentPos.getPackageName());
        return rowView;
    }
}
