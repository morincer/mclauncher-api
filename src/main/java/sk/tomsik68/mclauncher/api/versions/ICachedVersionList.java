package sk.tomsik68.mclauncher.api.versions;

import java.io.IOException;
import java.nio.file.Path;

public interface ICachedVersionList extends IVersionList, IShortInfoVersionList {
    public void setCacheDirectory(Path cacheDirectory);
    public void invalidateCache() throws IOException;
}
