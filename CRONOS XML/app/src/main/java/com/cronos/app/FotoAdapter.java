package com.cronos.app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.card.MaterialCardView;
import java.util.List;

// adaptador de la lista de fotos
public class FotoAdapter extends RecyclerView.Adapter<FotoAdapter.FotoViewHolder> {

    public interface OnFotoClickListener {
        void onFotoSeleccionada(Foto foto);
        // mantener presionada una foto
        default void onFotoMantenida(Foto foto) { }
    }

    private final List<Foto> fotos;
    private final OnFotoClickListener listener;
    private int seleccionada = 0;

    public FotoAdapter(List<Foto> fotos, OnFotoClickListener listener) {
        this.fotos = fotos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_foto, parent, false);
        return new FotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FotoViewHolder holder, int position) {
        Foto foto = fotos.get(position);
        foto.cargarEn(holder.ivFotoMiniatura, 240);
        holder.tvFotoTitulo.setText(foto.getTitulo());

        // borde a la foto seleccionada
        holder.cardFotoItem.setStrokeWidth(position == seleccionada ? 4 : 0);

        holder.itemView.setOnLongClickListener(v -> {
            listener.onFotoMantenida(foto);
            return true;
        });

        holder.itemView.setOnClickListener(v -> {
            int anterior = seleccionada;
            seleccionada = holder.getBindingAdapterPosition();
            notifyItemChanged(anterior);
            notifyItemChanged(seleccionada);
            listener.onFotoSeleccionada(foto);
        });
    }

    @Override
    public int getItemCount() {
        return fotos.size();
    }

    static class FotoViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView cardFotoItem;
        ImageView ivFotoMiniatura;
        TextView tvFotoTitulo;

        FotoViewHolder(@NonNull View itemView) {
            super(itemView);
            cardFotoItem = itemView.findViewById(R.id.cardFotoItem);
            ivFotoMiniatura = itemView.findViewById(R.id.ivFotoMiniatura);
            tvFotoTitulo = itemView.findViewById(R.id.tvFotoTitulo);
        }
    }
}
