package com.example.appportaria;

import java.io.File;

import com.example.appportaria.FuncoesSistema.AbsUtil;
import com.example.appportaria.FuncoesSistema.Sistema;
import com.example.appportaria.Objetos.InfUsuario;
import com.example.appportaria.Objetos.NegUsuario;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class ActLogin extends Activity {
	
	private Button bEntrar;
	private EditText etLogin;
	private EditText etSenha;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_login);
		setTitle("Login Portaria");
		
		Sistema.contextoLogin = this;
		Sistema.itLogin = new Intent(Sistema.contextoLogin, ActLogin.class);
		Sistema.itPrincipal = new Intent(Sistema.contextoLogin, ActPrincipal.class);
		Sistema.itPesquisaFuncionarioVisitante = new Intent(Sistema.contextoLogin, ActPesquisaFuncionarioVisitante.class);
		Sistema.itPesquisaVeiculo = new Intent(Sistema.contextoLogin, ActPesquisaVeiculo.class);
		Sistema.itPesquisaDestino = new Intent(Sistema.contextoLogin, ActPesquisaDestino.class);
		Sistema.itPesquisaEntrada = new Intent(Sistema.contextoLogin, ActPesquisaEntrada.class);
		
		etLogin = (EditText)findViewById(R.id.etLogin);
		etSenha = (EditText)findViewById(R.id.etSenha);
		bEntrar = (Button)findViewById(R.id.bEntrada);
		
//		etLogin.setText("mario");
//		etSenha.setText("mario123");
		
		InputMethodManager imm = (InputMethodManager) this.getSystemService(INPUT_METHOD_SERVICE);  
        imm.hideSoftInputFromWindow(etLogin.getWindowToken(), 0);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		
		etSenha.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				boolean handled =  false ;
		        if  ( actionId ==  EditorInfo.IME_ACTION_GO)  { 
		            bEntrar.callOnClick();
		            handled =  true ; 
		        } 
		        return handled ;
			}
		});
		
		bEntrar.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if (etLogin.getText().toString().equals(""))
				{
					Sistema.ExibeMensagem("Insira o nome de usuário");
				}
				else if (etSenha.getText().toString().equals(""))
				{
					Sistema.ExibeMensagem("Insira a senha");
				}
				else
				{
					Sistema.IniciaFiles();
					NegUsuario negUsu = new NegUsuario();
					negUsu.getFiltro().setNome(etLogin.getText().toString());
					negUsu.Consultar(v.getContext(), new AbsUtil() {
						
						@Override
						public void Executa(Object obj) {
							if (obj != null)
							{
								NegUsuario negUsu = (NegUsuario)obj;
								if (negUsu.getInfo().getNome().equals(""))
									Sistema.ExibeMensagem("Nome de usuário não existe!");
								else if (!negUsu.getInfo().getSenha().equals(etSenha.getText().toString().trim()))
									Sistema.ExibeMensagem("Senha incorreta!");
								else
								{
									Sistema.infUsuario = negUsu.getInfo();
									startActivityForResult(Sistema.itPrincipal, Sistema.RequestCodePrincipal);
								}
							}
						}
					});
					
				}
			}
		});
	}
}
