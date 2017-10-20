

import me.postaddict.instagram.scraper.AuthenticatedInsta;
import me.postaddict.instagram.scraper.Instagram;
import me.postaddict.instagram.scraper.cookie.CookieHashSet;
import me.postaddict.instagram.scraper.cookie.DefaultCookieJar;
import me.postaddict.instagram.scraper.domain.Account;
import me.postaddict.instagram.scraper.interceptor.ErrorInterceptor;
import me.postaddict.instagram.scraper.interceptor.UserAgentInterceptor;
import me.postaddict.instagram.scraper.interceptor.UserAgents;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;


public class Main {
    private static AuthenticatedInsta client;

    public static void main(String[] args) throws IOException {
        start();
        try {
            testFollowers();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void start()throws IOException{
        Credentials credentials = new Credentials();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(loggingInterceptor)
                .addInterceptor(new UserAgentInterceptor(UserAgents.OSX_CHROME))
                .addInterceptor(new ErrorInterceptor())
                .cookieJar(new DefaultCookieJar(new CookieHashSet()))
                .build();
        client = new Instagram(httpClient);
        client.basePage();
        client.login(credentials.getLogin(), credentials.getPassword());
        client.basePage();
    }
    public static void testFollowers() throws Exception {

        Account account = client.getAccountByUsername("elenagord");
        List<Account> followers = client.getFollowers(account.id, 50);
        System.out.println("=======");
        System.out.println(followers.size());
        FileWriter writer = new FileWriter("C:\\Users\\I\\Downloads\\output.txt");
        for (Account a : followers) {
            System.out.println(a.username);
            String n = a.username;
            writer.write(n + System.getProperty("line.separator"));
        }
        writer.close();

    }
}
