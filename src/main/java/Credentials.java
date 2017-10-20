import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by vasily on 27.04.17.
 * Login and password which stores in external file. Create a file in PATH which contains login and password
 */
final class Credentials {

    private final static String PATH = "C:\\Users\\I\\Documents\\MyProgekt\\instaparse\\src\\main\\resources\\credentials.properties";
    private String login;
    private String password;


    Credentials(int c) throws IOException {
        Properties properties = new Properties();
        String lo = "login" + c;
        String pa = "password" + c;
        try(FileInputStream is = new FileInputStream(PATH)) {

            if (is == null) {
                throw new IOException("can't find credentials file");
            }
            properties.load(is);
            login = properties.getProperty(lo);
            password = properties.getProperty(pa);

        }
    }

    String getLogin() {
        return login;
    }

    String getPassword() {
        return password;
    }

}