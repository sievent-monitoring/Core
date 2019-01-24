package sievent.monitoring.store.alert.jdbc;

import org.jooq.*;
import sievent.monitoring.model.alert.Alert;
import sievent.monitoring.store.StoreResult;
import sievent.monitoring.store.alert.AlertStoreBase;
import sievent.monitoring.store.exception.StoreException;
import sievent.monitoring.store.model.Tables;
import sievent.monitoring.store.model.tables.records.AlertsRecord;
import sievent.monitoring.tools.MessageManager;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class JdbcAlertStore extends AlertStoreBase {
    private static final String STORE_NULL_DSL_ERROR = "STORE_NULL_DSL_ERROR";
    private static final String STORE_INSERTED_ALERT_MESSAGE = "STORE_INSERTED_ALERT_MESSAGE";
    private static final String STORE_UPDATED_ALERT_MESSAGE = "STORE_UPDATED_ALERT_MESSAGE";
    private static final String STORE_DELETED_ALERT_MESSAGE = "STORE_DELETED_ALERT_MESSAGE";

    private final DSLContext dsl;

    public JdbcAlertStore(MessageManager messages, DSLContext dsl) {
        super(messages);
        this.dsl = dsl;
    }

    @Override
    protected StoreResult doCreate(Alert alert) {
        if (dsl == null) throw new StoreException(getMessage(JdbcAlertStore.STORE_NULL_DSL_ERROR));
        int count = buildInsertQuery(alert).execute();
        return new StoreResult(alert.getId(), count > 0, getFormatMessage(STORE_INSERTED_ALERT_MESSAGE, count));
    }


    @Override
    protected StoreResult doUpdate(Alert alert) {
        if (dsl == null) throw new StoreException(getMessage(JdbcAlertStore.STORE_NULL_DSL_ERROR));
        int count = buildUpdateQuery(alert).execute();
        return new StoreResult(alert.getId(), count > 0, getFormatMessage(STORE_UPDATED_ALERT_MESSAGE, count));
    }

    @Override
    protected StoreResult doDelete(UUID alertId) {
        if (dsl == null) throw new StoreException(getMessage(JdbcAlertStore.STORE_NULL_DSL_ERROR));
        int count = buildDeleteQuery(alertId).execute();
        return new StoreResult(alertId, count > 0, getFormatMessage(STORE_DELETED_ALERT_MESSAGE, count));
    }

    @Override
    protected Alert doRead(UUID alertId) {
        if (dsl == null) throw new StoreException(getMessage(JdbcAlertStore.STORE_NULL_DSL_ERROR));
        Alert alert = dsl.selectFrom(Tables.ALERTS).where(Tables.ALERTS.ID.equal(alertId.toString())).fetchOne(new AlertMapper());
        return alert;
    }

    @Override
    protected void doCreateMultiple(List<Alert> alerts, Map<UUID, StoreResult> results) {
        if (dsl == null) throw new StoreException(getMessage(JdbcAlertStore.STORE_NULL_DSL_ERROR));

        Query[] queries = new Query[alerts.size()];

        for (int index = 0; index < alerts.size(); index++) {
            queries[index] = buildInsertQuery(alerts.get(index));
        }

        int[] counts = dsl.batch(queries).execute();

        for (int index = 0; index < alerts.size(); index++) {
            UUID id = alerts.get(index).getId();
            StoreResult result = new StoreResult(id, counts[index] > 0, getFormatMessage(STORE_INSERTED_ALERT_MESSAGE, counts[index]));
            results.put(id, result);
        }

    }

    @Override
    protected void doUpdateMultiple(List<Alert> alerts, Map<UUID, StoreResult> results) {

        if (dsl == null) throw new StoreException(getMessage(JdbcAlertStore.STORE_NULL_DSL_ERROR));
        Query[] queries = new Query[alerts.size()];

        for (int index = 0; index < alerts.size(); index++) {
            queries[index] = buildUpdateQuery(alerts.get(index));
        }

        int[] counts = dsl.batch(queries).execute();

        for (int index = 0; index < alerts.size(); index++) {
            UUID id = alerts.get(index).getId();
            StoreResult result = new StoreResult(id, counts[index] > 0, getFormatMessage(STORE_UPDATED_ALERT_MESSAGE, counts[index]));
            results.put(id, result);
        }

    }

    @Override
    protected void doDeleteMultiple(List<UUID> alertIds, Map<UUID, StoreResult> results) {
        if (dsl == null) throw new StoreException(getMessage(JdbcAlertStore.STORE_NULL_DSL_ERROR));
        Query[] queries = new Query[alertIds.size()];

        for (int index = 0; index < alertIds.size(); index++) {
            queries[index] = buildDeleteQuery(alertIds.get(index));
        }

        int[] counts = dsl.batch(queries).execute();

        for (int index = 0; index < alertIds.size(); index++) {
            UUID id = alertIds.get(index);
            StoreResult result = new StoreResult(id, counts[index] > 0, getFormatMessage(STORE_DELETED_ALERT_MESSAGE, counts[index]));
            results.put(id, result);
        }

    }

    @Override
    protected List<Alert> doRead(List<UUID> alertIds) {
        if (dsl == null) throw new StoreException(getMessage(JdbcAlertStore.STORE_NULL_DSL_ERROR));
        return dsl.selectFrom(Tables.ALERTS).where(Tables.ALERTS.ID.in(alertIds)).fetch(new AlertMapper());
    }


    private InsertSetMoreStep<AlertsRecord> buildInsertQuery(Alert alert) {
        return dsl.insertInto(Tables.ALERTS)
                .set(Tables.ALERTS.ID, alert.getId().toString())
                .set(Tables.ALERTS.TIMESTAMP, alert.getTimestamp())
                .set(Tables.ALERTS.APPLICATION, alert.getApplication())
                .set(Tables.ALERTS.USERMESSAGE, alert.getUserMessage())
                .set(Tables.ALERTS.TECHNICALMESSAGE, alert.getTechnicalMessage())
                .set(Tables.ALERTS.RIGHTMASK, alert.getRightMask())
                .set(Tables.ALERTS.SEVERITY, alert.getSeverity().getValue())
                .set(Tables.ALERTS.CATEGORY, alert.getCategory().getValue())
                .set(Tables.ALERTS.SUBCATEGORY, alert.getSubCategory().getValue())
                .set(Tables.ALERTS.ORIGIN, alert.getOrigin().getValue())
                .set(Tables.ALERTS.STATUS, alert.getStatus().getValue())
                .set(Tables.ALERTS.VERSION, alert.getVersion())
                .set(Tables.ALERTS.ASSET, alert.getAsset())
                .set(Tables.ALERTS.REALM, alert.getRealm());
    }

    private UpdateConditionStep<AlertsRecord> buildUpdateQuery(Alert alert) {
        return dsl.update(Tables.ALERTS)
                .set(Tables.ALERTS.TIMESTAMP, alert.getTimestamp())
                .set(Tables.ALERTS.APPLICATION, alert.getApplication())
                .set(Tables.ALERTS.USERMESSAGE, alert.getUserMessage())
                .set(Tables.ALERTS.TECHNICALMESSAGE, alert.getTechnicalMessage())
                .set(Tables.ALERTS.RIGHTMASK, alert.getRightMask())
                .set(Tables.ALERTS.SEVERITY, alert.getSeverity().getValue())
                .set(Tables.ALERTS.CATEGORY, alert.getCategory().getValue())
                .set(Tables.ALERTS.SUBCATEGORY, alert.getSubCategory().getValue())
                .set(Tables.ALERTS.ORIGIN, alert.getOrigin().getValue())
                .set(Tables.ALERTS.STATUS, alert.getStatus().getValue())
                .set(Tables.ALERTS.VERSION, alert.getVersion())
                .set(Tables.ALERTS.ASSET, alert.getAsset())
                .set(Tables.ALERTS.REALM, alert.getRealm())
                .where(Tables.ALERTS.ID.equal(alert.getId().toString()));
    }

    private DeleteConditionStep<AlertsRecord> buildDeleteQuery(UUID alertId) {
        return dsl.delete(Tables.ALERTS).where(Tables.ALERTS.ID.equal(alertId.toString()));
    }

}
