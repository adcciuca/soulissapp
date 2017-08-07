package it.angelic.soulissclient.model.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;

import it.angelic.soulissclient.Constants;
import it.angelic.soulissclient.util.FontAwesomeUtil;

/**
 * Classe helper per l'esecuzione di interrogazioni al DB, Inserimenti eccetera
 *
 * @author Ale
 */
public class SoulissDBLowHelper extends SoulissDBHelper {

    public SoulissDBLowHelper(Context context) {
        super(context);
    }

    // Database fields

    public static synchronized SQLiteDatabase getDatabase() {
        return database;
    }


    /**
     * aggiorna l'health dei nodi, pensato per la risposta UDP
     * della health request. L'array di byte contiene la salute dei nodi
     *
     * @return
     */
    public int refreshNodeHealths(ArrayList<Short> healths, int startOffset) {
        int ret = 0;

        for (int i = 0; i < healths.size(); i++) {
            ContentValues values = new ContentValues();
            values.put(SoulissDBOpenHelper.COLUMN_NODE_LASTMOD, Calendar.getInstance().getTime().getTime());
            values.put(SoulissDBOpenHelper.COLUMN_NODE_HEALTH, healths.get(i));
            long upd = database.update(SoulissDBOpenHelper.TABLE_NODES, values, SoulissDBOpenHelper.COLUMN_NODE_ID + " = " + String.valueOf(i + startOffset), null);
            ret += upd;
        }
        return ret;
    }

    /**
     * In seguito alla DB structure, viene aggiornata
     * la struttura dei nodi, senza cancellarla
     *  @param numnodes nodi da creare
     *
     */
    public int createOrUpdateStructure(int numnodes) {
        int ret = 0;
        for (int i = 0; i < numnodes; i++) {
            ContentValues values = new ContentValues();
            // wrap values from object
            values.put(SoulissDBOpenHelper.COLUMN_NODE_LASTMOD, Calendar.getInstance().getTime().getTime());
            values.put(SoulissDBOpenHelper.COLUMN_NODE_ID, i);
            values.put(SoulissDBOpenHelper.COLUMN_NODE_HEALTH, 0);
            //values.put(SoulissDB.COLUMN_NODE_ICON, nodeIN.getIconResourceId());
            int upd = database.update(SoulissDBOpenHelper.TABLE_NODES, values, SoulissDBOpenHelper.COLUMN_NODE_ID + " = " + i,
                    null);
            if (upd == 0) {
                values.put(SoulissDBOpenHelper.COLUMN_NODE_ICON, FontAwesomeUtil.getCodeIndexByFontName(context, "fa-cube"));
                long insertId = database.insert(SoulissDBOpenHelper.TABLE_NODES, null, values);
                Log.d(Constants.TAG, "Node " + i + " insert returned: " + insertId);
                ret++;
            } else
                ret += upd;
        }
        return ret;
    }


}
