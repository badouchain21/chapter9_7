package com.bdc.common.utils;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Http 工具类
 *
 * @author zhongzhilong
 * @date 2020-11-12 11:45
 */
public class HttpUtils {

    public static String post (String url, Map<String,String> params){
        CloseableHttpResponse httpResponse = null;
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(url);
            List<NameValuePair> pairArrayList = new ArrayList<NameValuePair>();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                pairArrayList.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
            httpPost.setEntity(new UrlEncodedFormEntity(pairArrayList, "UTF-8"));

            httpResponse = httpClient.execute(httpPost);
            if (Objects.nonNull(httpResponse)){
                HttpEntity httpEntity = httpResponse.getEntity();
                String result = EntityUtils.toString(httpEntity, "utf-8");
                EntityUtils.consume(httpEntity);
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (httpResponse != null){
                try {
                    httpResponse.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

}
