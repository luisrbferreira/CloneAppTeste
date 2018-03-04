package br.com.luisferreira.cloneappteste.adapter;

import android.content.Context;

import android.content.Intent;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import br.com.luisferreira.cloneappteste.activities.InsertActivity;
import br.com.luisferreira.cloneappteste.model.Clone;
import br.com.luisferreira.cloneappteste.R;

/**
 * Created by Luis Ferreira on 24/02/2018.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private List<Clone> cloneList;
    private Context context;
    private FirebaseFirestore firebaseFirestore;

    public RecyclerAdapter(List<Clone> cloneList, Context context, FirebaseFirestore firebaseFirestore) {
        this.cloneList = cloneList;
        this.context = context;
        this.firebaseFirestore = firebaseFirestore;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final int itemPosition = position;
        final Clone clone = cloneList.get(itemPosition);

        holder.textViewNomeClone.setText(clone.getNome());
        holder.textViewIdadeClone.setText(String.valueOf(clone.getIdade()) + " Anos");
        holder.textViewDataCriacao.setText(clone.getDataCriacao());

        if (clone.getAdicionais().isEmpty()) {
            holder.textViewAdicionais.setText("Este clone não possui itens adicionais!");
        } else {
            holder.textViewAdicionais.setText(TextUtils.join(", ", clone.getAdicionais()));
        }

        holder.btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateClone(clone);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cloneList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewNomeClone, textViewIdadeClone, textViewDataCriacao, textViewAdicionais;
        ImageButton btnEditar;

        public ViewHolder(View view) {
            super(view);
            textViewNomeClone = view.findViewById(R.id.textViewNomeClone);
            textViewIdadeClone = view.findViewById(R.id.textViewIdadeClone);
            textViewDataCriacao = view.findViewById(R.id.textViewDataCriacao);
            textViewAdicionais = view.findViewById(R.id.textViewAdicionais);

            btnEditar = view.findViewById(R.id.btnEditar);
        }
    }

    private void updateClone(Clone clone) {
        Intent intent = new Intent(context, InsertActivity.class);
        intent.putExtra("UpdateCloneId", clone.getId());
        intent.putExtra("UpdateCloneNome", clone.getNome());
        intent.putExtra("UpdateCloneIdade", clone.getIdade());
        intent.putExtra("UpdateCloneAdicionais", clone.getAdicionais().toString());
        context.startActivity(intent);
    }

    public void deleteClone(String id, final int position) {
        firebaseFirestore.collection("clones")
                .document(id)
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        cloneList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, cloneList.size());
                        Toast.makeText(context, "O clone foi deletado!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}