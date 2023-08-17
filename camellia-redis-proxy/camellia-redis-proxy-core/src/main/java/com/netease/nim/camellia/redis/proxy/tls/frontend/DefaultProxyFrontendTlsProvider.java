package com.netease.nim.camellia.redis.proxy.tls.frontend;

import com.netease.nim.camellia.redis.proxy.conf.ProxyDynamicConf;
import com.netease.nim.camellia.tools.ssl.SSLContextUtil;
import com.netease.nim.camellia.tools.utils.FileUtil;
import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;

/**
 * Created by caojiajun on 2023/8/9
 */
public class DefaultProxyFrontendTlsProvider implements ProxyFrontendTlsProvider {

    private SSLContext sslContext;
    @Override
    public boolean init() {
        createSSLContext();
        return true;
    }

    @Override
    public SslHandler createSslHandler() {
        SSLEngine sslEngine = sslContext.createSSLEngine();
        sslEngine.setNeedClientAuth(ProxyDynamicConf.getBoolean("proxy.frontend.tls.need.client.auth", true));
        sslEngine.setWantClientAuth(ProxyDynamicConf.getBoolean("proxy.frontend.tls.want.client.auth", true));
        sslEngine.setUseClientMode(false);
        return new SslHandler(sslEngine);
    }

    private void createSSLContext() {
        String caCertFilePath;
        String caCertFile = ProxyDynamicConf.getString("proxy.frontend.tls.ca.cert.file", null);
        if (caCertFile == null) {
            caCertFilePath = ProxyDynamicConf.getString("proxy.frontend.tls.ca.cert.file.path", null);
        } else {
            caCertFilePath = FileUtil.getFilePath(caCertFile);
        }
        String crtFilePath;
        String crtFile = ProxyDynamicConf.getString("proxy.frontend.tls.cert.file", null);
        if (crtFile == null) {
            crtFilePath = ProxyDynamicConf.getString("proxy.frontend.tls.cert.file.path", null);
        } else {
            crtFilePath = FileUtil.getFilePath(crtFile);
        }
        if (crtFilePath == null) {
            throw new IllegalArgumentException("crtFilePath not found");
        }
        String keyFilePath;
        String keyFile = ProxyDynamicConf.getString("proxy.frontend.tls.key.file", null);
        if (keyFile == null) {
            keyFilePath = ProxyDynamicConf.getString("proxy.frontend.tls.key.file.path", null);
        } else {
            keyFilePath = FileUtil.getFilePath(keyFile);
        }
        if (keyFilePath == null) {
            throw new IllegalArgumentException("keyFilePath not found");
        }
        String password = ProxyDynamicConf.getString("proxy.frontend.tls.password", null);
        this.sslContext = SSLContextUtil.genSSLContext(caCertFilePath, crtFilePath, keyFilePath, password);
    }


}
