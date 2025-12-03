package ch.mabaka.birdbox.website.util;

public class VersionInfo {
    public static String getImplementationVersion() {
        Package pkg = VersionInfo.class.getPackage();
        if (pkg != null) {
            String version = pkg.getImplementationVersion();
            if (version != null) {
                return version;
            }
        }
        return "unknown";
    }
}
