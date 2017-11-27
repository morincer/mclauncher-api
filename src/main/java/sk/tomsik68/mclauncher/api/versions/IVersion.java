package sk.tomsik68.mclauncher.api.versions;

/**
 * General interface for versions
 *
 * @author Tomsik68
 */
public interface IVersion extends Comparable<IVersion>, IVersionShort {
    /**
     * @return Human-readable name of this version
     */
    public String getDisplayName();

    /**
     * @return Unique ID of this version, like s1.7.5 or r1.7.5
     */
    public String getUniqueID();

    /**
     * @return Installer that can install this version
     */
    public IVersionInstaller getInstaller();

    /**
     * @return Launcher that can run this version
     */
    public IVersionLauncher getLauncher();

    /**
     * @return True if this version is compatible with current runtime.
     */
    public boolean isCompatible();

    /**
     * @return Reason why it isn't compatible with specified runtime
     */
    public String getIncompatibilityReason();
}
