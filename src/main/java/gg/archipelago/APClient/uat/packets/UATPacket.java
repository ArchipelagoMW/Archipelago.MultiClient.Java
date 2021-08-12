package gg.archipelago.APClient.uat.packets;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UATPacket {

    @Expose
    @SerializedName("cmd")
    public UATPacketType cmd;


    public enum UATPacketType {
        @SerializedName("Sync")
        SYNC,
        @SerializedName("Info")
        INFO,
        @SerializedName("Var")
        VAR,
        @SerializedName("ErrorReply")
        ERROR_REPLY
    }
}
