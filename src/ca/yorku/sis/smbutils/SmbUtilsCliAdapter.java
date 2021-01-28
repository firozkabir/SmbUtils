package ca.yorku.sis.smbutils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;


public class SmbUtilsCliAdapter {

	private static SmbMountOptions smbMountOptions = new SmbMountOptions();
	
	private static void build(String[] args) {
		
		String configFileValue;
		
		
		// setup CLI Options
		Options cliOptions = new Options();
		
		
		
		Option serverOption = new Option("sv", "server", true, "server: -sv server.yorku.yorku.ca");
		serverOption.setRequired(true);
		cliOptions.addOption(serverOption);
		
		
		Option domainOption = new Option("d", "domain", true, "domain: -d YORKU");
		domainOption.setRequired(true);
		cliOptions.addOption(domainOption);
		
		
		Option usernameOption = new Option("u", "username", true, "username: -u myusername");
		usernameOption.setRequired(true);
		cliOptions.addOption(usernameOption);
		
		
		Option passwordOption = new Option("pw", "password", true, "password: -pw mypassword");
		passwordOption.setRequired(true);
		cliOptions.addOption(passwordOption);
		
		
		Option sharenameOption = new Option("sh", "sharename", true, "sharename: -sh TEAMSHARE");
		sharenameOption.setRequired(true);
		cliOptions.addOption(sharenameOption);
		
		
		Option remotePathOption = new Option("r", "remote-path", true, "remote-path: -r \\\\REMOTE\\\\PATH");
		remotePathOption.setRequired(true);
		cliOptions.addOption(remotePathOption);
		
		
		Option localPathOption = new Option("l", "local-path", true, "local-path: -l /path/on/local/share");
		localPathOption.setRequired(true);
		cliOptions.addOption(localPathOption);
		
		Option actionOption = new Option("a", "action", true, "action: -a [list | download | upload | print]");
		actionOption.setRequired(true);
		cliOptions.addOption(actionOption);
		
		Option copiesOption = new Option("c", "copies", true, "copies: -c <integer>");
		copiesOption.setRequired(false);
		cliOptions.addOption(copiesOption);
		
		
		Option noclobberOption = new Option("nc", "no-clobber", false, "no-clobber: -nc");
		noclobberOption.setRequired(false);
		cliOptions.addOption(noclobberOption);
		
		
		Option verboseOption = new Option("v", "verbose", false, "verbose: -v");
		verboseOption.setRequired(false);
		cliOptions.addOption(verboseOption);
		
		Option runtimePortOption = new Option("p", "runtime-port", true, "port used to prevent duplicate runtime: -p");
		runtimePortOption.setRequired(true);
		cliOptions.addOption(runtimePortOption);
		
		Option todayOnlyOption = new Option("t", "today-only", false, "today-only: -t");
		todayOnlyOption.setRequired(false);
		cliOptions.addOption(todayOnlyOption);
		
		Option useTempFileOption = new Option("b", "use-tempfile", false, "use-tempfile: -b");
		useTempFileOption.setRequired(false);
		cliOptions.addOption(useTempFileOption);
		
		
		Option takeNapsOption = new Option("n", "take-naps", false, "take a 10 second nap every 100 transaction: -n");
		takeNapsOption.setRequired(false);
		cliOptions.addOption(takeNapsOption);
		
		
		
		// setup parser
		CommandLineParser parser = new org.apache.commons.cli.DefaultParser();
		HelpFormatter formatter = new HelpFormatter();
		
		
		
		// try to parse command line parameter into string
		try {
		
			
			CommandLine cmd = parser.parse(cliOptions, args);
			
			smbMountOptions.setServer(cmd.getOptionValue("sv"));
			smbMountOptions.setDomain(cmd.getOptionValue("d"));
			smbMountOptions.setUsername(cmd.getOptionValue("u"));
			smbMountOptions.setPassword(cmd.getOptionValue("pw"));
			smbMountOptions.setSharename(cmd.getOptionValue("sh"));
			smbMountOptions.setRemoteFilePath(cmd.getOptionValue("r"));
			smbMountOptions.setLocalFilePath(cmd.getOptionValue("l"));
			smbMountOptions.setAction(cmd.getOptionValue("a"));
			
			
			
						
			if (cmd.hasOption("nc"))
				smbMountOptions.setOverwrite(false);
			else
				smbMountOptions.setOverwrite(true);
			
			
			
			if (cmd.hasOption("v"))
				smbMountOptions.setVerbose(true);
			else
				smbMountOptions.setVerbose(false);
			
			
			if (cmd.hasOption("t"))
				smbMountOptions.setTodayOnly(true);
			else
				smbMountOptions.setTodayOnly(false);
			
			
			if (cmd.hasOption("b"))
				smbMountOptions.setUseTempFile(true);
			else
				smbMountOptions.setUseTempFile(false);
			
			if (cmd.hasOption("n"))
				smbMountOptions.setTakeNaps(true);
			else
				smbMountOptions.setTakeNaps(false);
			
			
			/*
			if ( cmd.getOptionValue("g") == null )
				smbMountOptions.setSearchGlob("*");
			else
				smbMountOptions.setSearchGlob( cmd.getOptionValue("g") );
			*/	
			
			if ( cmd.getOptionValue("c") != null ) {
					
				try {
							int tempCopies = Integer.parseUnsignedInt(cmd.getOptionValue("c"));
							smbMountOptions.setCopies(tempCopies);
							
					} catch (NumberFormatException e) {
						
						e.printStackTrace(System.err);
						System.exit(1);
					}
			}
			
			
			
			
				try {
			
				
				int tempInt = Integer.parseUnsignedInt(cmd.getOptionValue("p"));
				smbMountOptions.setRuntimePort(tempInt);
				
				
			} catch (NumberFormatException e) {
				
				e.printStackTrace(System.err);
				System.exit(1);
				
			}
					
			
			
		} catch (ParseException e) {
			
			System.err.println(e.getMessage());		
			formatter.printHelp("java -jar smbutils.jar -sv server.yorku.yorku.ca -d YORKU -u myusername -pw myPa$$word -r \\\\REMOTE\\\\PATH -l /path/to/local -a upload -sh TEAMSHARE",cliOptions);
			
			System.exit(1); 
			
		}
		
			
		
	}
	
	
	public static SmbMountOptions getSmbMountOptions(String[] args) {
		
		build(args);
		return smbMountOptions;
		
		
	}
	
	
}
