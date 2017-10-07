package bigappcompany.com.artist.adapters;

/**
 * Created by pratap.kesaboyina on 24-12-2014.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import bigappcompany.com.artist.R;
import bigappcompany.com.artist.models.Constants;
import bigappcompany.com.artist.models.SingleItemModel;
import bigappcompany.com.artist.network.PicassoTrustAll;


public class SectionListDataAdapter extends RecyclerView.Adapter<SectionListDataAdapter.SingleItemRowHolder> {

    private ArrayList<SingleItemModel> itemsList;
    private Context mContext;
    boolean isReq=false;
    public SectionListDataAdapter(Context context, ArrayList<SingleItemModel> itemsList) {
        this.itemsList = itemsList;
        this.mContext = context;
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_single_card, null);
        SingleItemRowHolder mh = new SingleItemRowHolder(v);
        return mh;
    }
    public void isRequests(boolean stat)
    {
        isReq=stat;
    }

    @Override
    public void onBindViewHolder(final SingleItemRowHolder holder, int i) {

        final SingleItemModel singleItem = itemsList.get(i);

        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(singleItem.getDate());
            holder.tvDay.setText((new SimpleDateFormat("dd").format(date)).toString());
            holder.tvMMyy.setText((new SimpleDateFormat("yyyy-MM").format(date)).toString());
            holder.tvTime.setText((new SimpleDateFormat("HH:mm").format(date)).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.tvTitle.setText(singleItem.getName());
        if(isReq)
        {
            if(itemsList.get(i).isNew())
            {
                holder.tvLabel.setVisibility(View.VISIBLE);
            }
        }
        /*ViewTreeObserver vto = holder.itemImage.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                int finalHeight, finalWidth;

                holder.itemImage.getViewTreeObserver().removeOnPreDrawListener(this);

                finalHeight = holder.itemImage.getMeasuredHeight();
                finalWidth = holder.itemImage.getMeasuredWidth();
                PicassoTrustAll.getInstance(holder.itemImage.getContext()).load(singleItem.getUrl()).resize(finalWidth,finalHeight).centerCrop().into(holder.itemImage);

                return true;
            }
        });*/

             /* Glide.with(mContext)
                .load(feedItem.getImageURL())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .error(R.drawable.bg)
                .into(feedListRowHolder.thumbView);*/
    }

    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
    }

    public void addItem(SingleItemModel model, int i) {
        Log.e("inside","second_add");
        model.setNew(true);
        itemsList.add(i,model);
        notifyItemInserted(i);
        notifyDataSetChanged();
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        protected TextView tvTitle,tvLabel,tvTime,tvMMyy,tvDay;

        //protected ImageView itemImage;


        public SingleItemRowHolder(View view) {
            super(view);

            this.tvTitle = (TextView) view.findViewById(R.id.tvTitle);
            this.tvLabel = (TextView) view.findViewById(R.id.tvLbl);
            this.tvTime = (TextView) view.findViewById(R.id.tvTime);
            this.tvMMyy = (TextView) view.findViewById(R.id.tvMMYY);
            this.tvDay = (TextView) view.findViewById(R.id.tvDay);
            //this.itemImage = (ImageView) view.findViewById(R.id.itemImage);


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //v.getContext().startActivity(new Intent(v.getContext(), ArtistsActivity.class).putExtra(Constants.CATG,tvTitle.getText()));
                    Toast.makeText(v.getContext(), tvTitle.getText(), Toast.LENGTH_SHORT).show();

                }
            });


        }

    }

}