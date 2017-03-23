package net.cnheider.movieapp;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;

import net.cnheider.movieapp.fragments.DiscoverFragment;
import net.cnheider.movieapp.fragments.FavoritesFragment;
import net.cnheider.movieapp.fragments.PlaylistFragment;
import net.cnheider.movieapp.fragments.SeenFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

  @BindView(R.id.main_content_frame)
  FrameLayout mMainContentFrame;
  @BindView(R.id.drawer_layout)
  DrawerLayout mDrawer;

  FragmentManager mFragmentManager;

  @Override
  public void onBackPressed() {
    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    if (drawer.isDrawerOpen(GravityCompat.START)) {
      drawer.closeDrawer(GravityCompat.START);
    } else {
      super.onBackPressed();
    }
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    ButterKnife.bind(this);
    mFragmentManager = getFragmentManager();

    mFragmentManager.beginTransaction().replace(mMainContentFrame.getId(), new DiscoverFragment()).commit();

    NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
    navigationView.setNavigationItemSelectedListener(this);

    Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
    setSupportActionBar(myToolbar);
    myToolbar.inflateMenu(R.menu.main);
  }

  @Override
  public boolean onNavigationItemSelected(MenuItem item) {
    // Handle navigation view item clicks here.
    int id = item.getItemId();
    Fragment fragment = new Fragment();

    if (id == R.id.nav_discover) {
      fragment = new DiscoverFragment();
    } else if (id == R.id.nav_seen) {
      fragment = new SeenFragment();
    } else if (id == R.id.nav_favorites) {
      fragment = new FavoritesFragment();
    } else if (id == R.id.nav_playlist) {
      fragment = new PlaylistFragment();
    } else if (id == R.id.nav_settings) {
      Intent i = new Intent(this, SettingsActivity.class);
      startActivity(i);
    }

    if (fragment != null) {
      mFragmentManager.beginTransaction().replace(mMainContentFrame.getId(), fragment).commit();
    }
    mDrawer.closeDrawer(GravityCompat.START);
    return true;
  }
}
