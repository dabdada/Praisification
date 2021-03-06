package de.dada.praisification.model;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DAO {

  // Database fields
  private SQLiteDatabase db;
  private Database dbHelper;
  private String[] allColumns = { Database.COLUMN_ID,
		  Database.COLUMN_NAME, Database.COLUMN_DRINKS, Database.COLUMN_FOOD,
		  Database.COLUMN_EXTRAS, Database.COLUMN_ARRIVAL, Database.COLUMN_DEPARTURE,
		  Database.COLUMN_PICTURE, Database.COLUMN_BONUS, Database.COLUMN_TREE};

  public DAO(Context context) {
    dbHelper = new Database(context);
  }

  public void open() throws SQLException {
    db = dbHelper.getWritableDatabase();
  }

  public void close() {
    dbHelper.close();
  }

  public void createProtocol(String hostname) {
    ContentValues values = new ContentValues();
    values.put(Database.COLUMN_NAME, hostname);
    values.put(Database.COLUMN_DRINKS, "");
    values.put(Database.COLUMN_FOOD, "");
    values.put(Database.COLUMN_EXTRAS, "");
    values.put(Database.COLUMN_ARRIVAL, "");
    values.put(Database.COLUMN_DEPARTURE, "");
    values.put(Database.COLUMN_PICTURE, "");
    values.put(Database.COLUMN_BONUS, 0);
    values.put(Database.COLUMN_TREE, 0);
    
    long insertId = db.insert(Database.TABLE_PROTOCOLLS, null,
        values);
    Cursor cursor = db.query(Database.TABLE_PROTOCOLLS,
        allColumns, Database.COLUMN_ID + " = " + insertId, null,
        null, null, null);
    cursor.close();
  }

  public void deleteProtocol(ProtocolContent protocol) {
    String name = protocol.getName();
    System.out.println("Protocol deleted with name: " + name);
    db.delete(Database.TABLE_PROTOCOLLS, Database.COLUMN_NAME
        + "=?", new String [] { name });
  }

  public List<ProtocolContent> getAllProtocolls() {
    List<ProtocolContent> protocols = new ArrayList<ProtocolContent>();

    Cursor cursor = db.query(Database.TABLE_PROTOCOLLS,
        allColumns, null, null, null, null, null);

    cursor.moveToFirst();
    while (!cursor.isAfterLast()) {
      ProtocolContent protocol = cursorToProtocol(cursor);
      protocols.add(protocol);
      cursor.moveToNext();
    }
    // make sure to close the cursor
    cursor.close();
    return protocols;
  }
  
  public ProtocolContent getProtocolByName(String name) {
	    ProtocolContent protocol = new ProtocolContent(name);

	    Cursor cursor = db.rawQuery("select * from " + Database.TABLE_PROTOCOLLS +
	    		" where " + Database.COLUMN_NAME + "=" + "'" + name + "'"  , null);

	    if (cursor != null)
        {
         if (cursor.moveToFirst())
            {         
		         protocol = cursorToProtocol(cursor);
		    }
		         cursor.close();
		 }
	    return protocol;
	  }
  
  public void updateProtocol(ProtocolContent protocol) {
	  ContentValues values = new ContentValues();
	  values.put(Database.COLUMN_NAME, protocol.getName());
	  values.put(Database.COLUMN_DRINKS, protocol.getDrinks());
	  values.put(Database.COLUMN_FOOD, protocol.getFood());
	  values.put(Database.COLUMN_EXTRAS, protocol.getExtras());
	  values.put(Database.COLUMN_ARRIVAL, protocol.getArrivalTime());
	  values.put(Database.COLUMN_DEPARTURE, protocol.getDepatureTime());
	  values.put(Database.COLUMN_PICTURE, protocol.getPicturePath());
	  values.put(Database.COLUMN_BONUS, protocol.getRating());
	  values.put(Database.COLUMN_TREE, protocol.getTreeRating());

	  db.update(Database.TABLE_PROTOCOLLS, values, Database.COLUMN_NAME + 
			  " =? ", new String [] {protocol.getName()});
}

  private ProtocolContent cursorToProtocol(Cursor cursor) {
	  ProtocolContent protocol = new ProtocolContent(cursor.getString(cursor.getColumnIndex(Database.COLUMN_NAME)));
	  String drinks = cursor.getString(cursor.getColumnIndex(Database.COLUMN_DRINKS));
      String food = cursor.getString(cursor.getColumnIndex(Database.COLUMN_FOOD));
      String extras = cursor.getString(cursor.getColumnIndex(Database.COLUMN_EXTRAS));
      String arrival = cursor.getString(cursor.getColumnIndex(Database.COLUMN_ARRIVAL));
      String departure = cursor.getString(cursor.getColumnIndex(Database.COLUMN_DEPARTURE));
      String picture = cursor.getString(cursor.getColumnIndex(Database.COLUMN_PICTURE));
      float rating = cursor.getFloat(cursor.getColumnIndex(Database.COLUMN_BONUS));
      float tree = cursor.getFloat(cursor.getColumnIndex(Database.COLUMN_TREE));
      
      protocol.setDrinks(drinks);
      protocol.setFood(food);
      protocol.setExtras(extras);
      protocol.setArrivalTime(arrival);
      protocol.setDepatureTime(departure);
      protocol.setPicturePath(picture);
      protocol.setRating(rating);
      protocol.setTreeRating(tree);
    return protocol;
  }
} 
