package gg.archipelago.APClient.uat.packets;

import com.google.gson.annotations.SerializedName;

public class UATSyncPacket extends UATPacket {

    @SerializedName("slot")
    public String slot;

    UATSyncPacket() {
        this.cmd = UATPacketType.SYNC;
    }
}
