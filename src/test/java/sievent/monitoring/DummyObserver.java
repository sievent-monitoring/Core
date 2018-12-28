package sievent.monitoring;

import com.google.common.collect.Lists;
import sievent.monitoring.logic.alert.AlertProcessorBase;
import sievent.monitoring.model.alert.Alert;

import java.util.List;

public class DummyObserver extends AlertProcessorBase {

    private final List<Alert> alerts;

    public DummyObserver() {
        alerts = Lists.newArrayList();
    }

    @Override
    protected void doProcess(Alert alert) {
        alerts.add(alert);
    }

    public List<Alert> getAlerts() {
        return alerts;
    }
}
