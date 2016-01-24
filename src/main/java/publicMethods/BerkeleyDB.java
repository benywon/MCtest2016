package publicMethods;

import com.sleepycat.je.*;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by benywon on 1/24 0024.
 */
public class BerkeleyDB
{
    private Environment myDbEnvironment = null;
    private Database database;
    public void setUp(String path, long cacheSize) {
        EnvironmentConfig envConfig = new EnvironmentConfig();
        envConfig.setAllowCreate(true);
        envConfig.setCacheSize(cacheSize);
        try {
            myDbEnvironment = new Environment(new File(path),envConfig);
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
    }
    public String getDataBaseName()
    {
        try
        {
            return database.getDatabaseName();
        }
        catch (DatabaseException e)
        {
            e.printStackTrace();
        }
        return null;
    }
    public void open(String dbName)
    {
        DatabaseConfig dbConfig = new DatabaseConfig();
        dbConfig.setAllowCreate(true);
        try
        {
            database = myDbEnvironment.openDatabase(null, dbName, dbConfig);
        }
        catch (DatabaseException e)
        {
            e.printStackTrace();
        }
    }

    public void close()
    {
        try
        {
            if (myDbEnvironment != null)
            {
                myDbEnvironment.close();
            }
        }
        catch (DatabaseException dbe)
        {
            System.err.println(dbe.getMessage());
        }
    }
    public String get(String key) {
        DatabaseEntry queryKey = new DatabaseEntry();
        DatabaseEntry value = new DatabaseEntry();
        try
        {
            queryKey.setData(key.getBytes("UTF-8"));
            OperationStatus status = database.get(null, queryKey, value,
                    LockMode.DEFAULT);
            if (status == OperationStatus.SUCCESS) {
                return new String(value.getData());
            }
            return null;
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        catch (DatabaseException e)
        {
            e.printStackTrace();
        }
        return null;
    }
    public void delete(String key)
    {
        try {
            DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
            database.delete(null, theKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public boolean put(String key, String value) {
        try
        {
            byte[]  theKey = key.getBytes("UTF-8");
            byte[] theValue = value.getBytes("UTF-8");
            OperationStatus status = database.put(null, new DatabaseEntry(theKey),
                    new DatabaseEntry(theValue));
            if(status == OperationStatus.SUCCESS) {
                return true;
            }
            return false;
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        catch (DatabaseException e)
        {
            e.printStackTrace();
        }
        return false;
    }
    public void getEveryItem()
    {
        Cursor cursor = null;

        try {

            cursor = database.openCursor(null, null);

            DatabaseEntry foundKey = new DatabaseEntry();

            DatabaseEntry foundData = new DatabaseEntry();

            // 通过cursor.getNex方法来遍历记录

            while (cursor.getNext(foundKey, foundData, LockMode.DEFAULT) ==

                    OperationStatus.SUCCESS) {

                String keyString = new String(foundKey.getData(), "UTF-8");

                String dataString = new String(foundData.getData(), "UTF-8");

                System.out.println("Key | Data : " + keyString + " | " +

                        dataString + "");

            }

        } catch (DatabaseException de) {

            System.err.println("Error accessing database." + de);

        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        finally {

            // 使用后必须关闭游标

            try
            {
                cursor.close();
            }
            catch (DatabaseException e)
            {
                e.printStackTrace();
            }

        }
    }
    public void cleanLog()
    {
        try {
            if (myDbEnvironment != null) {
                myDbEnvironment.cleanLog(); // 在关闭环境前清理下日志
                myDbEnvironment.close();
            }
        } catch (DatabaseException dbe) {
            dbe.printStackTrace();
        }
    }
    public static void main(String[] args) throws Exception
    {
        BerkeleyDB mbdb = new BerkeleyDB();
        mbdb.setUp("O:\\db", 10000000);
        mbdb.open("myDB");
        mbdb.close();

    }
}
