import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

public class ClientCurl {
    public static Map makeReq(String[] args) throws Exception {
        if (args.length >= 2) {
            var method = args[0];
            var auth = args[1];
            var url = args[2];
            var body = args.length == 4 ? args[3] : "";
            HttpURLConnection http = sendRequest(url, auth, method, body);
            return receiveResponse(http);
        } else {
            //TODO: What do I do here?
        }
        return null;
    }

    private static HttpURLConnection sendRequest(String url, String auth, String method, String body)
                                                    throws URISyntaxException, IOException {
        URI uri = new URI(url);
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod(method);
        if (auth != null){
            http.setRequestProperty("Authorization", auth);
        }
        writeRequestBody(body, http);
        http.connect();
        return http;
    }

    private static void writeRequestBody(String body, HttpURLConnection http) throws IOException {
        if (!body.isEmpty()) {
            http.setDoOutput(true);
            try (var outputStream = http.getOutputStream()) {
                outputStream.write(body.getBytes());
            }
        }
    }

    private static Map receiveResponse(HttpURLConnection http) throws IOException {
        var statusCode = http.getResponseCode();
        var statusMessage = http.getResponseMessage();
        Map responseBody = readResponseBody(http);
        System.out.printf("= Response =========\n[%d] %s\n\n%s\n\n", statusCode, statusMessage, responseBody);

        return responseBody;
    }

    private static Map readResponseBody(HttpURLConnection http) throws IOException {
        Map responseBody;
        try (InputStream respBody = http.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(respBody);
             responseBody = new Gson().fromJson(inputStreamReader, Map.class);
        }
        return responseBody;
    }
}
