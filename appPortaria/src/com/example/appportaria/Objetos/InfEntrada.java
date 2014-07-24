package com.example.appportaria.Objetos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;

import com.example.appportaria.FuncoesSistema.Sistema;

public class InfEntrada {
	
	private int codigo;
	private Date data;
	private int km;
	private String vistoria;
	private String tipo;
	private String codigoVeiculo;
	private int codigoUsuario;
	private String codigoMotorista;
	private List<InfFuncionarioVisitante> listPassageiro;
	private List<InfDestino> listDestino;
	private InfFuncionarioVisitante infMotorista;
	private InfVeiculo infVeiculo;
	
	public InfEntrada(){
		this.Clear();
	}
	
	public InfEntrada(JSONObject json)
	{
		try
		{
			this.codigo = Integer.parseInt(json.getString("ENT_CODIGO"));
			this.data = Sistema.getData(json.getString("ENT_DATA"));
			this.km = Integer.parseInt(json.getString("ENT_KM"));
			this.vistoria = json.getString("ENT_VISTORIA");
			this.tipo = json.getString("ENT_TIPO");
			this.codigoVeiculo = json.getString("VEI_CODIGO");
			this.codigoUsuario = Integer.parseInt(json.getString("USU_CODIGO"));
			this.codigoMotorista = json.getString("FUV_CODIGO");
			this.listPassageiro = new ArrayList<InfFuncionarioVisitante>();
			this.listDestino = new ArrayList<InfDestino>();
			this.infMotorista = new InfFuncionarioVisitante(json);
			this.infVeiculo = new InfVeiculo(json);
			
			try{
				JSONArray jsonArray = new JSONArray(json.getString("PASSAGEIROS"));
				for (int i=0; i< jsonArray.length(); i++)
					listPassageiro.add(new InfFuncionarioVisitante(jsonArray.getJSONObject(i)));
			} catch(Exception ex) {}
			
			try{
				JSONArray jsonArray = new JSONArray(json.getString("DESTINOS"));
				for (int i=0; i< jsonArray.length(); i++)
					listDestino.add(new InfDestino(jsonArray.getJSONObject(i)));
			} catch(Exception ex) {}
		}
		catch(JSONException ex)
		{
			Sistema.ExibeMensagem("ERRO: " + ex.getMessage());	
		}
	}
	
	public InfEntrada(int codigo, Date data, int km, String vistoria,
			String tipo, String codigoVeiculo, int codigoUsuario,
			String codigoMotorista) {
		super();
		this.codigo = codigo;
		this.data = data;
		this.km = km;
		this.vistoria = vistoria;
		this.tipo = tipo;
		this.codigoVeiculo = codigoVeiculo;
		this.codigoUsuario = codigoUsuario;
		this.codigoMotorista = codigoMotorista;
	}
	
	public void Clear(){
		this.codigo = 0;
		this.data = new Date(0);
		this.km = 0;
		this.vistoria = "";
		this.tipo = "";
		this.codigoVeiculo = "";
		this.codigoUsuario = 0;
		this.codigoMotorista = "";
		this.listPassageiro = new ArrayList<InfFuncionarioVisitante>();
		this.listDestino = new ArrayList<InfDestino>();
		this.infMotorista = new InfFuncionarioVisitante();
		this.infVeiculo = new InfVeiculo();
	}
	
	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date date) {
		this.data = date;
	}

	public int getKm() {
		return km;
	}

	public void setKm(String km) {
		try{
			this.km = Integer.parseInt(km);
		}
		catch(Exception ex){
			this.km = 0;
		}
	}

	public String getVistoria() {
		return vistoria;
	}

	public void setVistoria(String vistoria) {
		this.vistoria = vistoria;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getCodigoVeiculo() {
		return codigoVeiculo;
	}

	public void setCodigoVeiculo(String codigoVeiculo) {
		this.codigoVeiculo = codigoVeiculo;
	}

	public int getCodigoUsuario() {
		return codigoUsuario;
	}

	public void setCodigoUsuario(int codigoUsuario) {
		this.codigoUsuario = codigoUsuario;
	}

	public String getCodigoMotorista() {
		return codigoMotorista;
	}

	public void setCodigoMotorista(String codigoMotorista) {
		this.codigoMotorista = codigoMotorista;
	}

	public InfFuncionarioVisitante getInfMotorista() {
		return infMotorista;
	}

	public void setInfMotorista(InfFuncionarioVisitante infMotorista) {
		this.infMotorista = infMotorista;
	}

	public InfVeiculo getInfVeiculo() {
		return infVeiculo;
	}

	public void setInfVeiculo(InfVeiculo infVeiculo) {
		this.infVeiculo = infVeiculo;
	}
	
	public List<InfFuncionarioVisitante> getListPassageiro() {
		return listPassageiro;
	}

	public void setListPassageiro(List<InfFuncionarioVisitante> listPassageiro) {
		this.listPassageiro = listPassageiro;
	}

	public List<InfDestino> getListDestino() {
		return listDestino;
	}

	public void setListDestino(List<InfDestino> listDestino) {
		this.listDestino = listDestino;
	}

}
