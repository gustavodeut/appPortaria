package com.example.appportaria.Objetos;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.appportaria.FuncoesSistema.Sistema;

public class InfUsuario {

	private int codigo;
	private String nome;
	private String email;
	private String senha;
	private String perfil;
	
	public InfUsuario() {
		super();
		Clear();
	}
	
	public InfUsuario(int codigo, String nome, String email, String senha,
			String perfil) {
		super();
		this.codigo = codigo;
		this.nome = nome;
		this.email = email;
		this.senha = senha;
		this.perfil = perfil;
	}
	
	public InfUsuario(JSONObject json)
	{
		try
		{
			this.codigo = Integer.parseInt(json.getString("USU_CODIGO"));
			this.nome = json.getString("USU_NOME");
			this.email = json.getString("USU_EMAIL");
			this.senha = json.getString("USU_SENHA");
			this.perfil = json.getString("USU_PERFIL");
		}
		catch(JSONException ex)
		{
			Sistema.ExibeMensagem("ERRO: " + ex.getMessage());	
		}
	}
	
	public void Clear(){
		this.codigo = 0;
		this.nome = "";
		this.email = "";
		this.senha = "";
		this.perfil = "";
	}
	
	public int getCodigo() {
		return codigo;
	}
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	public String getPerfil() {
		return perfil;
	}
	public void setPerfil(String perfil) {
		this.perfil = perfil;
	}
	
	
}
