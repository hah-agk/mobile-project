package com.example.currencyconverter;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class HistoryFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolbarHistory);

        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);

        if (((AppCompatActivity) requireActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity) requireActivity())
                    .getSupportActionBar()
                    .setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setNavigationOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack()
        );


        RecyclerView recyclerView = view.findViewById(R.id.recyclerHistory);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new HistoryAdapter(loadHistory()));

        return view;
    }

    private ArrayList<ConversionHistory> loadHistory() {

        SharedPreferences prefs = requireContext()
                .getSharedPreferences("currency_history", getContext().MODE_PRIVATE);

        Gson gson = new Gson();
        String json = prefs.getString("history_list", null);

        Type type = new TypeToken<ArrayList<ConversionHistory>>(){}.getType();
        return json == null ? new ArrayList<>() : gson.fromJson(json, type);
    }
}
