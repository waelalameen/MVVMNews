package com.example.apple.myapplication.newsFragments;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.apple.myapplication.EndlessScroll;
import com.example.apple.myapplication.NewsRepository;
import com.example.apple.myapplication.R;
import com.example.apple.myapplication.adapters.NewsPaginationAdapter;
import com.example.apple.myapplication.databinding.FragmentNewsBinding;
import com.example.apple.myapplication.db.news.News;
import com.example.apple.myapplication.utils.InjectorUtils;
import com.example.apple.myapplication.utils.JobNewsService;

import java.util.List;
import java.util.Objects;


public class NewsFragment extends Fragment implements LifecycleOwner {

    private static final String LOG_TAG = NewsFragment.class.getSimpleName();
    private static final int TOTAL_PAGES = 5;
    private int currentPage = 0;
    public static boolean isLoading = false;
    public static boolean isLastPage = false;
    private FragmentNewsBinding mBinding;
    private LifecycleRegistry mRegistry;
    NewsViewModel mViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getActivity() != null && getContext() != null) {

            // Create a life registry
            mRegistry = new LifecycleRegistry(getActivity());
            mRegistry.markState(Lifecycle.State.CREATED);

            // Create a component
            ComponentName componentName = new ComponentName(getActivity(), JobNewsService.class);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                /*
                    JobInfo.NETWORK_TYPE_NONE: No network connectivity required.
                    JobInfo.NETWORK_TYPE_UNMETERED: An unmetered WiFi or Ethernet connection.
                    JobInfo.NETWORK_TYPE_ANY: Any network connection (WiFi or cellular).
                */

                JobInfo jobInfo = new JobInfo.Builder(10, componentName)
                        .setPersisted(true)
                        .setBackoffCriteria(JobInfo.DEFAULT_INITIAL_BACKOFF_MILLIS,
                                JobInfo.BACKOFF_POLICY_LINEAR)
                        .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                        .build();

                JobScheduler jobScheduler = (JobScheduler) getContext()
                        .getSystemService(Context.JOB_SCHEDULER_SERVICE);

                int result = jobScheduler != null ? jobScheduler.schedule(jobInfo) : 0;

                if (result == JobScheduler.RESULT_SUCCESS) {
                    Log.d(LOG_TAG, "RESULT_SUCCESS");
                } else {
                    Log.d(LOG_TAG, "RESULT_FAILURE");
                }
            }

            NewsRepository repository = InjectorUtils.provideRepository(getContext());

            NewsViewModelFactory factory = new NewsViewModelFactory(repository);

            mViewModel = ViewModelProviders.of(getActivity(), factory).get(NewsViewModel.class);

            mViewModel.getNews().observe(this, (List<News> newsList) -> {
                if (newsList != null) {
                    updateUI(newsList);
                }
            });
        }
    }

    private void updateUI(List<News> newsList) {
        NewsPaginationAdapter adapter = new NewsPaginationAdapter(getContext(), newsList);
        adapter.swapList(newsList);
        mBinding.recyclerView.setAdapter(adapter);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_news, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Set layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        // Set recycler view properties
        mBinding.recyclerView.setHasFixedSize(true);
        mBinding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        mBinding.recyclerView.requestLayout();
        mBinding.recyclerView.setLayoutManager(layoutManager);
        mBinding.recyclerView.setSaveFromParentEnabled(true);
        mBinding.recyclerView.setDrawingCacheEnabled(true);
        mBinding.recyclerView.setItemViewCacheSize(10);
        mBinding.recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_AUTO);

        // Add scroll listener
        mBinding.recyclerView.addOnScrollListener(new EndlessScroll(layoutManager) {
            @Override
            public void loadMoreItems() {
                isLoading = true;
                currentPage += 1;

                //TODO implement populating data
                mViewModel.getNews().observe(Objects.requireNonNull(getActivity()), (List<News> newsList) -> {
                    if (newsList != null) {
                        updateUI(newsList);
                    }
                });
            }

            @Override
            public int getTotalItemsCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        mRegistry.markState(Lifecycle.State.STARTED);
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return mRegistry;
    }

    @Override
    public void onResume() {
        super.onResume();

        mRegistry.markState(Lifecycle.State.RESUMED);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mRegistry.markState(Lifecycle.State.DESTROYED);
    }
}
