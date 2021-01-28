package ca.yorku.sis.smbutils;

public class SmbMountOptions {

	private String server;
	private String domain;
	private String username;
	private String password;
	private String sharename;
	private String remoteFilePath;
	private String localFilePath;
	private String action;
	private boolean overwrite;
	private boolean verbose;
	private int runtimePort;
	private int copies;
	//private String searchGlob;
	private boolean todayOnly;
	private boolean useTempFile;
	private boolean takeNaps;
	
	public SmbMountOptions() {
		
	}
	
	public SmbMountOptions(String server, String domain, String username, String password, String sharename,
			String remoteFilePath, String localFilePath, String action, boolean overwrite, 
			boolean verbose, int runtimePort, int copies, boolean todayOnly, boolean useTempFile, boolean takeNaps
			) {
		super();
		this.server = server;
		this.domain = domain;
		this.username = username;
		this.password = password;
		this.sharename = sharename;
		this.remoteFilePath = remoteFilePath;
		this.localFilePath = localFilePath;
		this.action = action;
		this.overwrite = overwrite;
		this.verbose = verbose;
		this.copies = copies;
		//this.searchGlob = searchGlob;
		this.todayOnly = todayOnly;
		this.useTempFile = useTempFile;
		this.takeNaps = takeNaps;
	}



	public String getServer() {
		return server;
	}



	public void setServer(String server) {
		this.server = server;
	}



	public String getDomain() {
		return domain;
	}



	public void setDomain(String domain) {
		this.domain = domain;
	}



	public String getUsername() {
		return username;
	}



	public void setUsername(String username) {
		this.username = username;
	}



	public String getPassword() {
		return password;
	}



	public void setPassword(String password) {
		this.password = password;
	}



	public String getSharename() {
		return sharename;
	}



	public void setSharename(String sharename) {
		this.sharename = sharename;
	}



	public String getRemoteFilePath() {
		return remoteFilePath;
	}



	public void setRemoteFilePath(String remoteFilePath) {
		this.remoteFilePath = remoteFilePath;
	}



	public String getLocalFilePath() {
		return localFilePath;
	}



	public void setLocalFilePath(String localFilePath) {
		this.localFilePath = localFilePath;
	}



	public String getAction() {
		return action;
	}



	public void setAction(String action) {
		this.action = action;
	}

	

	public boolean isOverwrite() {
		return overwrite;
	}



	public void setOverwrite(boolean overwrite) {
		this.overwrite = overwrite;
	}

	

	public boolean isVerbose() {
		return verbose;
	}



	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

	public int getRuntimePort() {
		
		return runtimePort;
	}
	
	public void setRuntimePort(int runtimeport) {
		
		this.runtimePort = runtimeport;
	}
	
	
	public int getCopies () {
		return this.copies;
	}
	
	public void setCopies(int copies) {
		
		this.copies = copies;
	}
	
	public void setTodayOnly(boolean todayOnly) {
		this.todayOnly = todayOnly;
	}
	
	public boolean isTodayOnly() {
		return this.todayOnly;
	}
	
	public void setUseTempFile(boolean useTempFile) {
		this.useTempFile = useTempFile;
	}
	
	public boolean getUseTempFile() {
		return this.useTempFile;
	}
	
	public void setTakeNaps(boolean takeNaps) {
		this.takeNaps = takeNaps;
	}
	
	public boolean getTakeNaps() {
		return this.takeNaps;
	}
	
	
	@Override
	public String toString() {
		return "\nSmbUtilsOptions: \n" +
	            "server=" + server + "\n" + 
				"domain=" + domain + "\n" + 
	            "username=" + username  + "\n" +
				"password=******\n" + 
	            "sharename=" + sharename  + "\n" + 
				"remoteFilePath=" + remoteFilePath  + "\n" +
				"localFilePath=" + localFilePath  + "\n" +
				"action=" + action  + "\n" +
				"overwrite=" + overwrite  + "\n" +
				"verbose=" + verbose  + "\n" + 
				"runtimeport=" +  runtimePort + "\n" + 
				"copies=" + copies  + "\n" +
				"today-only=" + todayOnly + 
				"use-temp-file=" + useTempFile + 
				"take-naps=" + takeNaps
				
				;
	}
	
	
	
	
	
	
	
}
