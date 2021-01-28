package ca.yorku.sis.smbutils;



import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.AgeFileFilter;

import com.hierynomus.msdtyp.AccessMask;
import com.hierynomus.mserref.NtStatus;
import com.hierynomus.msfscc.FileAttributes;
import com.hierynomus.msfscc.fileinformation.FileIdBothDirectoryInformation;
import com.hierynomus.mssmb2.SMB2CreateDisposition;
import com.hierynomus.mssmb2.SMB2CreateOptions;
import com.hierynomus.mssmb2.SMB2ShareAccess;
import com.hierynomus.mssmb2.SMBApiException;
import com.hierynomus.protocol.commons.EnumWithValue;
import com.hierynomus.smbj.SMBClient;
import com.hierynomus.smbj.SmbConfig;
import com.hierynomus.smbj.auth.AuthenticationContext;
import com.hierynomus.smbj.connection.Connection;
import com.hierynomus.smbj.session.Session;
import com.hierynomus.smbj.share.DiskShare;
import com.hierynomus.smbj.share.PrinterShare;
import com.hierynomus.smbj.transport.tcp.async.AsyncDirectTcpTransportFactory;
import com.hierynomus.smbj.utils.SmbFiles;

public class SmbMountWorker {

	
	public static void doPrint(SmbMountOptions mo) {
		
		File fileToPrint = new File(mo.getLocalFilePath());
		
		
		SMBClient client = new SMBClient();
		
		// trying to connect to server
				try {
					
					Connection connection = client.connect(mo.getServer());
					AuthenticationContext ac = new AuthenticationContext(mo.getUsername(), mo.getPassword().toCharArray(), mo.getDomain());
					Session session = connection.authenticate(ac);
					
					// trying to mount the share
					try ( PrinterShare share = (PrinterShare) session.connectShare(mo.getSharename()) ){
						
						
						// now listing the remote location
						System.out.println("Connected to printer " + share.getSmbPath());
						System.out.println("Printing " + mo.getCopies() + " copies of file " + mo.getLocalFilePath());
						
						for ( int i = 0; i < mo.getCopies(); i++ ) {
							
							share.print( FileUtils.openInputStream(fileToPrint), new PrintProgressListener());
							
						}
											
						
						share.close();
						
						
					} catch (Exception e) {
						
						e.printStackTrace(System.err);
						
					} // done trying to mount the share
					
					
				
					session.close();
					connection.close();
					client.close();
					
					
				} catch (Exception e) {
					
					
					e.printStackTrace(System.err);
				
				} // done trying to connect 
		
		
	} // end of doPrint()
	
	
	public static void doUpload(SmbMountOptions mo) {
		
		java.io.File localDirectory = new java.io.File(mo.getLocalFilePath());
		//String[] searchExt = { mo.getSearchGlob().substring(mo.getSearchGlob().indexOf(".")) };
		
		List<java.io.File> localFileList;
		
		if (mo.isTodayOnly())
		{
			
			System.out.print("only uploading files created since midnight today.");
			
			LocalTime midnight = LocalTime.MIDNIGHT;
			LocalDate today = LocalDate.now();
			LocalDateTime todayMidnight = LocalDateTime.of(today, midnight);
			Date date = Date.from( todayMidnight.toInstant(ZoneOffset.UTC) );
			
			AgeFileFilter filesTodayOrLater = new AgeFileFilter(date,false);
			
			localFileList = (List<java.io.File>) FileUtils.listFiles(localDirectory, filesTodayOrLater, null);
			
			
		} else {
		
			localFileList = (List<java.io.File>) FileUtils.listFiles(localDirectory, null, false);
		}
		
		
		SmbConfig smbconfig = SmbConfig
			      .builder()
			      .withMultiProtocolNegotiate(true)
			      //.withSigningRequired(true).build()
			      .withTransportLayerFactory(new AsyncDirectTcpTransportFactory())
			      .build();
		
		
		
		SMBClient client = new SMBClient(smbconfig);
		
		
		// trying to connect to server
		try {
			
			Connection connection = client.connect(mo.getServer());
			AuthenticationContext ac = new AuthenticationContext(mo.getUsername(), mo.getPassword().toCharArray(), mo.getDomain());
			Session session = connection.authenticate(ac);
			
			// trying to mount the share
			try ( DiskShare share = (DiskShare) session.connectShare(mo.getSharename()) ){
				
				
				// now listing the remote location
				System.out.println("Connected to " + share.getSmbPath());
				System.out.println("uploading " + mo.getLocalFilePath() + " ---> " + share.getSmbPath() + mo.getRemoteFilePath()  );

				int count = 1;
				int size = localFileList.size();
				
				System.out.println(size + " file(s) to upload");
				
				
				
				for ( java.io.File file : localFileList ) {
					
					String realFileName = file.getName();
					String tmpFileName = file.getName() + ".tmp";
					
					System.out.print("uploading " + ( mo.getUseTempFile() ? tmpFileName : realFileName )  + "   [" + count++ + "/" + size + "]" );
					
					try {
					
						if ( mo.getTakeNaps() && count % 100 == 0) {
							System.out.println(" taking a 10 second power-nap ");
							Thread.sleep(10000);  // 10_000 ms = 10 seconds
						}
							
						if ( mo.getUseTempFile() ) {
							
							
							// do the copy with temp file
							SmbFiles.copy(file, share, mo.getRemoteFilePath() + tmpFileName, mo.isOverwrite());
							System.out.println(" .... done");
							
							
							// now add the part where we rename the remote file 
							
							// get the file we just copied
							DiskShare tmpShare = (DiskShare) session.connectShare(mo.getSharename() );
							
							com.hierynomus.smbj.share.File tmpRemoteFile = tmpShare.openFile(
									    mo.getRemoteFilePath() + tmpFileName,
					                    EnumSet.of(AccessMask.DELETE, AccessMask.GENERIC_WRITE),
					                    EnumWithValue.EnumUtils.toEnumSet(
					                            tmpShare.getFileInformation(mo.getRemoteFilePath() + tmpFileName).getBasicInformation().getFileAttributes(), 
					                            FileAttributes.class), // copy original file attributes
					                    SMB2ShareAccess.ALL,
					                    SMB2CreateDisposition.FILE_OPEN,
					                    null);
					            tmpRemoteFile.rename( mo.getRemoteFilePath() + realFileName, true); 
							
							System.out.println("renamed " + tmpFileName + " to " + realFileName + " on remote");
							
							
						} else {
						
							SmbFiles.copy(file, share, mo.getRemoteFilePath() + file.getName(), mo.isOverwrite());
							System.out.println(" .... done");
							
						}
							
						
						
					} catch (SMBApiException e) {
						
						if (e.getStatus().equals(NtStatus.STATUS_OBJECT_NAME_COLLISION)) {
							
							System.out.println(" .... skipping due to STATUS_OBJECT_NAME_COLLISION");
							continue;
							
							
						} else 
							throw e;
						
						
					} catch (Exception e) {
						
					
						System.out.println("Error: ");
						e.printStackTrace(System.err);
						System.exit(1);
						
						
					}
					
					
					
					
				}
				
				share.close();
				
				
			} catch (Exception e) {
				
				e.printStackTrace(System.err);
				
			} // done trying to mount the share
			
			
		
			session.close();
			connection.close();
			client.close();
			
			
		} catch (Exception e) {
			
			e.printStackTrace(System.err);
		
		} // done trying to connect 
		
		
		
	} // end of doUpload()
	
		
	
