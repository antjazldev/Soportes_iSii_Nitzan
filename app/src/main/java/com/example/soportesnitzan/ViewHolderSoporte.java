package com.example.soportesnitzan;


import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class ViewHolderSoporte extends RecyclerView.ViewHolder {
    private TextView textViewDetalleSoporte, textViewNombreSoporte,textViewFincaSoporte,textViewFechaSoporte;

    ViewHolderSoporte(@NonNull View itemView) {
        super(itemView);
        textViewNombreSoporte = itemView.findViewById(R.id.textNameSoporte);
        textViewDetalleSoporte = itemView.findViewById(R.id.textDetalleSoporte);
        textViewFincaSoporte = itemView.findViewById(R.id.textFincaSoporte);
        textViewFechaSoporte = itemView.findViewById(R.id.textFechaSoporte);
    }

    TextView getTextViewNombreSoporte() {
        return textViewNombreSoporte;
    }

    TextView getTextViewDetalleSoporte() {
        return textViewDetalleSoporte;
    }
    TextView getTextViewFincaSoporte() {
        return textViewFincaSoporte;
    }
    TextView getTextViewFechaSoporte() {
        return textViewFechaSoporte;
    }

}