package sievent.monitoring.store.alert.jdbc;

import org.jooq.RecordMapper;
import sievent.monitoring.model.alert.*;
import sievent.monitoring.store.model.tables.records.AlertsRecord;

import java.util.UUID;

public class AlertMapper implements RecordMapper<AlertsRecord, Alert> {
    @Override
    public Alert map(AlertsRecord record) {
        Alert alert = new AlertBuilder()
                .id(UUID.fromString(record.getId()))
                .timestamp(record.getTimestamp())
                .application(record.getApplication())
                .userMessage(record.getUsermessage())
                .technicalMessage(record.getTechnicalmessage())
                .rightMask(record.getRightmask())
                .severity(Severity.valueOf(record.getSeverity()))
                .category(Category.valueOf(record.getCategory()))
                .subCategory(SubCategory.valueOf(record.getSubcategory()))
                .origin(Origin.valueOf(record.getOrigin()))
                .status(Status.valueOf(record.getStatus()))
                .version(record.getVersion())
                .asset(record.getAsset())
                .realm(record.getRealm()).build();
        return alert;
    }
}
