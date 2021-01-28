package ca.yorku.sis.smbutils;

public class Main {

	public static void main(String[] args) {
		
		System.out.println("smbutils v0.0.4 FK 20200111");
		
		SmbMountOptions mountOptions =  SmbUtilsCliAdapter.getSmbMountOptions(args);
		
		System.out.println("\n*** START ***\n");
		
		
		if (mountOptions.isVerbose())
			System.out.println(mountOptions.toString());
		
		
		switch ( mountOptions.getAction() ) {
		
		case "list" :
			SmbMountWorker.doList(mountOptions);
			break;
			
			
			
		case "upload" :
			SmbMountWorker.doUpload(mountOptions);
			break;
			
			
			
		case "download" :
			System.out.println("download feature is still under construction");
			break;
			
		case "print" :
			SmbMountWorker.doPrint(mountOptions);
			break;	
			
			
		default :
			System.err.println("nothing known about action: " + mountOptions.getAction() );
			System.exit(2);
		
		}
		
				
	
		System.out.println("\n=== END ===\n");
		
		
	}
	
}
