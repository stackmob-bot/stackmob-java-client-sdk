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

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;

import org.apache.http.conn.scheme.LayeredSocketFactory;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

public class SimpleSSLSocketFactory implements SocketFactory, LayeredSocketFactory {

  private SSLContext sslContext = null;

  private static SSLContext createSimpleSSLContext() throws IOException {
    try {
      SSLContext context = SSLContext.getInstance("TLS");
      context.init(null, new TrustManager[] { new AcceptInvalidX509TrustManager() }, null);
      return context;
    } catch (Exception e) {
      throw new IOException(e.getMessage());
    }
  }

  private SSLContext getSSLContext() throws IOException {
    if (sslContext == null) {
      sslContext = createSimpleSSLContext();
    }
    return sslContext;
  }

  @Override
  public Socket connectSocket(Socket sock, String host, int port, InetAddress localAddress,
                              int localPort, HttpParams params) throws IOException {
    int connTimeout = HttpConnectionParams.getConnectionTimeout(params);
    int soTimeout = HttpConnectionParams.getSoTimeout(params);

    InetSocketAddress remoteAddress = new InetSocketAddress(host, port);
    SSLSocket sslsock = (SSLSocket) ((sock != null) ? sock : createSocket());

    if ((localAddress != null) || (localPort > 0)) {
      if (localPort < 0) {
        localPort = 0;
      }
      InetSocketAddress isa = new InetSocketAddress(localAddress, localPort);
      sslsock.bind(isa);
    }

    sslsock.connect(remoteAddress, connTimeout);
    sslsock.setSoTimeout(soTimeout);
    return sslsock;
  }

  @Override
  public Socket createSocket() throws IOException {
    return getSSLContext().getSocketFactory().createSocket();
  }

  @Override
  public boolean isSecure(Socket socket)
      throws IllegalArgumentException {
    return true;
  }

  @Override
  public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException {
    return getSSLContext().getSocketFactory().createSocket(socket, host, port, autoClose);
  }
  
}
