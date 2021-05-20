package org.jmarkdownviewer.jmdviewer;

import java.io.File;

import javax.swing.SwingUtilities;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;


public class App implements Runnable {
	
	String lastdir;
	String startfile = null;
	
	private static App m_instance;
	
	private App() {
				
		//lastdir = System.getProperty("user.home");
		lastdir = System.getProperty("user.dir");
		
	}
	
	@Override
	public void run() {
			MainFrame m = new MainFrame();
			m.pack();
			m.setLocationRelativeTo(null);
			m.setVisible(true);
			if (startfile != null) {
				File file = new File(startfile);				
				if(file.exists())
					m.openfile(file);
			}
	}

	
	public static App getInstance() {
		if(m_instance == null)
			m_instance = new App();
		return m_instance;
	}
	
	
    public static void main( String[] args )
    {
    	App app = getInstance();
    	Options options = new Options();
    	Option help = new Option("h", "help", false, "help");
    	options.addOption(help);
    	
    	CommandLine line;
    	CommandLineParser parser = new DefaultParser();
        try {
             line = parser.parse( options, args);
             if(line.hasOption("help")) {
            	 HelpFormatter formatter = new HelpFormatter();
            	 formatter.printHelp("java -jar jmdviewer.jar [filename.md]", options);
            	 System.exit(0);
             }
             args = line.getArgs();
             if(args != null && args.length > 0) {
            	 app.setStartfile(args[0]);
             }
        }
        catch( ParseException exp ) {
            // oops, something went wrong
            System.err.println( "Parsing failed.  Reason: " + exp.getMessage() );
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("java -jar jmdviewer.jar [filename.md]", options);
            System.exit(1);
        }
        
    	SwingUtilities.invokeLater(app);
    }

	public String getLastdir() {
		return lastdir;
	}

	public void setLastdir(String lastdir) {
		this.lastdir = lastdir;
	}

	public String getStartfile() {
		return startfile;
	}

	public void setStartfile(String startfile) {
		this.startfile = startfile;
	}
}
