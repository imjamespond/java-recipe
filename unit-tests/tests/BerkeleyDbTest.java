package test;

import java.io.File;
import java.util.List;

import com.sleepycat.bind.EntryBinding;
import com.sleepycat.bind.serial.SerialBinding;
import com.sleepycat.bind.serial.StoredClassCatalog;
import com.sleepycat.je.Cursor;
import com.sleepycat.je.CursorConfig;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.je.EnvironmentMutableConfig;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;

public class BerkeleyDbTest {

/**
 * 嵌入式数据库
 * 
 * @author zhangqinjian
 *
 */

	private static final String RESOURCE = ".//src//util//importData";
	private Environment env;

	private Database db, classDB;

	private StoredClassCatalog classCatalog;
	/**
	 * 初始化，设置数据库的环境和创建数据库
	 */
	public void setUp() throws Exception {

		env = new Environment(new File(".//src//util//importData"), null);
		EnvironmentMutableConfig envMutableConfig = new EnvironmentMutableConfig();
		envMutableConfig.setTxnNoSync(true);
		env.setMutableConfig(envMutableConfig);

		DatabaseConfig dbConfig = new DatabaseConfig();
		dbConfig.setAllowCreate(true);
		db = env.openDatabase(null, "myDB", dbConfig);
		classDB = env.openDatabase(null, "classDB", dbConfig);
		classCatalog = new StoredClassCatalog(classDB);
	}
	// test: put key-value
	public void testPut(String key, People p) throws Exception {
		EntryBinding dataBinding = new SerialBinding(classCatalog,
				People.class);
		DatabaseEntry keyEntry = new DatabaseEntry(key.getBytes("UTF-8"));
		DatabaseEntry dataEntry = new DatabaseEntry();
		dataBinding.objectToEntry(p, dataEntry);
		db.put(null, keyEntry, dataEntry);
	}
	// test: get
	public void testGet() throws Exception {
		EnvironmentConfig envConfig = new EnvironmentConfig();
		envConfig.setAllowCreate(true);
		// envConfig.setReadOnly(true);
		env = new Environment(new File(".//src//util//importData"), envConfig);
		List myDbNames = env.getDatabaseNames();
		System.out.println("Database size: " + myDbNames.size());
		for (int i = 0; i < myDbNames.size(); i++) {
			System.out.println("Database Name: " + (String) myDbNames.get(i));
		}

		DatabaseConfig dbConfig = new DatabaseConfig();
		dbConfig.setAllowCreate(true);
		// dbConfig.setReadOnly(true);
		db = env.openDatabase(null, "myDB", dbConfig);
		classDB = env.openDatabase(null, "classDB", dbConfig);
		classCatalog = new StoredClassCatalog(classDB);

		System.out.println("Db: " + db.count());

		EntryBinding dataBinding = new SerialBinding(classCatalog,
				People.class);

		DatabaseEntry keyEntry = new DatabaseEntry("key".getBytes("UTF-8"));
		DatabaseEntry dataEntry = new DatabaseEntry();
		db.get(null, keyEntry, dataEntry, LockMode.DEFAULT);
		People p = (People) dataBinding.entryToObject(dataEntry);
		System.out.println(p.foo);
	}

	/**
	 * test: read database
	 * @throws Exception
	 */
	public void testStore() throws Exception {

		EnvironmentConfig envConfig = new EnvironmentConfig();
		envConfig.setAllowCreate(true);
		env = new Environment(new File(".//src//util//importData"), envConfig);

		DatabaseConfig dbConfig = new DatabaseConfig();
		dbConfig.setAllowCreate(true);
		db = env.openDatabase(null, "myDB", dbConfig);
		classDB = env.openDatabase(null, "classDB", dbConfig);
		classCatalog = new StoredClassCatalog(classDB);

		EntryBinding dataBinding = new SerialBinding(classCatalog,People.class);

		Cursor cursor = null;
		CursorConfig config = new CursorConfig();
	    config.setReadUncommitted(true);
	    cursor = db.openCursor(null, config);		// open cursor

		try {
			// Database and environment open omitted for brevity
			// Open the cursor. cursor = myDatabase.openCursor(null, null);
			// Cursors need a pair of DatabaseEntry objects to operate. These hold
			// the key and data found at any given position in the database.
			DatabaseEntry foundKey = new DatabaseEntry();
			DatabaseEntry foundData = new DatabaseEntry();
			// To iterate, just call getNext() until the last database record has been
			// read. All cursor operations return an OperationStatus, so just read
			// until we no longer see OperationStatus.SUCCESS
			while (cursor.getNext(foundKey, foundData, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
				// getData() on the DatabaseEntry objects returns the byte array
				// held by that object. We use this to get a String value. If the
				// DatabaseEntry held a byte array representation of some other data
				// type (such as a complex object) then this operation would look
				// considerably different.
				String keyString = new String(foundKey.getData());
				
				People p = (People)dataBinding.entryToObject(foundData);
				System.out.println("Key - Data : " + keyString + " - "
						+ p.foo + "");
			}
		} catch (DatabaseException de) {
			System.err.println("Error accessing database." + de);
		} finally {
			// Cursors must be closed.
			cursor.close();
			db.close();
			classDB.close();
			env.cleanLog();
			env.close();
		}

	}

	public void tearDown() throws Exception {
		db.close();
		classDB.close();
		env.cleanLog();
		env.close();
	}

	public static void main(String[] args) {
		BerkeleyDbTest db = new BerkeleyDbTest();
		try {
			db.setUp();
			People p = new People();
			p.foo = 888;
			
			// t.testPut("5",supp);
			// t.testGet();
			// t.testStore();
                        // t.tearDown();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}

class People{
	public int foo;
	public int bar;
}
