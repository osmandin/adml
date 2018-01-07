package edu.mit.aspace;

import edu.mit.properties.Credentials;
import edu.mit.properties.PropertiesConfigurationUtil;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.json.simple.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.*;

import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

public class Aspace {

    private final Logger logger = getLogger(this.getClass());

    /**
     * Exports EAD and returns path
     * @param token auth
     * @return Path where the file is written
     */
    private String export(final String token, final String endPoint) {

        final HttpServiceUtil httpServiceUtil = new HttpServiceUtil();
        final HttpGet httpGet = new HttpGet(endPoint);
        httpGet.addHeader("X-ArchivesSpace-Session", token);

        StringBuffer sb = new StringBuffer();

        final Date date = new Date();
        long time = date.getTime();

        try {
            final HttpResponse response = httpServiceUtil.getHttpClient().execute(httpGet);

            logger.debug("EAD service response:{}", response.getStatusLine());

            final BufferedReader br = new BufferedReader(
                    new InputStreamReader((response.getEntity().getContent())));

            String output;

            while (!(output = br.readLine()).contains("</ead>")) {
                sb.append(output);
                sb.append(System.getProperty("line.separator"));
            }

            sb.append(output);

            logger.debug("Done reading:{}", sb.toString());

            //JSONObject json = (JSONObject)new JSONParser().parse(sb.toString());
            //final String session =  (String) json.get("session");

            return sb.toString();

        } catch (Exception e) {
            logger.error("Error", e);
        }

        return "";
    }

    private String authenticate(final String loginUrl) {

        final Credentials credentials = PropertiesConfigurationUtil.getCredentials();

        // Make the request
        final HttpServiceUtil httpServiceUtil = new HttpServiceUtil();

        final HttpPost httpPost = new HttpPost(loginUrl);
        final List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("password", credentials.getPassword()));
        httpPost.setEntity(new UrlEncodedFormEntity(nvps, Charset.defaultCharset()));
        try {
            final HttpResponse response = httpServiceUtil.getHttpClient().execute(httpPost);

            logger.debug("Auth handshake status:{}", response.getStatusLine());

            final BufferedReader br = new BufferedReader(
                    new InputStreamReader((response.getEntity().getContent())));
            StringBuffer sb = new StringBuffer();
            String output = "";
            while ((output = br.readLine()) != null) {
                sb.append(output);
                //   System.out.println(sb.toString());
            }

            JSONObject json = (JSONObject)new org.json.simple.parser.JSONParser().parse(sb.toString());
            final String session =  (String) json.get("session");

            if (session == null) {
                //TODO
            }

            return session;
        } catch (Exception e) {
            logger.error("Error auth", e);
        }

        return "";
    }

}
