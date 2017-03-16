
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;

/**
 * Created by enclaveit on 01/03/2017.
 */
/*Reference to
* http://www.androidhive.info/2013/11/android-sliding-menu-using-navigation-drawer/
* http://stackoverflow.com/questions/19451715/same-navigation-drawer-in-different-activities
* http://mateoj.com/2015/06/21/adding-toolbar-and-navigation-drawer-all-activities-android/
* */
public class BaseActivity extends AppCompatActivity implements View.OnClickListener {
    public DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView navigationView;
    private View navHeader;
    private ImageButton btnChangeChild;


    //index to identify the current nav item menu
    private static int navCurrentItemIndex = 0;

    /*tags used to attach the fragments*/
    private static final String TAG_HOME = "home";
    private static final String TAG_PROFILE = "profile";
    private static final String TAG_CHILDREN = "children";
    private static final String TAG_NOTIFICATION = "notifications";
    private static final String TAG_SETTINGS = "settings";
    private static String CURRENT_TAG = TAG_HOME;
    /*Use to check if use is staying at MainActivity
    * If click home, then don't go back if it's MainActivity*/
    private static boolean IS_MAIN_ACTIVITY = false;
    /*Should re-check it in onStart()
    * Because when it call other activities, it's call onStop()
    * then call onStart() again when it restart*/

    //Toolbar titles respected to the selected nav item menu
    private String activityTitles[];

