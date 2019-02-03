package sievent.monitoring.store.alert.jdbc;

import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.Test;
import sievent.monitoring.store.alert.AlertStoreTest;
import sievent.monitoring.store.alert.IAlertStore;
import sievent.monitoring.tools.MessageManager;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

public class JdbcAlertStoreTest extends AlertStoreTest {

    @Test
    void testWriteReadUpdateDelete() {
        DSLContext create = DSL.using("jdbc:h2:./target/data/sievent","sa","");
        IAlertStore store  = new JdbcAlertStore(new MessageManager(Locale.US, "messages/StoreMessage"),create);
        testWriteReadUpdateDelete(store);
    }

    @Test
    void testMultipleWriteReadUpdateDelete() {
        DSLContext create = DSL.using("jdbc:h2:./target/data/sievent","sa","");
        IAlertStore store  = new JdbcAlertStore(new MessageManager(Locale.US, "messages/StoreMessage"),create);
        testMultipleWriteReadUpdateDelete(store);
    }
}