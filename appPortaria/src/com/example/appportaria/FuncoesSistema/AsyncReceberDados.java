package com.example.appportaria.FuncoesSistema;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;

import com.example.appportaria.ActPrincipal;
import com.example.appportaria.Objetos.InfFoto;
import com.example.appportaria.Objetos.InfTransferecia;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;

public class AsyncReceberDados extends AsyncTask<Void, Integer, Boolean> {

    private ProgressDialog aviso;
    private InfTransferecia infTransferencia;
    private AbsTransferencia absResposta;
    private String strResposta;
    private Context context; 
    
	public AsyncReceberDados(InfTransferecia infTra, AbsTransferencia absResposta, Context context)
	{
		super();
		this.infTransferencia = infTra;
		this.absResposta = absResposta;
		this.context = context;
		
		if (Sistema.VerificaWifi(context))
    		super.execute();
	}

	@Override
    protected Boolean doInBackground(Void... params) {
		
        try 
        {
        	StringBuilder sbResposta = new StringBuilder();
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(infTransferencia.getUrl());
            HttpResponse resposta = httpClient.execute(httpGet);
            int status = resposta.getStatusLine().getStatusCode();
            if(status == HttpStatus.SC_OK) { 
                // Se conexão estiver OK (Status 200), então lê o conteúdo da página
                HttpEntity entidade = resposta.getEntity();
                InputStream conteudo = entidade.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(conteudo));

                String linha;
                
                while((linha = reader.readLine()) != null) {
                	sbResposta.append(linha);
                }
            }
            strResposta = sbResposta.toString();
            
            if (infTransferencia.getHasFile())
            {
	            //aqui faz o download das fotos do registro se existirem
	            try{
	            	String resAux = sbResposta.toString();
	            	if (resAux.charAt(0) != '[')
	        			resAux = "[" + resAux + "]";
	            	//JSONArray jsImages = new JSONArray(new JSONArray(resAux).getJSONObject(0).getString("IMAGENS"));
	            	
	            	//for(int i=0; i<jsImages.length(); i++)
	            	int i =0;
	            	{
	            		URL url = new URL("http://t0.gstatic.com/images?q=tbn:ANd9GcSipiqDZUYEKiv6fsnubAmgAfNvVkZbZ4vnh8l7o3HE6gra9DvkyA");
	            		//URL url = new URL(Sistema.URL + jsImages.getJSONObject(i).getString("NOME"));  
	    	            String nomeArquivoLocal = url.getPath();  
	    	            InputStream is = url.openStream();
	    	            String nomeFoto = Sistema.DIR_TEMP + "FOTO_" + (i + 1) + ".jpg";
	    	            FileOutputStream fos = new FileOutputStream(nomeFoto);
	    	            ActPrincipal.negFoto.getListFoto().add(new InfFoto(nomeFoto));
	    	      
	    	            int umByte = 0;  
	    	            while ((umByte = is.read()) != -1){  
	    	                fos.write(umByte);  
	    	            }  
	    	            is.close();  
	    	            fos.close();
	            	}
	            	
	            }catch(Exception e){ }
            }
            
            // retornar verdadeiro para indicar que a execução foi concluída com sucesso!
            return Boolean.valueOf(true);

        } catch (ClientProtocolException e) {
            e.printStackTrace();
            this.absResposta.Error("ERRO: " + e.toString());
        } catch (IOException e) {
            e.printStackTrace();
            this.absResposta.Error("ERRO: " + e.toString());
        }catch(Exception e){
        	e.printStackTrace();
        	aviso.dismiss();
        	this.absResposta.Error("ERRO: " + e.toString());
        }
        finally{
        }

        // retornar falso para indicar que houve algum problema na execução
        return Boolean.valueOf(false);
    }
    
    @Override
    protected void onPreExecute() {
        // TODO Auto-generated method stub
        super.onPreExecute();
        
        aviso = new ProgressDialog(this.context);
        aviso.setMessage("Aguarde, comunicando com servidor...");
        aviso.setIndeterminate(true);
        aviso.show();
    }
    
    @Override
    protected void onPostExecute(Boolean result) {
        // TODO Auto-generated method stub
        super.onPostExecute(result);
        
        aviso.dismiss();
        if(result)
        	this.absResposta.Sucess(strResposta);
        else
        	this.absResposta.Error("Ocorreu um erro inesperado!");
    }
}