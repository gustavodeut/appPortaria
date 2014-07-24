package com.example.appportaria.Objetos;

import java.util.ArrayList;
import java.util.List;

import com.example.appportaria.FuncoesSistema.Sistema;

public class NegFoto {
	
	private InfFoto info;
	private List<InfFoto> listFoto;

	public NegFoto() {
		super();
		Clear();
	}
	
	public NegFoto(InfFoto info, List<InfFoto> listFoto) {
		super();
		this.info = info;
		this.listFoto = listFoto;
	}
	
	public void Clear(){
		this.info = new InfFoto();
		this.listFoto = new ArrayList<InfFoto>();
	}

	public InfFoto getInfo() {
		return info;
	}
	
	public void setInfo(InfFoto info) {
		this.info = info;
	}
	
	public List<InfFoto> getListFoto() {
		return listFoto;
	}
	
	public void setListFoto(List<InfFoto> listFoto) {
		this.listFoto = listFoto;
	}
	
	public String getNewFullNameFoto(){
		return Sistema.DIR_TEMP + "FOTO_" + (listFoto.size() + 1) + ".jpg";
	}
}
