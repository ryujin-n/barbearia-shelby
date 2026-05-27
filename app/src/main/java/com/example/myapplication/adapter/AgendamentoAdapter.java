package com.example.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.model.Agendamento;

import java.util.List;

public class AgendamentoAdapter extends RecyclerView.Adapter<AgendamentoAdapter.AgendamentoViewHolder> {

    private List<Agendamento> agendamentos;
    private OnExcluirClickListener excluirClickListener;

    public interface OnExcluirClickListener {
        void onExcluirClick(Agendamento agendamento);
    }

    public AgendamentoAdapter(List<Agendamento> agendamentos, OnExcluirClickListener excluirClickListener) {
        this.agendamentos = agendamentos;
        this.excluirClickListener = excluirClickListener;
    }

    @NonNull
    @Override
    public AgendamentoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_agendamento, parent, false);
        return new AgendamentoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AgendamentoViewHolder holder, int position) {
        Agendamento agendamento = agendamentos.get(position);
        holder.tvServico.setText(agendamento.getServico());
        holder.tvBarbeiro.setText(agendamento.getBarbeiro());
        holder.tvDataHorario.setText(agendamento.getDataHorario());

        holder.btnExcluir.setOnClickListener(v -> {
            if (excluirClickListener != null) {
                excluirClickListener.onExcluirClick(agendamento);
            }
        });
    }

    @Override
    public int getItemCount() {
        return agendamentos.size();
    }

    public void updateList(List<Agendamento> newList) {
        this.agendamentos = newList;
        notifyDataSetChanged();
    }

    public static class AgendamentoViewHolder extends RecyclerView.ViewHolder {
        TextView tvServico, tvBarbeiro, tvDataHorario;
        ImageButton btnExcluir;

        public AgendamentoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvServico = itemView.findViewById(R.id.tvServico);
            tvBarbeiro = itemView.findViewById(R.id.tvBarbeiro);
            tvDataHorario = itemView.findViewById(R.id.tvDataHorario);
            btnExcluir = itemView.findViewById(R.id.btnExcluir);
        }
    }
}