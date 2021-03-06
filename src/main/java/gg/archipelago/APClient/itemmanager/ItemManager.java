package gg.archipelago.APClient.itemmanager;

import gg.archipelago.APClient.APClient;
import gg.archipelago.APClient.APWebSocket;
import gg.archipelago.APClient.network.SyncPacket;
import gg.archipelago.APClient.parts.DataPackage;
import gg.archipelago.APClient.parts.NetworkItem;

import java.util.ArrayList;

public class ItemManager {


    APClient apClient;
    APWebSocket webSocket;

    ArrayList<NetworkItem> receivedItems = new ArrayList<>();

    int index;

    public ItemManager(APClient apClient) {
        this.apClient = apClient;
    }

    public void receiveItems(ArrayList<NetworkItem> ids, long index) {
        if (index == 0) {
            receivedItems = new ArrayList<>();
        }
        if (receivedItems.size() == index) {
            receivedItems.addAll(ids);
            DataPackage dp = apClient.getDataPackage();
            int myTeam = apClient.getTeam();
            for (int i = this.index; i < receivedItems.size(); i++) {
                NetworkItem item = receivedItems.get(i);
                item.itemName = dp.getItem(item.itemID);
                item.locationName = dp.getLocation(item.locationID);
                item.playerName = apClient.getRoomInfo().getPlayer(myTeam,item.playerID).alias;
                apClient.onReceiveItem(item);
            }

            this.index = receivedItems.size();
            apClient.getDataManager().save();
        }
        else {
            if(webSocket != null) {
                webSocket.sendPacket(new SyncPacket());
                apClient.getLocationManager().sendAllLocations();
            }
        }
    }

    public void writeFromSave(ArrayList<NetworkItem> receivedItems, int index) {
        this.receivedItems = receivedItems;
        this.index = index;
    }

    public void setAPWebSocket(APWebSocket apWebSocket) {
        this.webSocket = apWebSocket;
    }

    public int getIndex() {
        return index;
    }

    public ArrayList<NetworkItem> getReceivedItems() {
        return receivedItems;
    }

    public ArrayList<Long> getReceivedItemIDs() {
        ArrayList<Long> ids = new ArrayList<>();
        for (NetworkItem receivedItem : receivedItems) {
            ids.add(receivedItem.itemID);
        }
        return ids;
    }
}
