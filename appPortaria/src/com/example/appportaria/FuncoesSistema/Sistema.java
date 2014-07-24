package com.example.appportaria.FuncoesSistema;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.appportaria.ActPrincipal;
import com.example.appportaria.Objetos.InfUsuario;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.widget.Toast;

public class Sistema {
	
	public final static String URL = "http://cpro20212.publiccloud.com.br:8081/";//"http://cpro20212.publiccloud.com.br:8081/frmSincronizar.ashx?TABELA=%22SPO_ENTRADA%22";
	//public final static String URL = "http://192.168.1.109:6830/frmSincronizar.ashx";//"http://cpro20212.publiccloud.com.br:8081/frmSincronizar.ashx?TABELA=%22SPO_ENTRADA%22";
	
	public static InfUsuario infUsuario;
	public static Context contextoLogin;
	public static Intent itLogin;
	public static Intent itPrincipal;
	public static Intent itPesquisaFuncionarioVisitante;
	public static Intent itPesquisaVeiculo;
	public static Intent itPesquisaDestino;
	public static Intent itPesquisaEntrada;
	public static final int RequestCodeLogin = 10;
	public static final int RequestCodePrincipal = 20;
	public static final int RequestCodePesquisaFuncionarioVisitante = 30;
	public static final int RequestCodePesquisaVeiculo = 40;
	public static final int RequestCodePesquisaDestino = 50;
	public static final int RequestCodePesquisaEntrada = 60;
	
	public static final int REQUESTCODE_TIRARFOTO = 10002;
	public static final String DIR_TEMP = Environment.getExternalStorageDirectory() + File.separator + "Porteiro" + File.separator + "Temp" + File.separator;
	public static final int QUALIDADE_FOTO = 50;
	
	public static void ExibeMensagem(String msg)
	{
		if (msg != "")
		{
			Toast to = Toast.makeText(contextoLogin, msg, Toast.LENGTH_SHORT);
			to.setGravity(Gravity.CENTER_VERTICAL , 0, 0);
			to.show();
		}
	}

	public static Date getData(String data){
		try{
			return new Date(Date.parse(data));
		}catch (Exception ex){
			return new Date(0);
		}
	}

	public static String formataData(Date data){
		SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy  HH:mm");
		return f.format( data);
	}

	public static void TakePicture(String fullName, Activity act){
		File arquivo = new File(fullName);
		Uri imageUri = Uri.fromFile(arquivo);
		Intent itFoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		itFoto.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		
		act.startActivityForResult(itFoto, Sistema.REQUESTCODE_TIRARFOTO);
	}

	public static void IniciaFiles(){
		File rootTemp = new File(Sistema.DIR_TEMP);	  
	    
	    if (rootTemp.exists()) {	    	
			for(File f : rootTemp.listFiles()) {
				if(f.exists()) {
					boolean deleted = f.delete();
				}			
			}
	    } else {
	    	try {
	    		boolean criar = rootTemp.mkdirs();
	    	}
	    	catch (Exception ex) {
	    		Sistema.ExibeMensagem(ex.getMessage());
	    	}
	    }
	}

	public static boolean VerificaWifi(final Context context){
		try{
			final WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
			if (wifi.getWifiState() == wifi.WIFI_STATE_ENABLED)
				return true;
			if (wifi.getWifiState() == wifi.WIFI_STATE_DISABLED){
				AlertDialog.Builder j = new AlertDialog.Builder(context);
				j.setTitle("WIFI");
				j.setMessage("Deseja ligar o wifi?");
				j.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						wifi.setWifiEnabled(true);
						VerificaWifi(context);
					}
				});
				j.setNegativeButton("Não", null);
				j.show();
			}
		}
		catch(Exception ex){
			ExibeMensagem("ERRO: " + ex.toString());
		}
		return false;
	}

	public static Bitmap DecodeSampledBitmapFromFile(String nameFile, int reqWidth, int reqHeight) {

	    final BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    
	    BitmapFactory.decodeFile(nameFile, options);
	    
	    options.inSampleSize = 1;
	    if (options.outHeight > reqHeight || options.outWidth > reqWidth) {

	        final int heightRatio = Math.round((float) options.outHeight / (float) reqHeight);
	        final int widthRatio = Math.round((float) options.outWidth / (float) reqWidth);
	        options.inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
	    }

	    options.inJustDecodeBounds = false;
	    return BitmapFactory.decodeFile(nameFile, options);
	}
}
