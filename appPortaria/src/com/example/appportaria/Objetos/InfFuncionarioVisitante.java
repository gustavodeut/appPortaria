package com.example.appportaria.Objetos;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.appportaria.FuncoesSistema.Sistema;

public class InfFuncionarioVisitante {
	
	private String codigo;
	private String nome;
	private String cracha;
	private String funcao;
	
	public InfFuncionarioVisitante() {
		super();
		this.Clear();
	}
	
	public InfFuncionarioVisitante(String codigo, String nome, String cracha, String funcao) {
		super();
		this.codigo = codigo;
		this.nome = nome;
		this.cracha = cracha;
		this.funcao = funcao;
	}
	
	public InfFuncionarioVisitante(JSONObject json)
	{
		try
		{
			this.codigo = json.getString("FUV_CODIGO");
			this.nome = json.getString("FUV_NOME");
			this.cracha = json.getString("FUV_CRACHA");
			this.funcao = json.getString("FUV_FUNCAO");
		}
		catch(JSONException ex)
		{	
			if (this.cracha == null) this.cracha = "";
			if (this.funcao == null) this.funcao = "";
		}
	}
	
	public void Clear(){
		this.codigo = "";
		this.nome = "";
		this.cracha = "";
		this.funcao = "";
	}
			
	
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getCracha() {
		return cracha;
	}
	public void setCracha(String cracha) {
		this.cracha = cracha;
	}
	public String getFuncao() {
		return funcao;
	}
	public void setFuncao(String funcao) {
		this.funcao = funcao;
	}
	
	
}
