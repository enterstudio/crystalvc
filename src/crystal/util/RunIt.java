package crystal.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

/**
 * The main runner. This performs system commands and captures their output.
 * 
 * @author rtholmes
 * 
 */
public class RunIt {

	/**
	 * Runs a command twice. A not-nice hack for those times when executions don't seem to be coming out consistently.
	 * 
	 * @param command
	 * @param args
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public static String executeTwice(String command, String[] args, String path) throws IOException {
		execute(command, args, path);
		String result = execute(command, args, path);
		return result;
	}

	/**
	 * Executes a command.
	 * 
	 * @param command
	 * @param args
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public static String execute(String command, String[] args, String path) throws IOException {
		// System.out.println("\t" + TimeUtility.getCurrentLSMRDateString() + ": RunIt::execute(..) - : " + command +
		// " ...");

		long start = System.currentTimeMillis();

		ProcessBuilder builder = new ProcessBuilder();
		File directory = new File(path);

		assert directory.exists();
		assert directory.isDirectory();

		// Assert.assertTrue(directory.exists(), "Directory does not exist: " + path);
		// Assert.assertTrue(directory.isDirectory(), "This is not a directory: " + path);

		builder.directory(new File(path));
		if (args == null || args.length == 0) {
			builder.command(command);
		} else {
			List<String> cmd = new Vector<String>();
			cmd.add(command);

			for (String arg : args)
				cmd.add(arg);

			builder.command(cmd);
		}

		System.out.println("\tRunIt::execute(..) - command: " + builder.command().toString() + "; in path: " + builder.directory());

		Process proc = builder.start();

		// configure the streams
		BufferedInputStream err = new BufferedInputStream(proc.getErrorStream());
		BufferedInputStream out = new BufferedInputStream(proc.getInputStream());

		StreamCatcher outCatcher = new StreamCatcher(out);
		Thread outCatcherThread = new Thread(outCatcher);
		outCatcherThread.start();

		StreamCatcher errCatcher = new StreamCatcher(err);
		Thread errCatcherThread = new Thread(errCatcher);
		errCatcherThread.start();

		try {
			errCatcherThread.join();
			outCatcherThread.join();
			System.out.println("\t\tRunIt::execute(..) - Threads joined peacefully after: " + TimeUtility.msToHumanReadableDelta(start));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		String goodOutput = outCatcher.getOutput();
		String errOutput = errCatcher.getOutput();
		String output = "";

		if (errOutput.length() > 0) {
			output += "*****-START-ERROR-*****\n";
			output += errOutput;
			output += "*****-END-ERROR-*****\n";
		}

		output += "*****-START-OUTPUT-*****\n";
		output += outCatcher.getOutput();
		output += "*****-END-OUTPUT-*****\n";

		// System.out.println("\t\tRunIt::execute(..) - output: " + output);

		return output;
	}
}
