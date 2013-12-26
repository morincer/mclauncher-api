import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import sk.tomsik68.mclauncher.api.common.ILaunchSettings;
import sk.tomsik68.mclauncher.api.common.IObservable;
import sk.tomsik68.mclauncher.api.common.IObserver;
import sk.tomsik68.mclauncher.api.common.mc.IMinecraftInstance;
import sk.tomsik68.mclauncher.api.login.ISession;
import sk.tomsik68.mclauncher.api.versions.IVersion;
import sk.tomsik68.mclauncher.impl.common.mc.MinecraftInstance;
import sk.tomsik68.mclauncher.impl.login.legacy.LegacyProfile;
import sk.tomsik68.mclauncher.impl.login.yggdrasil.YDAuthProfile;
import sk.tomsik68.mclauncher.impl.login.yggdrasil.YDLoginService;
import sk.tomsik68.mclauncher.impl.versions.mcdownload.MCDownloadVersionList;

public class TestMCDownloadLaunch {

    @Test
    public void test() {

        try {
            final IMinecraftInstance mc = new MinecraftInstance(new File("testmc"));
            MCDownloadVersionList versionList = new MCDownloadVersionList();
            versionList.addObserver(new IObserver<IVersion>() {

                @Override
                public void onUpdate(IObservable<IVersion> observable, IVersion changed) {
                    if (changed.getUniqueID().equals("r1.7.4")) {
                        try {

                            try {
                                changed.getInstaller().install(changed, mc, null);
                            } catch (Exception ignore) {
                            }

                            YDLoginService service = new YDLoginService();

                            ISession session = service.login(new LegacyProfile("Tomsik68@gmail.com", "mypassword"));
                            /*IProfileIO profileReader = new YDProfileIO(Platform.getCurrentPlatform().getWorkingDirectory());
                            ISession session = service.login(profileReader.read()[0]);*/
                            Process proc = changed.getLauncher().launch(session, mc, null, changed, new ILaunchSettings() {

                                @Override
                                public boolean isModifyAppletOptions() {
                                    return false;
                                }

                                @Override
                                public boolean isErrorStreamRedirected() {
                                    return true;
                                }

                                @Override
                                public File getJavaLocation() {
                                    return null;
                                }

                                @Override
                                public List<String> getJavaArguments() {
                                    return null;
                                }

                                @Override
                                public String getInitHeap() {
                                    return null;
                                }

                                @Override
                                public String getHeap() {
                                    return null;
                                }

                                @Override
                                public Map<String, String> getCustomParameters() {
                                    return null;
                                }

                                @Override
                                public List<String> getCommandPrefix() {
                                    return null;
                                }
                            });
                            BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
                            String line;
                            while (isProcessAlive(proc)) {
                                line = br.readLine();
                                if (line != null && line.length() > 0)
                                    System.out.println(line);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }
            });
            versionList.startDownload();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected boolean isProcessAlive(Process proc) {
        try {
            proc.exitValue();
            return false;
        } catch (Exception e) {
            return true;
        }

    }

}
