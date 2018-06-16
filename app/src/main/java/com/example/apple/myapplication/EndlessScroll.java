package com.example.apple.myapplication;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public abstract class EndlessScroll extends RecyclerView.OnScrollListener {

    private LinearLayoutManager layoutManager;

    protected EndlessScroll(LinearLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int visibleItemsCount = layoutManager.getChildCount();
        int totalItemsCount = layoutManager.getItemCount();
        int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

        if (!isLoading() && !isLastPage()) {
            if (visibleItemsCount + firstVisibleItemPosition >= totalItemsCount
                    && firstVisibleItemPosition >= 0
                    && totalItemsCount == getTotalItemsCount()) {
                loadMoreItems();
            }
        }
    }

    public abstract void loadMoreItems();

    public abstract int getTotalItemsCount();

    public abstract boolean isLoading();

    public abstract boolean isLastPage();
}
