package com.example.appportaria.Objetos;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.protocol.ResponseServer;
import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.os.AsyncTask;

import com.example.appportaria.FuncoesSistema.AsyncReceberDados;
import com.example.appportaria.FuncoesSistema.AbsUtil;
import com.example.appportaria.FuncoesSistema.AbsTransferencia;
import com.example.appportaria.FuncoesSistema.Sistema;

public class NegFuncionarioVisitante {

	private InfFuncionarioVisitante infFuncionarioVisitante;
	private InfFuncionarioVisitante filFuncionarioVisitante;
	private List<InfFuncionarioVisitante> listConsulta;
	
	public NegFuncionarioVisitante(){
		infFuncionarioVisitante = new InfFuncionarioVisitante();
		filFuncionarioVisitante = new InfFuncionarioVisitante();
		listConsulta = new ArrayList<InfFuncionarioVisitante>();
	}
	
	public NegFuncionarioVisitante getThis(){
		return this;
	}
	
	public void Consultar(Context context, final AbsUtil respCon){
		infFuncionarioVisitante.Clear();
		listConsulta.clear();
		
		AbsTransferencia absTransf = new AbsTransferencia() {
			@Override
			
			public void Sucess(String resposta) {
				
				try{
				
					listConsulta = new ArrayList<InfFuncionarioVisitante>();
					JSONArray jsonArray = new JSONArray(resposta);
					if (jsonArray.length() >= 1)
						infFuncionarioVisitante = new InfFuncionarioVisitante(jsonArray.getJSONObject(0));
					for (int i=0; i< jsonArray.length(); i++)
					{
						listConsulta.add(new InfFuncionarioVisitante(jsonArray.getJSONObject(i)));
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
		infTra.setTabela("SPO_FUNCIONARIO_VISITANTE");
		
		if (!filFuncionarioVisitante.getNome().trim().equals(""))
			infTra.setParametro(filFuncionarioVisitante.getNome().trim());
		else if (!filFuncionarioVisitante.getCodigo().trim().equals(""))
			infTra.setParametro(filFuncionarioVisitante.getCodigo().trim());
		
		AsyncTask<Void, Integer, Boolean> mOperacao = new AsyncReceberDados(infTra, absTransf, context);
	}

	public InfFuncionarioVisitante getInfo(){
		return infFuncionarioVisitante;
	}
	
	public void setInfo(InfFuncionarioVisitante infMotorista){
		this.infFuncionarioVisitante = infMotorista;
	}
	
	public InfFuncionarioVisitante getFiltro(){
		return filFuncionarioVisitante;
	}
	
	public void serFiltro(InfFuncionarioVisitante filMotorista){
		this.filFuncionarioVisitante = filMotorista;
	}
	
	public List<InfFuncionarioVisitante> getListConsulta(){
		return listConsulta;
	}

	public List<String> getListNomeCracha(){
		return getListNomeCracha(listConsulta);
	}
	
	public List<String> getListNomeCracha(List<InfFuncionarioVisitante> list){
		List<String> listNome = new ArrayList<String>();
		for (InfFuncionarioVisitante info : list) {
			listNome.add(info.getNome() + " - " + info.getCracha());
		}
		return listNome;
	}

}
