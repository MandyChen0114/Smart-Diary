package zju.smartdiary;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDataBaseAdapter {
	private static final String	TAG				= "MyDataBaseAdapter";// 用于打印log
	private Context				mContext		= null;
	private static final int	DB_VERSION		= 1;
	private static final String	DB_NAME			= "SmartDiary.db";	
	private static final String	DB_TABLE1		= "OriginalData";		
	private static final String	DB_TABLE2		= "SpeculateRecord";	
	
	//表1的字段
	public static final String	KEY_ID		= "_id";												
	public static final String	KEY_TIMESTAMP		= "timestamp";												
	public static final String	KEY_SCREENSTATE		= "screenState";
	public static final String	KEY_GEOLAT		= "geoLat";	
	public static final String	KEY_GEOLNG		= "geoLng";	
	public static final String	KEY_ADDR		= "addr";
	
	//表2的字段
//	public static final String	KEY_ID		= "_id";
	public static final String	KEY_DATE		= "date";
	
	public static final String	KEY_STUDYTIME		= "studyTime";
	public static final String	KEY_SLEEPTIME		= "sleepTime";
	public static final String	KEY_SPORTTIME		= "sportTime";
	public static final String	KEY_MEALTIME		= "mealTime";
	public static final String	KEY_ENTERTAINMENTTIME		= "entertainmentTime";
	
	public static final String	KEY_STUDYQUALITY		= "studyQuality";	
	public static final String	KEY_SLEEPQUALITY		= "sleepQuality";	
	public static final String	KEY_SPORTQUALITY		= "sportQuality";	
	public static final String	KEY_MEALQUALITY		= "mealQuality";

//	public static final String	KEY_PLAYPHONETIME		= "playPhoneTime";
	
//	public static final String	KEY_FALLASLEEPMOMENT		= "fallAsleepMoment";
//	public static final String	KEY_GETUPMOMENT		= "getUpMoment";
//	public static final String	KEY_BREAKFAST1		= "breakfast1";
//	public static final String	KEY_BREAKFAST2		= "breakfast2";
//	public static final String	KEY_LUNCH1		= "lunch1";
//	public static final String	KEY_LUNCH2		= "lunch2";
//	public static final String	KEY_DINNER1		= "dinner1";
//	public static final String	KEY_DINNER2		= "dinner2";	
	
//	public static final String	KEY_PLAYPHONENUM		= "playPhoneNum";
//	public static final String	KEY_WAKEUPNUM		= "wakeUpNum";
//	public static final String	KEY_SPORTDIS		= "sportDis";

	
	//创建一个表
	private static final String	DB_CREATE1		= 
			"CREATE TABLE " + DB_TABLE1 + " (" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," 
			+ KEY_TIMESTAMP + " TIMESTAMP NOT NULL DEFAULT (datetime('now','localtime'))," + KEY_GEOLAT + " DECIMAL(7,4)," 
			+ KEY_GEOLNG + " DECIMAL(7,4)," + KEY_ADDR + " VARCHAR(20),"+ KEY_SCREENSTATE + " BOOLEAN)";
	
	private static final String	DB_CREATE2		= 
			"CREATE TABLE " + DB_TABLE2 + " (" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," 
			+ KEY_DATE + " DATE NOT NULL DEFAULT (date('now','localtime'))," + KEY_STUDYTIME + " INTEGER," 
			+ KEY_SLEEPTIME + " INTEGER," + KEY_SPORTTIME + " INTEGER," + KEY_MEALTIME + " INTEGER," + KEY_ENTERTAINMENTTIME + " INTEGER,"
			+ KEY_STUDYQUALITY + " INTEGER, "+ KEY_SLEEPQUALITY + " INTEGER, "+ KEY_SPORTQUALITY + " INTEGER, " + KEY_MEALQUALITY + " INTEGER)";

	// 执行open（）打开数据库时，保存返回的数据库对象
	private SQLiteDatabase		mSQLiteDatabase	= null;
	// 由SQLiteOpenHelper继承过来
	private DatabaseHelper		mDatabaseHelper	= null;
	
	
	private static class DatabaseHelper extends SQLiteOpenHelper
	{
		/* 构造函数-创建一个数据库 */
		DatabaseHelper(Context context)
		{
			//当调用getWritableDatabase() 
			//或 getReadableDatabase()方法时
			//则创建一个数据库
			super(context, DB_NAME, null, DB_VERSION);						
		}

		/* 创建一个表 */
		@Override
		public void onCreate(SQLiteDatabase db)
		{
//			db.execSQL("DROP TABLE IF EXISTS OriginalData");//删！！！
			
			// 数据库没有表时创建一个
			db.execSQL(DB_CREATE1);
			db.execSQL(DB_CREATE2);
		}

		/* 升级数据库 */
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
		{
			db.execSQL("DROP TABLE IF EXISTS notes");
			onCreate(db);
		}
	}
	
	/* 构造函数-取得Context */
	public MyDataBaseAdapter(Context context)
	{
		mContext = context;
	}


	// 打开数据库，返回数据库对象
	public void open() throws SQLException
	{
		mDatabaseHelper = new DatabaseHelper(mContext);
		mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
	}


	// 关闭数据库
	public void close()
	{
		mDatabaseHelper.close();
	}

	/* 插入一条数据 */
	public long insertData1(double geoLat,double geoLng,String addr,int screenState)
	{
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_GEOLAT, geoLat);
		initialValues.put(KEY_GEOLNG, geoLng);
		initialValues.put(KEY_ADDR, addr);
		initialValues.put(KEY_SCREENSTATE, screenState);

		return mSQLiteDatabase.insert(DB_TABLE1, KEY_ID, initialValues);
	}

	/* 插入一条数据 */
	public long insertData2(long studyTime,long sleepTime,
			long sportTime,long mealTime,long entertainmentTime,int studyQuality,
			int sleepQuality,int sportQuality,int mealQuality)
	{
		ContentValues initialValues = new ContentValues();
//		initialValues.put(KEY_DATE, date);
		initialValues.put(KEY_STUDYTIME, studyTime);
		initialValues.put(KEY_SLEEPTIME, sleepTime);
		initialValues.put(KEY_SPORTTIME, sportTime);
		initialValues.put(KEY_MEALTIME, mealTime);
		initialValues.put(KEY_ENTERTAINMENTTIME, entertainmentTime);
		initialValues.put(KEY_STUDYQUALITY, studyQuality);
		initialValues.put(KEY_SLEEPQUALITY, sleepQuality);
		initialValues.put(KEY_SPORTQUALITY, sportQuality);
		initialValues.put(KEY_MEALQUALITY, mealQuality);

		return mSQLiteDatabase.insert(DB_TABLE2, KEY_ID, initialValues);
	}
	
