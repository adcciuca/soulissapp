package it.angelic.soulissclient.db;

import java.math.BigDecimal;

import android.database.Cursor;
import android.util.Log;

public class SoulissGraphData {
	
	public double average;
	public double max;
	public double min;
	
	int key;
	
	public SoulissGraphData() {
		super();
	}
	
	public SoulissGraphData(Cursor cursor) {
		//MONTH index zero based
		key = (cursor.getInt(0));
		//average = cursor.getFloat(1);
		//max = cursor.getFloat(3);
		//min = cursor.getFloat(2);
		int decimalPlaces = 1;
		BigDecimal bd = new BigDecimal(cursor.getFloat(2));

		// setScale is immutable
		bd = bd.setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP);
		min = bd.doubleValue();
		bd = new BigDecimal(cursor.getFloat(3));
		bd = bd.setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP);
		max = bd.doubleValue();
		bd = new BigDecimal(cursor.getFloat(1));
		bd = bd.setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP);
		average = bd.doubleValue();
		Log.d("GRAPH", "key="+key+" AVG="+average);
	}


	


}
