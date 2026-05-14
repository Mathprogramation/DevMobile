package com.example.ac2.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.ac2.R;
import com.example.ac2.models.Treino;

import java.util.List;

public class TreinoAdapter extends BaseAdapter {

    private final Context context;
    private List<Treino> treinos;

    public TreinoAdapter(Context context, List<Treino> treinos) {
        this.context = context;
        this.treinos  = treinos;
    }

    public void atualizar(List<Treino> novos) {
        this.treinos = novos;
        notifyDataSetChanged();
    }

    @Override public int getCount() {
        return treinos.size();
    }
    @Override public Treino getItem(int pos) {
        return treinos.get(pos);
    }

    //Revisar
    @Override public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.item_treino, parent, false);
        }

        Treino t = treinos.get(position);

        TextView tvNomeTreino = convertView.findViewById(R.id.tvNomeTreino);
        TextView tvInfo   = convertView.findViewById(R.id.tvInfo);

        tvNomeTreino.setText(t.getNomeTreino());
        tvInfo.setText(t.getTipoAtividade() + " • " + t.getData() + " • "
                + (t.getDuracao()) + " Min " + t.getIntensidade());

        return convertView;
    }


}
