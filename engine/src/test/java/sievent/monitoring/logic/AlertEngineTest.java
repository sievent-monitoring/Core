package sievent.monitoring.logic;

import org.junit.jupiter.api.Test;
import sievent.monitoring.DummyObserver;
import sievent.monitoring.logic.alert.AlertEngine;
import sievent.monitoring.model.alert.Alert;
import sievent.monitoring.model.alert.AlertBuilder;
import sievent.monitoring.model.alert.Severity;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class AlertEngineTest {

    @Test
    void raiseAlert() {
        AlertEngine engine  = new AlertEngine();

        DummyObserver observerA = new DummyObserver();
        engine.register(observerA);

        Alert alert1 = new AlertBuilder().severity(Severity.High).build();
        engine.raiseAlert(alert1);

        DummyObserver observerB = new DummyObserver();
        engine.register(observerB);

        Alert alert2 = new AlertBuilder().severity(Severity.Medium).build();
        engine.raiseAlert(alert2);

        assertNotNull(observerA.getAlerts().get(0));
        assertNotNull(observerA.getAlerts().get(1));
        assertNotNull(observerB.getAlerts().get(0));
    }
}