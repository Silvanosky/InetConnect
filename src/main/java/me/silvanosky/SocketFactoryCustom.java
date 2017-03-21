package me.silvanosky;

import javax.net.SocketFactory;

import java.io.IOException;
import java.net.*;

/**
 * ╱╲＿＿＿＿＿＿╱╲
 * ▏╭━━╮╭━━╮▕
 * ▏┃＿＿┃┃＿＿┃▕
 * ▏┃＿▉┃┃▉＿┃▕
 * ▏╰━━╯╰━━╯▕
 * ╲╰╰╯╲╱╰╯╯╱  Created by Silvanosky on 13/03/2017
 * ╱╰╯╰╯╰╯╰╯╲
 * ▏▕╰╯╰╯╰╯▏▕
 * ▏▕╯╰╯╰╯╰▏▕
 * ╲╱╲╯╰╯╰╱╲╱
 * ＿＿╱▕▔▔▏╲＿＿
 * ＿＿▔▔＿＿▔▔＿＿
 */
public class SocketFactoryCustom extends SocketFactory {
    private static final String ERROR_MSG = "On unconnected sockets are supported";

    private final SocketFactory delegate;
    private final NetworkInterface localNet;

    public SocketFactoryCustom(final SocketFactory delegate,
                                     final NetworkInterface net) {
        this.delegate = delegate;
        this.localNet = net;
    }

    @Override
    public Socket createSocket() throws IOException {
        Socket socket = delegate.createSocket();
        socket.bind(new InetSocketAddress(localNet.getInetAddresses().nextElement(), 0));
        return socket;
    }

    @Override
    public Socket createSocket(final String remoteAddress, final int remotePort)
            throws IOException, UnknownHostException {
        throw new UnsupportedOperationException(ERROR_MSG);
    }

    @Override
    public Socket createSocket(final InetAddress remoteAddress, final int remotePort)
            throws IOException {
        throw new UnsupportedOperationException(ERROR_MSG);
    }

    @Override
    public Socket createSocket(final String remoteAddress, final int remotePort,
                               final InetAddress localAddress, final int localPort)
            throws IOException, UnknownHostException {
        throw new UnsupportedOperationException(ERROR_MSG);
    }

    @Override
    public Socket createSocket(final InetAddress remoteAddress, final int remotePort,
                               final InetAddress localAddress, final int localPort)
            throws IOException {
        throw new UnsupportedOperationException(ERROR_MSG);
    }
}
