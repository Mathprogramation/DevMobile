package com.example.ac2.models;

public class Treino {

    private String id;
    private String nomeTreino;
    private String tipoAtividade;
    private String data;
    private double duracao;
    private String intensidade;
    private boolean treinoConcluido;

    public Treino() {}

    public Treino(String nomeTreino, String tipoAtividade, double duracao,
                   String data, String intensidade, boolean treinoConcluido) {
        this.nomeTreino = nomeTreino;
        this.tipoAtividade = tipoAtividade;
        this.duracao = duracao;
        this.data = data;
        this.intensidade = intensidade;
        this.treinoConcluido = treinoConcluido;
    }

    public Treino(String id, String nomeTreino, String tipoAtividade, double duracao,
                   String data, String intensidade, boolean treinoConcluido) {
        this(nomeTreino, tipoAtividade, duracao, data, intensidade, treinoConcluido);
        this.id = id;
    }
    public String getId() {
            return id;
        }
    public void setId(String id) {
            this.id = id;
    }

    public String getNomeTreino(){
            return nomeTreino;
    }
    public void setNomeTreino(String v){
            this.nomeTreino = v;
    }

    public String getTipoAtividade(){
            return tipoAtividade;
    }
    public void setTipoAtividade(String v){
            this.tipoAtividade = v;
    }

    public String getData(){
            return data;
    }
    public void setData(String v){
            this.data = v;
    }

    public double getDuracao(){
            return duracao;
    }
    public void setDuracao(double v){
            this.duracao = v;
    }

    public String getIntensidade(){
            return intensidade;
    }
    public void setIntensidade(String v){
            this.intensidade = v;
    }

    public boolean isTreinoConcluido(){
            return treinoConcluido;
    }
    public void setTreinoConcluido(boolean v){
            this.treinoConcluido = v;
    }

}
