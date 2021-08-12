package gg.archipelago.APClient.uat.packets;

import com.google.gson.annotations.SerializedName;

public class UATInfoPacket extends UATPacket{

    @SerializedName("name")
    private String name;

    @SerializedName("version")
    private String version;

    @SerializedName("protocol")
    private int protocol;

    @SerializedName("slots")
    private String[] slots;

    @SerializedName("features")
    private String[] tags;

    public UATInfoPacket(String name, String version, int protocol, String[] tags, String[] slots) {
        cmd = UATPacketType.INFO;
        this.name = name;
        this.version = version;
        this.protocol = protocol;
        this.tags = tags;
        this.slots = slots;
    }

}
