import me.postaddict.instagram.scraper.Endpoint;
import me.postaddict.instagram.scraper.Instagram;
import me.postaddict.instagram.scraper.domain.Account;
import me.postaddict.instagram.scraper.exception.InstagramException;
import okhttp3.Cookie;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Insta extends Instagram {
    List<Account> followers = new ArrayList<Account>();


    public Insta(OkHttpClient httpClient) {
        super(httpClient);
    }
    private Request withCsrfToken(Request request) {
        List<Cookie> cookies = httpClient.cookieJar()
                .loadForRequest(request.url());
        for (Iterator<Cookie> iterator = cookies.iterator(); iterator.hasNext(); ) {
            Cookie cookie = iterator.next();
            if (!cookie.name().equals("csrftoken")) {
                iterator.remove();
            }
        }
        if (!cookies.isEmpty()) {
            Cookie cookie = cookies.get(0);
            return request.newBuilder()
                    .addHeader("X-CSRFToken", cookie.value())
                    .build();
        }
        return request;
    }
    private Account account(Map edgeObj) {
        Account account = new Account();
        Map edgeNode = (Map) edgeObj.get("node");
        account.id = Long.valueOf((String) edgeNode.get("id"));
        account.username = (String) edgeNode.get("username");
        account.profilePicUrl = (String) edgeNode.get("profile_pic_url");
        account.isVerified = (Boolean) edgeNode.get("is_verified");
        account.fullName = (String) edgeNode.get("full_name");
        return account;
    }
    @Override
    public List<Account> getFollowers(long userId, int count) throws IOException {
        boolean hasNext = true;

        String fullLink = Main.getLink();
        String followsLink;
        if(fullLink == null){
        followsLink = Endpoint.getFollowersLinkVariables(userId, 200, "");
        } else{
            followsLink = fullLink;}
//            followsLink = "https://www.instagram.com/graphql/query/?query_id=17851374694183129&variables={\"id\": 26563598, \"first\": 200, \"after\": \"AQBiHVi2rY5bJBZbX-yPn3G6OdjDkdG5XXlDNeOBnXONgmOmtohiMn8LZVgLLTdeUpuAuJ7SdSbv_KebXmyssaYrcbgmtImGPfTvUfXH1xbWiA\"}";
        while (followers.size() < count && hasNext) {
            Request request = new Request.Builder()
                    .url(followsLink)
                    .header("Referer", Endpoint.BASE_URL + "/")
                    .build();
            FileWriter writer = new FileWriter("C:\\Users\\I\\Downloads\\url10.txt", true);
                writer.write(followsLink + System.getProperty("line.separator"));
            writer.close();
            try {
                Response response = this.httpClient.newCall(withCsrfToken(request)).execute();
                String jsonString = response.body().string();

                response.body().close();
                Map commentsMap = gson.fromJson(jsonString, Map.class);
                Map edgeFollow = (Map) ((Map) ((Map) commentsMap.get("data")).get("user")).get("edge_followed_by");
                List edges = (List) edgeFollow.get("edges");
                for (Object edgeObj : edges) {
                    Account account = account((Map) edgeObj);
                    followers.add(account);
//                    if (followers.size() == count) {
//                        return followers;
//                    }
                }
                boolean hasNexPage = (Boolean) ((Map) edgeFollow.get("page_info")).get("has_next_page");
                if (hasNexPage) {
                    followsLink = Endpoint.getFollowersLinkVariables(userId, 200, (String) ((Map) edgeFollow.get("page_info")).get("end_cursor"));


                    hasNext = true;
                } else {
                    hasNext = false;
                }
            } catch (InstagramException e){
                try {
                    System.out.println("=======ЗАМЕНА=======");
                    System.out.println(followsLink);
                    Thread.sleep(3000);
                    Main.allbase(followsLink, followers);
                    followers.clear();
                    Main.startrun();

                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return followers;
    }
}
