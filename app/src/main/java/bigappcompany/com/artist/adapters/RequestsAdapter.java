package bigappcompany.com.artist.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import bigappcompany.com.artist.R;
import bigappcompany.com.artist.RequestsActivity;
import bigappcompany.com.artist.RippleView;
import bigappcompany.com.artist.models.RequestsModel;
import bigappcompany.com.artist.network.ApiUrl;
import bigappcompany.com.artist.network.Download_web;
import bigappcompany.com.artist.network.JsonParser;


public class RequestsAdapter extends RecyclerView.Adapter<RequestsAdapter.ViewHolder> {
    private ArrayList<RequestsModel> models;
    int lastPosition;


    public RequestsAdapter(ArrayList<RequestsModel> models) {
        this.models = models;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_request, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final RequestsModel model = this.models.get(position);


        holder.titleTV.setText(model.getEvent_name());
        holder.tv_addr.setText(model.getAddress());
        holder.tv_bkName.setText(model.getName());
        holder.tv_date.setText(model.getDate());
        holder.tv_contact.setText(model.getPhone());
        holder.itemRV.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                remove(holder.getAdapterPosition(),holder.itemRV.getContext(),1);
                //rippleView.getContext().startActivity(new Intent(rippleView.getContext(), BookingActivity.class).putExtra("MODEL",model.toString()));
            }
        });

        holder.rvclose.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                remove(holder.getAdapterPosition(),holder.rvclose.getContext(),3);

            }
        });

        setAnimation(holder.itemView,position);
        //Picasso.with(holder.thumbnailTV.getContext()).load(model.getPhoto()).resize(holder.thumbnailTV.getMeasuredWidth(),holder.thumbnailTV.getMeasuredHeight()).centerCrop().into(holder.thumbnailTV);

    }



    private void remove(int position,Context context,int status) {
        try {
            Download_web web = new Download_web(context, null);
            web.setReqType(false);
            web.setData((new JSONObject().put(JsonParser.EVENT_ID, models.get(position).getId()).put(JsonParser.RESPONSE_STATUS, status).put(JsonParser.CUSTOMER_ID,models.get(position).getCustomer_id())).toString());
            web.execute(ApiUrl.UPDATE_EVENT);
            models.remove(position);
            this.notifyItemRemoved(position);
        }
        catch (JSONException je)
        {
            je.printStackTrace();

        }
    }

    @Override
    public int getItemCount() {
        return models.size();
    }



    class ViewHolder extends RecyclerView.ViewHolder implements RippleView.OnRippleCompleteListener {
        TextView titleTV, tv_bkName,tv_date,tv_contact,tv_addr;

        RippleView itemRV,rvclose;

        ViewHolder(View itemView) {
            super(itemView);


            titleTV = (TextView) itemView.findViewById(R.id.tvTitle);
            tv_bkName = (TextView) itemView.findViewById(R.id.tv_bkName);
            tv_addr = (TextView) itemView.findViewById(R.id.tv_addr);
            tv_date = (TextView) itemView.findViewById(R.id.tv_date);
            tv_contact = (TextView) itemView.findViewById(R.id.tv_ph);
            itemRV = (RippleView) itemView.findViewById(R.id.rp_confirm);
            rvclose = (RippleView) itemView.findViewById(R.id.rp_deny);




            //itemRV.setOnRippleCompleteListener(this);
        }



        @Override
        public void onComplete(RippleView rippleView) {

        }
    }
    public static float dipToPixels(Context context, float dipValue) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
    }
    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(viewToAnimate.getContext(), R.anim.slidefromdown);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
}

