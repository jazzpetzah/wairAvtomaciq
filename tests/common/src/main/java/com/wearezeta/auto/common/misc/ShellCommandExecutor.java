package com.wearezeta.auto.common.misc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ShellCommandExecutor {
	/**
	 * Executes given command
	 * 
	 * @param <br>
	 *            <code>path</code> - Path to the program. Example:
	 *            c:\\windows\\system32\\ <br>
	 *            <code>command</code> - Command to run <br>
	 *            <code>nix</code> - Set true to run command on *nix using bash
	 *            or false to run on win using cmd /c <br>
	 *            <code>passString</code> - Command to run <br>
	 *            <code>error</code> - Stores execution status, true if
	 *            passString found in the command output
	 */
	public static boolean runCommand(String path, String command,
			String passString) throws IOException, InterruptedException {
		if (path == null)
			path = "";
		boolean error = checkCommandAndPath(path);
		if (!error)
			try {
				ProcessBuilder procBuilder = null;
				if (isWindows()) {
					String processForm = " /c \"%s%s pause \"";
					procBuilder = new ProcessBuilder("cmd", String.format(
							processForm, path, command));
					String run = "cmd" + String.format(processForm, path, command);
					System.out.println("\nRUN:\t" + run);
				}
				if (isMac()) {
					procBuilder = new ProcessBuilder("bash", "-c", command);
					String run = "bash -c " + command;
					System.out.println("\nRUN:\t" + run);
				}
				procBuilder.redirectErrorStream(true);
				Process process = procBuilder.start();
//				int exitCode = process.waitFor();
//				if (exitCode == 1)
//					System.out
//							.println("Command not found. Please make sure that command directory is in a PATH.");
				InputStream stdout = process.getInputStream();
				InputStreamReader isrStdout = new InputStreamReader(stdout);
				BufferedReader brStdout = new BufferedReader(isrStdout);
	
				String line = null;
				String streamOutput = "";
				while ((line = brStdout.readLine()) != null)
					streamOutput = streamOutput.concat(line).concat("\n");
				//System.out.println(streamOutput); // shell output
	
				if (!streamOutput.contains(passString)) {
					System.out.println("passString \"" + passString
							+ "\" is not found in the output");
					error = true;
				} else {
					System.out.println("Command executed sucessfully");
					error = false;
				}
	
			} catch (IOException ex) {
				System.out.println("Error executing command: " + command);
			}
		return error;
	}

	private static boolean checkCommandAndPath(String path) {
		try {
			ProcessBuilder procBuilder = null;

			if (isWindows())
				procBuilder = new ProcessBuilder("cmd", " /c " + path + "curl");
			else if (isMac())
				procBuilder = new ProcessBuilder("bash", "-c " + path + "curl");
			procBuilder.redirectErrorStream(true);
			Process process = procBuilder.start();
			int exitCode = process.waitFor();
			
			InputStream stdout = process.getInputStream();
			InputStreamReader isrStdout = new InputStreamReader(stdout);
			BufferedReader brStdout = new BufferedReader(isrStdout);

			String line = null;
			String streamOutput = "";
			while ((line = brStdout.readLine()) != null)
				streamOutput = streamOutput.concat(line).concat("\n");
			//System.out.println(streamOutput); // shell output
			
			if (exitCode == 1) {
				System.out.println("Command under given path is not found."
								+"\nPlease make sure that command directory is right or added to the system PATH variable.");
				return true;
			} else {
				//System.out.println("checkCommandAndPath passed");
				return false;
			}
		} catch (IOException ex) {
			ex.printStackTrace();
			System.out
			.println("Command under given path is not found. \nPlease make sure that command directory is right or added to the system PATH variable.");
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.out
			.println("Command under given path is not found. \nPlease make sure that command directory is right or added to the system PATH variable.");
		}
		return true;
	}

	private static boolean isWindows() {
		String os = System.getProperty("os.name").toLowerCase();
		// windows
		return (os.indexOf("win") >= 0);
	}

	private static boolean isMac() {
		String os = System.getProperty("os.name").toLowerCase();
		// Mac
		return (os.indexOf("mac") >= 0);
	}
}