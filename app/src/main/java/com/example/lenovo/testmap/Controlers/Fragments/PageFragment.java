package com.example.lenovo.testmap.Controlers.Fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;

import com.example.lenovo.testmap.Adapters.BestOfAdapter;
import com.example.lenovo.testmap.DataBase.DataBaseManager;
import com.example.lenovo.testmap.R;


public class PageFragment extends Fragment {

    private View v;
    private static DataBaseManager db = null;
    private RecyclerView rv;
    private Spinner sortBy;
    private BestOfAdapter bestOfAdapter;
    private SearchView searchView;
    private static Activity activity;


    public static PageFragment newInstance()
    {
        return (new PageFragment());
    }

    public static Activity getPageActivity() {
        return activity;
    }

    public BestOfAdapter getBestOfAdapter() {
        return bestOfAdapter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        activity=this.getActivity();

        v = inflater.inflate(R.layout.fragment_page, container, false);


        rv = v.findViewById(R.id.list);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        if(db==null)
            db = new DataBaseManager(this.getContext());

        bestOfAdapter = new BestOfAdapter(db, this.getContext(), activity);
        rv.setAdapter(bestOfAdapter);



        sortBy = v.findViewById(R.id.sortBy);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.choices, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortBy.setAdapter(spinnerAdapter);

        initSpinner();

        searchView = v.findViewById(R.id.search);
        initSearch();


        return v;

    }

    public static DataBaseManager getDb() {
        return db;
    }

    public void initSpinner()
    {
        sortBy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                String tmp = selectedItem.toLowerCase();
                Log.d( "Spinner" ,"The selected item is: " + selectedItem + " to lower case: " + tmp);
                bestOfAdapter.modifyListSpinner(tmp, db);

                bestOfAdapter.notifyDataSetChanged();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    void initSearch()
    {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                Log.d("Search Listener submit", "Query:" + query);
                bestOfAdapter.modifyListSearch(query, db);
                bestOfAdapter.notifyDataSetChanged();


                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("Search Listener submit", "Query: " + newText);

                bestOfAdapter.modifyListSearch(newText, db);
                bestOfAdapter.notifyDataSetChanged();

                return false;
            }
        });
    }
}