package it.angelic.soulissclient.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import it.angelic.soulissclient.Constants;
import it.angelic.soulissclient.R;
import it.angelic.soulissclient.SoulissApp;
import it.angelic.soulissclient.helpers.SoulissPreferenceHelper;
import it.angelic.soulissclient.model.db.SoulissDBHelper;
import it.angelic.soulissclient.model.db.SoulissDBOpenHelper;
import it.angelic.soulissclient.model.db.SoulissTypicalDTO;
import it.angelic.soulissclient.net.UDPHelper;
import it.angelic.soulissclient.util.FontAwesomeEnum;
import it.angelic.soulissclient.util.FontAwesomeUtil;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Modella un tipico, ovvero una classe di dispositivi
 * Per ogni nodo ce ne possono essere da 0 a n, con
 * n < MAX_TYP_PER_NODE
 * <p/>
 * has-a SoulissTypicalDTO che riflette il DB
 *
 * @author shine@angelic.it
 */
public class SoulissTypical implements Serializable, ISoulissTypical {
    /**
     *
     */
    private static final long serialVersionUID = -7375342157142543740L;
    public static final String NOT_AVAILABLE = "NA";
    public static final String UNKNOWN = "UNKNOWN";
    protected transient Context context;
    // nodo di appartenenza
    protected SoulissNode parentNode;
    // contenitore dati specchio del DB
    protected SoulissTypicalDTO typicalDTO;
    //transient per evitare problemi di serializzazione
    protected transient SoulissPreferenceHelper prefs;
    private boolean isRelated = false;// indica se includerlo nelle liste
    //AUTOF
    public SoulissTypical(Context context, SoulissPreferenceHelper pre) {
        super();
        prefs = pre;
        setTypicalDTO(new SoulissTypicalDTO());
        this.context = context;
    }

    @Override
    public void getActionsLayout(final Context ctx, LinearLayout convertView) {
    }

