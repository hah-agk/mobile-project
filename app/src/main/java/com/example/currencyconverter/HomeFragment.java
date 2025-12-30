package com.example.currencyconverter;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class HomeFragment extends Fragment {

    private EditText et_firstConversion, et_secondConversion;
    private Spinner spinner_firstConversion, spinner_secondConversion;

    private String baseCurrency = "EUR";
    private String targetCurrency = "USD";

    private final Map<String, Double> rates = new HashMap<>();

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        et_firstConversion = view.findViewById(R.id.et_firstConversion);
        et_secondConversion = view.findViewById(R.id.et_secondConversion);
        spinner_firstConversion = view.findViewById(R.id.spinner_firstConversion);
        spinner_secondConversion = view.findViewById(R.id.spinner_secondConversion);

        setupRates();
        setupSpinners();
        setupTextWatcher();

        Button btnHistory = view.findViewById(R.id.btnHistory);

        btnHistory.setOnClickListener(v -> {
            FragmentTransaction ft = requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction();

            ft.replace(R.id.frame_layout, new HistoryFragment());
            ft.addToBackStack(null);
            ft.commit();
        });


        return view;
    }

    // Manual exchange rates (base = EUR)
    private void setupRates() {
        rates.put("EUR", 1.0);
        rates.put("GBP", 0.86);
        rates.put("USD", 1.09);
        rates.put("DKK", 7.46);
        rates.put("SEK", 11.20);
        rates.put("AUD", 1.65);
        rates.put("CAD", 1.47);
        rates.put("JPY", 158.00);
        rates.put("LBP", 89000.0);
    }

    private void setupTextWatcher() {
        et_firstConversion.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                convert();
            }
            @Override public void afterTextChanged(Editable s) {}
        });
    }

    private void convert() {
        if (et_firstConversion.getText() == null) return;

        String input = et_firstConversion.getText().toString();
        if (input.isEmpty()) {
            et_secondConversion.setText("");
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(input);
        } catch (NumberFormatException e) {
            return;
        }

        if (!rates.containsKey(baseCurrency) || !rates.containsKey(targetCurrency)) return;

        double result;

        if (baseCurrency.equals(targetCurrency)) {
            result = amount;
        } else {
            double amountInEur = amount / rates.get(baseCurrency);
            result = amountInEur * rates.get(targetCurrency);
        }

        et_secondConversion.setText(String.format(Locale.getDefault(), "%.2f", result));

        // SAVE HISTORY
        saveHistory(amount, result);
    }


    private void setupSpinners() {

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.currencies,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner_firstConversion.setAdapter(adapter);
        spinner_secondConversion.setAdapter(adapter);

        spinner_firstConversion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                baseCurrency = parent.getItemAtPosition(position).toString();
                convert();
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        spinner_secondConversion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                targetCurrency = parent.getItemAtPosition(position).toString();
                convert();
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void saveHistory(double amount, double result) {

        SharedPreferences prefs = requireContext()
                .getSharedPreferences("currency_history", getContext().MODE_PRIVATE);

        Gson gson = new Gson();

        String json = prefs.getString("history_list", null);
        Type type = new TypeToken<ArrayList<ConversionHistory>>(){}.getType();

        ArrayList<ConversionHistory> list =
                json == null ? new ArrayList<>() : gson.fromJson(json, type);

        list.add(0, new ConversionHistory(
                baseCurrency,
                targetCurrency,
                amount,
                result,
                new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                        .format(new Date())
        ));

        // Optional: limit history size
        if (list.size() > 50) {
            list.remove(list.size() - 1);
        }

        prefs.edit().putString("history_list", gson.toJson(list)).apply();
    }

}