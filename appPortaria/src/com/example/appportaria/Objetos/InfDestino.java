package com.example.appportaria.Objetos;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.appportaria.FuncoesSistema.Sistema;

public class InfDestino {
	
	private int codigo;
	private String descricao;
	private String tipo;
	
	public InfDestino(){
		super();
		this.Clear();
	}
	
	public InfDestino(int codigo, String descricao, String tipo) {
		super();
		this.codigo = codigo;
		this.descricao = descricao;
		this.tipo = tipo;
	}
	
	public InfDestino(JSONObject json)
	{
		try
		{
			this.codigo = Integer.parseInt(json.getString("DES_CODIGO"));
			this.descricao = json.getString("DES_DESCRICAO");
			this.tipo = json.getString("DES_TIPO");
		}
		catch(JSONException ex)
		{
			//Sistema.ExibeMensagem("ERRO: " + ex.getMessage());	
		}
	}
	
	public void Clear(){
		this.codigo = 0;
		this.descricao = "";
		this.tipo = "";
	}
	
	public int getCodigo() {
		return codigo;
	}
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	
}
