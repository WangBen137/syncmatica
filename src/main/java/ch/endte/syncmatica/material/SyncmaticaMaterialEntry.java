package ch.endte.syncmatica.material;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.function.Predicate;

public class SyncmaticaMaterialEntry {
    private int amountRequired;
    private int amountPresent;
    private String claimedBy;

    public JsonElement toJson() {
        final JsonObject obj = new JsonObject();
        obj.add("amountRequired", new JsonPrimitive(amountRequired));
        obj.add("amountPresent", new JsonPrimitive(amountPresent));
        if (claimedBy != null) {
            obj.add("claimedBy", new JsonPrimitive(claimedBy));
        }
        return obj;
    }

    public static SyncmaticaMaterialEntry fromJson(JsonElement element) {
        final SyncmaticaMaterialEntry entry = new SyncmaticaMaterialEntry();
        final JsonObject obj = element.getAsJsonObject();
        entry.amountRequired = obj.get("amountRequired").getAsInt();
        entry.amountPresent = obj.get("amountPresent").getAsInt();
        if (obj.has("claimedBy")) {
            entry.claimedBy = obj.get("claimedBy").getAsString();
        }
        return entry;
    }

    public static final Unclaimed UNCLAIMED = new Unclaimed();
    public static final Unfinished UNFINISHED = new Unfinished();

    public int getAmountRequired() {
        return amountRequired;
    }

    public int getAmountPresent() {
        return amountPresent;
    }

    public int getAmountMissing() {
        return amountRequired - amountPresent;
    }

    public boolean isClaimed() {
        return claimedBy != null;
    }

    public boolean isFinished() {
        return amountPresent >= amountRequired;
    }

    public static class Unclaimed implements Predicate<SyncmaticaMaterialEntry> {
        @Override
        public boolean test(final SyncmaticaMaterialEntry arg0) {
            return !arg0.isClaimed();
        }
    }

    public static class Unfinished implements Predicate<SyncmaticaMaterialEntry> {
        @Override
        public boolean test(final SyncmaticaMaterialEntry arg0) {
            return !arg0.isFinished();
        }
    }
}
