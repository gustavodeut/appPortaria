package com.example.appportaria.Objetos;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.os.AsyncTask;

import com.example.appportaria.FuncoesSistema.AsyncReceberDados;
import com.example.appportaria.FuncoesSistema.AbsUtil;
import com.example.appportaria.FuncoesSistema.AbsTransferencia;
import com.example.appportaria.FuncoesSistema.Sistema;

public class NegUsuario {
	
	private InfUsuario infUsuario;
	private InfUsuario filUsuario;
	private List<InfUsuario> listConsulta;
	
	public NegUsuario(){
		infUsuario = new InfUsuario();
		filUsuario = new InfUsuario();
		listConsulta = new ArrayList<InfUsuario>();
	}
	
	public NegUsuario getThis(){
		return this;
	}
	
	public void Consultar(Context context, final AbsUtil respCon){
		infUsuario.Clear();
		listConsulta.clear();
		
		AbsTransferencia absTransf = new AbsTransferencia() {
			@Override
			
			public void Sucess(String resposta) {
				
				try{
				
					listConsulta = new ArrayList<InfUsuario>();
					JSONArray jsonArray = new JSONArray(resposta);
					if (jsonArray.length() >= 1)
						infUsuario = new InfUsuario(jsonArray.getJSONObject(0));
					for (int i=0; i< jsonArray.length(); i++)
					{
						listConsulta.add(new InfUsuario(jsonArray.getJSONObject(i)));
					}
				}
				catch(JSONException ex){
					Sistema.ExibeMensagem(ex.toString());
				}
				
				respCon.Executa(getThis());
			}

			@Override
			public void Error(String resposta) {
				Sistema.ExibeMensagem(resposta);
				respCon.Executa(null);
			}
		};
		InfTransferecia infTra = new InfTransferecia();
		infTra.setTabela("SPO_USUARIO");
		infTra.setParametro(filUsuario.getNome());
		AsyncTask<Void, Integer, Boolean> mOperacao = new AsyncReceberDados(infTra, absTransf, context);
	}

	public InfUsuario getInfo() {
		return infUsuario;
	}

	public void setInfo(InfUsuario infUsuario) {
		this.infUsuario = infUsuario;
	}

	public InfUsuario getFiltro() {
		return filUsuario;
	}

	public void setFiltro(InfUsuario filUsuario) {
		this.filUsuario = filUsuario;
	}

	public List<InfUsuario> getList() {
		return listConsulta;
	}

	public void setList(List<InfUsuario> listConsulta) {
		this.listConsulta = listConsulta;
	}
	
	public List<String> getListNome(){
		List<String> listNome = new ArrayList<String>();
		for (InfUsuario info : listConsulta) {
			listNome.add(info.getNome());
		}
		return listNome;
	}

}
