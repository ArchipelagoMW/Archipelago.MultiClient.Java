package gg.archipelago.APClient.uat.packets;

import com.google.gson.annotations.SerializedName;

public class UATErrorReplyPacket extends UATPacket {

    @SerializedName("reason")
    ErrorReason reason;

    @SerializedName("description")
    String message;

    @SerializedName("name")
    String command;


    public UATErrorReplyPacket(ErrorReason reason, String command, String message) {
        cmd = UATPacketType.ERROR_REPLY;
        this.reason = reason;
        this.message = message;
        this.command = command;
    }

    public enum ErrorReason {
        @SerializedName("unknown cmd")
        UNKNOWN_CMD,
        @SerializedName("missing argument")
        MISSING_ARGUMENT,
        @SerializedName("bad value")
        BAD_VALUE,
        @SerializedName("unknown")
        UNKNOWN
    }
}
