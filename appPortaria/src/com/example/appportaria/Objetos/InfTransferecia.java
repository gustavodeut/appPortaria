package com.example.appportaria.Objetos;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import com.example.appportaria.FuncoesSistema.Sistema;

public class InfTransferecia {
	
	private String tabela;
	private String parametro;
	private String parametro2;
	private String jSon;
	private boolean hasFile;
	
	public InfTransferecia() {
		super();
		this.Clear();
	}
	
	public void Clear(){
		this.tabela = "";
		this.parametro = "";
		this.parametro2 = "";
		this.jSon = "";
		this.hasFile = false;
	}
	
	public String getTabela() {
		return tabela;
	}

	public void setTabela(String tabela) {
		this.tabela = tabela;
	}

	public String getParametro() {
		return parametro;
	}

	public void setParametro(String parametro) {
		this.parametro = parametro;
	}
	
	public String getParametro2() {
		return parametro2;
	}

	public void setParametro2(String parametro) {
		this.parametro2 = parametro;
	}

	public String getjSon() {
		return jSon;
	}

	public void setjSon(String jSon) {
		this.jSon = jSon;
	}

	public void setHasFile(boolean b){
		this.hasFile = b;
	}
	
	public boolean getHasFile(){
		return this.hasFile;
	}
	
	public String getUrl()
	{
		String url = Sistema.URL + "frmSincronizar.ashx";
		
		try 
		{
	        String par = URLEncoder.encode(parametro, "UTF-8");
	        String par2 = URLEncoder.encode(parametro2, "UTF-8");
			String json = URLEncoder.encode(jSon, "UTF-8");
	        
	        url += "?TABELA=" + URLEncoder.encode(tabela, "UTF-8");
	      	url += (par == "" ? "" : "&PARAMETRO=" + par);
	      	url += (par2 == "" ? "" : "&PARAMETRO2=" + par2);
			url += (jSon == "" ? "" : "&SCRIPT=" + json);
			
        } catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		//this.Clear();
		
		return url;
	}

}