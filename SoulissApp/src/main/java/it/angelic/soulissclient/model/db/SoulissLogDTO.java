package it.angelic.soulissclient.model.db;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.Calendar;
import java.util.Date;

public class SoulissLogDTO {

    Long logId;
    short nodeId;
    short slot;
    float logValue;
    Calendar logTime;

    public SoulissLogDTO() {
        // niente di fatto
    }

    public SoulissLogDTO(Cursor cursor) {
        logId = (cursor.getLong(cursor.getColumnIndex(SoulissDBOpenHelper.COLUMN_LOG_ID)));
        nodeId = (cursor.getShort(cursor.getColumnIndex(SoulissDBOpenHelper.COLUMN_LOG_NODE_ID)));
        slot = (cursor.getShort(cursor.getColumnIndex(SoulissDBOpenHelper.COLUMN_LOG_SLOT)));
        Calendar now = Calendar.getInstance();
        now.setTime(new Date(cursor.getLong(cursor.getColumnIndex(SoulissDBOpenHelper.COLUMN_LOG_DATE))));
        logTime = (now);
        logValue = (cursor.getFloat(cursor.getColumnIndex(SoulissDBOpenHelper.COLUMN_LOG_VAL)));
    }


    /**
     * @return
     */
    public long persist() {
        ContentValues values = new ContentValues();
        // assert (typicalIN.getSlot() != -1);
        // values.put(SoulissDB.COLUMN_COMMAND_ID, typicalIN.getCommandId());
        values.put(SoulissDBOpenHelper.COLUMN_LOG_NODE_ID, nodeId);
        values.put(SoulissDBOpenHelper.COLUMN_LOG_SLOT, slot);
        values.put(SoulissDBOpenHelper.COLUMN_LOG_VAL, logValue);
        values.put(SoulissDBOpenHelper.COLUMN_LOG_DATE, logTime.getTime().getTime());

        long upd;
        if (logId != null) {
            upd = SoulissDBHelper.getDatabase().update(SoulissDBOpenHelper.TABLE_LOGS, values,
                    SoulissDBOpenHelper.COLUMN_LOG_ID + " = " + logId, null);
            if (upd == 0) {
                upd = SoulissDBHelper.getDatabase().insert(SoulissDBOpenHelper.TABLE_LOGS, null, values);
                setLogId(upd);
            }
        } else {
            upd = SoulissDBHelper.getDatabase().insert(SoulissDBOpenHelper.TABLE_LOGS, null, values);
            setLogId(upd);
        }
        return upd;
    }

    public Long getLogId() {
        return logId;
    }

    public void setLogId(Long logId) {
        this.logId = logId;
    }

    public short getNodeId() {
        return nodeId;
    }

    public void setNodeId(short nodeId) {
        this.nodeId = nodeId;
    }

    public short getSlot() {
        return slot;
    }

    public void setSlot(short slot) {
        this.slot = slot;
    }

    public float getLogValue() {
        return logValue;
    }

    public void setLogValue(float logValue) {
        this.logValue = logValue;
    }

    public Calendar getLogTime() {
        return logTime;
    }

    public void setLogTime(Calendar logTime) {
        this.logTime = logTime;
    }


}
