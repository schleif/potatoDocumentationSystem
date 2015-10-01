package com.potatodoc.potatodocumentation;

import java.util.ArrayList;
import java.util.List;
import android.support.v4.app.Fragment;

/**
 * This class represents one item of the navigationbar of the app.
 * It consists of the name, the path of the icon and the fragment.
 *
 * Created by Ochi on 01.10.2015.
 */
public class NavigationItem {
    private String name;
    private int icon;
    private Fragment fragment;

    public NavigationItem(int icon, String name, Fragment fragment) {
        this.icon = icon;
        this.name = name;
        this.fragment = fragment;
    }

    public int getIcon() {
        return icon;
    }

    public String getName() {
        return name;
    }

    public Fragment getFragment() {
        return fragment;
    }


    public static List<NavigationItem> getAllMenuItems(){
        ArrayList<NavigationItem> navigationItems = new ArrayList<>();

        navigationItems.add(new NavigationItem(R.drawable.icon_tour, "Tour", new TourFragment()));

        navigationItems.add(new NavigationItem(R.drawable.icon_map, "Karte", new MapFragment()));

        navigationItems.add(new NavigationItem(R.drawable.icon_sync, "Sync", new SyncFragment()));



        return navigationItems;

    }
}
