package sievent.monitoring.store.alert;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import sievent.monitoring.model.alert.*;
import sievent.monitoring.store.StoreResult;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collector;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AlertStoreTest {


    protected void testWriteReadUpdateDelete(IAlertStore store) {
        AlertBuilder builder = new AlertBuilder().severity(Severity.High);
        Random alea = new Random(System.nanoTime());
        for (int tag = 0; tag < alea.nextInt(100); tag++) {
            builder.tag(UUID.randomUUID().toString());
        }
        Alert alert = builder.build();

        StoreResult result = store.create(alert);
        assertEquals(alert.getId(), result.getId());
        assertEquals(1, result.getCount());
        assertTrue(result.getMessage().contains("1"));

        final Alert readInsertAlert = store.read(alert.getId());
        assertNotNull(readInsertAlert);
        assertEquals(alert.getId(), readInsertAlert.getId());
        assertEquals(alert.getTimestamp(), readInsertAlert.getTimestamp());
        assertEquals(alert.getApplication(), readInsertAlert.getApplication());
        assertEquals(alert.getUserMessage(), readInsertAlert.getUserMessage());
        assertEquals(alert.getTechnicalMessage(), readInsertAlert.getTechnicalMessage());
        assertEquals(alert.getRightMask(), readInsertAlert.getRightMask());
        assertEquals(alert.getSeverity(), readInsertAlert.getSeverity());
        assertEquals(alert.getCategory(), readInsertAlert.getCategory());
        assertEquals(alert.getSubCategory(), readInsertAlert.getSubCategory());
        assertEquals(alert.getOrigin(), readInsertAlert.getOrigin());
        assertEquals(alert.getStatus(), readInsertAlert.getStatus());
        assertEquals(alert.getVersion(), readInsertAlert.getVersion());
        assertEquals(alert.getAsset(), readInsertAlert.getAsset());
        assertEquals(alert.getRealm(), readInsertAlert.getRealm());

        assertEquals(alert.getTags().size(), readInsertAlert.getTags().size());
        alert.getTags().stream().forEach(t -> {
            assertTrue(readInsertAlert.getTags().contains(t));
        });

        alert = alert.builder().unTagAll().tag(UUID.randomUUID().toString()).severity(Severity.Medium).category(Category.Business).subCategory(SubCategory.Chain).build();
        result = store.update(alert);
        assertEquals(alert.getId(), result.getId());


        final Alert readUpdateAlert = store.read(alert.getId());
        assertNotNull(readUpdateAlert);
        assertEquals(alert.getId(), readUpdateAlert.getId());
        assertEquals(alert.getTimestamp(), readUpdateAlert.getTimestamp());
        assertEquals(alert.getApplication(), readUpdateAlert.getApplication());
        assertEquals(alert.getUserMessage(), readUpdateAlert.getUserMessage());
        assertEquals(alert.getTechnicalMessage(), readUpdateAlert.getTechnicalMessage());
        assertEquals(alert.getRightMask(), readUpdateAlert.getRightMask());
        assertEquals(alert.getSeverity(), readUpdateAlert.getSeverity());
        assertEquals(alert.getCategory(), readUpdateAlert.getCategory());
        assertEquals(alert.getSubCategory(), readUpdateAlert.getSubCategory());
        assertEquals(alert.getOrigin(), readUpdateAlert.getOrigin());
        assertEquals(alert.getStatus(), readUpdateAlert.getStatus());
        assertEquals(alert.getVersion(), readUpdateAlert.getVersion());
        assertEquals(alert.getAsset(), readUpdateAlert.getAsset());
        assertEquals(alert.getRealm(), readUpdateAlert.getRealm());

        assertEquals(1, readUpdateAlert.getTags().size());
        alert.getTags().stream().forEach(t -> {
            assertTrue(readUpdateAlert.getTags().contains(t));
        });

        result = store.delete(alert.getId());
        assertEquals(alert.getId(), result.getId());
        assertTrue(result.isStatus());
        Alert readDeleteAlert = store.read(alert.getId());
        assertNull(readDeleteAlert);
    }


    protected void testMultipleWriteReadUpdateDelete(IAlertStore store) {

        List<Alert> alerts = Lists.newArrayList();
        for (int index = 0; index < 100; index++) {
            AlertBuilder builder = new AlertBuilder().severity(Severity.High);
            Random alea = new Random(System.nanoTime());
            for (int tag = 0; tag < alea.nextInt(100); tag++) {
                builder.tag(UUID.randomUUID().toString());
            }
            alerts.add(builder.build());
        }
        Map<UUID, StoreResult> results = store.createMultiple(Lists.newArrayList(alerts));
        for (Alert alert : alerts) {
            assertEquals(alert.getId(), results.get(alert.getId()).getId());
            assertEquals(1, results.get(alert.getId()).getCount());
            assertTrue(results.get(alert.getId()).getMessage().contains("1"));
            assertTrue(results.get(alert.getId()).getMessage().contains(String.valueOf(alert.getTags().size())));
        }

        final List<Alert> readInsertAlerts = store.read(Lists.transform(alerts, a -> a.getId()));
        assertNotNull(readInsertAlerts);
        for (final Alert alert : alerts) {
            Alert readInsertAlert = readInsertAlerts.stream()
                    .filter(a -> alert.getId().equals(a.getId()))
                    .findAny()
                    .orElse(null);
            assertEquals(alert.getId(), readInsertAlert.getId());
            assertEquals(alert.getTimestamp(), readInsertAlert.getTimestamp());
            assertEquals(alert.getApplication(), readInsertAlert.getApplication());
            assertEquals(alert.getUserMessage(), readInsertAlert.getUserMessage());
            assertEquals(alert.getTechnicalMessage(), readInsertAlert.getTechnicalMessage());
            assertEquals(alert.getRightMask(), readInsertAlert.getRightMask());
            assertEquals(alert.getSeverity(), readInsertAlert.getSeverity());
            assertEquals(alert.getCategory(), readInsertAlert.getCategory());
            assertEquals(alert.getSubCategory(), readInsertAlert.getSubCategory());
            assertEquals(alert.getOrigin(), readInsertAlert.getOrigin());
            assertEquals(alert.getStatus(), readInsertAlert.getStatus());
            assertEquals(alert.getVersion(), readInsertAlert.getVersion());
            assertEquals(alert.getAsset(), readInsertAlert.getAsset());
            assertEquals(alert.getRealm(), readInsertAlert.getRealm());

            assertEquals(alert.getTags().size(), readInsertAlert.getTags().size());
            alert.getTags().stream().forEach(t -> {
                assertTrue(readInsertAlert.getTags().contains(t));
            });
        }

        List<Alert> modifiedAlerts = Lists.newArrayList();
        for (Alert alert : alerts) {
            modifiedAlerts.add( alert.builder().unTagAll().tag(UUID.randomUUID().toString()).severity(Severity.Medium).category(Category.Business).subCategory(SubCategory.Chain).build());
        }

        results = store.updateMultiple(modifiedAlerts);

        for (Alert alert : modifiedAlerts) {
            assertEquals(alert.getId(), results.get(alert.getId()).getId());
            assertEquals(1, results.get(alert.getId()).getCount());
            assertTrue(results.get(alert.getId()).getMessage().contains("1"));
        }



        final List<Alert> readUpdateAlerts  = store.read(Lists.transform(modifiedAlerts, a -> a.getId()));
        assertNotNull(readUpdateAlerts);
        for (final Alert alert : modifiedAlerts) {
            Alert readUpdateAlert = readUpdateAlerts.stream()
                    .filter(a -> alert.getId().equals(a.getId()))
                    .findAny()
                    .orElse(null);
            assertEquals(alert.getId(), readUpdateAlert.getId());
            assertEquals(alert.getTimestamp(), readUpdateAlert.getTimestamp());
            assertEquals(alert.getApplication(), readUpdateAlert.getApplication());
            assertEquals(alert.getUserMessage(), readUpdateAlert.getUserMessage());
            assertEquals(alert.getTechnicalMessage(), readUpdateAlert.getTechnicalMessage());
            assertEquals(alert.getRightMask(), readUpdateAlert.getRightMask());
            assertEquals(alert.getSeverity(), readUpdateAlert.getSeverity());
            assertEquals(alert.getCategory(), readUpdateAlert.getCategory());
            assertEquals(alert.getSubCategory(), readUpdateAlert.getSubCategory());
            assertEquals(alert.getOrigin(), readUpdateAlert.getOrigin());
            assertEquals(alert.getStatus(), readUpdateAlert.getStatus());
            assertEquals(alert.getVersion(), readUpdateAlert.getVersion());
            assertEquals(alert.getAsset(), readUpdateAlert.getAsset());
            assertEquals(alert.getRealm(), readUpdateAlert.getRealm());
            assertEquals(alert.getTags().size(), readUpdateAlert.getTags().size());
            alert.getTags().stream().forEach(t -> {
                assertTrue(readUpdateAlert.getTags().contains(t));
            });
        }

        results = store.deleteMultiple(Lists.transform(modifiedAlerts, a->a.getId()));
        for (Alert alert : modifiedAlerts) {
            assertEquals(alert.getId(), results.get(alert.getId()).getId());
            assertEquals(1, results.get(alert.getId()).getCount());
        }
        final List<Alert> readDeletedAlerts  = store.read(Lists.transform(modifiedAlerts, a -> a.getId()));
        assertNotNull(readDeletedAlerts);
        assertEquals(0, readDeletedAlerts.size());

    }
}