    public List<ISoulissCommand> getCommands(Context ctx) {
        // to be overridden
        return new ArrayList<>();
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getDefaultName() {
        short typical = typicalDTO.getTypical();
        assertTrue(typical != -1);

        int id;
        if (typical == Constants.Typicals.Souliss_T11)
            id = R.string.Souliss_T11_desc;
        else if (typical == Constants.Typicals.Souliss_T12)
            id = R.string.Souliss_T12_desc;
        else if (typical == Constants.Typicals.Souliss_T1A)
            id = R.string.Souliss_T1A_desc;
        else if (typical == Constants.Typicals.Souliss_T13)
            id = R.string.Souliss_T13_desc;
        else if (typical == Constants.Typicals.Souliss_T14)
            id = R.string.Souliss_T14_desc;
        else if (typical == Constants.Typicals.Souliss_T16)
            id = R.string.Souliss_T16_desc;
        else if (typical == Constants.Typicals.Souliss_T18)
            id = R.string.Souliss_T18_desc;
        else if (typical == Constants.Typicals.Souliss_T19)
            id = R.string.Souliss_T19_desc;
        else if (typical == Constants.Typicals.Souliss_T21)
            id = R.string.Souliss_T21_desc;
        else if (typical == Constants.Typicals.Souliss_T22)
            id = R.string.Souliss_T22_desc;
        else if (typical == Constants.Typicals.Souliss_T31)
            id = R.string.Souliss_T31_desc;
        else if (typical == Constants.Typicals.Souliss_T32_IrCom_AirCon)
            id = R.string.Souliss_TAircon_desc;
        else if (typical == Constants.Typicals.Souliss_T15_RGB)
            id = R.string.Souliss_TRGB_desc;
        else if (typical == Constants.Typicals.Souliss_T41_Antitheft_Main)
            id = R.string.Souliss_T41_desc;
        else if (typical == Constants.Typicals.Souliss_T42_Antitheft_Peer || typical == Constants.Typicals.Souliss_T43_Antitheft_LocalPeer)
            id = R.string.Souliss_T42_desc;
        else if (typical == Constants.Typicals.Souliss_T51)
            id = R.string.Souliss_T51_desc;
        else if (typical == Constants.Typicals.Souliss_T52_TemperatureSensor)
            id = R.string.Souliss_TTemperature_desc;
        else if (typical == Constants.Typicals.Souliss_T53_HumiditySensor)
            id = R.string.Souliss_THumidity_desc;
        else if (typical == Constants.Typicals.Souliss_T54_LuxSensor)
            id = R.string.Souliss_T54_desc;
        else if (typical == Constants.Typicals.Souliss_T55_VoltageSensor)
            id = R.string.Souliss_T55_desc;
        else if (typical == Constants.Typicals.Souliss_T56_CurrentSensor)
            id = R.string.Souliss_T56_desc;
        else if (typical == Constants.Typicals.Souliss_T57_PowerSensor)
            id = R.string.Souliss_T57_desc;
        else if (typical == Constants.Typicals.Souliss_T58_PressureSensor)
            id = R.string.Souliss_T58_desc;
        else if (typical == Constants.Typicals.Souliss_T61)
            id = R.string.Souliss_T61_desc;
        else if (typical == Constants.Typicals.Souliss_T62)
            id = R.string.Souliss_T62_desc;
        else if (typical == Constants.Typicals.Souliss_T63)
            id = R.string.Souliss_T63_desc;
        else if (typical == Constants.Typicals.Souliss_T64)
            id = R.string.Souliss_T64_desc;
        else if (typical == Constants.Typicals.Souliss_T65)
            id = R.string.Souliss_T65_desc;
        else if (typical == Constants.Typicals.Souliss_T66)
            id = R.string.Souliss_T66_desc;
        else if (typical == Constants.Typicals.Souliss_T67)
            id = R.string.Souliss_T67_desc;
        else if (typical == Constants.Typicals.Souliss_T68)
            id = R.string.Souliss_T68_desc;
        else
            id = R.string.unknown_typical;

        Context cc = SoulissApp.getAppContext();
        return cc.getResources().getString(id);
    }

    @Override
    public int getIconResourceId() {
        short typical = typicalDTO.getTypical();
        assertTrue(typical != -1);

        if (typicalDTO.getIconId() != 0)
            return typicalDTO.getIconId();
        if (typical == Constants.Typicals.Souliss_T11)
            return FontAwesomeUtil.getCodeIndexByFontName(context, FontAwesomeEnum.fa_lightbulb_o.getFontName());
        else if (typical == Constants.Typicals.Souliss_T12)
            return FontAwesomeUtil.getCodeIndexByFontName(context, FontAwesomeEnum.fa_lightbulb_o.getFontName());
        else if (typical == Constants.Typicals.Souliss_T1A)
            return FontAwesomeUtil.getCodeIndexByFontName(context, FontAwesomeEnum.fa_lightbulb_o.getFontName());
        else if (typical == Constants.Typicals.Souliss_T13)
            return FontAwesomeUtil.getCodeIndexByFontName(context, FontAwesomeEnum.fa_lightbulb_o.getFontName());
        else if (typical == Constants.Typicals.Souliss_T14)
            return FontAwesomeUtil.getCodeIndexByFontName(context, FontAwesomeEnum.fa_lock.getFontName());
        else if (typical == Constants.Typicals.Souliss_T16)
            return FontAwesomeUtil.getCodeIndexByFontName(context, FontAwesomeEnum.fa_sliders.getFontName());
        else if (typical == Constants.Typicals.Souliss_T18)
            return FontAwesomeUtil.getCodeIndexByFontName(context, FontAwesomeEnum.fa_step_forward.getFontName());
        else if (typical == Constants.Typicals.Souliss_T19)
            return FontAwesomeUtil.getCodeIndexByFontName(context, FontAwesomeEnum.fa_signal.getFontName());
        else if (typical == Constants.Typicals.Souliss_T21)
            return FontAwesomeUtil.getCodeIndexByFontName(context, FontAwesomeEnum.fa_codepen.getFontName());
        else if (typical == Constants.Typicals.Souliss_T22)
            return FontAwesomeUtil.getCodeIndexByFontName(context, FontAwesomeEnum.fa_gears.getFontName());
        else if (typical == Constants.Typicals.Souliss_T31)
            return FontAwesomeUtil.getCodeIndexByFontName(context, FontAwesomeEnum.fa_thermometer_half.getFontName());
        else if (typical == Constants.Typicals.Souliss_T41_Antitheft_Main)
            return FontAwesomeUtil.getCodeIndexByFontName(context, FontAwesomeEnum.fa_shield.getFontName());
        else if (typical == Constants.Typicals.Souliss_T42_Antitheft_Peer || typical == Constants.Typicals.Souliss_T43_Antitheft_LocalPeer)
            return FontAwesomeUtil.getCodeIndexByFontName(context, FontAwesomeEnum.fa_shield.getFontName());
        else if (typical == Constants.Typicals.Souliss_T_related)
            return FontAwesomeUtil.getCodeIndexByFontName(context, FontAwesomeEnum.fa_angle_double_up.getFontName());
        else if (typical == Constants.Typicals.Souliss_T32_IrCom_AirCon)
            return FontAwesomeUtil.getCodeIndexByFontName(context, FontAwesomeEnum.fa_snowflake_o.getFontName());
        else if (typical == Constants.Typicals.Souliss_T15_RGB)
            return FontAwesomeUtil.getCodeIndexByFontName(context, FontAwesomeEnum.fa_wifi.getFontName());
        else if (typical == Constants.Typicals.Souliss_T51)
            return FontAwesomeUtil.getCodeIndexByFontName(context, FontAwesomeEnum.fa_line_chart.getFontName());
        else if (typical == Constants.Typicals.Souliss_T52_TemperatureSensor)
            return FontAwesomeUtil.getCodeIndexByFontName(context, FontAwesomeEnum.fa_thermometer_half.getFontName());
        else if (typical == Constants.Typicals.Souliss_T53_HumiditySensor)
            return FontAwesomeUtil.getCodeIndexByFontName(context, FontAwesomeEnum.fa_tint.getFontName());
        else if (typical == Constants.Typicals.Souliss_T54_LuxSensor)
            return FontAwesomeUtil.getCodeIndexByFontName(context, FontAwesomeEnum.fa_sun_o.getFontName());
        else if (typical == Constants.Typicals.Souliss_T55_VoltageSensor)
            return FontAwesomeUtil.getCodeIndexByFontName(context, FontAwesomeEnum.fa_flash.getFontName());
        else if (typical == Constants.Typicals.Souliss_T56_CurrentSensor)
            return FontAwesomeUtil.getCodeIndexByFontName(context, FontAwesomeEnum.fa_flash.getFontName());
        else if (typical == Constants.Typicals.Souliss_T57_PowerSensor)
            return FontAwesomeUtil.getCodeIndexByFontName(context, FontAwesomeEnum.fa_flash.getFontName());
        else if (typical == Constants.Typicals.Souliss_T58_PressureSensor)
            return FontAwesomeUtil.getCodeIndexByFontName(context, FontAwesomeEnum.fa_compress.getFontName());
        else if (typical == Constants.Typicals.Souliss_T61)
            return FontAwesomeUtil.getCodeIndexByFontName(context, FontAwesomeEnum.fa_level_up.getFontName());
        else if (typical == Constants.Typicals.Souliss_T62)
            return FontAwesomeUtil.getCodeIndexByFontName(context, FontAwesomeEnum.fa_thermometer_half.getFontName());
        else if (typical == Constants.Typicals.Souliss_T63)
            return FontAwesomeUtil.getCodeIndexByFontName(context, FontAwesomeEnum.fa_tint.getFontName());
        else if (typical == Constants.Typicals.Souliss_T64)
            return FontAwesomeUtil.getCodeIndexByFontName(context, FontAwesomeEnum.fa_sun_o.getFontName());
        else if (typical == Constants.Typicals.Souliss_T65)
            return FontAwesomeUtil.getCodeIndexByFontName(context, FontAwesomeEnum.fa_plug.getFontName());
        else if (typical == Constants.Typicals.Souliss_T66)
            return FontAwesomeUtil.getCodeIndexByFontName(context, FontAwesomeEnum.fa_plug.getFontName());
        else if (typical == Constants.Typicals.Souliss_T67)
            return FontAwesomeUtil.getCodeIndexByFontName(context, FontAwesomeEnum.fa_plug.getFontName());
        else
            return FontAwesomeUtil.getCodeIndexByFontName(context, FontAwesomeEnum.fa_cube.getFontName());
    }

    @Override
    public void setIconResourceId(int resId) {
        typicalDTO.setIconId(resId);
    }

    @Override
    public String getName() {
        if (typicalDTO.getName() != null)
            return typicalDTO.getName();
        return getDefaultName();
    }

    @Override
    public void setName(String newName) {
        typicalDTO.setName(newName);
    }

    @Override
    public String getNiceName() {
        if (typicalDTO.getName() != null)
            return typicalDTO.getName();
        return getDefaultName();

    }


    public short getNodeId() {
        return typicalDTO.getNodeId();
    }

    public Float getOutput() {
        return (float) typicalDTO.getOutput();
    }


    /**
     * Should be sub-implemented
     * Get a meaningful result. Also Called from widget
     *
     * @return
     */
    public String getOutputDesc() {
        return "TOBEIMPL";
    }

    public SoulissNode getParentNode() {
        return parentNode;
    }

    public void setParentNode(@NonNull SoulissNode parentNode) {
        if (typicalDTO != null && parentNode.getNodeId() != it.angelic.soulissclient.Constants.MASSIVE_NODE_ID)
            assertEquals(parentNode.getNodeId(), typicalDTO.getNodeId());
        this.parentNode = parentNode;
    }

    public SoulissPreferenceHelper getPrefs() {
        return prefs;
    }

    public void setPrefs(SoulissPreferenceHelper prefs) {
        this.prefs = prefs;
    }

    protected TextView getQuickActionTitle() {
        // Infotext nascosto all'inizio
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);

        final TextView cmd = new TextView(context);
        cmd.setText(/*SoulissClient.getAppContext().getString(R.string.actions)*/"");
        // cmd.setTextSize(ctx.getResources().getDimension(R.dimen.text_size));
        if (prefs != null && prefs.isLightThemeSelected())
            cmd.setTextColor(ContextCompat.getColor(context, R.color.black));

        cmd.setLayoutParams(lp);
        return cmd;
    }

