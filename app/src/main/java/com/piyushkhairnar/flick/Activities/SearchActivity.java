package com.piyushkhairnar.flick.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.piyushkhairnar.flick.Adapters.SearchResultsAdapter;
import com.piyushkhairnar.flick.R;
import com.piyushkhairnar.flick.Utils.Constants;
import com.piyushkhairnar.flick.Utils.NetworkConnection;
import com.piyushkhairnar.flick.network.search.SearchAsyncTaskLoader;
import com.piyushkhairnar.flick.network.search.SearchResponse;
import com.piyushkhairnar.flick.network.search.SearchResult;

import java.util.ArrayList;
import java.util.List;

import static com.piyushkhairnar.flick.R.color.black;

public class SearchActivity extends AppCompatActivity {

    private String mQuery;

    //    private SmoothProgressBar mSmoothProgressBar;
    private RecyclerView mSearchResultsRecyclerView;
    private SearchResultsAdapter mSearchResultsAdapter;

    private TextView mEmptyTextView;
    List<SearchResult> mSearchResults;


    private boolean pagesOver = false;
    private int presentPage = 1;
    private boolean loading = true;
    private int previousTotal = 0;
    private int visibleThreshold = 5;

    EditText searchEdt;
    private ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mEmptyTextView = (TextView) findViewById(R.id.text_view_empty_search);
        searchEdt = (EditText) findViewById(R.id.search_activity_edittext);
        mSearchResultsRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_search);
        imgBack = (ImageView)findViewById(R.id.search_activity_back);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        searchEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    mQuery = v.getText().toString();
                    mSearchResults = new ArrayList<>();
                    mSearchResultsAdapter = new SearchResultsAdapter(SearchActivity.this, mSearchResults);
                    mSearchResultsRecyclerView.setAdapter(mSearchResultsAdapter);
                    performSearch(mQuery);
                    mQuery = "";
                    return true;
                }


                return false;
            }
        });


    }


    private void performSearch(final String text){


        if (text == null || text.trim().isEmpty()){
            Toast.makeText(SearchActivity.this,"Please enter to search",Toast.LENGTH_SHORT).show();
        }
        else {

            final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SearchActivity.this, LinearLayoutManager.VERTICAL, false);
            mSearchResultsRecyclerView.setLayoutManager(linearLayoutManager);

            mSearchResultsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                    int visibleItemCount = linearLayoutManager.getChildCount();
                    int totalItemCount = linearLayoutManager.getItemCount();
                    int firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();

                    if (loading) {
                        if (totalItemCount > previousTotal) {
                            loading = false;
                            previousTotal = totalItemCount;
                        }
                    }
                    if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                        loadSearchResults(text);
                        loading = true;
                    }

                }
            });
            loadSearchResults(text);

        }

    }


    private void loadSearchResults(final String query1) {
        if (pagesOver) return;

//        mSmoothProgressBar.progressiveStart();

        getLoaderManager().initLoader(presentPage, null, new LoaderManager.LoaderCallbacks<SearchResponse>() {

            @Override
            public Loader<SearchResponse> onCreateLoader(int i, Bundle bundle) {
                return new SearchAsyncTaskLoader(SearchActivity.this, query1, String.valueOf(presentPage));
            }

            @Override
            public void onLoadFinished(Loader<SearchResponse> loader, SearchResponse searchResponse) {

                if (searchResponse == null) return;
                if (searchResponse.getResults() == null) return;

//                mSmoothProgressBar.progressiveStop();
                for (SearchResult searchResult : searchResponse.getResults()) {
                    if (searchResult != null)
                        mSearchResults.add(searchResult);
                }
                mSearchResultsAdapter.notifyDataSetChanged();
                if (mSearchResults.isEmpty()) mEmptyTextView.setVisibility(View.VISIBLE);
                if (searchResponse.getPage() == searchResponse.getTotalPages())
                    pagesOver = true;
                else
                    presentPage++;

            }

            @Override
            public void onLoaderReset(Loader<SearchResponse> loader) {

            }
        }).forceLoad();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
