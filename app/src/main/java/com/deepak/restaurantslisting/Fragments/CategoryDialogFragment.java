package com.deepak.restaurantslisting.Fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.deepak.restaurantslisting.Constants.AppConstants;
import com.deepak.restaurantslisting.R;

import java.util.ArrayList;

/**
 * Created by Sharma on 6/1/2016.
 */
public class CategoryDialogFragment extends DialogFragment {
    ListView mylist;
    ArrayList<String> categories=new ArrayList<>();
    public static CategoryDialogFragment newInstance(ArrayList<String> categories) {
        CategoryDialogFragment frag = new CategoryDialogFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(AppConstants.Categories, categories);
        frag.setArguments(args);
        return frag;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        categories = getArguments().getStringArrayList(AppConstants.Categories);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment, null, false);
        mylist = (ListView) view.findViewById(R.id.list);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, categories);
        mylist.setAdapter(adapter);
    }
}