    public short getSlot() {
        return typicalDTO.getSlot();
    }

    public short getTypical() {
        return typicalDTO.getTypical();
    }

    public SoulissTypicalDTO getTypicalDTO() {
        return typicalDTO;
    }

    public void setTypicalDTO(SoulissTypicalDTO typicalDTO) {
        this.typicalDTO = typicalDTO;
    }

    public boolean isEmpty() {
        return typicalDTO.getTypical() == Constants.Typicals.Souliss_T_empty;
    }

    public boolean isRelated() {
        return isRelated;
    }

    public void setRelated(SoulissTypical in) {
        throw new RuntimeException("Can't call setRelated on a single generic typical");
    }

    public boolean isSensor() {
      /*  if (!(this instanceof ISoulissTypicalSensor)) {
            Log.w(Constants.TAG, "SoulissTypical " + getNiceName() + " NOT instanceof ISoulissTypicalSensor");
        }*/
        return (this instanceof ISoulissTypicalSensor);
        // return isSensor;
    }

   /* public void setSensor(boolean isSensor) {
        this.isSensor = isSensor;
    }*/

    public void issueRefresh() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                UDPHelper.pollRequest(prefs, 1, getNodeId());
            }
        }).start();

    }

    /**
     * Decide come interpretare gli out e logga
     */
    public void logTypical() {
        ContentValues values = new ContentValues();

        // wrap values from object
        values.put(SoulissDBOpenHelper.COLUMN_LOG_NODE_ID, getNodeId());
        values.put(SoulissDBOpenHelper.COLUMN_LOG_DATE, Calendar.getInstance().getTime().getTime());
        values.put(SoulissDBOpenHelper.COLUMN_LOG_SLOT, getSlot());
        if (isSensor()) {
            Log.d(it.angelic.soulissclient.Constants.TAG, getDefaultName() + " saving sensor loggi: " + ((ISoulissTypicalSensor) this).getOutputFloat());
            values.put(SoulissDBOpenHelper.COLUMN_LOG_VAL, ((ISoulissTypicalSensor) this).getOutputFloat());
        } else {
            Log.d(it.angelic.soulissclient.Constants.TAG, getDefaultName() + " saving loggi: " + getOutput());
            values.put(SoulissDBOpenHelper.COLUMN_LOG_VAL, getOutput());
        }
        try {
            SoulissDBHelper.getDatabase().insert(SoulissDBOpenHelper.TABLE_LOGS, null, values);
        } catch (SQLiteConstraintException e) {
            // sensori NaN violano il constraint
            Log.e(it.angelic.soulissclient.Constants.TAG, "error saving log: " + e);
        }

    }


    public void refresh() {
        typicalDTO.refresh(this);
    }

    public void setOutputDescView(TextView textStatusVal) {
        textStatusVal.setText(getOutputDesc());
        if (typicalDTO.getOutput() == Constants.Typicals.Souliss_T1n_OffCoil
                || typicalDTO.getOutput() == Constants.Typicals.Souliss_T1n_OffFeedback
                || UNKNOWN.compareTo(getOutputDesc()) == 0
                || NOT_AVAILABLE.compareTo(getOutputDesc()) == 0) {
            textStatusVal.setTextColor(context.getResources().getColor(R.color.std_red));
            textStatusVal.setBackgroundResource(R.drawable.borderedbackoff);
        } else {
            textStatusVal.setTextColor(context.getResources().getColor(R.color.std_green));
            textStatusVal.setBackgroundResource(R.drawable.borderedbackon);
        }
    }

    public void setRelated(boolean isSlave) {
        this.isRelated = isSlave;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (getParentNode() != null) {
            sb.append(getParentNode().getNiceName());
        }
        sb.append(" (").append(context.getString(R.string.slot)).append(" ").append(getSlot()).append(") ")
                .append(" - ").append(getName());
        return sb.toString();
    }
}
