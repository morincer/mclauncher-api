package sk.tomsik68.mclauncher.api.versions;

public interface IVersionShort {
    /**
     * @return ID of this version, like 1.7.5
     */
    public String getId();

    String getTime();

    String getReleaseTime();

    String getType();

    String getUrl();
}
