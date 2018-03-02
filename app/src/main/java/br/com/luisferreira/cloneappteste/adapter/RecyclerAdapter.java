package br.com.luisferreira.cloneappteste.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.luisferreira.cloneappteste.Interface.ItemClickListener;
import br.com.luisferreira.cloneappteste.activities.InsertActivity;
import br.com.luisferreira.cloneappteste.model.Clone;
import br.com.luisferreira.cloneappteste.R;

/**
 * Created by Luis Ferreira on 24/02/2018.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private List<Clone> clones;
    private Context context;

    public RecyclerAdapter(Context context, List<Clone> clones) {
        this.context = context;
        this.clones = clones;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Clone clone = clones.get(position);

        holder.textViewNomeClone.setText(clones.get(position).getNome());
        holder.textViewIdadeClone.setText(String.valueOf(clones.get(position).getIdade()));
        holder.textViewDataCriacao.setText(clones.get(position).getDataCriacao());

        if (clone.getAdicionais() == null) {
            holder.textViewAdicionais.setText("Este clone não possui itens adicionais!");
        } else {
            holder.textViewAdicionais.setText(TextUtils.join(", ", clone.getAdicionais()));
        }

        holder.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(view.getContext(), InsertActivity.class);
                intent.putExtra("isUpdating", true);
                intent.putExtra("textViewNomeClone", holder.textViewNomeClone.getText());
                intent.putExtra("textViewIdadeClone", holder.textViewIdadeClone.getText());
                intent.putExtra("textViewAdicionais", holder.textViewAdicionais.getText());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return clones.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textViewNomeClone, textViewIdadeClone, textViewDataCriacao, textViewAdicionais;
        private ItemClickListener clickListener;

        public ViewHolder(View view) {
            super(view);
            textViewNomeClone = view.findViewById(R.id.textViewNomeClone);
            textViewIdadeClone = view.findViewById(R.id.textViewIdadeClone);
            textViewDataCriacao = view.findViewById(R.id.textViewDataCriacao);
            textViewAdicionais = view.findViewById(R.id.textViewAdicionais);

            view.setOnClickListener(this);
        }

        public void setClickListener(ItemClickListener itemClickListener) {
            this.clickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            clickListener.onClick(v, getAdapterPosition());
        }

        public void removeItem(int position) {
            clones.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, clones.size());
        }
    }
}
