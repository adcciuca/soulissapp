package it.angelic.soulissclient.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import it.angelic.soulissclient.Constants;
import it.angelic.soulissclient.R;
import it.angelic.soulissclient.util.FontAwesomeUtil;

/**
 * Classe che rappresenta il DB associato a Souliss client
 * <p/>
 * una tabella per i nodi e una per i tipici, collegate da foreign key. Tutte le
 * tabelle salvano l'ultima modifica come un long poi wrappato in Calendar
 * <p/>
 * tabella commands per esecuzione programmi
 * <p/>
 * tabella triggers fa riferimento alla tabella comandi, rappresenta l'input di
 * threshold
 *
 * @author Ale
 */
public class SoulissDBOpenHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "souliss.db";
    public static final String TABLE_TYPICALS = "typicals";
    public static final String TABLE_NODES = "nodes";
    public static final String TABLE_TRIGGERS = "triggers";
    public static final String TABLE_LOGS = "logs";
    public static final String TABLE_COMMANDS = "commands";
    public static final String TABLE_SCENES = "scenes";
    public static final String TABLE_TAGS = "tags";
    public static final String TABLE_TAGS_TYPICALS = "tags_typicals";
    public static final String TABLE_LAUNCHER = "launcher";
    /*
     * NODES TABLE
     */
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NODE_ID = "intnodeid";
    public static final String COLUMN_NODE_HEALTH = "inthlt";
    public static final String COLUMN_NODE_ICON = "intnodeico";
    public static final String COLUMN_NODE_NAME = "strnodename";
    public static final String COLUMN_NODE_LASTMOD = "cldnodemod";
    public static final String[] ALLCOLUMNS_NODES = {COLUMN_ID, COLUMN_NODE_ID, COLUMN_NODE_HEALTH, COLUMN_NODE_ICON,
            COLUMN_NODE_NAME, COLUMN_NODE_LASTMOD};
    /*
     * TYPICALS TABLE
     *
     * create table typicals( _id integer REFERENCES nodes (intnid), FOREIGN
     * KEY( intnid) per garantire esistenza nodo
     */
    public static final String COLUMN_TYPICAL_NODE_ID = "inttypnodeid";
    public static final String COLUMN_TYPICAL = "inttyp";
    public static final String COLUMN_TYPICAL_SLOT = "inttypslo";
    public static final String COLUMN_TYPICAL_VALUE = "inttypval";
    public static final String COLUMN_TYPICAL_WARNTIMER = "inttypwarn";
    public static final String COLUMN_TYPICAL_INPUT = "inttypcmd";
    public static final String COLUMN_TYPICAL_ICON = "inttypico";
    public static final String COLUMN_TYPICAL_NAME = "strtypname";
    public static final String COLUMN_TYPICAL_LASTMOD = "cldtypmod";
    public static final String COLUMN_TYPICAL_ISFAV = "flgtypisfav";
    public static final String[] ALLCOLUMNS_TYPICALS = {COLUMN_TYPICAL_NODE_ID, COLUMN_TYPICAL, COLUMN_TYPICAL_SLOT,
            COLUMN_TYPICAL_INPUT, COLUMN_TYPICAL_VALUE, COLUMN_TYPICAL_VALUE, COLUMN_TYPICAL_ICON, COLUMN_TYPICAL_ISFAV, COLUMN_TYPICAL_NAME,
            COLUMN_TYPICAL_LASTMOD, COLUMN_TYPICAL_WARNTIMER};
    /*
     * TABELLA COMANDI
     */
    public static final String COLUMN_COMMAND_ID = "cmdid";
    public static final String COLUMN_COMMAND_NODE_ID = "intcmdnodeid";
    public static final String COLUMN_COMMAND_SLOT = "intcmdslo";
    public static final String COLUMN_COMMAND_INPUT = "intcmdval";
    public static final String COLUMN_COMMAND_SCHEDTIME = "cldcmdsched";
    public static final String COLUMN_COMMAND_TYPE = "intcmdtype";
    public static final String COLUMN_COMMAND_EXECTIME = "cldcmdexec";
    public static final String COLUMN_COMMAND_SCHEDTIME_INTERVAL = "intcmdinterval";
    public static final String COLUMN_COMMAND_SCENEID = "intcmdsceneid";
    public static final String[] ALLCOLUMNS_COMMANDS = {COLUMN_COMMAND_ID, COLUMN_COMMAND_NODE_ID,
            COLUMN_COMMAND_SLOT, COLUMN_COMMAND_TYPE, COLUMN_COMMAND_INPUT, COLUMN_COMMAND_SCHEDTIME,
            COLUMN_COMMAND_EXECTIME, COLUMN_COMMAND_SCHEDTIME_INTERVAL, COLUMN_COMMAND_SCENEID};
    /*
     * TABELLA TRIGGERS
     */
    public static final String COLUMN_TRIGGER_ID = "trgid";
    public static final String COLUMN_TRIGGER_COMMAND_ID = "inttrgcmdid";
    public static final String COLUMN_TRIGGER_SLOT = "inttrgslo";
    public static final String COLUMN_TRIGGER_NODE_ID = "inttrgnodeid";
    public static final String COLUMN_TRIGGER_OP = "strtrgop";
    public static final String COLUMN_TRIGGER_THRESHVAL = "inttrgthreshold";
    public static final String COLUMN_TRIGGER_ACTIVE = "flgtrgactivated";
    public static final String[] ALLCOLUMNS_TRIGGERS = {COLUMN_TRIGGER_ID, COLUMN_TRIGGER_COMMAND_ID,
            COLUMN_TRIGGER_SLOT, COLUMN_TRIGGER_NODE_ID, COLUMN_TRIGGER_OP, COLUMN_TRIGGER_ACTIVE,
            COLUMN_TRIGGER_THRESHVAL};
    /* TABELLA LOGGING */
    public static final String COLUMN_LOG_ID = "logid";
    public static final String COLUMN_LOG_NODE_ID = "intlognodeid";
    public static final String COLUMN_LOG_SLOT = "intlogslo";
    public static final String COLUMN_LOG_VAL = "flologval";
    public static final String COLUMN_LOG_DATE = "cldlogwhen";
    public static final String[] ALLCOLUMNS_LOGS = {COLUMN_LOG_ID, COLUMN_LOG_NODE_ID, COLUMN_LOG_SLOT,
            COLUMN_LOG_VAL, COLUMN_LOG_DATE};
    /* TABELLA SCENE */
    public static final String COLUMN_SCENE_ID = "sceneid";
    public static final String COLUMN_SCENE_NAME = "strscenename";
    public static final String COLUMN_SCENE_ICON = "intsceneico";
    public static final String[] ALLCOLUMNS_SCENES = {COLUMN_SCENE_ID, COLUMN_SCENE_NAME, COLUMN_SCENE_ICON};
    /*TABELLA TAG*/
    public static final String COLUMN_TAG_ID = "inttagid";
    public static final String COLUMN_TAG_NAME = "strtagname";
    public static final String COLUMN_TAG_ICONID = "inttagico";
    public static final String COLUMN_TAG_IMGPTH = "strtagpat";
    public static final String COLUMN_TAG_ORDER = "inttagord";
    public static final String COLUMN_TAG_FATHER_ID = "inttagfathId";
    //   + " FOREIGN KEY( "+ COLUMN_COMMAND_NODE_ID + "," + COLUMN_COMMAND_SLOT + ") " + " REFERENCES " + TABLE_TYPICALS + " ("
    //   + COLUMN_TYPICAL_NODE_ID + "," + COLUMN_TYPICAL_SLOT + ") " + ");";
    public static final String[] ALLCOLUMNS_TAGS = {COLUMN_TAG_ID, COLUMN_TAG_NAME,
            COLUMN_TAG_ICONID, COLUMN_TAG_IMGPTH, COLUMN_TAG_ORDER, COLUMN_TAG_FATHER_ID};


    /*
        TABELLA LAUNCHER
       */
    public static final String COLUMN_LAUNCHER_ID = "intlauid";
    public static final String COLUMN_LAUNCHER_TYPE = "intlauType";
    public static final String COLUMN_LAUNCHER_NODE_ID = "intlaunodeid";

    public static final String COLUMN_LAUNCHER_SCENE_ID = "intlausceneid";
    public static final String COLUMN_LAUNCHER_SLOT_ID = "intlauslotid";//se not null ->typico
    public static final String COLUMN_LAUNCHER_TAG_ID = "intlautagid";
    public static final String COLUMN_LAUNCHER_ORDER = "intlauorder";
    public static final String COLUMN_LAUNCHER_TITLE = "intlautitle";
    public static final String COLUMN_LAUNCHER_DESC = "intlaudesc";
    public static final String COLUMN_LAUNCHER_FULL_SPAN = "intlaufullspan";
    public static final String[] ALLCOLUMNS_LAUNCHER = {COLUMN_LAUNCHER_ID, COLUMN_LAUNCHER_TYPE,
            COLUMN_LAUNCHER_NODE_ID, COLUMN_LAUNCHER_SCENE_ID, COLUMN_LAUNCHER_SLOT_ID, COLUMN_LAUNCHER_TAG_ID, COLUMN_LAUNCHER_ORDER, COLUMN_LAUNCHER_TITLE, COLUMN_LAUNCHER_DESC, COLUMN_LAUNCHER_FULL_SPAN};
    /*
     * TABELLA TAG'TYP
     * tabella di relazione n a m per TAG <-> typical
     */
    public static final String COLUMN_TAG_TYP_SLOT = "inttagtypslo";
    public static final String COLUMN_TAG_TYP_NODE_ID = "inttagtypnodeid";
    public static final String COLUMN_TAG_TYP_TAG_ID = "inttagtagid";
    public static final String COLUMN_TAG_TYP_PRIORITY = "inttagtyppriority";
    public static final String[] ALLCOLUMNS_TAGS_TYPICAL = {COLUMN_TAG_TYP_SLOT,
            COLUMN_TAG_TYP_NODE_ID, COLUMN_TAG_TYP_TAG_ID, COLUMN_TAG_TYP_PRIORITY};
    private static final int DATABASE_VERSION = 35;
    // Database creation sql statement
    private static final String DATABASE_CREATE_NODES = "create table " + TABLE_NODES
            + "( "
            // COLUMN DEF
            + COLUMN_ID + " integer primary key autoincrement, " + COLUMN_NODE_ID + " integer UNIQUE, "
            + COLUMN_NODE_HEALTH + " integer, " + COLUMN_NODE_ICON + " integer, " + COLUMN_NODE_NAME + " TEXT, "
            + COLUMN_NODE_LASTMOD + " integer not null" + ");";
    private static final String DATABASE_CREATE_TYPICALS = "create table "
            + TABLE_TYPICALS
            + "( "
            + COLUMN_TYPICAL_NODE_ID + " integer not null, "
            + COLUMN_TYPICAL + " integer not null, "
            + COLUMN_TYPICAL_SLOT + " integer not null, "
            + COLUMN_TYPICAL_INPUT + " integer, "
            + COLUMN_TYPICAL_VALUE + " integer not null, "
            + COLUMN_TYPICAL_ICON + " integer, "
            + COLUMN_TYPICAL_ISFAV + " integer, "
            + COLUMN_TYPICAL_NAME + " TEXT, "
            + COLUMN_TYPICAL_LASTMOD + " integer not null,"
            + COLUMN_TYPICAL_WARNTIMER + " integer, "
            + " FOREIGN KEY( " + COLUMN_TYPICAL_NODE_ID
            + ") REFERENCES " + TABLE_NODES + " (" + COLUMN_TYPICAL_NODE_ID + "), "
            + "CONSTRAINT typ_keys PRIMARY KEY(" + COLUMN_TYPICAL_NODE_ID + "," + COLUMN_TYPICAL_SLOT + ")" + ");";
    private static final String DATABASE_CREATE_COMMANDS = "create table "
            + TABLE_COMMANDS
            + "( "
            +
            COLUMN_COMMAND_ID + " integer primary key autoincrement, "
            + COLUMN_COMMAND_NODE_ID + " integer not null, "
            // TIPICO in caso di comando massivo
            + COLUMN_COMMAND_SLOT + " integer not null, "
            + COLUMN_COMMAND_TYPE + " integer not null, "
            + COLUMN_COMMAND_INPUT + " integer not null, "
            + COLUMN_COMMAND_SCHEDTIME + " integer, "
            + COLUMN_COMMAND_EXECTIME + " integer, "
            // Se il comando appartiene a scenario, rappresenta l'ordine di
            // esecuzione
            + COLUMN_COMMAND_SCHEDTIME_INTERVAL + " integer, "
            + COLUMN_COMMAND_SCENEID + " integer,"
            + " FOREIGN KEY( " + COLUMN_COMMAND_NODE_ID + "," + COLUMN_COMMAND_SLOT + ") " + " REFERENCES " + TABLE_TYPICALS + " ("
            + COLUMN_TYPICAL_NODE_ID + "," + COLUMN_TYPICAL_SLOT + ") " + ");";
    private static final String DATABASE_CREATE_TRIGGERS = "create table "
            + TABLE_TRIGGERS
            + "( "
            + COLUMN_TRIGGER_ID + " integer primary key autoincrement, "
            + COLUMN_TRIGGER_COMMAND_ID + " integer not null, "
            + COLUMN_TRIGGER_SLOT + " integer not null, "
            + COLUMN_TRIGGER_NODE_ID + " integer not null, "
            + COLUMN_TRIGGER_OP + " integer not null, "
            + COLUMN_TRIGGER_ACTIVE + " integer not null, "
            + COLUMN_TRIGGER_THRESHVAL + " REAL not null, "
            + " FOREIGN KEY( " + COLUMN_TRIGGER_COMMAND_ID
            + " ) REFERENCES " + TABLE_COMMANDS + " ("
            + COLUMN_COMMAND_ID + ") " + ");";
    private static final String DATABASE_CREATE_LOGS = "create table " + TABLE_LOGS
            + "( "
            // COLUMN DEF
            + COLUMN_LOG_ID + " integer primary key autoincrement, "
            + COLUMN_LOG_NODE_ID + " integer not null, "
            + // command to trig
            COLUMN_LOG_SLOT + " integer not null, "
            + // input slot
            COLUMN_LOG_VAL + " float not null, "
            + COLUMN_LOG_DATE + " integer not null, "
            + " FOREIGN KEY( " + COLUMN_LOG_NODE_ID + "," + COLUMN_LOG_SLOT + ") "
            + " REFERENCES " + TABLE_TYPICALS + " (" + COLUMN_TYPICAL_NODE_ID + "," + COLUMN_TYPICAL_SLOT + ") " + ");";
    private static final String DATABASE_CREATE_SCENES = "create table " + TABLE_SCENES + "( "
            // COLUMN DEF
            + COLUMN_SCENE_ID + " integer primary key autoincrement, " + COLUMN_SCENE_ICON + " integer, "
            + COLUMN_SCENE_NAME + " textname " + // command to trig

            ");";
    private static final String DATABASE_CREATE_TAGS = "create table "
            + TABLE_TAGS
            + "( "
            + COLUMN_TAG_ID + " integer primary key autoincrement, "
            + COLUMN_TAG_NAME + " TEXT, "
            + COLUMN_TAG_ICONID + " integer not null, "
            + COLUMN_TAG_IMGPTH + " TEXT, "
            + COLUMN_TAG_ORDER + " integer, "
            + COLUMN_TAG_FATHER_ID + " integer, "
            + " FOREIGN KEY( " + COLUMN_TAG_FATHER_ID
            + " ) REFERENCES " + TABLE_TAGS + " ("
            + COLUMN_TAG_ID + ") ON DELETE CASCADE "
            + ");";
    private static final String DATABASE_CREATE_LAUNCHER = "create table "
            + TABLE_LAUNCHER
            + "( "
            + COLUMN_LAUNCHER_ID + " integer primary key autoincrement, "
            + COLUMN_LAUNCHER_TYPE + " integer not null, "
            + COLUMN_LAUNCHER_ORDER + " integer not null, "
            + COLUMN_LAUNCHER_NODE_ID + " integer, "
            + COLUMN_LAUNCHER_SLOT_ID + " integer, "
            + COLUMN_LAUNCHER_TAG_ID + " integer, "
            + COLUMN_LAUNCHER_SCENE_ID + " integer, "
            + COLUMN_LAUNCHER_TITLE + " TEXT, "
            + COLUMN_LAUNCHER_DESC + " TEXT, "
            + COLUMN_LAUNCHER_FULL_SPAN + " integer, "
            + " FOREIGN KEY( " + COLUMN_LAUNCHER_NODE_ID
            + " ) REFERENCES " + TABLE_NODES + " ("
            + COLUMN_NODE_ID + "), "
            + " FOREIGN KEY( " + COLUMN_LAUNCHER_SCENE_ID
            + " ) REFERENCES " + TABLE_SCENES + " ("
            + COLUMN_SCENE_ID + "), "
            + " FOREIGN KEY( " + COLUMN_LAUNCHER_TAG_ID
            + " ) REFERENCES " + TABLE_TAGS + " ("
            + COLUMN_TAG_ID + "), "
            + " FOREIGN KEY( " + COLUMN_LAUNCHER_SLOT_ID
            + " ) REFERENCES " + TABLE_TYPICALS + " ("
            + COLUMN_TYPICAL_SLOT + ") "
            + ");";
    private static final String DATABASE_CREATE_TAG_TYPICAL = "create table "
            + TABLE_TAGS_TYPICALS
            + "( "
            + COLUMN_TAG_TYP_SLOT + " integer not null, "
            + COLUMN_TAG_TYP_NODE_ID + " integer not null, "
            + COLUMN_TAG_TYP_TAG_ID + " integer not null, "
            + COLUMN_TAG_TYP_PRIORITY + " integer not null DEFAULT 0 , "
            + " PRIMARY KEY (" + COLUMN_TAG_TYP_NODE_ID + "," + COLUMN_TAG_TYP_SLOT + "," + COLUMN_TAG_TYP_TAG_ID + "), "
            + " FOREIGN KEY ( " + COLUMN_TAG_TYP_TAG_ID + "), "
            + " REFERENCES " + TABLE_TAGS + " (" + COLUMN_TYPICAL_NODE_ID + "), "
            + " FOREIGN KEY ( " + COLUMN_TAG_TYP_NODE_ID + "," + COLUMN_TAG_TYP_SLOT + ") "
            + " REFERENCES " + TABLE_TYPICALS + " (" + COLUMN_TYPICAL_NODE_ID + "," + COLUMN_TYPICAL_SLOT + ") "
            + ");";
    public static long FAVOURITES_TAG_ID = 0;
    private Context context;


