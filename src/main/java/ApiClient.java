import com.google.gson.*;
import okhttp3.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ApiClient {

    /*
        BASE_URL 设置为你自己的网易云音乐API地址
     */
    private static final String BASE_URL = "";


    private static OkHttpClient client = new OkHttpClient();
    private static Gson gson = new Gson();

    public static boolean checkLoginStatus(String cookie) throws Exception {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(BASE_URL + "login/status")
                .addHeader("Cookie", cookie)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String jsonResponse = response.body().string(); // 获取响应的 JSON 字符串

                System.out.println("[Login Status] Response JSON: " + jsonResponse);

                JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();
                JsonObject dataObject = jsonObject.getAsJsonObject("data");
                System.out.println("Profile Response: " + dataObject.get("profile"));

                return dataObject.get("profile").toString() != "null";
            } else {
                throw new Exception("Code " + response.code() + " - " + response.message() + " 请检查你的API地址是否可用.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String login(String phone, String password) throws Exception {
        System.out.println("ApiClient 正在尝试通过登录接口登录");
        Request request = new Request.Builder()
                .url(BASE_URL + "login/cellphone" + "?phone=" + phone + "&password=" + password)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String jsonData = response.body().string();
                System.out.println("[Login] Response JSON: " + jsonData);
                JsonObject dataObject = JsonParser.parseString(jsonData).getAsJsonObject();
                int code = dataObject.get("code").getAsInt();
                System.out.println("[Login] Response Code: " + code);
                if(code == 200) {
                    List<String> cookies = response.headers("Set-Cookie");
                    StringBuilder cookieBuilder = new StringBuilder();
                    for (String cookie : cookies) {
                        cookieBuilder.append(cookie.split(";")[0]).append("; ");
                    }
                    System.out.println("[Login] Cookies: " + cookieBuilder.toString());
                    return cookieBuilder.toString();
                } else {
                    throw new Exception("[ApiClient Login] Error: Code - " + code);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static List<Song> searchSongs(String keyword, String cookie) {
        String url = BASE_URL + "search?keywords=" + keyword;
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Cookie", cookie)
                .build();

        List<Song> songs = new ArrayList<>();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                JsonObject json = gson.fromJson(response.body().string(), JsonObject.class);
                JsonObject result = json.getAsJsonObject("result");
                if (result != null && result.has("songs")) {
                    JsonArray songsArray = result.getAsJsonArray("songs");
                    for (JsonElement songElement : songsArray) {
                        JsonObject songObject = songElement.getAsJsonObject();
                        String id = songObject.get("id").getAsString();
                        String name = songObject.get("name").getAsString();
                        JsonArray artistsArray = songObject.getAsJsonArray("artists");
                        StringBuilder artists = new StringBuilder();
                        for (JsonElement artistElement : artistsArray) {
                            JsonObject artistObject = artistElement.getAsJsonObject();
                            if (artists.length() > 0) {
                                artists.append(", ");
                            }
                            artists.append(artistObject.get("name").getAsString());
                        }

                        songs.add(new Song(id, name, artists.toString()));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return songs;
    }

    public static String getDownloadUrl(String songId, String level, String cookie) throws IOException {
        OkHttpClient client = new OkHttpClient();
        String responseUrl = BASE_URL + "song/url/v1?id=" + songId + "&level=" + level;
        Request request = new Request.Builder()
                .url(responseUrl)
                .addHeader("Cookie", cookie) // 添加 Cookie
                .build();

        System.out.println("Response URL:" + responseUrl);
        System.out.println("Cookie: " + cookie); // 输出 Cookie

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String jsonData = response.body().string();
                System.out.println("Response JSON: " + jsonData); // 输出完整的响应 JSON

                JsonObject jsonObject = JsonParser.parseString(jsonData).getAsJsonObject();

                if (jsonObject.get("data") != null && jsonObject.get("data").isJsonArray()) {
                    JsonArray dataArray = jsonObject.getAsJsonArray("data");

                    if (dataArray.size() > 0 && dataArray.get(0).isJsonObject()) {
                        JsonObject dataObject = dataArray.get(0).getAsJsonObject();

                        if (dataObject.get("url") != null && !dataObject.get("url").isJsonNull()) {
                            return dataObject.get("url").getAsString(); // 返回下载 URL
                        } else {
                            System.err.println("URL 字段为空或不存在");
                        }
                    }
                }
            } else {
                System.err.println("Error: " + response.code() + " - " + response.message());
            }
        }
        return null;
    }
}
