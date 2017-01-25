package com.example.dictionaryapp;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.graphics.Typeface;
import java.util.ArrayList;

/**
 * Created by cuti-pie on 21/01/17.
 */

public class ListViewAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<Word> mDataSource;

    public ListViewAdapter(Context context, ArrayList<Word> items) {
        mContext = context;
        mDataSource = items;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return mDataSource.size();
    }

    @Override
    public Object getItem(int i) {
        return mDataSource.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View rowView = mInflater.inflate(R.layout.list_item, viewGroup, false);
        Word word = mDataSource.get(i);
        TextView textView = (TextView) rowView.findViewById(R.id.textView);
        textView.setText(word.getWord());
        Typeface titleTypeFace = Typeface.createFromAsset(mContext.getAssets(), "fonts/JosefinSans-SemiBoldItalic.ttf");
        textView.setTypeface(titleTypeFace);
        return rowView;
    }
}
