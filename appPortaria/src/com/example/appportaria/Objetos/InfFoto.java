package com.example.appportaria.Objetos;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.example.appportaria.FuncoesSistema.Sistema;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.widget.Gallery;
import android.widget.ImageView;

public class InfFoto {

	private String path;
	private String nome;
	//private Bitmap btmFoto;
	
//	public InfFoto(String nameFile, Context context){
//		try 
//		{
//			btmFoto = BitmapFactory.decodeFile(nameFile);
//
//			File arquivoFoto = new File(nameFile);
//			FileOutputStream stream;
//			stream = new FileOutputStream(arquivoFoto);
//		
//
//			btmFoto.compress(CompressFormat.JPEG, Sistema.QUALIDADE_FOTO, stream);
//			stream.flush();
//			stream.close();
//	
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	
	public InfFoto(String fullName){
		super();
		this.Clear();
		this.nome = fullName;
	}
	
	public InfFoto() {
		super();
		this.Clear();
	}
	
	public InfFoto(String path, String nome) {
		super();
		this.path = path;
		this.nome = nome;
	}
	
	public void Clear(){
		this.path = "";
		this.nome = "";
	}

	public String getFullName(){
		return path + java.io.File.separator + nome;
	}
	
	public String getPath() {
		return path;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
	
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
//	public Bitmap getBtmFoto() {
//		return btmFoto;
//	}
//
//	public void setBtmFoto(Bitmap btmFoto) {
//		this.btmFoto = btmFoto;
//	}


}
