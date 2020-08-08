package com.example.hw4_1_2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final String TEXT = "text";
    private static final String SIZE = "size";
    private static final String NOTE = "note";
    List<Map<String, String>> values;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        ListView list = findViewById(R.id.list);
        values = prepareContent();
        final BaseAdapter adapter = createAdapter(values);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                values.remove(position);
                adapter.notifyDataSetChanged();
            }
        });

        final SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                values.clear();
                values.addAll(prepareContent());
                swipeRefreshLayout.setRefreshing(false);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void init() {
        SharedPreferences sharedPreferences =
                getSharedPreferences(getString(R.string.large_text), MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String s = String.valueOf(getText(R.string.large_text));
        editor.putString(NOTE, s);
        editor.apply();
    }

    @NonNull
    private BaseAdapter createAdapter(List<Map<String, String>> values) {
        return new SimpleAdapter(this, values, R.layout.texts,
                new String[]{TEXT, SIZE}, new int[]{R.id.textView1, R.id.textView2});
    }

    @NonNull
    private List<Map<String, String>> prepareContent() {
        String[] arrayContent = getString(R.string.large_text).split("\n\n");
        List<Map<String, String>> list = new ArrayList<>();

        for (String s : arrayContent) {
            Map<String, String> map = new HashMap<>();
            map.put(TEXT, s);
            map.put(SIZE, s.length() + "");
            list.add(map);
        }
        return list;
    }
}