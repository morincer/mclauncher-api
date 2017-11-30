package sk.tomsik68.mclauncher.api.versions;

import java.io.IOException;
import java.nio.file.Path;

public interface ICachedVersionList extends IVersionList, IShortInfoVersionList {
    Path getVersionsDirectory();
    void setVersionsDirectory(Path cacheDirectory);
    void invalidateCache() throws IOException;
}
