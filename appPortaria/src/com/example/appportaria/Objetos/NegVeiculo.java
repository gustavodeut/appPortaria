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

public class NegVeiculo {
	
	private InfVeiculo infVeiculo;
	private InfVeiculo filVeiculo;
	private List<InfVeiculo> listConsulta;
	
	public NegVeiculo(){
		infVeiculo = new InfVeiculo();
		filVeiculo = new InfVeiculo();
		listConsulta = new ArrayList<InfVeiculo>();
	}
	
	public NegVeiculo getThis(){
		return this;
	}
	
	public void Consultar(Context context, final AbsUtil respCon){
		infVeiculo.Clear();
		listConsulta = new ArrayList<InfVeiculo>();
		
		AbsTransferencia absTransf = new AbsTransferencia() {
			@Override
			
			public void Sucess(String resposta) {
				
				try{	
					JSONArray jsonArray = new JSONArray(resposta);
					if (jsonArray.length() >= 1)
						infVeiculo = new InfVeiculo(jsonArray.getJSONObject(0));
					for (int i=0; i< jsonArray.length(); i++)
					{
						listConsulta.add(new InfVeiculo(jsonArray.getJSONObject(i)));
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
		infTra.setTabela("SPO_VEICULO");
		infTra.setParametro(filVeiculo.getDescricao());
		AsyncTask<Void, Integer, Boolean> mOperacao = new AsyncReceberDados(infTra, absTransf, context);
	}
	
	public InfVeiculo getInfo() {
		return infVeiculo;
	}

	public void setInfo(InfVeiculo infVeiculo) {
		this.infVeiculo = infVeiculo;
	}

	public InfVeiculo getFiltro() {
		return filVeiculo;
	}

	public void setFiltro(InfVeiculo filVeiculo) {
		this.filVeiculo = filVeiculo;
	}

	public List<InfVeiculo> getListConsulta() {
		return listConsulta;
	}

	public void setListConsulta(List<InfVeiculo> listConsulta) {
		this.listConsulta = listConsulta;
	}

	public List<String> getListDescricaoCracha(){
		List<String> listNome = new ArrayList<String>();
		for (InfVeiculo info : listConsulta) {
			listNome.add(info.getDescricao() + " - " + info.getCracha());
		}
		return listNome;
	}

}
