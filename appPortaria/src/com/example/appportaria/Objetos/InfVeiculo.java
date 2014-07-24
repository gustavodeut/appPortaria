package com.example.appportaria.Objetos;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.appportaria.FuncoesSistema.Sistema;

public class InfVeiculo {
	
	private String codigo;
	private String descricao;
	private String placa;
	private String cracha;
	
	
	public InfVeiculo() {
		super();
		this.Clear();
	}
	
	public InfVeiculo(String codigo, String descricao, String placa, String cracha) {
		super();
		this.codigo = codigo;
		this.descricao = descricao;
		this.placa = placa;
		this.cracha = cracha;
	}
	
	public InfVeiculo(JSONObject json)
	{
		try
		{
			this.codigo = json.getString("VEI_CODIGO");
			this.descricao = json.getString("VEI_DESCRICAO");
			this.cracha = json.getString("VEI_CRACHA");
			this.placa = json.getString("VEI_PLACA");
		}
		catch(JSONException ex)
		{
			if (this.cracha == null) this.cracha = "";
			if (this.placa == null) this.placa = "";
		}
	}
	
	public void Clear(){
		this.codigo = "";
		this.descricao = "";
		this.placa = "";
		this.cracha = "";
	}
	
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public String getPlaca() {
		return placa;
	}
	public void setPlaca(String placa) {
		this.placa = placa;
	}
	public String getCracha() {
		return cracha;
	}
	public void setCracha(String cracha) {
		this.cracha = cracha;
	}
	
	

}
