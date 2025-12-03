package ch.mabaka.birdbox.website.util;
/**
 * Utility class to retrieve version information of the application.
 */
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
