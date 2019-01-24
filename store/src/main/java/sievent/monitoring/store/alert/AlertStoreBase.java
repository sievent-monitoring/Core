package sievent.monitoring.store.alert;

import com.google.common.collect.Maps;
import sievent.monitoring.model.alert.Alert;
import sievent.monitoring.store.StoreResult;
import sievent.monitoring.store.exception.StoreException;
import sievent.monitoring.tools.MessageManager;

import java.util.*;

public abstract class AlertStoreBase implements IAlertStore {
    private static final String STORE_NULL_ALERT_ERROR = "STORE_NULL_ALERT_ERROR";
    private static final String STORE_NULL_ALERT_ID_ERROR = "STORE_NULL_ALERT_ID_ERROR";
    private static final String STORE_NULL_ALERT_LIST_ERROR = "STORE_NULL_ALERT_LIST_ERROR";
    private static final String STORE_NULL_ALERT_LIST_ID_ERROR = "STORE_NULL_ALERT_LIST_ID_ERROR";
    private static final String STORE_LIST_CONTAINS_NULL_ALERT_ERROR = "STORE_LIST_CONTAINS_NULL_ALERT_ERROR";
    private static final String STORE_LIST_CONTAINS_NULL_ALERT_ID_ERROR = "STORE_LIST_CONTAINS_NULL_ALERT_ID_ERROR";

    private static final UUID NULL_ALERT_ID = new UUID(0L, 0L);

    private MessageManager messages;

    protected AlertStoreBase(MessageManager messages) {
        this.messages = messages;
    }

    @Override
    public StoreResult create(Alert alert) throws StoreException {
        if (alert == null) {
            return buildEmptyResult(AlertStoreBase.STORE_NULL_ALERT_ERROR);
        }
        return doCreate(alert);
    }

    protected abstract StoreResult doCreate(Alert alert);

    @Override
    public StoreResult update(Alert alert) throws StoreException {
        if (alert == null) {
            return buildEmptyResult(AlertStoreBase.STORE_NULL_ALERT_ERROR);
        }
        return doUpdate(alert);
    }

    protected abstract StoreResult doUpdate(Alert alert);

    @Override
    public StoreResult delete(UUID alertId) throws StoreException {
        if (alertId == null) {
            return buildEmptyResult(AlertStoreBase.STORE_NULL_ALERT_ID_ERROR);
        }
        return doDelete(alertId);
    }

    protected abstract StoreResult doDelete(UUID alertId);

    @Override
    public Alert read(UUID alertId) throws StoreException {
        if (alertId == null) {
            throw new StoreException(getMessage(AlertStoreBase.STORE_NULL_ALERT_ID_ERROR));
        }
        return doRead(alertId);
    }

    protected abstract Alert doRead(UUID alertId);

    @Override
    public Map<UUID, StoreResult> createMultiple(List<Alert> alerts) throws StoreException {
        if (alerts == null) {
            return buildEmptyResultMap(AlertStoreBase.STORE_NULL_ALERT_LIST_ERROR);
        }
        Map<UUID, StoreResult> results = null;
        boolean hasNull = alerts.removeIf(a -> a == null);
        if (hasNull) {
            results = buildEmptyResultMap(AlertStoreBase.STORE_LIST_CONTAINS_NULL_ALERT_ERROR);
        }
        if (alerts.size() > 0) {
            results = (results == null) ? Maps.newHashMap() : results;
            doCreateMultiple(alerts, results);
        }
        return results;
    }

    protected abstract void doCreateMultiple(List<Alert> alerts, Map<UUID, StoreResult> results);

    @Override
    public Map<UUID, StoreResult> updateMultiple(List<Alert> alerts) throws StoreException {
        if (alerts == null) {
            return buildEmptyResultMap(AlertStoreBase.STORE_NULL_ALERT_LIST_ERROR);
        }
        Map<UUID, StoreResult> results = null;
        boolean hasNull = alerts.removeIf(a -> a == null);
        if (hasNull) {
            results = buildEmptyResultMap(AlertStoreBase.STORE_LIST_CONTAINS_NULL_ALERT_ERROR);
        }
        if (alerts.size() > 0) {
            results = (results == null) ? Maps.newHashMap() : results;
            doUpdateMultiple(alerts, results);
        }
        return results;
    }

    protected abstract void doUpdateMultiple(List<Alert> alerts, Map<UUID, StoreResult> results);

    @Override
    public Map<UUID, StoreResult> deleteMultiple(List<UUID> alertIds) throws StoreException {
        if (alertIds == null) {
            return buildEmptyResultMap(AlertStoreBase.STORE_NULL_ALERT_LIST_ID_ERROR);
        }

        Map<UUID, StoreResult> results = null;
        boolean hasNull = alertIds.removeIf(i -> i == null);
        if (hasNull) {
            results = buildEmptyResultMap(AlertStoreBase.STORE_LIST_CONTAINS_NULL_ALERT_ID_ERROR);
        }
        if (alertIds.size() > 0) {
            results = (results == null) ? Maps.newHashMap() : results;
            doDeleteMultiple(alertIds, results);
        }
        return results;
    }

    protected abstract void doDeleteMultiple(List<UUID> alertIds, Map<UUID, StoreResult> results);

    @Override
    public List<Alert> read(List<UUID> alertIds) throws StoreException {

        if (alertIds == null) {
            throw new StoreException(getMessage(AlertStoreBase.STORE_NULL_ALERT_LIST_ID_ERROR));
        }

        boolean hasNull = alertIds.removeIf(i -> i == null);
        if (hasNull) {
            throw new StoreException(getMessage(AlertStoreBase.STORE_LIST_CONTAINS_NULL_ALERT_ID_ERROR));
        }
        return doRead(alertIds);
    }

    protected abstract List<Alert> doRead(List<UUID> alertIds);

    protected String getMessage(String messageKey) {
        if (messages != null) {
            return messages.getMessage(messageKey);
        } else {
            return "< Message not found >";
        }
    }

    protected String getFormatMessage(String messageKey, Object... params) {
        if (messages != null) {
            return messages.getFormatMessage(messageKey, params);
        } else {
            return "< Message not found >";
        }
    }

    private StoreResult buildEmptyResult(String messageKey) {
        return new StoreResult(null, false, getMessage(messageKey));
    }

    private Map<UUID, StoreResult> buildEmptyResultMap(String messageKey) {
        Map<UUID, StoreResult> results = Maps.newHashMap();
        results.put(AlertStoreBase.NULL_ALERT_ID, new StoreResult(AlertStoreBase.NULL_ALERT_ID, false, getMessage(messageKey)));
        return results;
    }


}
