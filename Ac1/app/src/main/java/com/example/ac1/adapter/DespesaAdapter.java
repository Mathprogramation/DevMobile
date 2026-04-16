package com.example.ac1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.ac1.R;
import com.example.ac1.models.Despesa;

import java.util.List;

public class DespesaAdapter extends BaseAdapter {
    private final Context context;
    private List<Despesa> despesas;

    public DespesaAdapter(Context context, List<Despesa> despesas) {
        this.context = context;
        this.despesas  = despesas;
    }

    public void atualizar(List<Despesa> novos) {
        this.despesas = novos;
        notifyDataSetChanged();
    }

    @Override public int getCount() {
        return despesas.size();
    }
    @Override public Despesa getItem(int pos) {
        return despesas.get(pos);
    }
    @Override public long getItemId(int pos) {
        return despesas.get(pos).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.item_despesa, parent, false);
        }

        Despesa d = despesas.get(position);

        TextView tvDescricao = convertView.findViewById(R.id.tvDescricao);
        TextView tvInfo   = convertView.findViewById(R.id.tvInfo);

        tvDescricao.setText(d.getDescricao());
        tvInfo.setText(d.getFormaPagamento() + " • " + d.getData()
                + (d.isFoiPaga() ? " $" +d.getValor() : ""));

        return convertView;
    }
}
