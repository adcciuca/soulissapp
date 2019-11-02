package it.angelic.soulissclient;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import it.angelic.soulissclient.fragments.typicals.T16RGBAdvancedFragment;
import it.angelic.soulissclient.fragments.typicals.T19SingleChannelLedFragment;
import it.angelic.soulissclient.fragments.typicals.T1nGenericLightFragment;
import it.angelic.soulissclient.fragments.typicals.T31HeatingFragment;
import it.angelic.soulissclient.fragments.typicals.T32AirConFragment;
import it.angelic.soulissclient.fragments.typicals.T4nFragment;
import it.angelic.soulissclient.fragments.typicals.T5nSensorFragment;
import it.angelic.soulissclient.model.SoulissTypical;
import it.angelic.soulissclient.model.typicals.SoulissTypical11DigitalOutput;
import it.angelic.soulissclient.model.typicals.SoulissTypical12DigitalOutputAuto;
import it.angelic.soulissclient.model.typicals.SoulissTypical14PulseOutput;
import it.angelic.soulissclient.model.typicals.SoulissTypical15;
import it.angelic.soulissclient.model.typicals.SoulissTypical16AdvancedRGB;
import it.angelic.soulissclient.model.typicals.SoulissTypical18StepRelay;
import it.angelic.soulissclient.model.typicals.SoulissTypical19AnalogChannel;
import it.angelic.soulissclient.model.typicals.SoulissTypical31Heating;
import it.angelic.soulissclient.model.typicals.SoulissTypical32AirCon;
import it.angelic.soulissclient.model.typicals.SoulissTypical41AntiTheft;
import it.angelic.soulissclient.model.typicals.SoulissTypical42AntiTheftPeer;
import it.angelic.soulissclient.model.typicals.SoulissTypical43AntiTheftLocalPeer;

import static junit.framework.Assert.assertTrue;

/**
 * Wrapper per poter aprire dall'esterno direttamente un dettaglio
 * per me da abolire 2019
 */
public class TypicalDetailFragWrapper extends AbstractStatusedFragmentActivity {
    private SoulissTypical collected;

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (opzioni.isLightThemeSelected())
            setTheme(R.style.LightThemeSelector);
        else
            setTheme(R.style.DarkThemeSelector);
        super.onCreate(savedInstanceState);
        // recuper nodo da extra
        setContentView(R.layout.main_detailwrapper);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // If the screen is now in landscape mode, we can show the
            // dialog in-line with the list so we don't need this activity.
            supportFinishAfterTransition();
            return;
        }
        Bundle extras = getIntent().getExtras();
        // collected.setCtx(getActivity());

        if (extras != null && extras.get("TIPICO") != null)
            collected = (SoulissTypical) extras.get("TIPICO");

        assertTrue("TIPICO NULLO", collected != null);
        collected.setContext(TypicalDetailFragWrapper.this);
        // DRAWER gabola
        initDrawer(this, collected.getNodeId());
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        Fragment loadFragment = null;
        Log.w(Constants.TAG, "TypicalDetailFragWrapper should not be used like this: Legacy support");
        if (collected.isSensor())
            loadFragment = T5nSensorFragment.newInstance(collected.getTypicalDTO().getSlot(), collected);
        else if (collected instanceof SoulissTypical16AdvancedRGB)
            loadFragment = T16RGBAdvancedFragment.newInstance(collected.getTypicalDTO().getSlot(), collected);
        else if (collected instanceof SoulissTypical19AnalogChannel)
            loadFragment = T19SingleChannelLedFragment.newInstance(collected.getTypicalDTO().getSlot(), collected);
        else if (collected instanceof SoulissTypical31Heating)
            loadFragment = T31HeatingFragment.newInstance(collected.getTypicalDTO().getSlot(), collected);
        else if (collected instanceof SoulissTypical11DigitalOutput || collected instanceof SoulissTypical12DigitalOutputAuto)
            loadFragment = T1nGenericLightFragment.newInstance(collected.getTypicalDTO().getSlot(), collected);
        else if (collected instanceof SoulissTypical41AntiTheft || collected instanceof SoulissTypical42AntiTheftPeer || collected instanceof SoulissTypical43AntiTheftLocalPeer)
            loadFragment = T4nFragment.newInstance(collected.getTypicalDTO().getSlot(), collected);
        else if (collected instanceof SoulissTypical32AirCon)
            loadFragment = T32AirConFragment.newInstance(collected.getTypicalDTO().getSlot(), collected);
        else if (collected instanceof SoulissTypical14PulseOutput || collected instanceof SoulissTypical18StepRelay) {
            //no detail, notice user and return
            Toast.makeText(this,
                    getString(R.string.status_souliss_nodetail), Toast.LENGTH_SHORT)
                    .show();
            return;
        } else {
            //TODO transform these in Frags
            if (collected instanceof SoulissTypical15) {
                Intent nodeDatail = new Intent(this, T15RGBIrActivity.class);
                nodeDatail.putExtra("TIPICO", collected);
                startActivity(nodeDatail);
            } else {
                Log.e(Constants.TAG, "SERIOUS: Unknowsn typical");
            }
            supportFinishAfterTransition();
            return;
        }
        // During initial setup, plug in the details fragment.
        //T1nGenericLightFragment details = T1nGenericLightFragment.newInstance(collected.getTypicalDTO().getSlot(),
        //		collected);
        loadFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().add(R.id.detailPane, loadFragment).commit();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {

            if (mDrawerLayout.isDrawerOpen(mDrawerLinear)) {
                mDrawerLayout.closeDrawer(mDrawerLinear);
            } else {
                mDrawerLayout.openDrawer(mDrawerLinear);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setActionBarInfo(collected == null ? getString(R.string.status_souliss_nodetail) : collected.getNiceName());

        refreshStatusIcon();
    }
}
