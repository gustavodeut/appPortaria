package com.example.appportaria;

import barcode.IntentIntegrator;
import barcode.IntentResult;

import com.example.appportaria.FuncoesSistema.AbsUtil;
import com.example.appportaria.FuncoesSistema.Sistema;
import com.example.appportaria.Objetos.NegFuncionarioVisitante;
import com.example.appportaria.Objetos.NegVeiculo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class ActPesquisaVeiculo extends Activity {

	static EditText etPesquisa;
	static ImageButton bPesquisa, bBarCode;
	static ListView lvPesquisa;
	static protected ArrayAdapter adpPesquisa;
	static public NegVeiculo negVeiculo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_pesquisa_veiculo);
		setTitle("Pesquisa Veículo");
		
		etPesquisa = (EditText) findViewById(R.id.etPassageiro);
		bPesquisa = (ImageButton) findViewById(R.id.bPesquisa);
		lvPesquisa = (ListView) findViewById(R.id.lvPesquisa);
		bBarCode = (ImageButton) findViewById(R.id.bBarCode);

		etPesquisa.setText(getIntent().getStringExtra("filtro"));
		
		//quando abrir essa tela, a tela que chamou já vai ter feito um consulta, então carrega os dados da consulta
		View v  = getWindow().getDecorView().findViewById(android.R.id.content);
		adpPesquisa = new ArrayAdapter(v.getContext(), android.R.layout.simple_list_item_1, negVeiculo.getListDescricaoCracha());
		lvPesquisa.setAdapter(adpPesquisa);
		
		etPesquisa.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				boolean handled =  false ;
		        if  ( actionId ==  EditorInfo.IME_ACTION_SEARCH)  { 
		            bPesquisa.callOnClick();
		            handled =  true ; 
		        } 
		        return handled ;
			}
		});
		
		bPesquisa.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (etPesquisa.getText().toString().trim().equals(""))
					Sistema.ExibeMensagem("Informe um valor para pesquisar!");
				else
					Consulta();
			}
		});
		
		lvPesquisa.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				ActPrincipal.isVeiculo.ActionFire(negVeiculo.getListConsulta().get(position));
				getThis().finish();
				return false;
			}
			
		});
	
		bBarCode.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				IntentIntegrator scanIntegrator = new IntentIntegrator(getThis());
				//start scanning
				scanIntegrator.initiateScan();
			}
		});
	}

	public ActPesquisaVeiculo getThis(){
		return this;
	}
	
	public void Consulta(){
		negVeiculo.getFiltro().setDescricao(etPesquisa.getText().toString());
		negVeiculo.Consultar(this, new AbsUtil() {
			
			@Override
			public void Executa(Object obj) {
				if (obj == null)
					return;
				negVeiculo = (NegVeiculo) obj;
				
				View v  = getWindow().getDecorView().findViewById(android.R.id.content);
				
				adpPesquisa = new ArrayAdapter(v.getContext(), android.R.layout.simple_list_item_1, negVeiculo.getListDescricaoCracha());
				lvPesquisa.setAdapter(adpPesquisa);
				if (negVeiculo.getListConsulta().size() == 0)
					Sistema.ExibeMensagem("Não foi encontrado nenhum resultado!");
				else if (negVeiculo.getListConsulta().size() == 1)
				{
					ActPrincipal.isVeiculo.ActionFire(negVeiculo.getListConsulta().get(0));
					getThis().finish();
				}
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		//retrieve result of scanning - instantiate ZXing object
		IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
		//check we have a valid result
		if (scanningResult != null) {
			//get content from Intent Result
			String scanContent = scanningResult.getContents();
			//get format name of data scanned
			String scanFormat = scanningResult.getFormatName();
			//output to UI
			if (scanContent != null && !scanContent.equals(""))
			{
				etPesquisa.setText(scanContent);
				bPesquisa.callOnClick();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
