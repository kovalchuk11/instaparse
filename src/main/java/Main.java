

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
import java.util.ArrayList;
import java.util.List;


public class Main {
    private static AuthenticatedInsta client;

    public static List<Account> getAllFollowers() {
        return allFollowers;
    }

    private static List<Account> allFollowers = new ArrayList<>();
    private static String link;
    private static int c = 2;

    public static String getLink() {
        return link;
    }
    public static void setLink(String link) {
        Main.link = link;
    }

    public static void main(String[] args) throws IOException {
            startrun();

    }
    public static void startrun()throws IOException{
        start();
        try {
            testFollowers();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public static void start()throws IOException{

        Credentials credentials = new Credentials(c);
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(loggingInterceptor)
                .addInterceptor(new UserAgentInterceptor(UserAgents.OSX_CHROME))
                .addInterceptor(new ErrorInterceptor())
                .cookieJar(new DefaultCookieJar(new CookieHashSet()))
                .build();
        client = new Insta(httpClient);
        client.basePage();
        client.login(credentials.getLogin(), credentials.getPassword());
        client.basePage();
        if(c <= 12)
            c++;
        else c = 1;
    }
//    public static void start(int i)throws IOException{
//        Credentials credentials = new Credentials();
//        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
//        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);
//        OkHttpClient httpClient = new OkHttpClient.Builder()
//                .addNetworkInterceptor(loggingInterceptor)
//                .addInterceptor(new UserAgentInterceptor(UserAgents.WIN10_FIREFOX))
//                .addInterceptor(new ErrorInterceptor())
//                .cookieJar(new DefaultCookieJar(new CookieHashSet()))
//                .build();
//        client = new Insta(httpClient);
//        client.basePage();
//        client.login("pho.tour", "1243tour");
//        client.basePage();
//    }
    public static void testFollowers() throws Exception {

        Account account = client.getAccountByUsername("elenagord");
        client.getFollowers(account.id, 173000);
        System.out.println("ПО ПЛАНУ");
//        System.out.println(allFollowers.size());
//        if (allFollowers.size() == 0){
//            return;
//        }

//        if (followers.size() < 173000)
//            startrun();
    }
    public static void allbase(String link,  List<Account> allFollowers1) throws IOException {
        setLink(link);
        allFollowers.size();
        for (Account a : allFollowers1) {
//            if(allFollowers.size() < 173000)
            allFollowers.add(a);
        }
        FileWriter writer = new FileWriter("C:\\Users\\I\\Downloads\\output466.txt", true);
        for (Account a : allFollowers1) {
            writer.write(a.username + System.getProperty("line.separator"));
        }
        writer.close();
//        if (allFollowers.size() > 173000)
//            return;

    }
}
