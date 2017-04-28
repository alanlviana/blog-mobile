package com.tercalivre.blog.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tercalivre.blog.LeitorActivity;
import com.tercalivre.blog.R;
import com.tercalivre.blog.model.Post;
import com.tercalivre.blog.utils.RetornaHoraAmigavel;

import java.util.List;


public class CardPostagemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_POST = 1;
    private final int VIEW_PROGRESSBAR = 0;

    private List<Post> mDataset;
    private Context mContext;
    private int visibleThreshold = 1;
    private int lastVisibleItem, totalItemCount;
    private boolean loading = false;
    private boolean completeLoaded = false;
    private OnLoadMoreListener onLoadMoreListener;

    private int lastPosition = -1;

    // Provide a suitable constructor (depends on the kind of dataset)
    public CardPostagemAdapter(Context context, List<Post>
            myDataset, RecyclerView recyclerView)  {
        this.mDataset = myDataset;
        this.mContext = context;

        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

            recyclerView
                    .addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(RecyclerView recyclerView,
                                               int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);

                            if(dy < 0 || completeLoaded){
                                return;
                            }

                            totalItemCount = linearLayoutManager.getItemCount();
                            lastVisibleItem = linearLayoutManager
                                    .findLastVisibleItemPosition();
                            if (!loading
                                    && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                                // End has been reached
                                // Do something
                                if (onLoadMoreListener != null) {
                                    onLoadMoreListener.onLoadMore();
                                }

                            }
                        }
                    });
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mDataset.get(position) != null ? VIEW_POST : VIEW_PROGRESSBAR;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        RecyclerView.ViewHolder vh;

        if (viewType == VIEW_POST) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.postagemcard, parent, false);

             vh = new PostViewHolder(v);
        }else{
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.progressbar_item, parent, false);

            vh = new ProgressViewHolder(v);
        }

        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        final int mPosition = position;
        if (holder instanceof PostViewHolder){
            PostViewHolder postViewHolder = (PostViewHolder)holder;

            Picasso.with(mContext).load(mDataset.get(position).thumbnail)
                    .placeholder(R.drawable.backgroud_drawer)
                    .into(postViewHolder.mThumbnail);


            postViewHolder.mCaption.setText(Html.fromHtml(mDataset.get(position).title));
            if (mDataset.get(position) != null && mDataset.get(position).categories.size() > 0) {
                postViewHolder.mCategory.setText(Html.fromHtml(mDataset.get(position).categories.get(0).title));
            }


            String descricaoHora = new RetornaHoraAmigavel().retornaDescricaoDiferenca(mDataset.get(position).date);
            postViewHolder.mHora.setText(descricaoHora);

            postViewHolder.mCardItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(mContext, LeitorActivity.class);
                    i.putExtra(LeitorActivity.ARG_POST, mDataset.get(mPosition));
                    mContext.startActivity(i);
                }
            });

            setAnimation(postViewHolder.mCardItem, position);

        }else{
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }





    }


    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            anim.setDuration(350);
            viewToAnimate.startAnimation(anim);
            lastPosition = position;
        }
    }


    @Override
    public void onViewDetachedFromWindow(final RecyclerView.ViewHolder holder) {
        if (holder instanceof PostViewHolder){
            ((CardPostagemAdapter.PostViewHolder) holder).clearAnimation();
        }
    }



    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public void setLoaded() {
        loading = false;
    }
    public void setLoading() {
        loading = true;
    }

    public void setCompleteLoaded(){
        completeLoaded = true;
    }

    public void setIncompleteLoaded(){
        completeLoaded = false;
    }

    public boolean isLoading(){
        return loading;
    }


    public static class PostViewHolder extends RecyclerView.ViewHolder {
        public TextView mCaption;
        public ImageView mThumbnail;
        public TextView mCategory;
        public TextView mHora;
        public View mCardItem;

        public PostViewHolder(View v) {
            super(v);
            mCaption = (TextView) v.findViewById(R.id.txt_caption);
            mThumbnail = (ImageView) v.findViewById(R.id.img_header);
            mCategory = (TextView) v.findViewById(R.id.txt_category);
            mHora = (TextView) v.findViewById(R.id.txt_hora);
            mCardItem = (CardView) v.findViewById(R.id.card_view);
        }

        public void clearAnimation() {
            mCardItem.clearAnimation();
        }
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar1);
        }
    }

}





