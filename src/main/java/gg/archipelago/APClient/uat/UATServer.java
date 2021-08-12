package gg.archipelago.APClient.uat;

import com.google.gson.*;
import gg.archipelago.APClient.network.APPacket;
import gg.archipelago.APClient.uat.packets.UATErrorReplyPacket;
import gg.archipelago.APClient.uat.packets.UATInfoPacket;
import gg.archipelago.APClient.uat.packets.UATPacket;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

public class UATServer extends WebSocketServer {

    private final static java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(UATServer.class.getName());

    private final Gson gson = new Gson();

    private final UATPacket infoPacket;

    List<WebSocket> connections = new ArrayList<>();

    private UATServer(int port, String name, String version, int protocol, String[] tags, String[] slots) {
        super(new InetSocketAddress("0.0.0.0",port));
        infoPacket = new UATInfoPacket(name, version, protocol, tags, slots);
    }

    private UATServer(int port, String name, String version, int protocol, String[] tags) {
        this(port, name,version,protocol,tags,null);
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        connections.add(conn);
        LOGGER.info("[UAT] new connection from " + conn.getLocalSocketAddress());
        sendPacket(infoPacket, conn);
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        connections.remove(conn);
        LOGGER.info("[UAT] lost connection to " + conn.getLocalSocketAddress());
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        LOGGER.info("[UAT] packet from: " + conn.getLocalSocketAddress() + "\n"+message);
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(message);

        JsonArray cmdList = element.getAsJsonArray();

        for(int i = 0; cmdList.size() > i; ++i) {
            JsonElement packet = cmdList.get(i);
            //parse the packet first to see what command has been sent.
            UATPacket command = gson.fromJson(packet, UATPacket.class);

            switch (command.cmd) {
                case VAR:
                    sendPacket(new UATErrorReplyPacket(UATErrorReplyPacket.ErrorReason.UNKNOWN_CMD, "var", "Not Yet Implemented ¯\\_(ツ)_/¯"), conn);
                    break;
                case SYNC:
                    sendPacket(new UATErrorReplyPacket(UATErrorReplyPacket.ErrorReason.UNKNOWN_CMD, "sync", "Not Yet Implemented ¯\\_(ツ)_/¯"), conn);
                    break;
                default:
                    JsonObject rawPacket = packet.getAsJsonObject();
                    UATPacket error = new UATErrorReplyPacket(UATErrorReplyPacket.ErrorReason.UNKNOWN_CMD, rawPacket.get("cmd").getAsString(), "¯\\_(ツ)_/¯");
                    sendPacket(error, conn);
            }
        }
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        if (conn != null) {
            ex.printStackTrace();
            LOGGER.info("[UAT] error: " + ex.getLocalizedMessage());
        }
    }

    @Override
    public void onStart() {
        LOGGER.info("[UAT] Server Started");
        setConnectionLostTimeout(100);
    }

    public void sendPacket(UATPacket packet, WebSocket connection) {
        sendManyPackets(new UATPacket[]{packet}, connection);
    }

    public void sendManyPackets(UATPacket[] packet, WebSocket connection) {
        String json = gson.toJson(packet);
        LOGGER.info("Sent Packet: "+json);
        this.broadcast(json);
    }

    static public UATServer start(String name, String version, int protocol, String[] tags) {
        UATServer server = new UATServer(65399, name, version, protocol, tags);
        server.run();
        return server;
    }
}
