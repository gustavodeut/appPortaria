package com.example.appportaria.FuncoesSistema;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.CursorJoiner.Result;
import android.os.AsyncTask;

import com.example.appportaria.Objetos.InfTransferecia;

public class AsyncEnviarDados extends AsyncTask<Void, Integer, Boolean> {

    private ProgressDialog aviso;
    private InfTransferecia infTransferencia;
    private AbsTransferencia absResposta;
    private String strResposta;
    private Context context; 

    public AsyncEnviarDados(InfTransferecia infTra, AbsTransferencia absResposta, Context context)
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
            HttpPost httpPost = new HttpPost(infTransferencia.getUrl());
            MultipartEntity reqEntity = new MultipartEntity();
			File rootSincronizar = new File(Sistema.DIR_TEMP);
			
			if (rootSincronizar.exists()) {
				for (File f : rootSincronizar.listFiles()) {
				
					String nomeParametroFoto = f.getName();
					File imagem = new File(f.getPath());
					FileBody binImagem = new FileBody(imagem);
					reqEntity.addPart(nomeParametroFoto, binImagem);
				}
			}
			
			httpPost.setEntity(reqEntity);
            
            HttpResponse resposta = httpClient.execute(httpPost);
            int status = resposta.getStatusLine().getStatusCode();
            if(status == HttpStatus.SC_OK) {// Se conexão estiver OK (Status 200), então lê o conteúdo da página 
                HttpEntity entidade = resposta.getEntity();
                InputStream conteudo = entidade.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(conteudo));
                String linha;
                while((linha = reader.readLine()) != null)
                	sbResposta.append(linha);
            }
            strResposta = sbResposta.toString();
            
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