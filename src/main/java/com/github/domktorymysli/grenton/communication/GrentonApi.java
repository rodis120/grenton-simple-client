package com.github.domktorymysli.grenton.communication;

import com.github.domktorymysli.grenton.cipher.api.Encoder;
import com.github.domktorymysli.grenton.cipher.api.exception.GrentonEncoderException;
import com.github.domktorymysli.grenton.cipher.model.MessageDecoded;
import com.github.domktorymysli.grenton.cipher.model.MessageEncoded;
import com.github.domktorymysli.grenton.command.CluCommand;
import com.github.domktorymysli.grenton.command.CluCommandResponse;
import com.github.domktorymysli.grenton.excpetion.GrentonIoException;
import com.github.domktorymysli.grenton.model.Clu;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public final class GrentonApi implements Api {
    private final Clu clu;
    private final Encoder encoder;
    private final DatagramSocket socket;

    private static final int TIMEOUT = 2000;
    private static final int BUFFER_LENGTH = 2048;

    public GrentonApi(Clu clu, Encoder encoder) throws SocketException {
        this.clu = clu;
        this.encoder = encoder;
        this.socket = new DatagramSocket();
    }

    public CluCommandResponse send(CluCommand command) throws GrentonIoException, GrentonEncoderException {

        try {
            MessageEncoded messageEncoded = encoder.encode(new MessageDecoded(command.getCommand()));
            byte[] message = messageEncoded.getMsg();
            InetAddress address = clu.getIp();
            int port = clu.getPort();

            DatagramPacket datagramPacket = new DatagramPacket(message, message.length, address, port);
            DatagramPacket response = new DatagramPacket(new byte[BUFFER_LENGTH], BUFFER_LENGTH);

            socket.setSoTimeout(TIMEOUT);
            socket.send(datagramPacket);
            socket.receive(response);

            MessageDecoded messageDecoded = encoder.decode(new MessageEncoded(response.getData(), response.getLength()));

            return new CluCommandResponse(messageDecoded);

        } catch (IOException e) {
            throw new GrentonIoException(e);
        }
    }

    public void close() {
        socket.close();
    }
}

