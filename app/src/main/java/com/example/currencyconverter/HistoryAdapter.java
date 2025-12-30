package com.example.currencyconverter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private final ArrayList<ConversionHistory> list;

    public HistoryAdapter(ArrayList<ConversionHistory> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ConversionHistory item = list.get(position);
        holder.txtConversion.setText(
                item.amount + " " + item.from + " → " + item.result + " " + item.to
        );
        holder.txtDate.setText(item.date);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtConversion, txtDate;

        ViewHolder(View itemView) {
            super(itemView);
            txtConversion = itemView.findViewById(R.id.txtConversion);
            txtDate = itemView.findViewById(R.id.txtDate);
        }
    }
}
