package sievent.monitoring.logic.alert;

import com.google.common.eventbus.EventBus;
import sievent.monitoring.model.alert.Alert;

public class AlertEngine {

    private final EventBus engine;

    public AlertEngine() {
        engine = new EventBus();
    }

    public void raiseAlert(Alert alert) {
        if (alert != null) {
            engine.post(alert);
        }
    }

    public void register(AlertProcessorBase processor) {
        engine.register(processor);

    }

}
