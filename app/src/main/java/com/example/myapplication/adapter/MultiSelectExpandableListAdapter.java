package com.example.myapplication.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.myapplication.R;

import java.util.ArrayList;

public class MultiSelectExpandableListAdapter extends BaseExpandableListAdapter {

    private ArrayList<ArrayList<String>> mGroups;
    private Context mContext;

        public MultiSelectExpandableListAdapter(Context c, ArrayList<ArrayList<String>> mGroups) {
            this.mGroups = mGroups;
            this.mContext = c;
        }

    @Override
    public int getGroupCount() {
        return 0;
    }

    @Override
    public int getChildrenCount(int i) {
        return 0;
    }

    @Override
    public Object getGroup(int i) {
        return null;
    }

    @Override
    public Object getChild(int i, int i1) {
        return null;
    }

    @Override
    public long getGroupId(int i) {
        return 0;
    }

    @Override
    public long getChildId(int i, int i1) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    public View getGroupView( int groupPosition, final boolean isExpanded, View convertView,
                              ViewGroup parent) {
            //if (mGroups.get(groupPosition)!=null) {
            if (true) {
                Log.d("MyLog", "--getGroupView--  exp");
                // inflate and setup view that displays expandable view header
                if (convertView == null) {
                    LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = inflater.inflate(R.layout.activity_group, null);
                }

                TextView textGroup = convertView.findViewById(R.id.groupView);
                textGroup.setText(mGroups.get(groupPosition).toString());

                return convertView;
            } else {
                // inflate and setup view of element that should be displayed as single element
                Log.d("MyLog", "--getGroupView--  No exp");
                if (convertView == null) {
                    LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = inflater.inflate(R.layout.activity_group, null);
                }

                TextView textGroup = convertView.findViewById(R.id.childView);
                textGroup.setText((CharSequence) mGroups.get(groupPosition));

                return convertView;
            }

        }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                             View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.activity_child, null);
        }

        TextView textChild = (TextView) convertView.findViewById(R.id.childView);
        textChild.setText(mGroups.get(groupPosition).get(childPosition));


        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
}

