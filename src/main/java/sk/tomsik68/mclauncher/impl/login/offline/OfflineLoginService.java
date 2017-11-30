package sk.tomsik68.mclauncher.impl.login.offline;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sk.tomsik68.mclauncher.api.login.ILoginService;
import sk.tomsik68.mclauncher.api.login.IProfile;
import sk.tomsik68.mclauncher.api.login.ISession;
import sk.tomsik68.mclauncher.api.services.IServicesAvailability;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by AndreyMakarov on 30.11.2017.
 */
public class OfflineLoginService implements ILoginService {

    private final Logger log = LoggerFactory.getLogger(OfflineLoginService.class);

    private final Path accountsFile;

    public OfflineLoginService(String accountsFile) {
        this.accountsFile = Paths.get(accountsFile).toAbsolutePath();
    }

    public Path getAccountsFile() {
        return accountsFile;
    }

    @Override
    public boolean isAvailable(IServicesAvailability availability) {
        return true;
    }

    @Override
    public ISession login(IProfile profile) throws Exception {

        OfflineAccounts offlineAccounts = new OfflineAccounts();
        OfflineAccount currentAccount = null;
        ObjectMapper objectMapper = new ObjectMapper();

        log.info("Loging in " + profile.getName());

        if (Files.exists(accountsFile)) {
            try {
                offlineAccounts = objectMapper.readValue(accountsFile.toFile(), OfflineAccounts.class);

                for (OfflineAccount offlineAccount : offlineAccounts.getAccounts()) {
                    if (offlineAccount.getUserName().equals(profile.getName())) {
                        currentAccount = offlineAccount;
                    }
                }

            } catch (Exception e) {
                log.warn("Failed to read the accounts JSON, falling back to a new user creation: " + e.getMessage(), e);
            }
        }

        if (offlineAccounts == null) {
            offlineAccounts = new OfflineAccounts();
        }

        if (currentAccount == null) {
            // Initialize it
            currentAccount = new OfflineAccount(profile.getName());
            offlineAccounts.getAccounts().add(currentAccount);
        }

        try {
            objectMapper.writeValue(accountsFile.toFile(), offlineAccounts);
        } catch (Exception e) {
            log.warn("Failed to write " + accountsFile + ": " + e.getMessage(), e);
        }

        OfflineSession offlineSession = new OfflineSession(currentAccount);
        log.info("Created session for " + currentAccount.getUserName() + " with UUID " + currentAccount.getUuid());
        return offlineSession;
    }

    @Override
    public void logout(ISession session) throws Exception {
        // do nothing
    }
}
