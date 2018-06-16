package com.example.apple.myapplication.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.apple.myapplication.EndlessScroll;
import com.example.apple.myapplication.R;
import com.example.apple.myapplication.databinding.NewsCardsBinding;
import com.example.apple.myapplication.databinding.ProgressLayoutBinding;
import com.example.apple.myapplication.db.news.News;
import com.example.apple.myapplication.newsFragments.NewsFragment;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsPaginationAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<News> newsList;
    private static final int ITEM = 0;
    private static final int LOADING = 1;

    public NewsPaginationAdapter(Context context, List<News> newsList) {
        this.context = context;
        this.newsList = newsList;
    }

    public void swapList(List<News> newNewsList) {
        if (newsList == null) {
            newsList = newNewsList;
            notifyDataSetChanged();
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return newsList.size();
                }

                @Override
                public int getNewListSize() {
                    return newNewsList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return newsList.get(oldItemPosition).getId()
                            == newNewsList.get(newItemPosition).getId();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    return newsList.get(oldItemPosition).getPublishedAt()
                            .equals(newNewsList.get(newItemPosition).getPublishedAt());
                }
            });

            newsList = newNewsList;
            result.dispatchUpdatesTo(this);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder mViewHolder = null;

        if (viewType == ITEM) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.news_cards, parent, false);
            mViewHolder = new Holder(view);
        } else if (viewType == LOADING) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.progress_layout, parent, false);
            mViewHolder = new LoadingHolder(view);
        }
        return mViewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == newsList.size() - 1 && NewsFragment.isLoading ? LOADING : ITEM);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        News mNews = newsList.get(position);

        if (holder instanceof Holder) {
            if (((Holder) holder).mBinding != null) {
                ((Holder) holder).mBinding.setNewsViewModel(mNews);

                Picasso.with(context)
                        .load(mNews.getUrl())
                        .into(((Holder) holder).mBinding.img);

                Picasso.with(context)
                        .load(mNews.getUrlToImage())
                        .into(((Holder) holder).mBinding.newsImg);
            }
        } else {
            ((LoadingHolder) holder).mBinding.progress.setProgress(100);
        }
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        final NewsCardsBinding mBinding;

        Holder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }
    }

    class LoadingHolder extends RecyclerView.ViewHolder {
        final ProgressLayoutBinding mBinding;

        LoadingHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }
    }
}
