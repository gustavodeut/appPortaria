package com.example.appportaria.FuncoesSistema;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.james.mime4j.codec.DecoderUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.location.Location;
import android.location.LocationManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.appportaria.R;
import com.example.appportaria.Objetos.InfFoto;

public class AdpFoto extends BaseAdapter{

	private Context context;
	private List<InfFoto> list;
	
	public AdpFoto(Context context, List<InfFoto> list) {
		super();
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		LayoutInflater inflater = (LayoutInflater)
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.listview_fotos, null);
        ImageView img = (ImageView) view.findViewById(R.id.img);
		try{
			Bitmap btmFoto = BitmapFactory.decodeFile(list.get(position).getNome());			
			img.setImageBitmap(btmFoto);
		}catch(Exception ex){}
        return view;
        
//		ImageView imgView = new ImageView(context);
//		try 
//		{
//			String nameFile = list.get(position).getNome();
//			Bitmap btmFoto = BitmapFactory.decodeFile(nameFile);
//			File arquivoFoto = new File(nameFile);
//			FileOutputStream stream = new FileOutputStream(arquivoFoto);
//
//			btmFoto.compress(CompressFormat.JPEG, Sistema.QUALIDADE_FOTO, stream);
//			stream.flush();
//			stream.close();
//			
//			imgView.setTag(list.get(position).getFullName());
//			imgView.setImageBitmap(btmFoto);
//	        imgView.setLayoutParams(new Gallery.LayoutParams(400, 400));
//	        imgView.setScaleType(ImageView.ScaleType.FIT_XY);
//	
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return imgView;
	}

}
