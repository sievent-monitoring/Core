package sievent.monitoring.store.alert;

import sievent.monitoring.model.alert.Alert;
import sievent.monitoring.store.StoreResult;
import sievent.monitoring.store.exception.StoreException;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface IAlertStore {

    StoreResult create(Alert alert) throws StoreException;

    StoreResult update(Alert alert) throws StoreException;

    StoreResult delete(UUID alertId) throws StoreException;

    Alert read(UUID alertId) throws StoreException;

    Map<UUID,StoreResult> createMultiple(List<Alert> alerts) throws StoreException;

    Map<UUID,StoreResult> updateMultiple(List<Alert> alerts) throws StoreException;

    Map<UUID,StoreResult> deleteMultiple(List<UUID> alertId) throws StoreException;

    List<Alert> read(List<UUID> alertIds) throws StoreException;


}
