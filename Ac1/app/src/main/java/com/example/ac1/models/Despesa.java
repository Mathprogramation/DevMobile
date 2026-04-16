package com.example.ac1.models;

public class Despesa {

private int id;
private String descricao;
private String categoria;
private float valor;
private String data;
private String formaPagamento;
private boolean foiPaga;

public Despesa(int id, String descricao, String categoria, float valor, String data , String formaPagamento, boolean foiPaga){
    this.id = id;
    this.descricao = descricao;
    this.categoria=categoria;
    this.valor = valor;
    this.data = data;
    this.formaPagamento = formaPagamento;
    this.foiPaga = foiPaga;
}

public Despesa(String descricao, String categoria, float valor, String data , String formaPagamento, boolean foiPaga){
    this(-1, descricao, categoria, valor, data, formaPagamento, foiPaga);
}


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public boolean isFoiPaga() {
        return foiPaga;
    }

    public void setFoiPaga(boolean foiPaga) {
        this.foiPaga = foiPaga;
    }

    public String getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(String formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public String getData() {
        return data;
    }

    public void setDate(String data) {
        this.data = data;
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }
}
