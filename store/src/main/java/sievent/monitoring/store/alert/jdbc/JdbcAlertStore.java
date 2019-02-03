package sievent.monitoring.store.alert.jdbc;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.jooq.*;
import sievent.monitoring.model.alert.*;
import sievent.monitoring.store.StoreResult;
import sievent.monitoring.store.alert.AlertStoreBase;
import sievent.monitoring.store.exception.StoreException;
import sievent.monitoring.store.model.Tables;
import sievent.monitoring.store.model.tables.records.AlertTagsRecord;
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
    protected StoreResult doCreate(final Alert alert) {
        if (dsl == null) throw new StoreException(getMessage(JdbcAlertStore.STORE_NULL_DSL_ERROR));

        final StoreResult result = new StoreResult(alert.getId(), false, 0, null);
        dsl.transaction(c -> {
                    int alertCount = buildInsertQuery(alert).execute();
                    result.setStatus(true);
                    result.setCount(alertCount);
                    int tagCount = buildInsertTags(alert).execute();
                    result.setMessage(getFormatMessage(STORE_INSERTED_ALERT_MESSAGE, alertCount, tagCount));
                }
        );

        return result;
    }


    @Override
    protected StoreResult doUpdate(final Alert alert) {
        if (dsl == null) throw new StoreException(getMessage(JdbcAlertStore.STORE_NULL_DSL_ERROR));
        int count = buildUpdateQuery(alert).execute();

        final StoreResult result = new StoreResult(alert.getId(), false, 0, null);
        dsl.transaction(c -> {

                    int alertCount = buildUpdateQuery(alert).execute();
                    result.setStatus(true);
                    result.setCount(alertCount);
                    buildDeleteTags(alert.getId()).execute();
                    int tagCount = buildInsertTags(alert).execute();
                    result.setMessage(getFormatMessage(STORE_INSERTED_ALERT_MESSAGE, alertCount, tagCount));
                }
        );

        return result;
    }

    @Override
    protected StoreResult doDelete(final UUID alertId) {
        if (dsl == null) throw new StoreException(getMessage(JdbcAlertStore.STORE_NULL_DSL_ERROR));
        StoreResult result = new StoreResult(alertId, false, 0);
        dsl.transaction(c -> {
            int count = buildDeleteTags(alertId).execute();
            count = buildDeleteQuery(alertId).execute();
            result.setMessage(getFormatMessage(STORE_DELETED_ALERT_MESSAGE, count));
            result.setCount(count);
            result.setStatus(count > 0);
        });
        return result;
    }

    @Override
    protected Alert doRead(final UUID alertId) {
        if (dsl == null) throw new StoreException(getMessage(JdbcAlertStore.STORE_NULL_DSL_ERROR));



        final AlertBuilder builder = new AlertBuilder();

       List<AlertBuilder> results = dsl.select(Tables.ALERTS.fields())
                .select(Tables.ALERT_TAGS.TAG)
                .from(Tables.ALERTS)
                .leftOuterJoin(Tables.ALERT_TAGS).on(Tables.ALERTS.ID.eq(Tables.ALERT_TAGS.ID))
                .where(Tables.ALERTS.ID.equal(alertId.toString()))
                .fetch(r -> {
                    builder.id(alertId)
                            .timestamp(r.get(Tables.ALERTS.TIMESTAMP))
                            .application(r.get(Tables.ALERTS.APPLICATION))
                            .userMessage(r.get(Tables.ALERTS.USERMESSAGE))
                            .technicalMessage(r.get(Tables.ALERTS.TECHNICALMESSAGE))
                            .rightMask(r.get(Tables.ALERTS.RIGHTMASK))
                            .severity(Severity.valueOf(r.get(Tables.ALERTS.SEVERITY)))
                            .category(Category.valueOf(r.get(Tables.ALERTS.CATEGORY)))
                            .subCategory((r.get(Tables.ALERTS.SUBCATEGORY) != null) ? SubCategory.valueOf(r.get(Tables.ALERTS.SUBCATEGORY)) : null)
                            .origin((r.get(Tables.ALERTS.ORIGIN) != null) ? Origin.valueOf(r.get(Tables.ALERTS.ORIGIN)) : null)
                            .status(Status.valueOf(r.get(Tables.ALERTS.STATUS)))
                            .version(r.get(Tables.ALERTS.VERSION))
                            .asset(r.get(Tables.ALERTS.ASSET))
                            .realm(r.get(Tables.ALERTS.REALM));
                    String tag = r.get(Tables.ALERT_TAGS.TAG);
                    if (tag != null) {
                        builder.tag(tag);
                    }
                    return builder;
                });


        return (results.size() > 0)? builder.build():null;
    }

    @Override
    protected void doCreateMultiple(final List<Alert> alerts, final Map<UUID, StoreResult> results) {
        if (dsl == null) throw new StoreException(getMessage(JdbcAlertStore.STORE_NULL_DSL_ERROR));

        Query[] queries = new Query[2 * alerts.size()];

        for (int index = 0; index < alerts.size(); index++) {
            queries[2 * index] = buildInsertQuery(alerts.get(index));
            if (alerts.get(index).getTags().size() > 0) {
                queries[2 * index + 1] = buildInsertTags(alerts.get(index));
            }
        }


        dsl.transaction(c -> {
            int[] counts = dsl.batch(queries).execute();
            for (int index = 0; index < alerts.size(); index++) {
                UUID id = alerts.get(index).getId();
                boolean expectedAlertCount = counts[2 * index] > 0;
                boolean expectedTagtCount = alerts.get(index).getTags().size() == counts[2 * index + 1];
                StoreResult result = new StoreResult(id, expectedAlertCount && expectedTagtCount, counts[2 * index], getFormatMessage(STORE_INSERTED_ALERT_MESSAGE, counts[2 * index], counts[2 * index + 1]));
                results.put(id, result);
            }
        });


    }

    @Override
    protected void doUpdateMultiple(final List<Alert> alerts, final Map<UUID, StoreResult> results) {

        if (dsl == null) throw new StoreException(getMessage(JdbcAlertStore.STORE_NULL_DSL_ERROR));
        Query[] queries = new Query[3 * alerts.size()];

        for (int index = 0; index < alerts.size(); index++) {
            queries[3 * index] = buildUpdateQuery(alerts.get(index));
            queries[3 * index + 1] = buildDeleteTags(alerts.get(index).getId());
            if (alerts.get(index).getTags().size() > 0) {
                queries[3 * index + 2] = buildInsertTags(alerts.get(index));
            }
        }

        dsl.transaction(c -> {
            int[] counts = dsl.batch(queries).execute();
            for (int index = 0; index < alerts.size(); index++) {
                UUID id = alerts.get(index).getId();
                boolean expectedAlertCount = counts[3 * index] > 0;
                boolean expectedTagtCount = alerts.get(index).getTags().size() == counts[3 * index + 2];
                StoreResult result = new StoreResult(id, expectedAlertCount && expectedTagtCount, counts[3 * index], getFormatMessage(STORE_UPDATED_ALERT_MESSAGE, counts[index], counts[3 * index + 2]));
                results.put(id, result);
            }
        });
    }

    @Override
    protected void doDeleteMultiple(List<UUID> alertIds, Map<UUID, StoreResult> results) {
        if (dsl == null) throw new StoreException(getMessage(JdbcAlertStore.STORE_NULL_DSL_ERROR));
        Query[] queries = new Query[2 * alertIds.size()];

        for (int index = 0; index < alertIds.size(); index++) {
            queries[2 * index] = buildDeleteTags(alertIds.get(index));
            queries[2 * index + 1] = buildDeleteQuery(alertIds.get(index));
        }

        dsl.transaction(c -> {
            int[] counts = dsl.batch(queries).execute();

            for (int index = 0; index < alertIds.size(); index++) {
                UUID id = alertIds.get(index);
                StoreResult result = new StoreResult(id, counts[2 * index + 1] > 0, counts[index + 1], getFormatMessage(STORE_DELETED_ALERT_MESSAGE, counts[2 * index + 1]));
                results.put(id, result);
            }
        });

    }

    @Override
    protected List<Alert> doRead(List<UUID> alertIds) {
        if (dsl == null) throw new StoreException(getMessage(JdbcAlertStore.STORE_NULL_DSL_ERROR));
//        return dsl.selectFrom(Tables.ALERTS)
//                .where(Tables.ALERTS.ID.in(alertIds))
//                .fetch(new AlertMapper());


        final Map<UUID, AlertBuilder> builders = Maps.newConcurrentMap();

        List<AlertBuilder> alerts =
                dsl.select(Tables.ALERTS.fields())
                        .select(Tables.ALERT_TAGS.TAG)
                        .from(Tables.ALERTS)
                        .leftOuterJoin(Tables.ALERT_TAGS).on(Tables.ALERTS.ID.eq(Tables.ALERT_TAGS.ID))
                        .where(Tables.ALERTS.ID.in(alertIds))
                        .fetch(r -> {
                            UUID alertId = UUID.fromString(r.get(Tables.ALERTS.ID));
                            AlertBuilder builder = builders.get(alertId);
                            if (builder == null) {
                                builder = new AlertBuilder()
                                        .id(alertId)
                                        .timestamp(r.get(Tables.ALERTS.TIMESTAMP))
                                        .application(r.get(Tables.ALERTS.APPLICATION))
                                        .userMessage(r.get(Tables.ALERTS.USERMESSAGE))
                                        .technicalMessage(r.get(Tables.ALERTS.TECHNICALMESSAGE))
                                        .rightMask(r.get(Tables.ALERTS.RIGHTMASK))
                                        .severity(Severity.valueOf(r.get(Tables.ALERTS.SEVERITY)))
                                        .category(Category.valueOf(r.get(Tables.ALERTS.CATEGORY)))
                                        .subCategory((r.get(Tables.ALERTS.SUBCATEGORY) != null) ? SubCategory.valueOf(r.get(Tables.ALERTS.SUBCATEGORY)) : null)
                                        .origin((r.get(Tables.ALERTS.ORIGIN) != null) ? Origin.valueOf(r.get(Tables.ALERTS.ORIGIN)) : null)
                                        .status(Status.valueOf(r.get(Tables.ALERTS.STATUS)))
                                        .version(r.get(Tables.ALERTS.VERSION))
                                        .asset(r.get(Tables.ALERTS.ASSET))
                                        .realm(r.get(Tables.ALERTS.REALM));

                                builders.put(alertId, builder);
                            }
                            builder.tag(r.get(Tables.ALERT_TAGS.TAG));
                            return builder;
                        });

        return Lists.transform(Lists.newArrayList(builders.values()), a -> a.build());
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
                .set(Tables.ALERTS.SUBCATEGORY, (alert.getSubCategory() != null) ? alert.getSubCategory().getValue() : null)
                .set(Tables.ALERTS.ORIGIN, (alert.getOrigin() != null) ? alert.getOrigin().getValue() : null)
                .set(Tables.ALERTS.STATUS, alert.getStatus().getValue())
                .set(Tables.ALERTS.VERSION, alert.getVersion())
                .set(Tables.ALERTS.ASSET, alert.getAsset())
                .set(Tables.ALERTS.REALM, alert.getRealm());
    }

    private InsertValuesStep2<AlertTagsRecord, String, String> buildInsertTags(final Alert alert) {
        InsertValuesStep2<AlertTagsRecord, String, String> query = dsl.insertInto(Tables.ALERT_TAGS, Tables.ALERT_TAGS.ID, Tables.ALERT_TAGS.TAG);
        String alertId = alert.getId().toString();
        alert.getTags().stream().forEach(t -> {
            query.values(alertId, t);
        });
        return query;

    }

    private DeleteConditionStep<AlertTagsRecord> buildDeleteTags(final UUID alertId) {
        return dsl.delete(Tables.ALERT_TAGS).where(Tables.ALERT_TAGS.ID.equal(alertId.toString()));

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
                .set(Tables.ALERTS.SUBCATEGORY, (alert.getSubCategory() != null) ? alert.getSubCategory().getValue() : null)
                .set(Tables.ALERTS.ORIGIN, (alert.getOrigin() != null) ? alert.getOrigin().getValue() : null)
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
