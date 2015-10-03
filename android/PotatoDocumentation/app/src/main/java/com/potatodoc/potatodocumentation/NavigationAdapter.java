package com.potatodoc.potatodocumentation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Ochi on 01.10.2015.
 */
public class NavigationAdapter extends BaseAdapter {
    
    private final List<NavigationItem> navigationItemList;
    private final LayoutInflater layoutInflater;
    
    public NavigationAdapter(Context context){
        //Inflator setzen
        layoutInflater = LayoutInflater.from(context);
      
        //Items einfügen
        navigationItemList = NavigationItem.getAllNavigationItems();
        
    }
    
    @Override
    public int getCount(){
       return navigationItemList.size();
    }
    
    public Object getItem(int position){
        return navigationItemList.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            //Inflate Layout
            convertView = layoutInflater.inflate(R.layout.menu_item, parent, false);
        }

        Context context = parent.getContext();
        NavigationItem navigationItem = navigationItemList.get(position);

        //place icon
        ImageView icon = (ImageView) convertView.findViewById(R.id.item_icon);
        icon.setImageResource(navigationItem.getIcon());

        //Place name
        TextView name = (TextView) convertView.findViewById(R.id.item_name);
        name.setText(navigationItem.getName());

        return convertView;
    }

}
