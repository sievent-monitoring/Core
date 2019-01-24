package sievent.monitoring.logic.alert;

import com.google.common.eventbus.Subscribe;
import sievent.monitoring.model.alert.Alert;

public abstract class AlertProcessorBase {

    protected AlertProcessorBase() {
    }

    @Subscribe
    public void process(Alert alert) {
        if (alert != null) {
            doProcess(alert);
        }
    }

    protected abstract void doProcess(Alert alert);
}
