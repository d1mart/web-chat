package webchat.servlets;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;


import webchat.model.Message;
import org.apache.log4j.BasicConfigurator;
import org.json.simple.JSONObject;

import org.json.simple.parser.ParseException;
import org.xml.sax.SAXException;
import webchat.storage.XMLHistoryUtil;
import webchat.util.MessageUtil;
import webchat.util.ServletUtil;


@WebServlet(urlPatterns = "/chat")
public class MainServlet extends HttpServlet {
    //private static Logger logger = Logger.getLogger(MainServlet.class.getName());

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy hh:mm");
        String now = format.format(date.getTime());

        String data = ServletUtil.getMessageBody(req);
        System.out.println(now + ": " + data);

        try {
            JSONObject json = MessageUtil.stringToJson(data);
            Message message = MessageUtil.jsonToMessage(json);
            XMLHistoryUtil.addData(message);
            resp.setStatus(HttpServletResponse.SC_OK);

        } catch (ParseException e) {
            System.out.println("badRequest");
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String token = req.getParameter(MessageUtil.TOKEN);
        try {
            if (token != null && !"".equals(token)) {
                int index = MessageUtil.getIndex(token);

                String messages;
                messages = formResponse(index);
                PrintWriter out = resp.getWriter();
                out.print(messages);
                out.flush();
            } else {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "'token' parameter needed");
            }
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            loadHistory();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println("init");
    }


    private String formResponse(int index) throws SAXException, IOException, ParserConfigurationException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(MessageUtil.MESSAGES, XMLHistoryUtil.getSubMessagesByIndex(index));
        jsonObject.put(MessageUtil.TOKEN, MessageUtil.getToken(XMLHistoryUtil.getStorageSize()));
        return jsonObject.toJSONString();
    }

    private void loadHistory() throws SAXException, IOException, ParserConfigurationException, TransformerException {
        if (!XMLHistoryUtil.doesStorageExist()) {
            XMLHistoryUtil.createStorage();
        }
    }
}
