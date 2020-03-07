package com.example.certification;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MoveBookmarkFragment extends Fragment {

    ArrayList<String> items;
    ArrayAdapter<String> adapter;
    ListView listView;
    String num;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_move_bookmark, container, false);

        ActionBar actionBar = ((BookmarkActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle("북마크-자격증 이동화면");
        actionBar.setDisplayHomeAsUpEnabled(false);

        listView = (ListView) view.findViewById(R.id.listview_move);

        items = new ArrayList<String>();

        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, items);
        listView.setAdapter(adapter);
        listView.setChoiceMode(listView.CHOICE_MODE_SINGLE);

        addList();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), CertificationActivity.class);
                intent.putExtra("name", items.get(position));
                intent.putExtra("num", num);
                startActivity(intent);
            }
        });

        return view;
    }

    public void addList() {

        int max;
        String text;

        try {
            max = Integer.parseInt(PreferenceManager.getString_max(getContext(), "value")); // max value

            for(int i = 1; i <= max; i++) {
                String temp = String.valueOf(i);
                text = PreferenceManager.getString(getContext(), temp);
                if(!text.equals("")) {
                    items.add(text);
                    num = temp;
                }
            }
            adapter.notifyDataSetChanged();
        }
        catch (NumberFormatException e) {
        }
    }
}
