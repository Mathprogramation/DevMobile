package com.example.ac1.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.wifi.rtt.RangingRequest;

import com.example.ac1.models.Despesa;

import java.util.ArrayList;
import java.util.List;

public class DespesaDatabase extends SQLiteOpenHelper {
    private static final String NOME_BANCO = "despesa.db";
    private static final int VERSAO = 1;

    // Nomes da tabela e colunas
    public static final String TABELA = "despesas";
    public static final String COL_ID      = "id";
    public static final String COL_Descricao  = "descrição";
    public static final String COL_Categoria = "categoria";
    public static final String COL_Valor    = "valor";
    public static final String COL_Data    = "data";
    public static final String COL_Forma  = "formaPagamento";
    public static final String COL_Status  = "foiPaga";

    public DespesaDatabase(Context context) {
        super(context, NOME_BANCO, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABELA + " ("
                + COL_ID      + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_Descricao  + " TEXT NOT NULL, "
                + COL_Categoria+ " TEXT, "
                + COL_Valor    + " FLOAT NOT NULL, "
                + COL_Data    + " TEXT NOT NULL, "
                + COL_Forma  + " TEXT, "
                + COL_Status  + " INTEGER DEFAULT 0"
                + ")";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABELA);
        onCreate(db);
    }

    // ── CREATE ──────────────────────────────────────────────

    public Long inserir(Despesa f){
        ContentValues cv = toContentValues(f);
        return getWritableDatabase().insert(TABELA, null, cv);
    }

    // ── UPDATE ──────────────────────────────────────────────
    public int atualizar(Despesa f ){
        ContentValues cv = toContentValues(f);
        return getWritableDatabase().update(
          TABELA,cv,COL_ID +"=?",
                new String[]{String.valueOf(f.getId())});
    }


    // ── DELETE ──────────────────────────────────────────────
    public void deletar(int id) {
        getWritableDatabase().delete(
                TABELA, COL_ID + "=?",
                new String[]{String.valueOf(id)});
    }



    // ── Helpers ─────────────────────────────────────────────
    private ContentValues toContentValues(Despesa f) {
        ContentValues cv = new ContentValues();
        cv.put(COL_Descricao,  f.getDescricao());
        cv.put(COL_Categoria, f.getCategoria());
        cv.put(COL_Valor,     f.getValor());
        cv.put(COL_Data,    f.getData());
        cv.put(COL_Forma,  f.getFormaPagamento());
        cv.put(COL_Status,  f.isFoiPaga() ? 1:0 );
        return cv;
    }

    // ── READ — todos, com filtros opcionais ─────────────────
    public List<Despesa> listar(String filtroCatgeoria,
                              boolean foiPago,
                              String ordenacao) {
        List<Despesa> lista = new ArrayList<>();

        StringBuilder where = new StringBuilder("1=1");
        List<String> args = new ArrayList<>();

        if (filtroCatgeoria != null && !filtroCatgeoria.equals("Todos")) {
            where.append(" AND ").append(COL_Categoria).append("=?");
            args.add(filtroCatgeoria);
        }
        if (foiPago) {
            where.append(" AND ").append(COL_Status).append("=1");
        }

        String order = COL_Descricao; // padrão
        if ("Valor".equals(ordenacao))  order = COL_Valor  + " DESC";
        if ("Data".equals(ordenacao))   order = COL_Data  + " DESC";

        Cursor c = getReadableDatabase().query(
                TABELA, null,
                where.toString(),
                args.toArray(new String[0]),
                null, null, order);

        while (c.moveToNext()) {
            lista.add(cursorToDespesa(c));
        }
        c.close();
        return lista;
    }

    private Despesa cursorToDespesa(Cursor c) {
        return new Despesa(
                c.getInt(c.getColumnIndexOrThrow(COL_ID)),
                c.getString(c.getColumnIndexOrThrow(COL_Descricao)),
                c.getString(c.getColumnIndexOrThrow(COL_Categoria)),
                c.getInt(c.getColumnIndexOrThrow(COL_Valor)),
                c.getString(c.getColumnIndexOrThrow(COL_Data)),
                c.getString(c.getColumnIndexOrThrow(COL_Forma)),
                c.getInt(c.getColumnIndexOrThrow(COL_Status)) == 1
        );
    }


}
