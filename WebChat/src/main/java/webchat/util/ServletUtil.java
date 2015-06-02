package webchat.util;

import webchat.model.Message;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * Created by Pavel on 31.05.2015.
 */
public class ServletUtil {
    public static final String APPLICATION_JSON = "application/json";
    public static final String UTF_8 = "UTF-8";

    private ServletUtil() {
    }

    public static String getMessageBody(HttpServletRequest request) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }
}
