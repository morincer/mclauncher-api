package sk.tomsik68.mclauncher.impl.login.offline;

import java.util.UUID;

/**
 * Created by AndreyMakarov on 30.11.2017.
 */
public class OfflineAccount {
    public String userName, uuid;

    public OfflineAccount() {
    }

    public OfflineAccount(String name) {
        this.userName = name;
        this.uuid = UUID.randomUUID().toString().replace("-", "");
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
