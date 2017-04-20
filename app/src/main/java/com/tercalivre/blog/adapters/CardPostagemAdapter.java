package com.tercalivre.blog.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tercalivre.blog.LeitorActivity;
import com.tercalivre.blog.R;
import com.tercalivre.blog.model.ObjetoAPI;
import com.tercalivre.blog.utils.RetornaHoraAmigavel;

import java.util.List;


public class CardPostagemAdapter extends RecyclerView.Adapter<CardPostagemAdapter.ViewHolder> {

    private List<ObjetoAPI.WordpressJson.Post> mDataset;
    private Context mContext;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mCaption;
        public ImageView mThumbnail;
        public TextView mCategory;
        public TextView mHora;
        public View mCardItem;

        public ViewHolder(View v) {
            super(v);
            mCaption = (TextView) v.findViewById(R.id.txt_caption);
            mThumbnail = (ImageView) v.findViewById(R.id.img_header);
            mCategory = (TextView) v.findViewById(R.id.txt_category);
            mHora = (TextView) v.findViewById(R.id.txt_hora);
            mCardItem = (CardView) v.findViewById(R.id.card_view);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public CardPostagemAdapter(Context context, List<ObjetoAPI.WordpressJson.Post>
            myDataset) {
        this.mDataset = myDataset;
        this.mContext = context;
    }

    @Override
    public CardPostagemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.postagemcard, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final int mPosition = position;


        Picasso.with(mContext).load(mDataset.get(position).thumbnail)
                .placeholder(R.drawable.backgroud_drawer)
                .into(holder.mThumbnail);


        holder.mCaption.setText(Html.fromHtml(mDataset.get(position).title));
        if (mDataset.get(position) != null && mDataset.get(position).categories.size() > 0) {
            holder.mCategory.setText(Html.fromHtml(mDataset.get(position).categories.get(0).title));
        }


        String descricaoHora = new RetornaHoraAmigavel().retornaDescricaoDiferenca(mDataset.get(position).date);
        holder.mHora.setText(descricaoHora);

        holder.mCardItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, LeitorActivity.class);
                i.putExtra("title", mDataset.get(mPosition).title);
                i.putExtra("date", mDataset.get(mPosition).date);
                i.putExtra("content", mDataset.get(mPosition).content);
                i.putExtra("thumbnail", mDataset.get(mPosition).thumbnail);


                mContext.startActivity(i);
            }
        });


    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }


}





