package org.pwss.service.network;

import java.io.InputStream;
import java.security.KeyStore;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import org.pwss.exception.ssl.SSLsetupErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The SSLSetup class provides methods to set up an SSL context for secure
 * communication.
 * This class is final and cannot be subclassed.
 */
final class SSLSetup {
    /**
     * Logger instance used for logging debug and error messages.
     */
    final static Logger log = LoggerFactory.getLogger(SSLSetup.class);

    /**
     * Sets up the SSLContext using a truststore file.
     *
     * @return an initialized {@link javax.net.ssl.SSLContext} instance configured
     *         with trust managers
     * @throws SSLsetupErrorException if any error occurs during setup
     */
    final static SSLContext createSSLcontext() throws SSLsetupErrorException {

        try (InputStream inputStream = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("truststore.jks")) {

            char[] truststorePassword = System.getenv("TRUSTSTORE_FIS_GUI").toCharArray();
            KeyStore truststore = KeyStore.getInstance("JKS");
            truststore.load(inputStream, truststorePassword);

            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(truststore);

            if (truststore.size() == 0) {
                throw new SSLsetupErrorException("Truststore is empty! Inga trust anchors funna.");
            }

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), null);

            log.debug("SSL-Context setup is successful");
            return sslContext;
        }

        catch (Exception exception) {
            log.error("Error setting up SSL", exception.getMessage());
            log.debug("Error setting up SSL", exception);
            throw new SSLsetupErrorException("Error setting up SSL", exception);
        }

    }

}
