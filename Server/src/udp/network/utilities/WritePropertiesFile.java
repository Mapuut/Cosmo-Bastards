package udp.network.utilities;

import java.io.*;
import java.util.Properties;

/**
 * Write properties to a file.
 */
public class WritePropertiesFile {

    public static final String SERVER_USERS = "C:\\CosmoBastards\\config.properties";
    //public static final String SERVER_USERS = "C:\\Users\\t440s\\Desktop\\Progre\\Java GUI\\server_users.properties";
//    public static final String SERVER_USERS = "/var/services/homes/udp/server_users.properties";

    /**
     * write to file, prrovide key and data.
     * @param key key
     * @param data data
     */
    public void writeToFile(String key, String data) {
        FileOutputStream fileOut = null;
        FileInputStream fileIn;

        File directory = new File("C:\\CosmoBastards");
        if (! directory.exists()){
            directory.mkdir();
            // If you require it to make the entire directory path including parents,
            // use directory.mkdirs(); here instead.
        }
        directory = new File(SERVER_USERS);
        try {
            directory.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(directory.exists()) {

        }

        try {
            Properties configProperty = new Properties();

            fileIn = new FileInputStream(SERVER_USERS);
            configProperty.load(fileIn);
            configProperty.setProperty(key, data);
            fileOut = new FileOutputStream(SERVER_USERS);
            configProperty.store(fileOut, "Usernames and hashed passwords");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            try {
                assert fileOut != null;
                fileOut.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}