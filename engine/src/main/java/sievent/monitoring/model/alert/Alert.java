package sievent.monitoring.model.alert;

import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.UUID;

public class Alert {
    private final UUID id;
    private final List<String> tags;
    private final long timestamp;
    private final String application;
    private final String userMessage;
    private final String technicalMessage;
    private final int destination;
    private final long rightMask;
    private final Severity severity;
    private final Category category;
    private final SubCategory subCategory;
    private final String origin;
    private final Status status;
    private final int version;
    private final String asset;
    private final String realm;

    protected Alert(
            final UUID id,
            long timestamp,
            String application,
            String userMessage,
            String technicalMessage,
            int destination,
            long rightMask,
            Severity severity,
            Category category,
            SubCategory subCategory,
            String origin,
            Status status,
            int version,
            String asset,
            String realm,
            List<String> tags) {
        this.id = id;
        this.tags = ImmutableList.copyOf(tags);
        this.timestamp = timestamp;
        this.application = application;
        this.userMessage = userMessage;
        this.technicalMessage = technicalMessage;
        this.destination = destination;
        this.rightMask = rightMask;
        this.severity = severity;
        this.category = category;
        this.subCategory = subCategory;
        this.origin = origin;
        this.status = status;
        this.version = version;
        this.asset = asset;
        this.realm = realm;
    }

    public UUID getId() {
        return id;
    }

    public List<String> getTags() {
        return tags;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getApplication() {
        return application;
    }

    public String getUserMessage() {
        return userMessage;
    }

    public String getTechnicalMessage() {
        return technicalMessage;
    }

    public int getDestination() {
        return destination;
    }

    public long getRightMask() {
        return rightMask;
    }

    public Severity getSeverity() {
        return severity;
    }

    public Category getCategory() {
        return category;
    }

    public SubCategory getSubCategory() {
        return subCategory;
    }

    public String getOrigin() {
        return origin;
    }

    public Status getStatus() {
        return status;
    }

    public int getVersion() {
        return version;
    }

    public String getAsset() {
        return asset;
    }

    public String getRealm() {
        return realm;
    }
}
