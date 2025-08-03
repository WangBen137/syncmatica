package ch.endte.syncmatica.material;

import ch.endte.syncmatica.ServerPosition;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class SyncmaticaMaterialList {
    private ArrayList<SyncmaticaMaterialEntry> list;
    private ServerPosition deliveryPoint;

    public JsonElement toJson() {
        final JsonArray arr = new JsonArray();
        for (final SyncmaticaMaterialEntry entry : list) {
            arr.add(entry.toJson());
        }
        final JsonObject obj = new JsonObject();
        obj.add("list", arr);
        if (deliveryPoint != null) {
            obj.add("deliveryPoint", deliveryPoint.toJson());
        }
        return obj;
    }

    public static SyncmaticaMaterialList fromJson(JsonElement element) {
        final SyncmaticaMaterialList matList = new SyncmaticaMaterialList();
        final JsonObject obj = element.getAsJsonObject();
        final JsonArray arr = obj.get("list").getAsJsonArray();
        matList.list = new ArrayList<>();
        for (final JsonElement entryElement : arr) {
            matList.list.add(SyncmaticaMaterialEntry.fromJson(entryElement));
        }
        if (obj.has("deliveryPoint")) {
            matList.deliveryPoint = ServerPosition.fromJson(obj.get("deliveryPoint").getAsJsonObject());
        }
        return matList;
    }

    public SyncmaticaMaterialEntry getUnclaimedEntry() {
        final Optional<SyncmaticaMaterialEntry> unclaimed = list.parallelStream().filter(SyncmaticaMaterialEntry.UNFINISHED).filter(SyncmaticaMaterialEntry.UNCLAIMED).findFirst();
        if (unclaimed.isPresent()) {
            return unclaimed.get();
        }
        return null;
    }

    public Collection<DeliveryPosition> getDeliveryPosition(final SyncmaticaMaterialEntry entry) {
        if (!list.contains(entry)) {
            throw new IllegalArgumentException();
        }
        final DeliveryPosition delivery = new DeliveryPosition(deliveryPoint.getBlockPosition(), deliveryPoint.getDimensionId(), entry.getAmountMissing());
        final ArrayList<DeliveryPosition> deliveryList = new ArrayList<>();
        deliveryList.add(delivery);
        return deliveryList;
    }
}