//	/* 删除一条数据 */
//	public boolean deleteData(long rowId)
//	{
//		return mSQLiteDatabase.delete(DB_TABLE, KEY_ID + "=" + rowId, null) > 0;
//	}
//
//	/* 通过Cursor查询所有数据 */
//	public Cursor fetchAllData()
//	{
//		return mSQLiteDatabase.query(DB_TABLE, new String[] { KEY_ID, KEY_NUM, KEY_DATA }, null, null, null, null, null);
//	}
//
	/* 查询指定数据 */
	public Cursor fetchData1(int year,int month,int day) throws SQLException
	{
		Calendar cal1=Calendar.getInstance();
		Calendar cal2=Calendar.getInstance();
		cal1.set(year, month, day-1, 22, 0, 0);
		cal2.set(year, month, day, 21, 59, 59);
		
		Timestamp timestamp1 = new Timestamp(cal1.getTime().getTime());
		Timestamp timestamp2 = new Timestamp(cal2.getTime().getTime());

		Cursor mCursor =

		mSQLiteDatabase.query(true, DB_TABLE1, new String[] {KEY_ID, KEY_TIMESTAMP, KEY_GEOLAT, KEY_GEOLNG, KEY_ADDR, KEY_SCREENSTATE }, 
				"datetime(timestamp)  > datetime('" + timestamp1 +"') and datetime(timestamp)  < datetime('" + timestamp2 +"')",null,null,null,null,null); 

		if (mCursor != null)
		{
			mCursor.moveToFirst();
		}
		return mCursor;

	}
	
	/* 查询指定数据 */
	public Cursor fetchData(int year,int month,int day) throws SQLException
	{
		Calendar c = Calendar.getInstance();
		c.set(year, month, day);
		Date date=new Date(c.getTime().getTime());
//		Date date= new Date(year,month,day);//??
		Cursor mCursor =
		mSQLiteDatabase.query(true, DB_TABLE2, new String[] {KEY_ID, KEY_DATE, KEY_STUDYTIME, KEY_SLEEPTIME, 
				KEY_SPORTTIME, KEY_MEALTIME, KEY_ENTERTAINMENTTIME, KEY_STUDYQUALITY, KEY_SLEEPQUALITY, KEY_SPORTQUALITY, KEY_MEALQUALITY }, 
				"date  ='" + date +"'" ,null,null,null,null,null); 
//				"date(date)  = date('" + date +"')" ,null,null,null,null,null); 

		if (mCursor != null)
		{
			mCursor.moveToFirst();
		}
		return mCursor;

	}
	
	/* 查询指定数据 */
	public Cursor fetchDataSeven(int year,int month,int day) throws SQLException
	{
		Calendar c = Calendar.getInstance();
		c.set(year, month, day-7);
		Date date=new Date(c.getTime().getTime());

		Cursor mCursor =
		mSQLiteDatabase.query(true, DB_TABLE2, new String[] {KEY_ID, KEY_DATE, KEY_STUDYTIME, KEY_SLEEPTIME, 
				KEY_SPORTTIME, KEY_MEALTIME, KEY_ENTERTAINMENTTIME, KEY_STUDYQUALITY, KEY_SLEEPQUALITY, KEY_SPORTQUALITY, KEY_MEALQUALITY }, 
				"datetime(date)  > datetime('" + date +"') ",null,null,null,null,null); 

		if (mCursor != null)
		{
			mCursor.moveToFirst();
		}
		return mCursor;

	}
//
//	/* 更新一条数据 */
//	public boolean updateData(long rowId, int num, String data)
//	{
//		ContentValues args = new ContentValues();
//		args.put(KEY_NUM, num);
//		args.put(KEY_DATA, data);
//
//		return mSQLiteDatabase.update(DB_TABLE, args, KEY_ID + "=" + rowId, null) > 0;
//	}
	
}

