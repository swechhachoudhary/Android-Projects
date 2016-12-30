package com.busybee.calculator;

import android.widget.BaseAdapter;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;

/**
 * Created by cuti-pie on 18/12/16.
 */

public class GridAdapter extends BaseAdapter {

    private Context mContext;

    public GridAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return mButtonLabels.length;
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    // create a new button view for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        Button button;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            button = new Button(mContext);
            button.setLayoutParams(new GridView.LayoutParams(85, 85));
            button.setBackgroundResource(R.color.button);
            button.setPadding(8, 8, 8, 8);
            button.setFocusable(false);
            button.setClickable(false);   //To make grid item clickable
        } else {
            button = (Button) convertView;
        }

        button.setText(mButtonLabels[position]);
        button.setId(position);
        return button;
    }

    private String[] mButtonLabels = {
            "7","8","9","/","4","5","6","*","1","2","3","-","0",".","=","+"
    };

}
