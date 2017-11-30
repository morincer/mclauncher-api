package sk.tomsik68.mclauncher.impl.login.offline;

import sk.tomsik68.mclauncher.api.login.IProfile;

/**
 * Created by AndreyMakarov on 30.11.2017.
 */
public class OfflineProfile implements IProfile {

    private final String name;

    public OfflineProfile(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getSkinURL() {
        return null;
    }

    @Override
    public String getPassword() {
        return "";
    }
}
