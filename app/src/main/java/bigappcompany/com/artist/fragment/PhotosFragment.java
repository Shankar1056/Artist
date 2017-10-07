package bigappcompany.com.artist.fragment;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

import bigappcompany.com.artist.EditProfileActivity;
import bigappcompany.com.artist.MyProfile;
import bigappcompany.com.artist.PotosVideosActivity;
import bigappcompany.com.artist.R;
import bigappcompany.com.artist.adapters.PhAdapter;
import bigappcompany.com.artist.models.ImageObj;
import bigappcompany.com.artist.models.PhotoModel;
import bigappcompany.com.artist.network.PicassoTrustAll;


public class PhotosFragment extends Fragment implements  View.OnClickListener {
	ArrayList<ImageObj> models;
	int position;
	ImageView img;
	RecyclerView recyclerView;
	FloatingActionButton fab;
	ImageView img_photo;

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_photo, container, false);

		recyclerView=(RecyclerView)rootView.findViewById(R.id.photos_RV);
		fab=(FloatingActionButton)rootView.findViewById(R.id.fab);
		GridLayoutManager manager=new GridLayoutManager(getContext(),3);
		recyclerView.setLayoutManager(manager);

		img_photo=(ImageView)rootView.findViewById(R.id.img_add);
		img_photo.setImageResource(R.drawable.ic_photo_size_select_actual_black_48dp);

		recyclerView.setAdapter(new PhAdapter(models));
		if(models.size()>0)
		{
			recyclerView.setVisibility(View.VISIBLE);
		}
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				((PotosVideosActivity)getActivity()).pickFromGallery();
			}
		});
		return rootView;
	}



	@Override
	public void onClick(View v) {

	}


	public static Fragment newInstance(int pos, ArrayList<ImageObj> models) {
		PhotosFragment fragment=new PhotosFragment();
		fragment.models=models;
		fragment.position=pos;
		return fragment;
	}
}


