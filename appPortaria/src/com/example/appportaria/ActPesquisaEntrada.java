package com.example.appportaria;

import com.example.appportaria.FuncoesSistema.AbsUtil;
import com.example.appportaria.FuncoesSistema.AdpEntrada;
import com.example.appportaria.FuncoesSistema.Sistema;
import com.example.appportaria.Objetos.NegEntrada;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class ActPesquisaEntrada extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("Últimos registros Entrada/Saída");

		NegEntrada negEntrada = new NegEntrada();
		negEntrada.CosultarUltimosRegistros(this, new AbsUtil() {
			
			@Override
			public void Executa(Object obj) {
				if (obj == null)
					return;
				final NegEntrada negEntrada = (NegEntrada) obj;
				
				if (negEntrada.getList().size() == 0)
					Sistema.ExibeMensagem("Não foi encontrado nenhum resultado!");
				else
				{
					ListView lv = getThis().getListView();
					AdpEntrada adpEntrada = new AdpEntrada(getThis(), negEntrada.getList());
					lv.setAdapter(adpEntrada);
					
					lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
						
						@Override
						public boolean onItemLongClick(AdapterView<?> parent, View view,
								int position, long id) {
						
							ActPrincipal.isAlteracao.ActionFire(negEntrada.getList().get(position));
							
							getThis().finish();
							return false;
						}
						
					});
				}
			}
		});
	}

	public ActPesquisaEntrada getThis(){
		return this;
	}
}
