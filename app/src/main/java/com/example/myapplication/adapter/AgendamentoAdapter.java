package com.example.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.model.Agendamento;

import java.util.List;

public class AgendamentoAdapter extends RecyclerView.Adapter<AgendamentoAdapter.AgendamentoViewHolder> {

    private List<Agendamento> agendamentos;
    private OnItemLongClickListener longClickListener;

    public interface OnItemLongClickListener {
        void onItemLongClick(Agendamento agendamento);
    }

    public AgendamentoAdapter(List<Agendamento> agendamentos, OnItemLongClickListener longClickListener) {
        this.agendamentos = agendamentos;
        this.longClickListener = longClickListener;
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

        holder.itemView.setOnLongClickListener(v -> {
            if (longClickListener != null) {
                longClickListener.onItemLongClick(agendamento);
            }
            return true;
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

        public AgendamentoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvServico = itemView.findViewById(R.id.tvServico);
            tvBarbeiro = itemView.findViewById(R.id.tvBarbeiro);
            tvDataHorario = itemView.findViewById(R.id.tvDataHorario);
        }
    }
}