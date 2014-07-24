package com.example.appportaria;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import barcode.IntentIntegrator;
import barcode.IntentResult;

import com.example.appportaria.FuncoesSistema.AbsItemSelecionado;
import com.example.appportaria.FuncoesSistema.AbsUtil;
import com.example.appportaria.FuncoesSistema.Sistema;
import com.example.appportaria.Objetos.InfDestino;
import com.example.appportaria.Objetos.InfEntrada;
import com.example.appportaria.Objetos.InfFoto;
import com.example.appportaria.Objetos.InfFuncionarioVisitante;
import com.example.appportaria.Objetos.InfVeiculo;
import com.example.appportaria.Objetos.NegDestino;
import com.example.appportaria.Objetos.NegEntrada;
import com.example.appportaria.Objetos.NegFuncionarioVisitante;
import com.example.appportaria.Objetos.NegVeiculo;
import com.example.appportaria.Objetos.NegFoto;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class ActPrincipal extends FragmentActivity {

	private SectionsPagerAdapter mSectionsPagerAdapter;	
	private ViewPager mViewPager;
	
	static protected ImageButton bPesquisaMotorista, bPesquisaVeiculo, bPesquisaPassageiro, bPesquisaDestino, bCamera, bBCMotorista, bBCVeiculo, bBCPassageiro, bBCDestino;
	static protected Button bEntrada, bSaida;
	static protected ListView lvPassageiros, lvDestinos;
	static protected EditText etMotorista, etVeiculo, etKm, etPassageiro, etDestino, etVistoria;
	static protected View viewActFoto;
	static protected GridLayout glFotos;
	
	static public String operacao = "Registrar";//Registrar-Alterar
	static public boolean entrada = true;
	static public InfEntrada infEntrada = new InfEntrada();
	static public NegFoto negFoto = new NegFoto();
	static public List<InfDestino> lisDestinoEntrada, lisDestinoSaida;
	static protected int selectedIndexMotorista, selectedIndexVeiculo;
	static String nomeFoto;
	static Activity thisActivity;
	static private int bBarCode;
	
	static public AbsItemSelecionado isMotorista, isVeiculo, isPassageiro, isDestino, isAlteracao;
	
	protected static final int PADDING_GALERIA_FOTOS = 5;
	private final int ok = 1000;
	private final int cancelar = 1001;
	private final int pesquisa = 1002;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_principal);
		setTitle("Portaria - " + Sistema.infUsuario.getNome());

		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
	
		thisActivity = this;
	    isAlteracao = new AbsItemSelecionado() {
			
			@Override
			public void ActionFire(Object obj) {
				operacao = "Alterar";
				InfEntrada infEnt = (InfEntrada) obj;
				
				ClearObjetosAndFields();
				
				NegEntrada negEntrada = new NegEntrada();
				negEntrada.getFiltro().setCodigo(infEnt.getCodigo());
				negEntrada.CosultarPorCodigo(thisActivity, new AbsUtil() {
					
					@Override
					public void Executa(Object obj) {
						if (obj == null)
							return;
						final NegEntrada negEntrada = (NegEntrada) obj;
						
						if (negEntrada.getList().size() == 0)
							Sistema.ExibeMensagem("Não foi encontrado nenhum resultado!");
						else
						{
							infEntrada = negEntrada.getInfo();
							entrada = infEntrada.getTipo().equals("Entrada");
							
							if (entrada)
								lisDestinoEntrada = infEntrada.getListDestino();
							else
								lisDestinoSaida = infEntrada.getListDestino();
							thisActivity.finish();
							startActivityForResult(Sistema.itPrincipal, Sistema.RequestCodePrincipal);
						}
					}
				});
			}
		};
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(Menu.NONE, ok, Menu.NONE, "  OK  ").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		menu.add(Menu.NONE, cancelar, Menu.NONE, "  Cancelar ").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		menu.add(Menu.NONE, pesquisa, Menu.NONE, "  Pesquisar  ").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case ok: {
			if (ValidaCamposObrigatorios())
			{
				AlertDialog.Builder j = new AlertDialog.Builder(ActPrincipal.this);
				j.setTitle((entrada ? "ENTRADA" : "SAÍDA"));
				j.setMessage("Deseja "+ operacao + " " + (entrada ? "ENTRADA" : "SAÍDA") + " ?");
				j.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						OK_Click();
					}
				});
				j.setNegativeButton("Não", null);
				j.show();
			}
			return true;
		}
		case cancelar: {
			AlertDialog.Builder j = new AlertDialog.Builder(ActPrincipal.this);
			j.setTitle("Cancelar");
			j.setMessage("Deseja cancelar " + (operacao.equals("Alterar") ? "Alteração" : "Registro" ) + " " + (entrada ? "ENTRADA" : "SAÍDA") + " ?");
			j.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					ClearObjetosAndFields();
					thisActivity.finish();
					startActivityForResult(Sistema.itPrincipal, Sistema.RequestCodePrincipal);
				}
			});
			j.setNegativeButton("Não", null);
			j.show();
			return true;
		}
		case pesquisa:{
			startActivityForResult(Sistema.itPesquisaEntrada, Sistema.RequestCodePesquisaEntrada);
			return true;
		}
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			Fragment fragment = new DummySectionFragment();
			Bundle args = new Bundle();
			args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 5;//quantidade telas
		}

		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase();
			case 1:
				return getString(R.string.title_section2).toUpperCase();
			case 2:
				return getString(R.string.title_section3).toUpperCase();
			case 3:
				return getString(R.string.title_section4).toUpperCase();
			case 4:
				return getString(R.string.title_section5).toUpperCase();
			default: return null;
			}
		}
	}

	public static class DummySectionFragment extends Fragment {
		
		public static final String ARG_SECTION_NUMBER = "section_number";

		public DummySectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			
			int secao = getArguments().getInt("section_number");
			View view = null;
			
			switch (secao) {
				case 1: {
					view = inflater.inflate(com.example.appportaria.R.layout.act_registro, null);
					IniciaTelaRegistro(view);
					break;
				}
				case 2: {
					view = inflater.inflate(com.example.appportaria.R.layout.act_passageiro, null);
					
					IniciaTelaPassageiros(view);
					break;
				}
				case 3: {
					view = inflater.inflate(com.example.appportaria.R.layout.act_destino, null);
					
					IniciaTelaDestinos(view);
					break;
				}
				case 4: {
					view = inflater.inflate(com.example.appportaria.R.layout.act_vistoria, null);
					IniciaTelaVistoria(view);
					break;
				}
				case 5: {
					view = inflater.inflate(com.example.appportaria.R.layout.act_foto, null);
					IniciaTelaFotos(view);
					break;
				}
			}
			
			return view;
		}
		
		public void IniciaTelaRegistro(View v){
			
			etMotorista = (EditText) v.findViewById(R.id.etMotorista);
			etVeiculo = (EditText) v.findViewById(R.id.etVeiculo);
			etKm = (EditText) v.findViewById(R.id.etKm);
			bEntrada = (Button) v.findViewById(R.id.bEntrada);
			bSaida = (Button) v.findViewById(R.id.bSaida);
			bPesquisaMotorista = (ImageButton) v.findViewById(R.id.bPesquisaMotorista);
			bPesquisaVeiculo = (ImageButton) v.findViewById(R.id.bPesquisaVeiculo);
			bBCMotorista = (ImageButton) v.findViewById(R.id.bBCMotorista);
			bBCVeiculo = (ImageButton) v.findViewById(R.id.bBCVeiculo);

			etMotorista.setFocusableInTouchMode(true);
			etMotorista.requestFocus();
			InputMethodManager imm = (InputMethodManager) thisActivity.getSystemService(INPUT_METHOD_SERVICE);  
	        imm.hideSoftInputFromWindow(etKm.getWindowToken(), 0);
	        thisActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
			
			bEntrada.setEnabled(entrada);
			bSaida.setEnabled(!entrada);
			etMotorista.setText((infEntrada.getInfMotorista().getCodigo().equals("") ? "" : infEntrada.getInfMotorista().getNome() + " - " + infEntrada.getInfMotorista().getCracha()));
			etVeiculo.setText((infEntrada.getInfVeiculo().getCodigo().equals("") ? "" : infEntrada.getInfVeiculo().getDescricao() + " - " + infEntrada.getInfVeiculo().getCracha()));
			etKm.setText((infEntrada.getKm() == 0 ? "" : Integer.toString(infEntrada.getKm())));
			
			etMotorista.setOnEditorActionListener(new OnEditorActionListener() {
				
				@Override
				public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
					boolean handled =  false ;
			        if  ( actionId ==  EditorInfo.IME_ACTION_SEARCH)  { 
			            bPesquisaMotorista.callOnClick();
			            handled =  true ; 
			        } 
			        return handled ;
				}
			});
			
			etVeiculo.setOnEditorActionListener(new OnEditorActionListener() {
				
				@Override
				public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
					boolean handled =  false ;
			        if  ( actionId ==  EditorInfo.IME_ACTION_SEARCH)  { 
			            bPesquisaVeiculo.callOnClick();
			            handled =  true ; 
			        } 
			        return handled ;
				}
			});
			
			etMotorista.setOnFocusChangeListener(new View.OnFocusChangeListener() {
				
				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					if (!hasFocus)
						ValidaCampos(v);
				}
			});
			
			etVeiculo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
				
				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					if (!hasFocus)
						ValidaCampos(v);
				}
			});
			
			bEntrada.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					entrada = false;
					bEntrada.setEnabled(false);
					bSaida.setEnabled(true);
				}
			});
			
			bSaida.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					entrada = true;
					bEntrada.setEnabled(true);
					bSaida.setEnabled(false);
				}
			});

			bPesquisaMotorista.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if (!etMotorista.getText().toString().trim().equals(""))
					{	
						ActPesquisaFuncionarioVisitante.negFunVis = new NegFuncionarioVisitante();
						ActPesquisaFuncionarioVisitante.negFunVis.getFiltro().setNome(etMotorista.getText().toString());
						ActPesquisaFuncionarioVisitante.negFunVis.Consultar(v.getContext(), new AbsUtil() {
							
							@Override
							public void Executa(Object obj) {
								if (obj == null)
									return;
								
								if (ActPesquisaFuncionarioVisitante.negFunVis.getListConsulta().size() == 1)
								{
									ActPrincipal.isMotorista.ActionFire(ActPesquisaFuncionarioVisitante.negFunVis.getListConsulta().get(0));
								}
								else
								{
									Sistema.itPesquisaFuncionarioVisitante.putExtra("consulta", "Motorista");
									Sistema.itPesquisaFuncionarioVisitante.putExtra("filtro", etMotorista.getText().toString());
									startActivityForResult(Sistema.itPesquisaFuncionarioVisitante, Sistema.RequestCodePesquisaFuncionarioVisitante);
								}
							}
						});
					}
					else
						Sistema.ExibeMensagem("Informe um valor para pesquisar!");
				}
			});
			
			bPesquisaVeiculo.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if (!etVeiculo.getText().toString().trim().equals(""))
					{	
						ActPesquisaVeiculo.negVeiculo = new NegVeiculo();
						ActPesquisaVeiculo.negVeiculo.getFiltro().setDescricao(etVeiculo.getText().toString());
						ActPesquisaVeiculo.negVeiculo.Consultar(v.getContext(), new AbsUtil() {
							
							@Override
							public void Executa(Object obj) {
								if (obj == null)
									return;
						
								if (ActPesquisaVeiculo.negVeiculo.getListConsulta().size() == 1)
								{
									ActPrincipal.isVeiculo.ActionFire(ActPesquisaVeiculo.negVeiculo.getListConsulta().get(0));
								}
								else
								{
									Sistema.itPesquisaVeiculo.putExtra("filtro", etVeiculo.getText().toString());
									startActivityForResult(Sistema.itPesquisaVeiculo, Sistema.RequestCodePesquisaVeiculo);
								}
							}
						});
					}
					else
						Sistema.ExibeMensagem("Informe um valor para pesquisar!");
				}
			});
		
			isMotorista = new AbsItemSelecionado() {
				
				@Override
				public void ActionFire(Object obj) {
					
					InfFuncionarioVisitante infFunVis = (InfFuncionarioVisitante) obj;
					boolean temPassageiro = false;
					for (InfFuncionarioVisitante info : infEntrada.getListPassageiro()) 
						if (info.getCracha().equals(infFunVis.getCracha()))
							temPassageiro = true;
					if (temPassageiro)
						Sistema.ExibeMensagem("Essa pessoa está selecionada como passageiro!");
					else
					{
						infEntrada.setInfMotorista(infFunVis);
						etMotorista.setText(infEntrada.getInfMotorista().getNome() + " - " + infEntrada.getInfMotorista().getCracha());
					}
				}
			};
		
			isVeiculo = new AbsItemSelecionado() {
				
				@Override
				public void ActionFire(Object obj) {
					infEntrada.setInfVeiculo((InfVeiculo) obj);
					etVeiculo.setText(infEntrada.getInfVeiculo().getDescricao() + " - " + infEntrada.getInfVeiculo().getCracha());
				}
			};
		
			bBCMotorista.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					bBarCode = R.id.bBCMotorista;
					IntentIntegrator scanIntegrator = new IntentIntegrator(thisActivity);
					//start scanning
					scanIntegrator.initiateScan();
					
				}
			});
		
			bBCVeiculo.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					bBarCode = R.id.bBCVeiculo;
					IntentIntegrator scanIntegrator = new IntentIntegrator(thisActivity);
					//start scanning
					scanIntegrator.initiateScan();
				}
			});
		}
		
		public void ValidaCampos(View v){
			int idView = v.getId();
			if (idView == etMotorista.getId())
			{
				if (!etMotorista.getText().toString().trim().equals(infEntrada.getInfMotorista().getNome() + " - " + infEntrada.getInfMotorista().getCracha()))
				{
					infEntrada.getInfMotorista().Clear();
					etMotorista.setText("");
				}
			}
			else if (idView == etVeiculo.getId()){
				if (!etVeiculo.getText().toString().trim().equals(infEntrada.getInfVeiculo().getDescricao() + " - " + infEntrada.getInfVeiculo().getCracha()))
				{
					infEntrada.getInfVeiculo().Clear();
					etVeiculo.setText("");
				}
			}
		}
		
		public void IniciaTelaPassageiros(final View v){
			etPassageiro = (EditText) v.findViewById(R.id.etPassageiro);
			lvPassageiros = (ListView) v.findViewById(R.id.lvPassageiros);
			bPesquisaPassageiro = (ImageButton) v.findViewById(R.id.bPesquisar);
			bBCPassageiro = (ImageButton) v.findViewById(R.id.bBCPassageiro);
			
			NegFuncionarioVisitante negFunVis = new NegFuncionarioVisitante();
			lvPassageiros.setAdapter(new ArrayAdapter(v.getContext(), android.R.layout.simple_list_item_1, negFunVis.getListNomeCracha(infEntrada.getListPassageiro())));
			
			etPassageiro.setOnEditorActionListener(new OnEditorActionListener() {
				
				@Override
				public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
					boolean handled =  false ;
			        if  ( actionId ==  EditorInfo.IME_ACTION_SEARCH)  { 
			            bPesquisaPassageiro.callOnClick();
			            handled =  true ; 
			        } 
			        return handled ;
				}
			});
			
			bPesquisaPassageiro.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					if (etPassageiro.getText().toString().trim().equals("")) {
						Sistema.ExibeMensagem("Informe o nome do passageiro!");
					}
					else
					{
						ActPesquisaFuncionarioVisitante.negFunVis = new NegFuncionarioVisitante();
						ActPesquisaFuncionarioVisitante.negFunVis.getFiltro().setNome(etPassageiro.getText().toString());
						ActPesquisaFuncionarioVisitante.negFunVis.Consultar(v.getContext(), new AbsUtil() {
							
							@Override
							public void Executa(Object obj) {
								if (obj == null)
									return;
								
								if (ActPesquisaFuncionarioVisitante.negFunVis.getListConsulta().size() == 1)
								{
									ActPrincipal.isPassageiro.ActionFire(ActPesquisaFuncionarioVisitante.negFunVis.getListConsulta().get(0));
								}
								else
								{
									Sistema.itPesquisaFuncionarioVisitante.putExtra("consulta", "Passageiro");
									Sistema.itPesquisaFuncionarioVisitante.putExtra("filtro", etPassageiro.getText().toString());
									startActivityForResult(Sistema.itPesquisaFuncionarioVisitante, Sistema.RequestCodePesquisaFuncionarioVisitante);
								}
							}
						});
					}
				}
			});
			
			isPassageiro = new AbsItemSelecionado() {
				
				@Override
				public void ActionFire(Object obj) {

					InfFuncionarioVisitante infFunVis = (InfFuncionarioVisitante) obj;
					boolean temPassageiro = false;
					for (InfFuncionarioVisitante info : infEntrada.getListPassageiro()) 
						if (info.getCracha().equals(infFunVis.getCracha()))
							temPassageiro = true;
					
					if (temPassageiro)
						Sistema.ExibeMensagem("Esse passageiro já está na lista!");
					else if (infFunVis.getCracha().equals(infEntrada.getInfMotorista().getCracha()))
						Sistema.ExibeMensagem("Essa pessoa está selecionada como motorista!");
					else
						infEntrada.getListPassageiro().add(infFunVis);
					
					etPassageiro.setText("");
					NegFuncionarioVisitante negFunVis = new NegFuncionarioVisitante();
					lvPassageiros.setAdapter(new ArrayAdapter(v.getContext(), android.R.layout.simple_list_item_1, negFunVis.getListNomeCracha(infEntrada.getListPassageiro())));
				}
			};
		
			lvPassageiros.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
				
				@Override
				public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					
					infEntrada.getListPassageiro().remove(arg2);
					NegFuncionarioVisitante negFunVis = new NegFuncionarioVisitante();
					lvPassageiros.setAdapter(new ArrayAdapter(v.getContext(), android.R.layout.simple_list_item_1, negFunVis.getListNomeCracha(infEntrada.getListPassageiro())));
					return false;
				}
			});
		
			bBCPassageiro.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					bBarCode = R.id.bBCPassageiro;
					IntentIntegrator scanIntegrator = new IntentIntegrator(thisActivity);
					//start scanning
					scanIntegrator.initiateScan();
					
				}
			});
		}
	
		public void IniciaTelaDestinos(final View v){
			etDestino = (EditText) v.findViewById(R.id.etDestino);
			lvDestinos = (ListView) v.findViewById(R.id.lvDestinos);
			bPesquisaDestino = (ImageButton) v.findViewById(R.id.bPesquisa);
			bBCDestino = (ImageButton) v.findViewById(R.id.bBCDestino);
			
			NegDestino negDes = new NegDestino();
			if (entrada && lisDestinoEntrada != null && lisDestinoEntrada.size() > 0)
			{
				lvDestinos.setAdapter(new ArrayAdapter(v.getContext(), android.R.layout.simple_list_item_1, negDes.getListDescricao(lisDestinoEntrada)));
			}
			else if (!entrada && lisDestinoSaida != null && lisDestinoSaida.size() > 0)
			{
				lvDestinos.setAdapter(new ArrayAdapter(v.getContext(), android.R.layout.simple_list_item_1, negDes.getListDescricao(lisDestinoSaida)));
			}
			
			etDestino.setOnEditorActionListener(new OnEditorActionListener() {
				
				@Override
				public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
					boolean handled =  false ;
			        if  ( actionId ==  EditorInfo.IME_ACTION_SEARCH)  { 
			            bPesquisaDestino.callOnClick();
			            handled =  true ; 
			        } 
			        return handled ;
				}
			});
			
			bPesquisaDestino.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					if (etDestino.getText().toString().trim().equals("")) {
						Sistema.ExibeMensagem("Informe o nome do destino!");
					}
					else 
					{	
						ActPesquisaDestino.negDestino = new NegDestino();
						ActPesquisaDestino.negDestino.getFiltro().setDescricao(etDestino.getText().toString());
						ActPesquisaDestino.negDestino.getFiltro().setTipo((entrada ? "INTERNO" : "EXTERNO"));
						ActPesquisaDestino.negDestino.Consultar(v.getContext(), new AbsUtil() {
							
							@Override
							public void Executa(Object obj) {
								if (obj == null)
									return;
								
								if (ActPesquisaDestino.negDestino.getListConsulta().size() == 1)
								{
									ActPrincipal.isDestino.ActionFire(ActPesquisaDestino.negDestino.getListConsulta().get(0));
								}
								else
								{
									Sistema.itPesquisaDestino.putExtra("filtro", etDestino.getText().toString());
									startActivityForResult(Sistema.itPesquisaDestino, Sistema.RequestCodePesquisaDestino);
								}
							}
						});
					}
				}
			});
		
			isDestino = new AbsItemSelecionado() {
				
				@Override
				public void ActionFire(Object obj) {
					if (lisDestinoEntrada == null)
						lisDestinoEntrada = new ArrayList<InfDestino>();
					if (lisDestinoSaida == null)
						lisDestinoSaida = new ArrayList<InfDestino>();
					
					InfDestino infDes = (InfDestino) obj;
					boolean temDestino = false;
					
					if (((InfDestino) obj).getTipo().toUpperCase().equals("INTERNO"))
					{	
						for (InfDestino info : lisDestinoEntrada)
							if (info.getCodigo() == infDes.getCodigo())
								temDestino = true;
						if (!temDestino)
							lisDestinoEntrada.add(infDes);
					}
					else
					{
						for (InfDestino info : lisDestinoSaida)
							if (info.getCodigo() == infDes.getCodigo())
								temDestino = true;
						if (!temDestino)
							lisDestinoSaida.add(infDes);
					}
					if (temDestino)
						Sistema.ExibeMensagem("Esse destino já está na lista!");
					
					etDestino.setText("");
					NegDestino negDes = new NegDestino();
					lvDestinos.setAdapter(new ArrayAdapter(v.getContext(), android.R.layout.simple_list_item_1, negDes.getListDescricao((entrada ? lisDestinoEntrada : lisDestinoSaida))));
				}
			};

			lvDestinos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
				
				@Override
				public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					
					NegDestino negDes = new NegDestino();
					if (entrada)
					{
						lisDestinoEntrada.remove(arg2);
						lvDestinos.setAdapter(new ArrayAdapter(v.getContext(), android.R.layout.simple_list_item_1, negDes.getListDescricao(lisDestinoEntrada)));
					}
					else
					{
						lisDestinoSaida.remove(arg2);
						lvDestinos.setAdapter(new ArrayAdapter(v.getContext(), android.R.layout.simple_list_item_1, negDes.getListDescricao(lisDestinoSaida)));
					}
					
					return false;
				}
			});
		
			bBCDestino.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					bBarCode = R.id.bBCDestino;
					IntentIntegrator scanIntegrator = new IntentIntegrator(thisActivity);
					//start scanning
					scanIntegrator.initiateScan();
				}
			});
		}

		public void IniciaTelaVistoria(View v){
			etVistoria = (EditText) v.findViewById(R.id.etVistoria);
			etVistoria.setText(infEntrada.getVistoria());
		}
		
		public void IniciaTelaFotos(View v){
			viewActFoto = v;
			glFotos = (GridLayout) v.findViewById(R.id.glFotos);
			bCamera = (ImageButton) v.findViewById(R.id.bCamera);
			bCamera.setOnClickListener(new View.OnClickListener() {				
				@Override
				public void onClick(View v) {
					nomeFoto = negFoto.getNewFullNameFoto();
					Sistema.TakePicture(nomeFoto, getActivity());
				}
			});
			
			for (InfFoto info : negFoto.getListFoto()) {
			
				final ImageView imgFoto = new ImageView(v.getContext());
				try {
					Bitmap photo = BitmapFactory.decodeFile(info.getNome());
		            imgFoto.setTag(info.getNome());
		            		            
		            imgFoto.setPadding(PADDING_GALERIA_FOTOS, PADDING_GALERIA_FOTOS, PADDING_GALERIA_FOTOS, PADDING_GALERIA_FOTOS);
					imgFoto.setScaleType(ImageView.ScaleType.FIT_CENTER);
					imgFoto.setAdjustViewBounds(true);
		            
		            imgFoto.setImageBitmap(photo);
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
					            
	            imgFoto.setOnLongClickListener(new View.OnLongClickListener() {
					
					@Override
					public boolean onLongClick(View v) {
						// TODO Auto-generated method stub
						
						AlertDialog.Builder j = new AlertDialog.Builder(v.getContext());
						j.setTitle("Confirmação?");
						j.setMessage("Deseja excluir esta foto?");
						j.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								glFotos.removeView(imgFoto);
								String fileName = imgFoto.getTag().toString();
								int pos=0;
								while(pos <= negFoto.getListFoto().size() && !negFoto.getListFoto().get(pos).getNome().equals(fileName))
									pos++;
								File f = new File(negFoto.getListFoto().get(pos).getNome());
								if (f.exists())
									f.delete();
								negFoto.getListFoto().remove(pos);
							}
						});
						
						j.setNegativeButton("Não", null);
						
						j.show();
						
						return true;
					}
				});
	            
	            glFotos.addView(imgFoto);
			}
		}
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if ((requestCode == Sistema.REQUESTCODE_TIRARFOTO) && (resultCode == Activity.RESULT_OK)) 
		{
			negFoto.getListFoto().add(new InfFoto(nomeFoto));
			try {
				
				Bitmap photo = Sistema.DecodeSampledBitmapFromFile(nomeFoto, 500, 500);
				
				File arquivoFoto = new File(nomeFoto);
				FileOutputStream stream = new FileOutputStream(arquivoFoto);
				
				photo.compress(CompressFormat.JPEG, Sistema.QUALIDADE_FOTO, stream);
	            stream.flush();
	            stream.close();
	            
	            final ImageView imgFoto = new ImageView(thisActivity);
	            //imgFoto.setTag(negFoto.getListFoto().size() - 1);//aqui guardo em qual posição da lista a imagem está
	            imgFoto.setTag(nomeFoto);
	            imgFoto.setPadding(PADDING_GALERIA_FOTOS, PADDING_GALERIA_FOTOS, PADDING_GALERIA_FOTOS, PADDING_GALERIA_FOTOS);
				imgFoto.setScaleType(ImageView.ScaleType.FIT_CENTER);
				imgFoto.setAdjustViewBounds(true);
	            imgFoto.setImageBitmap(photo);
				            
	            imgFoto.setOnLongClickListener(new View.OnLongClickListener() {
					
					@Override
					public boolean onLongClick(View v) {
						// TODO Auto-generated method stub
						
						AlertDialog.Builder j = new AlertDialog.Builder(ActPrincipal.this);
						j.setTitle("Confirmação?");
						j.setMessage("Deseja excluir esta foto?");
						j.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								glFotos.removeView(imgFoto);
								String fileName = imgFoto.getTag().toString();
								int pos=0;
								while(pos <= negFoto.getListFoto().size() && !negFoto.getListFoto().get(pos).getNome().equals(fileName))
									pos++;
								File f = new File(negFoto.getListFoto().get(pos).getNome());
								if (f.exists())
									f.delete();
								negFoto.getListFoto().remove(pos);
							}
						});
						
						j.setNegativeButton("Não", null);
						
						j.show();
						
						return true;
					}
				});
	            
	            glFotos.addView(imgFoto);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}catch(OutOfMemoryError e){
				e.printStackTrace();
				Sistema.ExibeMensagem("ERRO: " + e.toString());
			}catch(Exception e){
				e.printStackTrace();
				Sistema.ExibeMensagem("ERRO: " + e.toString());
			}
		}
		else
		{
			IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
			if (scanningResult != null) {
				String scanContent = scanningResult.getContents();
				if (scanContent != null && !scanContent.equals(""))
				{
					if(bBarCode == R.id.bBCMotorista)
					{					
						etMotorista.setText(scanContent);
						bPesquisaMotorista.callOnClick();
					}
					else if(bBarCode == R.id.bBCVeiculo){
						etVeiculo.setText(scanContent);
						bPesquisaVeiculo.callOnClick();
					}
					else if(bBarCode == R.id.bBCPassageiro){
						etPassageiro.setText(scanContent);
						bPesquisaPassageiro.callOnClick();
					}
					else if(bBarCode == R.id.bBCDestino){
						etDestino.setText(scanContent);
						bPesquisaDestino.callOnClick();
					}
				}
			}
		}
		
		super.onActivityResult(requestCode, resultCode, data);
   }
	
	private boolean ValidaCamposObrigatorios(){
		if ((etMotorista != null && etMotorista.getText().toString().trim().equals("")) || infEntrada.getInfMotorista().getCracha().equals("")){
			Sistema.ExibeMensagem("Selecione um motorista!");
			return false;
		}
		if ((etVeiculo != null && etVeiculo.getText().toString().trim().equals("")) || infEntrada.getInfVeiculo().getCracha().trim().equals("")){
			Sistema.ExibeMensagem("Selecione um veículo!");
			return false;
		}
		if ((entrada && (lisDestinoEntrada == null || lisDestinoEntrada.size() == 0)) || (!entrada && (lisDestinoSaida == null || lisDestinoSaida.size() == 0))){
			Sistema.ExibeMensagem("Selecione um destino!");
			return false;
		}
		return true;
	}

	private void ClearObjetosAndFields(){
		entrada = true;
		operacao = "Registrar";
		infEntrada.Clear();
		lisDestinoEntrada = new ArrayList<InfDestino>();
		lisDestinoSaida = new ArrayList<InfDestino>();
		negFoto = new NegFoto();
		Sistema.IniciaFiles();
	}

	private void OK_Click(){
		if(infEntrada.getCodigo() == 0)
			infEntrada.setData(new Date());
		infEntrada.setCodigoMotorista(infEntrada.getInfMotorista().getCodigo());
		infEntrada.setCodigoVeiculo(infEntrada.getInfVeiculo().getCodigo());
		infEntrada.setKm(etKm.getText().toString());
		if(etVistoria != null) infEntrada.setVistoria(etVistoria.getText().toString());
		infEntrada.setTipo((entrada ? "Entrada" : "Saida"));
		infEntrada.setCodigoUsuario(Sistema.infUsuario.getCodigo());
		infEntrada.setListDestino((entrada ? lisDestinoEntrada : lisDestinoSaida));
		
		NegEntrada negEnt = new NegEntrada();
		negEnt.setInfo(infEntrada);
		negEnt.Inserir(this, new AbsUtil() {
			
			@Override
			public void Executa(Object obj) {
				
				if (obj.toString().equalsIgnoreCase("true"))
				{
					
					Sistema.ExibeMensagem((operacao.equals("Alterar") ? "Alterado" : "Inserido" ) + " com sucesso.");
					ClearObjetosAndFields();
					thisActivity.finish();
					startActivityForResult(Sistema.itPrincipal, Sistema.RequestCodePrincipal);
				}
				else Sistema.ExibeMensagem("Erro ao " + (operacao.equals("Alterar") ? "Alterar" : "Inserir" ) + "!");
			}
		});
	}
}