//AUTOF

    /**
     * super wrapper createDB
     *
     * @param context
     */
    public SoulissDBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    private void doDefaultInserts(SQLiteDatabase database) {
    /* DEFAULT TAG , Order=0 */
        database.execSQL("INSERT INTO " + TABLE_TAGS + " (" + COLUMN_TAG_ID + "," + COLUMN_TAG_NAME + "," + COLUMN_TAG_ORDER + "," + COLUMN_TAG_ICONID
                + ") VALUES (" + FAVOURITES_TAG_ID + ",'" + context.getResources().getString(R.string.favourites) + "'," + 0 + ","
                + FontAwesomeUtil.getCodeIndexByFontName(context, "fa-heart-o") + ")");
        /* DEFAULT SCENE */
        database.execSQL("INSERT INTO " + TABLE_SCENES + " (" + COLUMN_SCENE_NAME + "," + COLUMN_SCENE_ICON
                + ") VALUES ('" + context.getResources().getString(R.string.scene_turnoff_lights) + "',"
                + FontAwesomeUtil.getCodeIndexByFontName(context, "fa-toggle-off") + ")");
        database.execSQL("INSERT INTO " + TABLE_SCENES + " (" + COLUMN_SCENE_NAME + "," + COLUMN_SCENE_ICON
                + ") VALUES ('" + context.getResources().getString(R.string.scene_turnon_lights) + "',"
                + FontAwesomeUtil.getCodeIndexByFontName(context, "fa-toggle-on") + ")");
        // Comandi massivi di default OFF, NODEID = -1
        database.execSQL("INSERT INTO " + TABLE_COMMANDS + " (" + COLUMN_COMMAND_NODE_ID + "," + COLUMN_COMMAND_SLOT
                + "," + COLUMN_COMMAND_INPUT + "," + COLUMN_COMMAND_SCENEID + "," + COLUMN_COMMAND_TYPE + ","
                + COLUMN_COMMAND_SCHEDTIME_INTERVAL + ")"
                + " VALUES " + "(" + Constants.MASSIVE_NODE_ID + ", " + Constants.Typicals.Souliss_T11 + "," + Constants.Typicals.Souliss_T1n_OffCmd
                + ",1," + Constants.MASSIVE_NODE_ID + ",200)");
        database.execSQL("INSERT INTO " + TABLE_COMMANDS + " (" + COLUMN_COMMAND_NODE_ID + "," + COLUMN_COMMAND_SLOT
                + "," + COLUMN_COMMAND_INPUT + "," + COLUMN_COMMAND_SCENEID + "," + COLUMN_COMMAND_TYPE + ","
                + COLUMN_COMMAND_SCHEDTIME_INTERVAL + ")"
                + " VALUES " + "(" + Constants.MASSIVE_NODE_ID + ", " + Constants.Typicals.Souliss_T12 + "," + Constants.Typicals.Souliss_T1n_OffCmd
                + ",1," + Constants.MASSIVE_NODE_ID + ",400)");
        database.execSQL("INSERT INTO " + TABLE_COMMANDS + " (" + COLUMN_COMMAND_NODE_ID + "," + COLUMN_COMMAND_SLOT//RGB
                + "," + COLUMN_COMMAND_INPUT + "," + COLUMN_COMMAND_SCENEID + "," + COLUMN_COMMAND_TYPE + ","
                + COLUMN_COMMAND_SCHEDTIME_INTERVAL + ")"
                + " VALUES " + "(" + Constants.MASSIVE_NODE_ID + ", " + Constants.Typicals.Souliss_T16 + "," + Constants.Typicals.Souliss_T1n_OffCmd
                + ",1," + Constants.MASSIVE_NODE_ID + ",600)");
        // Comandi massivi di default ON
        database.execSQL("INSERT INTO " + TABLE_COMMANDS + " (" + COLUMN_COMMAND_NODE_ID + "," + COLUMN_COMMAND_SLOT
                + "," + COLUMN_COMMAND_INPUT + "," + COLUMN_COMMAND_SCENEID + "," + COLUMN_COMMAND_TYPE + ","
                + COLUMN_COMMAND_SCHEDTIME_INTERVAL + ")"
                + " VALUES " + "(" + Constants.MASSIVE_NODE_ID + ", " + Constants.Typicals.Souliss_T11 + "," + Constants.Typicals.Souliss_T1n_OnCmd
                + ",2," + Constants.MASSIVE_NODE_ID + ",200)");
        database.execSQL("INSERT INTO " + TABLE_COMMANDS + " (" + COLUMN_COMMAND_NODE_ID + "," + COLUMN_COMMAND_SLOT
                + "," + COLUMN_COMMAND_INPUT + "," + COLUMN_COMMAND_SCENEID + "," + COLUMN_COMMAND_TYPE + ","
                + COLUMN_COMMAND_SCHEDTIME_INTERVAL + ")"
                + " VALUES " + "(" + Constants.MASSIVE_NODE_ID + ", " + Constants.Typicals.Souliss_T12 + "," + Constants.Typicals.Souliss_T1n_OnCmd
                + ",2," + Constants.MASSIVE_NODE_ID + ",400)");
        database.execSQL("INSERT INTO " + TABLE_COMMANDS + " (" + COLUMN_COMMAND_NODE_ID + "," + COLUMN_COMMAND_SLOT
                + "," + COLUMN_COMMAND_INPUT + "," + COLUMN_COMMAND_SCENEID + "," + COLUMN_COMMAND_TYPE + ","
                + COLUMN_COMMAND_SCHEDTIME_INTERVAL + ")"
                + " VALUES " + "(" + Constants.MASSIVE_NODE_ID + ", " + Constants.Typicals.Souliss_T16 + "," + Constants.Typicals.Souliss_T1n_OnCmd
                + ",2," + Constants.MASSIVE_NODE_ID + ",600)");
    }

    protected void dropCreate(SQLiteDatabase db) {
        Log.w(SoulissDBOpenHelper.class.getName(), "DB dropCreate " + db.getPath());
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LAUNCHER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRIGGERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMMANDS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCENES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TAGS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TAGS_TYPICALS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TYPICALS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NODES);

        onCreate(db);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        Log.w(SoulissDBOpenHelper.class.getName(), "DB on create " + database.getPath());
        database.execSQL(DATABASE_CREATE_NODES);
        database.execSQL(DATABASE_CREATE_TYPICALS);
        database.execSQL(DATABASE_CREATE_COMMANDS);
        database.execSQL(DATABASE_CREATE_TRIGGERS);
        database.execSQL(DATABASE_CREATE_LOGS);
        database.execSQL(DATABASE_CREATE_SCENES);
        database.execSQL(DATABASE_CREATE_TAGS);
        database.execSQL(DATABASE_CREATE_LAUNCHER);
        database.execSQL(DATABASE_CREATE_TAG_TYPICAL);
        doDefaultInserts(database);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(SoulissDBOpenHelper.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion);
        boolean dropNeeded = true;
        if (oldVersion <= 30 && newVersion == DATABASE_VERSION) {
            //added warn TIMER
            try {
                String upgradeQuery = "ALTER TABLE " + TABLE_TYPICALS + " ADD COLUMN " + COLUMN_TYPICAL_WARNTIMER + " INTEGER";
                db.execSQL(upgradeQuery);

            } catch (Exception cazzo) {
                //somehow already existing, just log
                Log.e(SoulissDBOpenHelper.class.getName(), "Upgrading database ERROR:" + cazzo.getMessage());
            }
        }
        if (oldVersion <= 34 && newVersion == DATABASE_VERSION) {

            try {
                //font awesome now
                db.execSQL("UPDATE " + TABLE_TYPICALS + " SET " + COLUMN_TYPICAL_ICON + " = null");
                db.execSQL("UPDATE " + TABLE_NODES + " SET " + COLUMN_NODE_ICON + " = null");
                db.execSQL("UPDATE " + TABLE_SCENES + " SET " + COLUMN_SCENE_ICON + " = null");
                //costretto a droppare i TAGS
                //added TABLE_LAUNCHER and related
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_LAUNCHER);
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_TAGS_TYPICALS);
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_TAGS);

                db.execSQL(DATABASE_CREATE_TAGS);
                db.execSQL(DATABASE_CREATE_TAG_TYPICAL);
                db.execSQL(DATABASE_CREATE_LAUNCHER);

                /* DEFAULT TAG , Order=0 */
                db.execSQL("INSERT INTO " + TABLE_TAGS + " (" + COLUMN_TAG_ID + "," + COLUMN_TAG_NAME + "," + COLUMN_TAG_ORDER + "," + COLUMN_TAG_ICONID
                        + ") VALUES (" + FAVOURITES_TAG_ID + ",'" + context.getResources().getString(R.string.favourites) + "'," + 0 + ","
                        + FontAwesomeUtil.getCodeIndexByFontName(context, "fa-heart-o") + ")");

                dropNeeded = false;
            } catch (Exception cazzo) {
                //somehow already existing, just log
                Log.e(SoulissDBOpenHelper.class.getName(), "Upgrading database ERROR:" + cazzo.getMessage());
                dropNeeded = true;
            }
        }

        if (dropNeeded) {
            Log.e(SoulissDBOpenHelper.class.getName(), "Upgrading database went wrong, DROPPI&RE-CREATE");
            dropCreate(db);
        }


    }


}