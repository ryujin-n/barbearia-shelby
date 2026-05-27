package com.example.myapplication.model;

public class Agendamento {
    private int id;
    private int usuarioId;
    private String servico;
    private String barbeiro;
    private String dataHorario;

    public Agendamento(int id, int usuarioId, String servico, String barbeiro, String dataHorario) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.servico = servico;
        this.barbeiro = barbeiro;
        this.dataHorario = dataHorario;
    }

    public int getId() { return id; }
    public int getUsuarioId() { return usuarioId; }
    public String getServico() { return servico; }
    public String getBarbeiro() { return barbeiro; }
    public String getDataHorario() { return dataHorario; }
}