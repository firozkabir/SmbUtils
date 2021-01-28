package ca.yorku.sis.smbutils;

import com.hierynomus.smbj.ProgressListener;

public class PrintProgressListener implements ProgressListener {

	@Override
	public void onProgressChanged(long numBytes, long totalBytes) {
		// TODO Auto-generated method stub
		
		
		
		System.out.println( (numBytes/totalBytes * 100) + "%") ;
		
	}

}
