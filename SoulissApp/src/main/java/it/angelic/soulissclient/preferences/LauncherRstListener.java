package it.angelic.soulissclient.preferences;

import android.app.Activity;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.util.List;

import androidx.preference.Preference;
import it.angelic.soulissclient.Constants;
import it.angelic.soulissclient.SoulissApp;
import it.angelic.soulissclient.helpers.SoulissPreferenceHelper;
import it.angelic.soulissclient.model.LauncherElement;
import it.angelic.soulissclient.model.SoulissModelException;
import it.angelic.soulissclient.model.db.SoulissDBHelper;
import it.angelic.soulissclient.model.db.SoulissDBLauncherHelper;

public class LauncherRstListener implements androidx.preference.Preference.OnPreferenceClickListener {

    private static final String DB_BACKUP_FORMAT = ".csv";
    private static final int DIALOG_LOAD_FILE = 1000;
    private String mChosenFile;
    // EXPORT
    private String[] mFileList;
    private Activity parent;

    public LauncherRstListener(Activity parent) {
        super();
        this.parent = parent;
        SoulissPreferenceHelper opzioni = SoulissApp.getOpzioni();
        SoulissDBHelper datasource = new SoulissDBHelper(parent);
        File mPath = new File(Environment.getExternalStorageDirectory() + Constants.EXTERNAL_EXP_FOLDER);
    }

    @Override
    public boolean onPreferenceClick(Preference arg0) {
        if ("rstlauncher".equals(arg0.getKey()))
            resetDefaultPref();

        return true;

    }

    /**
     * Aggiunge i default mancanti
     */
    protected void resetDefaultPref() {
        SoulissDBLauncherHelper database = new SoulissDBLauncherHelper(parent);
        List<LauncherElement> po = database.getLauncherItems(parent);
        List<LauncherElement> def = database.getDefaultStaticDBLauncherElements();
        int cont = 0;
        //contains funziona con lo static solo perche id nulli da getDefaultStaticDBLauncherElements
        for (LauncherElement la : def) {
            if (!po.contains(la)) {
                try {
                    database.addElement(la);
                    cont++;
                } catch (SoulissModelException e) {
                    Toast.makeText(parent, "Errore salvataggio tile", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }
        Toast.makeText(parent, cont + " default tiles restored", Toast.LENGTH_SHORT).show();
    }


}
