package it.angelic.soulissclient.model.db;

import android.content.ContentValues;
import android.database.Cursor;

import java.io.Serializable;

/**
 * Classe per la persistenza sulla tabella dei log
 *
 * @author Ale
 */
public class SoulissTriggerDTO implements Serializable {

    private static final long serialVersionUID = -7375342156362543740L;
    private long triggerId;
    private String op;
    private short inputNodeId;//INPUT NODE
    private short inputSlotlot = -1;//INPUT SLOT
    private int activated;
    private long commandId;
    private float threshVal;

    public SoulissTriggerDTO(Cursor cursor) {

        setTriggerId(cursor.getLong(cursor.getColumnIndex(SoulissDBOpenHelper.COLUMN_TRIGGER_ID)));
        setCommandId(cursor.getLong(cursor.getColumnIndex(SoulissDBOpenHelper.COLUMN_TRIGGER_COMMAND_ID)));
        setInputNodeId(cursor.getShort(cursor.getColumnIndex(SoulissDBOpenHelper.COLUMN_TRIGGER_NODE_ID)));
        setInputSlot(cursor.getShort(cursor.getColumnIndex(SoulissDBOpenHelper.COLUMN_TRIGGER_SLOT)));
        setOp(cursor.getString(cursor.getColumnIndex(SoulissDBOpenHelper.COLUMN_TRIGGER_OP)));
        setThreshVal(cursor.getFloat(cursor.getColumnIndex(SoulissDBOpenHelper.COLUMN_TRIGGER_THRESHVAL)));
        setActive(cursor.getInt(cursor.getColumnIndex(SoulissDBOpenHelper.COLUMN_TRIGGER_ACTIVE)));
    }

    private void setActive(int int1) {
        activated = int1;
    }

    private int getActive() {
        return activated;
    }

    public void setActive(boolean int1) {
        if (int1)
            activated = 1;
        else
            activated = 0;
    }

    public boolean isActivated() {
        return activated == 1;
    }

    public SoulissTriggerDTO() {
        // everything@null
    }

    public long getTriggerId() {
        return triggerId;
    }

    public void setTriggerId(long triggerId) {
        this.triggerId = triggerId;
    }

    public String getOp() {
        return op;
    }

    public void setOp(String op) {
        this.op = op;
    }

    public short getInputNodeId() {
        return inputNodeId;
    }

    public void setInputNodeId(short inputNodeId) {
        this.inputNodeId = inputNodeId;
    }

    public short getInputSlot() {
        return inputSlotlot;
    }

    public void setInputSlot(short inputSlotlot) {
        this.inputSlotlot = inputSlotlot;
    }

    public long getCommandId() {
        return commandId;
    }

    public void setCommandId(long commandId) {
        this.commandId = commandId;
    }

    public float getThreshVal() {
        return threshVal;
    }

    public void setThreshVal(float threshVal) {
        this.threshVal = threshVal;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    /**
     * FIXME: usare SoulissDBHelper.getDatabase().insert
     *
     * @param database
     * @return
     */
    public SoulissTriggerDTO persist(SoulissDBHelper database) {
        ContentValues values = new ContentValues();
        //assertTrue(getSlot() != -1);
        values.put(SoulissDBOpenHelper.COLUMN_TRIGGER_COMMAND_ID, getCommandId());
        values.put(SoulissDBOpenHelper.COLUMN_TRIGGER_SLOT, getInputSlot());
        values.put(SoulissDBOpenHelper.COLUMN_TRIGGER_NODE_ID, getInputNodeId());
        values.put(SoulissDBOpenHelper.COLUMN_TRIGGER_OP, getOp());
        values.put(SoulissDBOpenHelper.COLUMN_TRIGGER_THRESHVAL, getThreshVal());
        values.put(SoulissDBOpenHelper.COLUMN_TRIGGER_ACTIVE, getActive());

        int upd = SoulissDBHelper.getDatabase().update(SoulissDBOpenHelper.TABLE_TRIGGERS, values,
                SoulissDBOpenHelper.COLUMN_TRIGGER_ID + " = " + getTriggerId(), null);
        if (upd == 0) {
            long insertId = SoulissDBHelper.getDatabase().insert(SoulissDBOpenHelper.TABLE_TRIGGERS, null, values);
            return database.getSoulissTrigger(insertId);
        }
        //ce gia
        return database.getSoulissTrigger(getTriggerId());
    }


}
