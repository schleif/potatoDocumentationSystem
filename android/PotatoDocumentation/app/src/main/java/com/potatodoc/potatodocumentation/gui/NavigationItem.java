package com.potatodoc.potatodocumentation.gui;

import java.util.ArrayList;
import java.util.List;
import android.support.v4.app.Fragment;

import com.potatodoc.potatodocumentation.R;

/**
 * This class represents one item of the navigationbar of the app.
 * It consists of the Id of the name, the Id of the icon and the fragment.
 *
 * Created by Ochi on 01.10.2015.
 */
public class NavigationItem {
    private int name;
    private int icon;
    private Fragment fragment;

    private static List<NavigationItem> allNavigationItems;

    public NavigationItem(int icon, int name, Fragment fragment) {
        this.icon = icon;
        this.name = name;
        this.fragment = fragment;
    }

    public int getIcon() {
        return icon;
    }

    public int getName() {
        return name;
    }

    public Fragment getFragment() {
        return fragment;
    }


    public static List<NavigationItem> getAllNavigationItems(){

        if(allNavigationItems == null) {
            ArrayList<NavigationItem> navigationItems = new ArrayList<>();

            navigationItems.add(new NavigationItem(R.drawable.icon_tour, R.string.title_section1, new TourFragment()));

            navigationItems.add(new NavigationItem(R.drawable.icon_map, R.string.title_section2, new MapFragment()));

            navigationItems.add(new NavigationItem(R.drawable.icon_sync, R.string.title_section3, new SyncFragment()));

            navigationItems.add(new NavigationItem(R.drawable.icon_history, R.string.title_section4, new LatestFragment()));

            allNavigationItems = navigationItems;

        }

        return allNavigationItems;

    }
}
