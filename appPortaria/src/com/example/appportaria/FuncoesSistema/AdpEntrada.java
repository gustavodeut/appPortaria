package com.example.appportaria.FuncoesSistema;

import java.util.List;

import com.example.appportaria.R;
import com.example.appportaria.Objetos.InfEntrada;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AdpEntrada extends BaseAdapter {
    private Context context;
    private List<InfEntrada> listEntrada;
    
    public AdpEntrada(Context context, List<InfEntrada> listEntrada){
        this.context = context;
        this.listEntrada = listEntrada;
    }
    
    @Override
    public int getCount() {
        return listEntrada.size();
    }
 
    @Override
    public Object getItem(int position) {
        return listEntrada.get(position);
    }
 
    @Override
    public long getItemId(int position) {
        return position;
    }
 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Recupera o estado da posição atual
        InfEntrada registro = listEntrada.get(position);
        
        LayoutInflater inflater = (LayoutInflater)
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.listview_registros, null);
        
        TextView tvRegistro = (TextView)view.findViewById(R.id.tvRegistro);
        tvRegistro.setText(registro.getTipo() + ": ");
        
        TextView tvData = (TextView)view.findViewById(R.id.tvData);
        tvData.setText(Sistema.formataData(registro.getData()));
        
        TextView tvMotorista = (TextView)view.findViewById(R.id.tvMotorista);
        tvMotorista.setText(registro.getInfMotorista().getNome());
        
        TextView tvVeiculo = (TextView)view.findViewById(R.id.tvVeiculo);
        tvVeiculo.setText(registro.getInfVeiculo().getDescricao());
        
        return view;
    }

}