    //Flag load to home fragment when press the back key
    private boolean shouldLoadHomeFragmentOnBackPress = false;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceStatus) {
        super.onCreate(savedInstanceStatus);
        Log.i("BASE_ACTIVITY", "On Create Base Activity");

    }

    @Override
    public void setContentView(@LayoutRes int layoutResID){
        drawerLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.base_activity_layout, null);
        FrameLayout activityContainer = (FrameLayout) drawerLayout.findViewById(R.id.content_frame);
        getLayoutInflater().inflate(layoutResID, activityContainer, true);
        super.setContentView(drawerLayout);
        onCreateDrawer();
    }


    protected void onCreateDrawer(){
        Toolbar my_toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(my_toolbar);
        mHandler = new Handler();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);

        /*Navigation View Header*/
        navHeader = navigationView.getHeaderView(0);
        btnChangeChild = (ImageButton) navHeader.findViewById(R.id.btnChangeChild);
            btnChangeChild.setOnClickListener(this);

        //Load Toolbar titles from String Resources
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);

        //initializing navigation menu
        setUpNavigationView();


        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.string.drawer_open, R.string.drawer_close){
            public void onDrawerClosed(View drawerView)
            {
                //getSupportActionBar().setTitle(R.string.app_name);
                super.onDrawerClosed(drawerView);
            }

            public void onDrawerOpened(View drawerView){
                super.onDrawerOpened(drawerView);
                //getSupportActionBar().setTitle(R.string.menu);
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    /*Returns respected fragment that user selected from navigation menu*/
    public void loadHomeFragment(){
        //selecting appropriate nav item menu
        selectNavMenu();

        //set Toolbar Title
        setToolbarTitle();
        /*If user select the current navigation menu again, don't do anything
        * just close navigation drawer*/
        if(getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null){
            Log.i("BASE_ACTIVITY","Choose the current navigation menu again");
            drawerLayout.closeDrawers();
            return;
        }
        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        final Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = getHomeFragment();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                            android.R.anim.fade_out);
                    fragmentTransaction.replace(R.id.content_frame, fragment, CURRENT_TAG);
                    fragmentTransaction.commitAllowingStateLoss();
            }
        };
        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }
        //Closing drawer on item click
        drawerLayout.closeDrawers();
        // refresh toolbar menu
        invalidateOptionsMenu();
    }

    private Fragment getHomeFragment(){
        switch (navCurrentItemIndex){
            case 0: //home
                HomeFragment homeFragment = new HomeFragment();
                return homeFragment;
            case 1: //profile
                ProfileFragment profileFragment = new ProfileFragment();
                    return profileFragment;
            case 2: // children
                ChildrenFragment childrenFragment = new ChildrenFragment();
                    return childrenFragment;
            case 3: // notification
                NotificationFragment notificationFragment = new NotificationFragment();
                    return notificationFragment;
            case 4: //setting
                SettingFragment settingFragment = new SettingFragment();
                    return settingFragment;
            default:
                return new HomeFragment();
        }
    }

    /*Set Toolbar Title*/
    public void setToolbarTitle(){
        getSupportActionBar().setTitle(activityTitles[navCurrentItemIndex]);
    }
    public void setToolbarTitle(String title){
        getSupportActionBar().setTitle(title);
    }

    private void selectNavMenu(){
        navigationView.getMenu().getItem(navCurrentItemIndex).setChecked(true);
    }

    private void setUpNavigationView(){
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_home:
                        navCurrentItemIndex = 0;
                        CURRENT_TAG = TAG_HOME;
                        //Check if user is not at Main Activity
                        //then move to MainActivity
                        if( !IS_MAIN_ACTIVITY ) {
                            Log.i("BASE_ACTIVITY", "Return to Main Activity");
                            shouldLoadHomeFragmentOnBackPress = true;
                            onBackPressed();
                        }
                        break;
                    case R.id.nav_profile:
                        navCurrentItemIndex = 1;
                        CURRENT_TAG = TAG_PROFILE;
                        break;
                    case R.id.nav_children:
                        navCurrentItemIndex = 2;
                        CURRENT_TAG = TAG_CHILDREN;
                        break;
                    case R.id.nav_notifications:
                        navCurrentItemIndex = 3;
                        CURRENT_TAG = TAG_NOTIFICATION;
                        break;
                    case R.id.nav_settings:
                        navCurrentItemIndex = 4;
                        CURRENT_TAG = TAG_SETTINGS;
                        break;
                    case R.id.nav_about_us:
                        // launch new intent instead of loading fragment
                        //TODO -startActivity(new Intent(MainActivity.this, AboutUsActivity.class));
                        //drawer.closeDrawers();
                        return true;
                    case R.id.nav_privacy_policy:
                        // launch new intent instead of loading fragment
                        //TODO -startActivity(new Intent(MainActivity.this, PrivacyPolicyActivity.class));
                        //drawer.closeDrawers();
                        return true;
                    default:
                        navCurrentItemIndex = 0;
                        CURRENT_TAG = TAG_HOME;
                }

                //Checking if the item is in checked state or not, if not make it in checked state
                if(item.isChecked())    item.setChecked(false);
                else item.setChecked(true);

                Log.i("BASE_ACTIVITY", "Nav Item menu is clicked");
                loadHomeFragment();
                return true;
            }
        });
    }

    @Override
    public void onBackPressed(){
        Log.i("BASE_ACTIVITY", "On Back Pressed");
        if(drawerLayout.isDrawerOpen(GravityCompat.START) && !shouldLoadHomeFragmentOnBackPress ){
            drawerLayout.closeDrawers();
            return;
        }
        /*This code is return Home Fragment when user in another Fragment than home*/
        //If current item is not Home, then load Home (blank) Fragment
            if(navCurrentItemIndex != 0 ){
                navCurrentItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                loadHomeFragment();
                return;
            }
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            /*Action for Change Child*/
            case R.id.btnChangeChild:
                //TODO-Change Child
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
            Log.i("BASE_ACTIVITY", "On Destroy Base Activity");

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("BASE_ACTIVITY", "On Pause Base Activity");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("BASE_ACTIVITY", "On Stop Base Activity");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("BASE_ACTIVITY", "On Start Base Activity");
        if(this instanceof MainActivity) {
            IS_MAIN_ACTIVITY = true;
        }
        else IS_MAIN_ACTIVITY = false;
        Log.i("BASE_ACTIVITY", "Is main Activity: "+IS_MAIN_ACTIVITY);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("BASE_ACTIVITY", "On Restart Base Activity");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("BASE_ACTIVITY", "On Resume Base Activity");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i("BASE_ACTIVITY", "On Save Instance State Base Activity");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i("BASE_ACTIVITY", "On Restore Instance State Base Activity  ");
    }
}
