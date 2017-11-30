package sk.tomsik68.mclauncher.impl.login.offline;

import sk.tomsik68.mclauncher.api.login.ESessionType;
import sk.tomsik68.mclauncher.api.login.ISession;

import java.util.List;
import java.util.UUID;

/**
 * Created by AndreyMakarov on 30.11.2017.
 */
public class OfflineSession implements ISession {

    private final OfflineAccount account;
    private final String sessionId;

    public OfflineSession(OfflineAccount account) {
        this.account = account;
        this.sessionId = UUID.randomUUID().toString().replace("-", "");
    }

    @Override
    public String getUsername() {
        return this.account.getUserName();
    }

    @Override
    public String getSessionID() {
        return this.sessionId;
    }

    @Override
    public String getUUID() {
        return this.account.getUuid();
    }

    @Override
    public ESessionType getType() {
        return ESessionType.OFFLINE;
    }

    @Override
    public List<Prop> getProperties() {
        return null;
    }
}
