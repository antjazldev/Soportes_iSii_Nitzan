package com.example.soportesnitzan;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecVAdapter extends RecyclerView.Adapter<ViewHolderSoporte> {
    private ArrayList<Soporte> soportes;
    private InterfazClickRecyclerView interfazClickRecyclerView;

    public RecVAdapter(ArrayList<Soporte> soportes) {
        this.soportes = soportes;
    }

    public ArrayList<Soporte> getSoportes() {
        return soportes;
    }

    public void setSoportes(ArrayList<Soporte> soportes) {
        this.soportes = soportes;
        this.notifyDataSetChanged();
    }

    public  RecVAdapter(InterfazClickRecyclerView interfazClickRecyclerView) {
        this.interfazClickRecyclerView = interfazClickRecyclerView;
        this.soportes = new ArrayList<>();
    }





    @NonNull
    @Override
    public ViewHolderSoporte onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Esta es la vista del layout que muestra los detalles de la mascota (fila_mascota.xml)
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardgrid, parent, false);
        // Crear el viewholder a partir de esta vista. Mira la clase ViewHolderMascota si quieres
        final ViewHolderSoporte viewHolder = new ViewHolderSoporte(vista);
        // En el click de la vista (la mascota en general) invocamos a nuestra interfaz personalizada pasándole la vista y la mascota
        vista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interfazClickRecyclerView.onClick(v, soportes.get(viewHolder.getAdapterPosition()));
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderSoporte holder, int position) {
        Soporte soporte = this.soportes.get(position);
        holder.getTextViewNombreSoporte().setText(soporte.getCategoria());
        holder.getTextViewDetalleSoporte().setText(soporte.getDetalle());
        holder.getTextViewFincaSoporte().setText(soporte.getFinca());
        holder.getTextViewFechaSoporte().setText(soporte.getFecha());
    }



    @Override
    public int getItemCount() {
        return this.soportes.size();
    }
}