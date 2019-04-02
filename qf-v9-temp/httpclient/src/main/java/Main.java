import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * @author HuangGuiZhao
 * @Date 2019/3/26
 */
public class Main {
    public static void main(String[] args) throws IOException {

        Map<String,String> params = new HashMap<String, String>();
        params.put("username","zhangsan");
        params.put("password","123");
        doGet("https://www.baidu.com",params);
        /*//1.打开浏览器
        CloseableHttpClient client = HttpClients.createDefault();
        //2.输入网址
        String url = "https://www.baidu.com";
        //3.敲回车，发送请求
        HttpGet get = new HttpGet(url);
        CloseableHttpResponse response = client.execute(get);
        //4.解析服务端的响应信息
        int statusCode = response.getStatusLine().getStatusCode();
        if(statusCode == 200){
            //获取响应的信息
            HttpEntity entity = response.getEntity();
            //这个输入流对着服务端的响应内容
            *//*InputStream inputStream = entity.getContent();
            //看具体业务
            byte[] bs = new byte[1024];
            int len;
            while((len=inputStream.read(bs))!=-1){
                System.out.println(new String(bs,0,len));
            }*//*
            String info = EntityUtils.toString(entity,"utf-8");
            System.out.println(info);
        }else{
            System.out.println(statusCode);
        }
        client.close();*/
    }

    //
    public static String doGet(String url, Map<String, String> param) {
        //
        String result = "";
        // 1.创建Httpclient对象
        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        try {
            // 2.创建uri对象
            URIBuilder builder = new URIBuilder(url);
            if (param != null) {
                for (String key : param.keySet()) {
                    builder.addParameter(key, param.get(key));
                }
            }
            URI uri = builder.build();
            // 3.创建http GET请求
            HttpGet httpGet = new HttpGet(uri);
            // 4.执行请求
            response = httpclient.execute(httpGet);
            // 5.判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                // 6.进行UTF-8编码处理
                result = EntityUtils.toString(response.getEntity(), "UTF-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static String doGet(String url) throws IOException {
        //1.打开浏览器
        CloseableHttpClient client = HttpClients.createDefault();
        //3.敲回车，发送请求
        HttpGet get = new HttpGet(url);
        CloseableHttpResponse response = client.execute(get);
        //4.解析服务端的响应信息
        int statusCode = response.getStatusLine().getStatusCode();
        if(statusCode == 200){
            //获取响应的信息
            HttpEntity entity = response.getEntity();
            //这个输入流对着服务端的响应内容
            String info = EntityUtils.toString(entity,"utf-8");
            return info;
        }else{
            return String.valueOf(statusCode);
        }
    }
}