	public static void doList(SmbMountOptions mo) {
	
		SMBClient client = new SMBClient();
		
		
		// trying to connect to server
		try {
			
			Connection connection = client.connect(mo.getServer());
			AuthenticationContext ac = new AuthenticationContext(mo.getUsername(), mo.getPassword().toCharArray(), mo.getDomain());
			Session session = connection.authenticate(ac);
			
			// trying to mount the share
			try ( DiskShare share = (DiskShare) session.connectShare(mo.getSharename()) ){
				
							
				
				
				// now listing the remote location
				System.out.println("Connected to " + share.getSmbPath());
				System.out.println("listing " + share.getSmbPath() + mo.getRemoteFilePath() );
				
				for (FileIdBothDirectoryInformation f : share.list(mo.getRemoteFilePath())) {
				
					if (EnumWithValue.EnumUtils.isSet(f.getFileAttributes(), FileAttributes.FILE_ATTRIBUTE_DIRECTORY)) 
						System.out.println( "Last modified: " +  f.getChangeTime() +  "  -  " + f.getFileName() + "/");
					else
						System.out.println( "Last modified: " + f.getChangeTime() + "  -  " + f.getFileName());
					
					
				}
				
				
				share.close();
				
				
			} catch (Exception e) {
				
				e.printStackTrace(System.err);
				
			} // done trying to mount the share
			
			
		
			session.close();
			connection.close();
			client.close();
			
			
		} catch (Exception e) {
			
			
			e.printStackTrace(System.err);
		
		} // done trying to connect 
		
		
		
	} // end of doList()

	
	
	

}
