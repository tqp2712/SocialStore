package socialstore.webservice;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URI;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;

import android.content.Context;
import android.util.Log;

public class Server {
    private Context context;

    public Server(Context context){
        this.context = context;
    };
    private static final int CONNECTION_TIMEOUT = 20000;
    private static final int SOCKET_TIMEOUT = 60000;

    private static String HEADER_ACCEPT = "Accept";
    private static String HTTP_USER_AGENT = "User-Agent";
    

    public String excuteGet(String url, List<NameValuePair> paramList) {
        if (null != paramList) {
            url += getStrParam(paramList);
        }
        HttpClient hClient = new DefaultHttpClient();
//      hClient = sslClient(hClient);
        HttpConnectionParams.setConnectionTimeout(hClient.getParams(),
                CONNECTION_TIMEOUT); // Timeout
        HttpConnectionParams.setSoTimeout(hClient.getParams(), SOCKET_TIMEOUT);
        try {
            HttpGet hget = new HttpGet();
            hget.setHeader(HEADER_ACCEPT,"application/json");
            hget.setHeader(HEADER_ACCEPT,"text/html");
            hget.setHeader(HTTP_USER_AGENT, "Android");
            hget.setURI(new URI(url));
            HttpResponse response = hClient.execute(hget);
            StatusLine statusLine = response.getStatusLine();

            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                
                String responseString = out.toString();
                out.close();
                return responseString;
            } else {
                return "";
            }
        } catch (ConnectTimeoutException e) {

            return "";

        } catch (Exception e) {
            return "";

        }
    }
    
    public String excutePost(String url, List<NameValuePair> paramList) {
        HttpClient httpclient = new DefaultHttpClient();
        httpclient = sslClient(httpclient);
        HttpConnectionParams.setConnectionTimeout(httpclient.getParams(),
                CONNECTION_TIMEOUT);
        HttpConnectionParams.setSoTimeout(httpclient.getParams(),
                SOCKET_TIMEOUT);
        HttpPost httppost = new HttpPost(url);

        HttpResponse response = null;
        try {
            httppost.setHeader(HEADER_ACCEPT,"application/json");
            httppost.setHeader(HEADER_ACCEPT,"text/html");
            httppost.setHeader(HTTP_USER_AGENT, "Android");
            httppost.setEntity(new UrlEncodedFormEntity(paramList, "UTF-8"));
            response = httpclient.execute(httppost);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            if (HttpStatus.SC_NO_CONTENT == response.getStatusLine()
                    .getStatusCode()) {
                return "";
            } else if (HttpStatus.SC_OK == response.getStatusLine()
                    .getStatusCode()) {
                response.getEntity().writeTo(byteArrayOutputStream);
                String ret = byteArrayOutputStream.toString();
                return ret;
            } else {
                return "";
            }

        } catch (ConnectTimeoutException e) {
            return "";
        } catch (Exception e) {
            return "";
        }
    }
    public String execPostFile(String url, MultipartEntity mulEntity) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpConnectionParams
                .setConnectionTimeout(httpclient.getParams(), CONNECTION_TIMEOUT); 
        HttpConnectionParams.setSoTimeout(httpclient.getParams(), SOCKET_TIMEOUT);
        HttpPost httppost = new HttpPost(url);
        try {
            httppost.setEntity(mulEntity);
            httppost.setHeader(HEADER_ACCEPT,"application/json");
            httppost.setHeader(HEADER_ACCEPT,"text/html");
            HttpResponse response = httpclient.execute(httppost);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            response.getEntity().writeTo(byteArrayOutputStream);

            if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
                String ret = byteArrayOutputStream.toString();
                return ret;
            } 

        }  catch (Exception e) {
            e.printStackTrace();

        }
        return "";
    }
    private HttpClient sslClient(HttpClient client) {
        try {
            X509TrustManager tm = new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] xcs,
                        String string) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] xcs,
                        String string) throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            SSLContext ctx = SSLContext.getInstance("TLS");
            ctx.init(null, new TrustManager[] { tm }, null);
            SSLSocketFactory ssf = new MySSLSocketFactory(ctx);
            ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            ClientConnectionManager ccm = client.getConnectionManager();
            SchemeRegistry sr = ccm.getSchemeRegistry();
            sr.register(new Scheme("https", ssf, 443));
            sr.register(new Scheme("http", PlainSocketFactory
                    .getSocketFactory(), 80));
            return new DefaultHttpClient(ccm, client.getParams());
        } catch (Exception ex) {
            return null;
        }
    }

    public class MySSLSocketFactory extends SSLSocketFactory {
        SSLContext sslContext = SSLContext.getInstance("TLS");

        public MySSLSocketFactory(KeyStore truststore)
                throws NoSuchAlgorithmException, KeyManagementException,
                KeyStoreException, UnrecoverableKeyException {
            super(truststore);

            TrustManager tm = new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] chain,
                        String authType) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] chain,
                        String authType) throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };

            sslContext.init(null, new TrustManager[] { tm }, null);
        }

        public MySSLSocketFactory(SSLContext context)
                throws KeyManagementException, NoSuchAlgorithmException,
                KeyStoreException, UnrecoverableKeyException {
            super(null);
            sslContext = context;
        }

        @Override
        public Socket createSocket(Socket socket, String host, int port,
                boolean autoClose) throws IOException, UnknownHostException {
            return sslContext.getSocketFactory().createSocket(socket, host,
                    port, autoClose);
        }

        @Override
        public Socket createSocket() throws IOException {
            return sslContext.getSocketFactory().createSocket();
        }
    }

    private String getStrParam(List<NameValuePair> params) {
        String ret = "?";
        return ret + URLEncodedUtils.format(params, "utf8");

    }
    
    public String sendAlert(String urlServer, String storeId, String date, 
            String alertTime, String alertType, 
            String content, String timeBegin, 
            String timeEnd, String token) {
        try {
            List<NameValuePair> paramList = new ArrayList<NameValuePair>();
            paramList.add(new BasicNameValuePair("storeid", "" + storeId));
            paramList.add(new BasicNameValuePair("date", date));
            paramList.add(new BasicNameValuePair("alerttime", alertTime));
            paramList.add(new BasicNameValuePair("alerttype", alertType));
            paramList.add(new BasicNameValuePair("content", content));
            paramList.add(new BasicNameValuePair("timebegin", timeBegin));
            paramList.add(new BasicNameValuePair("timeend", timeEnd));
            paramList.add(new BasicNameValuePair("token", token));
            return excutePost(urlServer, paramList);
        } catch (Exception e) {
            Log.v("Exception", "" + e);
        }
        return "";
    }
    
}
