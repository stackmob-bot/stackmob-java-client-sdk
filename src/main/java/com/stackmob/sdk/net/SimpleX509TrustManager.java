/**
 * Copyright 2011 StackMob
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.stackmob.sdk.net;

import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class SimpleX509TrustManager implements X509TrustManager {

  private X509TrustManager standardTrustManager = null;

  public SimpleX509TrustManager(KeyStore keystore) throws NoSuchAlgorithmException, KeyStoreException {
    TrustManagerFactory factory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
    factory.init(keystore);
    TrustManager[] trustmanagers = factory.getTrustManagers();
    if (trustmanagers.length == 0) {
      throw new NoSuchAlgorithmException("No trust manager found");
    }
    this.standardTrustManager = (X509TrustManager) trustmanagers[0];
  }

  /**
   * @see javax.net.ssl.X509TrustManager#checkClientTrusted(X509Certificate[],
   *      String authType)
   */
  @Override
  public void checkClientTrusted(X509Certificate[] certificates, String authType) throws CertificateException {
    standardTrustManager.checkClientTrusted(certificates, authType);
  }

  /**
   * @see javax.net.ssl.X509TrustManager#checkServerTrusted(X509Certificate[],
   *      String authType)
   */
  @Override
  public void checkServerTrusted(X509Certificate[] certificates, String authType) throws CertificateException {
    if ((certificates != null) && (certificates.length == 1)) {
      certificates[0].checkValidity();
    } else {
      standardTrustManager.checkServerTrusted(certificates, authType);
    }
  }

  /**
   * @see javax.net.ssl.X509TrustManager#getAcceptedIssuers()
   */
  @Override
  public X509Certificate[] getAcceptedIssuers() {
    return this.standardTrustManager.getAcceptedIssuers();
  }
  
}
