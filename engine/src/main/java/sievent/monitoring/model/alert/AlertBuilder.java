package sievent.monitoring.model.alert;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AlertBuilder {

    private UUID id;
    private List<String> tags;
    private long timestamp;
    private String application;
    private String userMessage;
    private String technicalMessage;
    private long rightMask;
    private Severity severity;
    private Category category;
    private SubCategory subCategory;
    private Origin origin;
    private Status status;
    private int version;
    private String asset;
    private String realm;

    public AlertBuilder() {
        this.id = UUID.randomUUID();
        this.tags = Lists.newArrayList();
        this.status = Status.Open;
        this.category = Category.Applicative;
        this.severity = Severity.Informational;
        this.version = 0;
    }

    public AlertBuilder(Alert alert) {
        this();
        if (alert != null) {
            this.id = alert.getId();
            this.tags = new ArrayList<>(alert.getTags());
            this.timestamp = alert.getTimestamp();
            this.application = alert.getApplication();
            this.userMessage = alert.getUserMessage();
            this.technicalMessage = alert.getTechnicalMessage();
            this.rightMask = alert.getRightMask();
            this.severity = alert.getSeverity();
            this.category = alert.getCategory();
            this.subCategory = alert.getSubCategory();
            this.origin = alert.getOrigin();
            this.status = alert.getStatus();
            this.version = alert.getVersion();
            this.asset = alert.getAsset();
            this.realm = alert.getRealm();
        }
    }

    public Alert build() {
        return new Alert(this.id, this.timestamp, this.application, this.userMessage, this.technicalMessage,
                this.rightMask, this.severity, this.category, this.subCategory
                , this.origin, this.status, this.version, this.asset, this.realm, this.tags);
    }

    public AlertBuilder id(UUID id) {
        this.id = id;
        return this;
    }

    public AlertBuilder tag(List<String> tags) {
        if (tags == null) return this;
        this.tags.addAll(tags);
        return this;
    }

    public AlertBuilder tag(String tag) {
        if (Strings.isNullOrEmpty(tag)) return this;
        this.tags.add(tag);
        return this;
    }

    public AlertBuilder unTag(String tag) {
        if (Strings.isNullOrEmpty(tag)) return this;
        this.tags.remove(tag);
        return this;
    }

    public AlertBuilder unTagAll() {
        this.tags.clear();
        return this;
    }

    public AlertBuilder timestamp(long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public AlertBuilder application(String application) {
        this.application = application;
        return this;
    }

    public AlertBuilder userMessage(String userMessage) {
        this.userMessage = userMessage;
        return this;
    }

    public AlertBuilder technicalMessage(String technicalMessage) {
        this.technicalMessage = technicalMessage;
        return this;
    }

    public AlertBuilder rightMask(long rightMask) {
        this.rightMask = rightMask;
        return this;
    }

    public AlertBuilder severity(Severity severity) {
        this.severity = severity;
        return this;
    }

    public AlertBuilder category(Category category) {
        this.category = category;
        return this;
    }

    public AlertBuilder subCategory(SubCategory subCategory) {
        this.subCategory = subCategory;
        return this;
    }

    public AlertBuilder origin(Origin origin) {
        this.origin = origin;
        return this;
    }

    public AlertBuilder status(Status status) {
        this.status = status;
        return this;
    }

    public AlertBuilder version(int version) {
        this.version = version;
        return this;
    }

    public AlertBuilder asset(String asset) {
        this.asset = asset;
        return this;
    }

    public AlertBuilder realm(String realm) {
        this.realm = realm;
        return this;
    }
}
