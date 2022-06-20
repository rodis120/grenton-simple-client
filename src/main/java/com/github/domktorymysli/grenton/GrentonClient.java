package com.github.domktorymysli.grenton;

import com.github.domktorymysli.grenton.cipher.api.Encoder;
import com.github.domktorymysli.grenton.cipher.api.EncoderGrenton;
import com.github.domktorymysli.grenton.communication.Api;
import com.github.domktorymysli.grenton.communication.GrentonApi;
import com.github.domktorymysli.grenton.model.Clu;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class GrentonClient {

    private final Api api;

    GrentonClient (String ip, int port, String key, String iv) throws UnknownHostException, SocketException {
        final Clu clu = new Clu(ip, port);
        final Encoder encoder = new EncoderGrenton(key, iv);

        api = new GrentonApi(clu, encoder, new DatagramSocket());
    }

    public Api getGrentonApi() {
        return api;
    }

    public void close() {
        api.close();
    }

}
