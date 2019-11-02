package it.angelic.soulissclient;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import it.angelic.soulissclient.adapters.TypicalsListAdapter;
import it.angelic.soulissclient.drawer.NavDrawerAdapter;
import it.angelic.soulissclient.fragments.NodeDetailFragment;
import it.angelic.soulissclient.helpers.AlertDialogHelper;
import it.angelic.soulissclient.helpers.SoulissPreferenceHelper;
import it.angelic.soulissclient.model.SoulissNode;
import it.angelic.soulissclient.model.db.SoulissDBHelper;


public class NodeDetailActivity extends AbstractStatusedFragmentActivity {
    //private Handler timeoutHandler;
    private SoulissNode collected;
    private SoulissDBHelper database;
    private SoulissDataService mBoundService;
    private boolean mIsBound;
    private TypicalsListAdapter ta;
    /* SOULISS DATA SERVICE BINDING */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SoulissPreferenceHelper opzioni = SoulissApp.getOpzioni();
        if (opzioni.isLightThemeSelected())
            setTheme(R.style.LightThemeSelector);
        else
            setTheme(R.style.DarkThemeSelector);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_detailwrapper);
        database = new SoulissDBHelper(this);
        SoulissDBHelper.open();
        // recuper nodo da extra
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // If the screen is now in landscape mode, we can show the
            // dialog in-line with the list so we don't need this activity.
            finish();
            return;
        }

        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.get("NODO") != null) {
            collected = database.getSoulissNode(extras.getShort("NODO"));
            initDrawer(NodeDetailActivity.this, collected.getNodeId());
            setActionBarInfo(collected.getNiceName());
        }
        if (savedInstanceState == null) {
            // During initial setup, plug in the details fragment.
            NodeDetailFragment details = new NodeDetailFragment();
            details.setArguments(getIntent().getExtras());
            // questo fragment viene usato anche per typ detail
            getSupportFragmentManager().beginTransaction().add(R.id.detailPane, details).commit();
        }
    }

    /**
     * chiamato dal layout
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.nodedetail_menu, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (opzioni.isAnimationsEnabled())
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        TextView icon = findViewById(R.id.node_icon);//nullable
        switch (item.getItemId()) {
            case android.R.id.home:

                if (getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
                    if (mDrawerLayout.isDrawerOpen(mDrawerLinear)) {
                        mDrawerLayout.closeDrawer(mDrawerLinear);
                    } else {
                        mDrawerLayout.openDrawer(mDrawerLinear);
                    }
                    return true;
                }
                return true;
            case R.id.Opzioni:
                Intent settingsActivity = new Intent(this, SettingsActivity.class);
                startActivity(settingsActivity);
                final Intent preferencesActivity = new Intent(this.getBaseContext(), SettingsActivity.class);
                // evita doppie aperture per via delle sotto-schermate
                preferencesActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(preferencesActivity);
                return true;
            case R.id.CambiaIcona:
                AlertDialog.Builder alert2 = AlertDialogHelper.chooseIconDialog(this, icon, null, database, collected);
                alert2.show();
                return true;
            case R.id.Rinomina:
                AlertDialog.Builder alert = AlertDialogHelper.renameSoulissObjectDialog(this, getActionTitleTextView(), null, database,
                        collected);
                alert.show();
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        setActionBarInfo(collected == null ? getString(R.string.manual_title) : collected.getNiceName());

        super.onStart();
        mDrawermAdapter = new NavDrawerAdapter(NodeDetailActivity.this, R.layout.drawer_list_item, dmh.getStuff(), collected.getNodeId());
        mDrawerList.setAdapter(mDrawermAdapter);
        mDrawerToggle.syncState();

        setActionBarInfo(collected.getNiceName());
        refreshStatusIcon();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    // meccanismo per timeout detection
    /*
     * private BroadcastReceiver timeoutReceiver = new BroadcastReceiver() {
	 * 
	 * @Override public void onReceive(Context context, Intent intent) {
	 * Log.w(TAG, "Posting timeout from " + intent.toString()); Bundle extras =
	 * intent.getExtras(); int delay = extras.getInt("REQUEST_TIMEOUT_MSEC"); }
	 * };
	 */

}
