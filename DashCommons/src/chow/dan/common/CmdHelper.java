package chow.dan.common;

import java.io.IOException;

import org.apache.commons.io.IOUtils;

public class CmdHelper {
	public static CmdResult runCmd(String[] cmd) throws IOException, InterruptedException {
		Process process = new ProcessBuilder(cmd).redirectError(ProcessBuilder.Redirect.INHERIT)
				.redirectOutput(ProcessBuilder.Redirect.INHERIT).start();
		process.waitFor();

		return new CmdResult(process);
	}

	public static class CmdResult {

		private String output;
		private String error;

		public boolean succeed() {
			return error.trim().isEmpty();
		}

		public boolean failed() {
			return !succeed();
		}

		public String getOutput() {
			return output;
		}

		public String getError() {
			return error;
		}

		public CmdResult(Process process) throws IOException {
			output = IOUtils.toString(process.getInputStream(), "UTF-8");
			error = IOUtils.toString(process.getErrorStream(), "UTF-8");
		}
	}
}
