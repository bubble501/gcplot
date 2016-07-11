package com.gcplot.model.account.orientdb;

import com.gcplot.configuration.OrientDbConfigurationManager;
import com.gcplot.model.account.Account;
import com.gcplot.model.account.AccountImpl;
import com.gcplot.repository.AccountOrientDbRepository;
import com.gcplot.repository.AccountRepository;
import com.gcplot.commons.ConfigProperty;
import com.gcplot.repository.FiltersOrientDbRepository;
import com.gcplot.repository.OrientDbConfig;
import com.orientechnologies.orient.core.db.OPartitionedDatabasePoolFactory;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

public class OrientDbRepositoryTest {

    protected OrientDbConfig config;
    protected ODatabaseDocumentTx database;

    @Before
    public void setUp() throws Exception {
        int i = new Random().nextInt();
        database = new ODatabaseDocumentTx("memory:test" + i).create();
        config = new OrientDbConfig("memory:test" + i, "admin", "admin");
    }

    @After
    public void tearDown() throws Exception {
        new ODatabaseDocumentTx(config.connectionString).open(config.user, config.password).drop();
    }

    @Test
    public void test() throws Exception {
        AccountOrientDbRepository repository = new AccountOrientDbRepository(config, new OPartitionedDatabasePoolFactory());
        repository.init();
        Assert.assertFalse(repository.account("token").isPresent());
        AccountImpl account = AccountImpl.createNew("abc", "Artem", "Dmitriev",
                "artem@reveno.org", "token", "pass", "salt");
        account = (AccountImpl) repository.store(account);
        Assert.assertNotNull(account.getOId());
        Assert.assertTrue(repository.account("token").isPresent());
        Assert.assertFalse(repository.account("token").get().isConfirmed());
        Assert.assertTrue(repository.confirm("token", "salt"));
        Account account1 = repository.account("token").get();
        Assert.assertTrue(account1.isConfirmed());
        Assert.assertFalse(account1.isBlocked());

        Assert.assertEquals(repository.accounts().size(), 1);
        Assert.assertNotNull(repository.account(account1.id()).get());

        Assert.assertTrue(repository.account("abc", "pass", AccountRepository.LoginType.USERNAME).isPresent());

        repository.block("abc");
        account1 = repository.account("token").get();
        Assert.assertTrue(account1.isBlocked());

        repository.delete(account1);
        Assert.assertEquals(repository.accounts().size(), 0);
        repository.destroy();
    }

    @Test
    public void testFilters() throws Exception {
        FiltersOrientDbRepository repository = new FiltersOrientDbRepository(config, new OPartitionedDatabasePoolFactory());
        repository.init();
        Assert.assertEquals(0, repository.getAllFiltered("type1").size());
        repository.filter("type1", "value1");
        repository.filter("type1", "value2");
        Assert.assertEquals(2, repository.getAllFiltered("type1").size());
        Assert.assertArrayEquals(new String[] { "value1", "value2" }, repository.getAllFiltered("type1").toArray());
        repository.notFilter("type1", "value2");
        Assert.assertEquals(1, repository.getAllFiltered("type1").size());

        repository.destroy();
    }

    @Test
    public void testConfigs() throws Exception {
        OrientDbConfigurationManager cm = new OrientDbConfigurationManager(config, new OPartitionedDatabasePoolFactory());
        cm.setHostGroup("dev");
        cm.init();

        cm.putProperty(ConfigProperty.TEST1_CONFIG, "value");
        cm.putProperty(ConfigProperty.POLL_INTERVAL, 16);
        Assert.assertEquals("value", cm.readString(ConfigProperty.TEST1_CONFIG));
        Assert.assertEquals(16, cm.readInt(ConfigProperty.POLL_INTERVAL));

        cm.destroy();

        cm = new OrientDbConfigurationManager(config, new OPartitionedDatabasePoolFactory());
        cm.setHostGroup("dev");
        cm.init();
        Assert.assertEquals("value", cm.readString(ConfigProperty.TEST1_CONFIG));
        Assert.assertEquals(16, cm.readInt(ConfigProperty.POLL_INTERVAL));
        cm.destroy();
    }

}