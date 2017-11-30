package sk.tomsik68.mclauncher.impl.login.offline;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AndreyMakarov on 30.11.2017.
 */
public class OfflineAccounts {
    private List<OfflineAccount> accounts = new ArrayList<>();

    public List<OfflineAccount> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<OfflineAccount> accounts) {
        this.accounts = accounts;
    }
}
