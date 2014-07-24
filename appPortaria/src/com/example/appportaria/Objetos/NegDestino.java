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

public class NegDestino {
	private InfDestino infDestino;
	private InfDestino filDestino;
	private List<InfDestino> listConsulta;
	
	public NegDestino(){
		this.infDestino = new InfDestino();
		this.filDestino = new InfDestino();
		listConsulta = new ArrayList<InfDestino>();
	}

	public NegDestino getThis(){
		return this;
	}
	
	public void Consultar(Context context, final AbsUtil respCon){
		infDestino.Clear();
		listConsulta.clear();
		
		AbsTransferencia absTransf = new AbsTransferencia() {
			@Override
			
			public void Sucess(String resposta) {
				
				try{
				
					listConsulta = new ArrayList<InfDestino>();
					JSONArray jsonArray = new JSONArray(resposta);
					if (jsonArray.length() >= 1)
						infDestino = new InfDestino(jsonArray.getJSONObject(0));
					for (int i=0; i< jsonArray.length(); i++)
					{
						listConsulta.add(new InfDestino(jsonArray.getJSONObject(i)));
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
		infTra.setTabela("SPO_DESTINO");
		infTra.setParametro(filDestino.getDescricao());
		infTra.setParametro2(filDestino.getTipo());
		AsyncTask<Void, Integer, Boolean> mOperacao = new AsyncReceberDados(infTra, absTransf, context);
	}
	
	public InfDestino getInfo() {
		return infDestino;
	}

	public void setInfo(InfDestino infDestino) {
		this.infDestino = infDestino;
	}
	
	public InfDestino getFiltro() {
		return filDestino;
	}
	
	public void setFiltro(InfDestino filDestino) {
		this.filDestino = filDestino;
	}
	
	public List<InfDestino> getListConsulta() {
		return listConsulta;
	}
	
	public void setListConsulta(List<InfDestino> listConsulta) {
		this.listConsulta = listConsulta;
	}

	public List<String> getListDescricao(){
		return getListDescricao(listConsulta);
	}
	
	public List<String> getListDescricao(List<InfDestino> list){
		List<String> listNome = new ArrayList<String>();
		for (InfDestino info : list) {
			listNome.add(info.getDescricao());
		}
		return listNome;
	}

}
