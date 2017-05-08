package edu.mills.cs115;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * The top-level activity that manages the navigation between fragments and implements the view
 * for the entire application.
 *
 * @author barango
 * @author Roberto Amparán (mr.amparan@gmail.com)
 * @author Ying Parks
 * @author Ashley Vo
 */
public class MainActivity extends AppCompatActivity {
    private ListFragment favListFragment;
    private ListFragment listFragment;
    private Fragment HomeFragment;
    private Fragment materialFragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private final String TAG = "MainActivity";

    private GoogleApiClient client;

    public void loadHomeFragment(){
        transaction = fragmentManager.beginTransaction();
        HomeFragment = new HomeFragment();
        transaction.replace(R.id.content_frame, HomeFragment);
        transaction.addToBackStack(null);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.commit();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        fragmentManager = getSupportFragmentManager();
        //Load HomeFragment once MainActivity Launches
        loadHomeFragment();

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_home:
                                Log.d("MainActivity", "onNavigationItemSelected() " + item);
                                loadHomeFragment();
                                break;
                            case R.id.action_history:
                                Log.d("MainActivity", "onNavigationItemSelected() " + item);
                                transaction = fragmentManager.beginTransaction();
                                listFragment = new HistoryListFragment();
                                transaction.replace(R.id.content_frame, listFragment);
                                transaction.addToBackStack(null);
                                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                                transaction.commit();
                                break;
                            case R.id.action_favorites:
                                Log.d("MainActivity", "onNavigationItemSelected() " + item);
                                transaction = fragmentManager.beginTransaction();
                                favListFragment = new FavoritesListFragment();
                                transaction.replace(R.id.content_frame, favListFragment);
                                transaction.addToBackStack(null);
                                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                                transaction.commit();
                                break;
                            case R.id.action_az:
                                Log.d("MainActivity", "onNavigationItemSelected() " + item);
                                transaction = fragmentManager.beginTransaction();
                                materialFragment = new AZCategoryFragment();
                                transaction.replace(R.id.content_frame, materialFragment);
                                transaction.addToBackStack(null);
                                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                                transaction.commit();
                                break;
                        }
                        return true;
                    }
                });

        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        ComponentName cn = new ComponentName(this, ResultActivity.class);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(cn));

        return true;
    }

    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page")
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
