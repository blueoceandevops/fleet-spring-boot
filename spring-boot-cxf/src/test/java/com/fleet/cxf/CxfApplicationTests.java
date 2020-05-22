package com.fleet.cxf;

import com.fleet.cxf.service.UserService;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.StringReader;

public class CxfApplicationTests {

    public static void main(String[] args) throws Exception {
//        CxfApplicationTests.main1();
        CxfApplicationTests.main2();
    }

    /**
     * 1.代理类工厂的方式,需要拿到对方的接口地址
     */
    public static void main1() {
        JaxWsProxyFactoryBean jaxWsProxyFactoryBean = new JaxWsProxyFactoryBean();
        String address = "http://127.0.0.1:8080/service/user?wsdl";
        jaxWsProxyFactoryBean.setAddress(address);
        jaxWsProxyFactoryBean.setServiceClass(UserService.class);
        UserService userService = (UserService) jaxWsProxyFactoryBean.create();
        String name = userService.getName(1L);
        System.out.println("查询用户名:" + name);
    }

    /**
     * 2：httpClient调用
     */
    public static void main2() throws Exception {
        String address = "http://127.0.0.1:8080/service/user";
        HttpPost request = new HttpPost(address);
        request.setHeader("Content-Type", "application/soap+xml; charset=utf-8");
        String requestXml = getRequestXml();
        HttpEntity entity = new StringEntity(requestXml, "UTF-8");
        request.setEntity(entity);
        HttpResponse httpResponse = new DefaultHttpClient().execute(request);
        if (httpResponse.getStatusLine().getStatusCode() == 200) {
            String xmlString = EntityUtils.toString(httpResponse.getEntity());
            System.out.println(xmlString);
            String jsonString = parseXMLSTRING(xmlString);
            System.out.println("---" + jsonString);
        }
    }

    public static String parseXMLSTRING(String xmlString) throws Exception {
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = builderFactory.newDocumentBuilder();
        Document document = builder.parse(new InputSource(new StringReader(xmlString)));
        Element element = document.getDocumentElement();//根节点
        Node node = element.getFirstChild();
        while (!node.getNodeName().equals("String")) {
            node = node.getFirstChild();
        }
        if (node.getFirstChild() != null) {
            return node.getFirstChild().getNodeValue();
        }
        return null;
    }

    private static String getRequestXml() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<?xml version=\"1.0\"?>");
        stringBuilder.append("<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" ");
        stringBuilder.append(" xmlns:sam=\"http://service.springboot.huaxun.com/\" ");  //前缀,这一串由服务端提供
        stringBuilder.append(" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"");
        stringBuilder.append(" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">");
        stringBuilder.append("<soap:Header/>");
        stringBuilder.append("<soap:Body>");
        stringBuilder.append("<sam:getName>");
        stringBuilder.append("<userId>1</userId>");
        stringBuilder.append("</sam:getName>");
        stringBuilder.append("</soap:Body>");
        stringBuilder.append("</soap:Envelope>");
        return stringBuilder.toString();
    }
}
