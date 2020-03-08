package com.example.certification;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;


public class DeleteBookmarkFragment extends Fragment {

    ArrayList<String> items;
    ArrayAdapter<String> adapter;
    ListView listView;

    Button delete;

    int max;
    String num;
    String words = "";

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_delete_bookmark, container, false);

        ActionBar actionBar = ((BookmarkActivity)getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);

        delete = (Button) view.findViewById(R.id.delete);
        listView = (ListView) view.findViewById(R.id.listview_delete);

        items = new ArrayList<String>();

        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_multiple_choice, items);
        listView.setAdapter(adapter);
        listView.setChoiceMode(listView.CHOICE_MODE_MULTIPLE);

        addList();

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                words = "";

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                final SparseBooleanArray sbArray = listView.getCheckedItemPositions();
                // Notify location of selected items. ex) 0, 3, 7, 9 => true return

                if (items.isEmpty()) {
                    Snackbar.make(v, "삭제할 북마크가 없는데용?", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                for(int i = 0; i < listView.getCount(); i++)
                    words += String.valueOf(sbArray.get(i));

                if(words.contains("true")) {
                    builder.setTitle("메시지")
                            .setMessage("정말 북마크를 삭제하시겠습니까?")
                            .setPositiveButton("예", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    if (sbArray.size() != 0) {
                                        for (int i = listView.getCount() - 1; i >= 0; i--)
                                            if (sbArray.get(i))
                                                for (int j = 1; j <= max; j++) {
                                                    String temp_num = String.valueOf(j);
                                                    String tmp = PreferenceManager.getString(getContext(), temp_num);
                                                    if (tmp.equals(items.get(i))) {
                                                        items.remove(i);
                                                        PreferenceManager.removeKey(getContext(), temp_num);
                                                        break;
                                                    }
                                                }
                                        listView.clearChoices();
                                        adapter.notifyDataSetChanged();

                                        /* This is an method about Fragment
                                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                        fragmentManager.beginTransaction().replace(R.id.move_layout, MoveBookmarkFragment.newInstance()).commit();
                                        */

                                        Snackbar.make(getView(), "북마크를 삭제했어염", Snackbar.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getActivity(), BookmarkActivity.class);
                                        getActivity().finish();
                                        startActivity(intent);
                                        getActivity().overridePendingTransition(0, 0);
                                    }
                                }
                            })
                            .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    return;
                                }
                            }).show();
                }
                else
                    builder.setTitle("메시지")
                            .setMessage("삭제할 북마크를 선택해 주세요.")
                            .setCancelable(false)
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    return;
                                }
                            }).show();
            }
        });

        listView.clearChoices();
        return view;
    }

    public void addList() {

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
