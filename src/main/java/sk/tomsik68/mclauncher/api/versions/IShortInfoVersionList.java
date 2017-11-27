package sk.tomsik68.mclauncher.api.versions;

import java.util.List;

public interface IShortInfoVersionList {

    /**
     * @return The list of current versions with basic information: id, type, time, release time, url
     * as they're retrieved from the manifest file.
     */
    List<IVersionShort> getShortInfoVersionList() throws Exception;
}
