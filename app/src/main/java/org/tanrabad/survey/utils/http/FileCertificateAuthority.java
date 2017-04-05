package org.tanrabad.survey.utils.http;

import android.net.http.SslCertificate;
import android.os.Bundle;
import android.support.annotation.NonNull;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

public class FileCertificateAuthority {

    private final SSLContext sslContext;
    private final TrustManager[] trustManagers;
    private Certificate certificate;

    public FileCertificateAuthority(InputStream inputStream) {
        certificate = createCertificate(inputStream);
        KeyStore keyStore = createKeyStore(certificate);
        trustManagers = createTrustManager(keyStore);
        sslContext = createSslContext(trustManagers);
    }

    public FileCertificateAuthority(File file) throws FileNotFoundException {
        this(new FileInputStream(file));
    }

    @NonNull public Certificate createCertificate(InputStream certificateFile) {
        try {
            InputStream caInput = new BufferedInputStream(certificateFile);
            return CertificateFactory.getInstance("X.509").generateCertificate(caInput);
        } catch (CertificateException ex) {
            throw new RuntimeException(ex);
        }
    }

    @NonNull public Certificate createCertificate(SslCertificate sslCertificate) {
        Bundle bundle = SslCertificate.saveState(sslCertificate);
        byte[] bytes = bundle.getByteArray("x509-certificate");
        return createCertificate(new ByteArrayInputStream(bytes));
    }

    @NonNull private KeyStore createKeyStore(Certificate ca) {
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);
            return keyStore;
        } catch (KeyStoreException | IOException | CertificateException | NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
    }

    private TrustManager[] createTrustManager(KeyStore keyStore) {
        try {
            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);
            return tmf.getTrustManagers();
        } catch (NoSuchAlgorithmException | KeyStoreException ex) {
            throw new RuntimeException(ex);
        }
    }

    @NonNull private SSLContext createSslContext(TrustManager[] trustManagers) {
        try {
            PrngFixes.apply();
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustManagers, new SecureRandom());
            return sslContext;
        } catch (NoSuchAlgorithmException | KeyManagementException ex) {
            throw new RuntimeException(ex);
        }
    }

    public SSLContext getSslContext() {
        return sslContext;
    }

    public TrustManager[] getTrustManagers() {
        return trustManagers;
    }

    public Certificate getCertificate() {
        return certificate;
    }
}
