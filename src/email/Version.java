package email;

public class Version {
	
	private static Version instance = null;
	
	private Version() {

	}
	
	public static Version getInstance() {
		if(instance == null) {
			instance = new Version();
		}
		return instance;
	}

	private String version = "V 1.5";
	private String newVersion = null;

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getNewVersion() {
		return newVersion;
	}

	public void setNewVersion(String newVersion) {
		this.newVersion = newVersion;
	}
}
