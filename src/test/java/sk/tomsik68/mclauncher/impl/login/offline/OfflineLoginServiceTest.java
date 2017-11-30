package sk.tomsik68.mclauncher.impl.login.offline;

import org.junit.Before;
import org.junit.Test;
import sk.tomsik68.mclauncher.api.login.ESessionType;
import sk.tomsik68.mclauncher.api.login.ISession;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/**
 * Created by AndreyMakarov on 30.11.2017.
 */
public class OfflineLoginServiceTest {

    private OfflineLoginService offlineLoginService;

    @Before
    public void setUp() throws Exception {
        this.offlineLoginService = new OfflineLoginService("testmc/accounts.json");
        Files.deleteIfExists(this.offlineLoginService.getAccountsFile());
    }

    @Test
    public void shouldCreateTheAccountsFileOnLoginIfNotPresent() throws Exception {
        assertThat(Files.exists(this.offlineLoginService.getAccountsFile()), not(true));
        OfflineProfile profile = new OfflineProfile("gromozeka");

        this.offlineLoginService.login(profile);

        assertThat(Files.exists(this.offlineLoginService.getAccountsFile()), is(true));
    }

    @Test
    public void shouldCreateValidSessionForNewProfiles() throws Exception {
        assertThat(Files.exists(this.offlineLoginService.getAccountsFile()), not(true));
        OfflineProfile profile = new OfflineProfile("gromozeka");

        ISession session = this.offlineLoginService.login(profile);

        assertThat(session.getSessionID(), notNullValue());
        assertThat(session.getType(), equalTo(ESessionType.OFFLINE));
        assertThat(session.getUUID(), not(nullValue()));
    }

    @Test
    public void shouldReadAccountDataFromThePersistedAccountsFileIfPresent() throws Exception {
        assertThat(Files.exists(this.offlineLoginService.getAccountsFile()), not(true));
        OfflineProfile profile = new OfflineProfile("gromozeka");

        ISession session = this.offlineLoginService.login(profile);
        String userId = session.getUUID();

        assertThat(userId, not(nullValue()));

        assertThat(Files.exists(this.offlineLoginService.getAccountsFile()), is(true));

        session = this.offlineLoginService.login(profile);

        assertThat(session.getUUID(),equalTo(userId));
    }
}