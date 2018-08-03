package com.zjlp.httpvolly;

import android.content.Context;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * Created by liht  on 2016/9/30 17:18.
 * Desc: 必须进行配置，否则Volly无法启动，直接死机
 */
public class VolleyConfig {
    public static boolean NeedTrustAllServers = true; // 是否需要信任所有服务器，默认不信任；在某些情况下测试需要信任所有服务器
    private int versionCode;
    private String sessionId;
    private Context context;
    private static VolleyConfig volleyConfig;
    private boolean isReleaseEnvironment;//当前是否正式环境
    public static void initConfig(VolleyConfig volleyConfig){
        VolleyConfig.volleyConfig = volleyConfig;
    }

    public static VolleyConfig getVolleyConfig(){
        return volleyConfig;
    }

    public static int getVersionCode() {
        return volleyConfig.versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public static String getSessionId() {
        return volleyConfig.sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public static Context getContext() {
        return volleyConfig.context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public boolean isReleaseEnvironment() {
        return isReleaseEnvironment;
    }

    public void setReleaseEnvironment(boolean releaseEnvironment) {
        isReleaseEnvironment = releaseEnvironment;
    }

    private TrustManagerFactory tmf;
    public synchronized TrustManagerFactory getTmf(){
        if (tmf==null){
            try {
                // Load CAs from an InputStream
                // (could be from a resource or ByteArrayInputStream or ...)
                CertificateFactory cf = CertificateFactory.getInstance("X.509");

                // From https://www.washington.edu/itconnect/security/ca/load-der.crt
                InputStream caInput = new BufferedInputStream(getContext().getAssets().open("o2osl.com.cer"));
                InputStream caMInput = new BufferedInputStream(getContext().getAssets().open("WoSignOvSSLCA.cer"));
                InputStream rootInput = new BufferedInputStream(getContext().getAssets().open("CertumTrustedNetworkCA.cer"));
                Certificate ca = null;
                Certificate caM = null;
                Certificate caR = null;
                try {
                    ca = cf.generateCertificate(caInput);
                    caM = cf.generateCertificate(caMInput);
                    caR = cf.generateCertificate(rootInput);
                } catch (CertificateException e) {
                    e.printStackTrace();
                } finally {
                    caInput.close();
                }
                // Create a KeyStore containing our trusted CAs
                String keyStoreType = KeyStore.getDefaultType();
                KeyStore keyStore = KeyStore.getInstance(keyStoreType);//
                keyStore.load(null, null);
                keyStore.setCertificateEntry("im.o2osl.com", ca);
                keyStore.setCertificateEntry("wosign-ovca.ocsp-certum.com", caM);
                keyStore.setCertificateEntry("subca.ocsp-certum.com", caR);

                // Create a TrustManager that trusts the CAs in our KeyStore
                String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
                tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
                tmf.init(keyStore);

            } catch (CertificateException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (KeyStoreException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return tmf;
    }

    private SSLContext sslContext;
    public synchronized SSLContext getSslContext(){
        if (sslContext==null){
            try {
                sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null,getTmf().getTrustManagers(),null);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (KeyManagementException e) {
                e.printStackTrace();
            }
        }
        return sslContext;
    }



    private SSLContext sslContextTrustAllSvr;
    public SSLContext getSslContextTrustAllSvr() {
        if (sslContextTrustAllSvr==null){
            try {
                sslContextTrustAllSvr = SSLContext.getInstance("TLS");
                sslContextTrustAllSvr.init(null,new TrustManager[]{new AllTrustManager()},null);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (KeyManagementException e) {
                e.printStackTrace();
            }
        }
        return sslContextTrustAllSvr;
    }

    private class AllTrustManager implements X509TrustManager{
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
            // TODO Auto-generated method stub
        }
        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType)

                throws CertificateException {
            // TODO Auto-generated method stub
        }
        @Override
        public X509Certificate[] getAcceptedIssuers() {
            // TODO Auto-generated method stub
            return null;
        }
    }


    private HostnameVerifier hostnameVerifier;
    public HostnameVerifier getHostnameVerifierTrustAllSvr() {
        if (hostnameVerifier==null){
            try {
                hostnameVerifier = new AllHostnameVerifier();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return hostnameVerifier;
    }
    private class AllHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            // TODO Auto-generated method stub
            return true;
        }

    }
}
