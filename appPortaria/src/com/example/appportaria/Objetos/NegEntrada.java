package com.example.appportaria.Objetos;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.os.AsyncTask;

import com.example.appportaria.FuncoesSistema.AsyncEnviarDados;
import com.example.appportaria.FuncoesSistema.AsyncReceberDados;
import com.example.appportaria.FuncoesSistema.AbsUtil;
import com.example.appportaria.FuncoesSistema.AbsTransferencia;
import com.example.appportaria.FuncoesSistema.Sistema;

public class NegEntrada {
	
	private InfEntrada infEntrada;
	private InfEntrada filEntrada;
	private List<InfEntrada> listConsulta;

	public NegEntrada(){
		this.infEntrada = new InfEntrada();
		this.filEntrada = new InfEntrada();
		this.listConsulta = new ArrayList<InfEntrada>();
	}

	public NegEntrada getThis(){
		return this;
	}
	
	public void Consultar(String tabela, Context context, final AbsUtil respCon, boolean downloadFoto){
		infEntrada.Clear();
		listConsulta.clear();
		
		AbsTransferencia absTransf = new AbsTransferencia() {
			@Override
			
			public void Sucess(String resposta) {
				
				try{
				
					listConsulta = new ArrayList<InfEntrada>();
					if (resposta.charAt(0) != '[')
						resposta = "[" + resposta + "]";
					JSONArray jsonArray = new JSONArray(resposta);
					if (jsonArray.length() >= 1)
						infEntrada = new InfEntrada(jsonArray.getJSONObject(0));
					for (int i=0; i< jsonArray.length(); i++)
					{
						listConsulta.add(new InfEntrada(jsonArray.getJSONObject(i)));
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
		infTra.setHasFile(downloadFoto);
		infTra.setTabela(tabela);
		infTra.setParametro((filEntrada.getCodigo() == 0 ? "" : Integer.toString(filEntrada.getCodigo())));
		AsyncTask<Void, Integer, Boolean> mOperacao = new AsyncReceberDados(infTra, absTransf, context);
	}
	
	public void CosultarUltimosRegistros(Context context, final AbsUtil respCon){
		Consultar("SPO_ENTRADA_PESQUISA_LISTA_SIMPLES", context, respCon, false);
	}
	
	public void CosultarPorCodigo(Context context, final AbsUtil respCon){
		Consultar("SPO_ENTRADA_PESQUISA", context, respCon, true);
	}
	
	public boolean Valida(){
		try{
			if (infEntrada.getCodigoMotorista().equals("") || infEntrada.getCodigoMotorista().equals("0"))
				throw new Exception("Informe o motorista!");
			if (infEntrada.getCodigoVeiculo().equals("") || infEntrada.getCodigoVeiculo().equals("0"))
				throw new Exception("Informe o veículo!");
			if (infEntrada.getListDestino().size() == 0)
				throw new Exception("Informe um destino!");
		}catch (Exception ex){
			Sistema.ExibeMensagem(ex.getMessage());
			return false;
		}
		return true;
	}
	
	public boolean Inserir(Context context, final AbsUtil respIns){
		if (!Valida())
			return false;
		
		try{
		
			AbsTransferencia absTransf = new AbsTransferencia() {
				@Override
				
				public void Sucess(String resposta) {				
					
					respIns.Executa(resposta.toUpperCase().contains("SUCESS"));
				}
	
				@Override
				public void Error(String resposta) {
					Sistema.ExibeMensagem(resposta);
					respIns.Executa(false);
				}
			};
			InfTransferecia infTra = new InfTransferecia();
			infTra.setTabela("SPO_ENTRADA_ADD");
			infTra.setjSon(getInfoJson());
			//infTra.setjSon("[{'ENT_CODIGO':0,'ENT_KM':13,'ENT_TIPO':'Entrada','ENT_VISTORIA':'tsste inserir','FUV_CODIGO':'65R','VEI_CODIGO':'67G','USU_CODIGO':1,'PASSAGEIROS':[{'FUV_CODIGO':'12'}], 'DESTINOS':[{'DES_CODIGO':6,'DES_SEQUENCIA': 1}]}]");
			AsyncTask<Void, Integer, Boolean> mOperacao = new AsyncEnviarDados(infTra, absTransf, context);
			
			return true;
		} catch(Exception ex){
			
		}
		return false;
	}
	
	public String getInfoJson(){
		String jsonEntrada = "", jsonPassageiro = "", jsonDestino = "";
		for (InfFuncionarioVisitante info : infEntrada.getListPassageiro()) {
			jsonPassageiro += (jsonPassageiro.equals("") ? "" : ",") + "{\"FUV_CODIGO\":\"" + info.getCodigo() + "\"}";
		}
		int i=1;
		for (InfDestino info : infEntrada.getListDestino()) {
			jsonDestino += (jsonDestino.equals("") ? "" : ",") + "{\"DES_CODIGO\":" + info.getCodigo() + ",\"DES_SEQUENCIA\": " + i + "}";
		}
		
		jsonEntrada = "[{"
				+ "\"ENT_CODIGO\":" + infEntrada.getCodigo()
				+ ",\"ENT_KM\":" + infEntrada.getKm()
				+ ",\"ENT_TIPO\":\"" + infEntrada.getTipo() + "\""
				+ ",\"ENT_VISTORIA\":\"" + infEntrada.getVistoria() + "\""
				+ ",\"FUV_CODIGO\":\"" + infEntrada.getCodigoMotorista() + "\""
				+ ",\"VEI_CODIGO\":\"" + infEntrada.getCodigoVeiculo() + "\""
				+ ",\"USU_CODIGO\":" + infEntrada.getCodigoUsuario()
				+ ",\"PASSAGEIROS\":[" + jsonPassageiro + "], "
				+ "\"DESTINOS\":[" + jsonDestino + "]"
				+ "}]";
		return jsonEntrada;
	}
	
	public InfEntrada getInfo() {
		return infEntrada;
	}

	public void setInfo(InfEntrada infEntrada) {
		this.infEntrada = infEntrada;
	}

	public InfEntrada getFiltro() {
		return filEntrada;
	}

	public void setFiltro(InfEntrada filEntrada) {
		this.filEntrada = filEntrada;
	}

	public List<InfEntrada> getList() {
		return listConsulta;
	}

	public void setList(List<InfEntrada> listConsulta) {
		this.listConsulta = listConsulta;
	}

	public List<String> getListDescricao(){
		return getListDescricao(listConsulta);
	}
	
	public List<String> getListDescricao(List<InfEntrada> list){
		List<String> listNome = new ArrayList<String>();
		for (InfEntrada info : list) {
			listNome.add(info.getData().toString());
		}
		return listNome;
	}
	
}
