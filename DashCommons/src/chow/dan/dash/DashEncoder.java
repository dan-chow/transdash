package chow.dan.dash;

import java.io.IOException;

import chow.dan.common.CmdHelper;
import chow.dan.common.CmdHelper.CmdResult;
import chow.dan.common.FileHelper;

public class DashEncoder {
	private static String ENCODE_SH = "/home/server/repo/transdash/DashCommons/res/encode_dash.sh";

	public static String encode(String input) throws IOException, InterruptedException {
		String outputFolder = FileHelper.createTmpFolder();
		doEncode(input, outputFolder);
		return outputFolder;
	}

	private static void doEncode(String input, String output) throws IOException, InterruptedException {
		String[] encode = { ENCODE_SH, input, output };
		CmdResult result = CmdHelper.runCmd(encode);
		if (result.failed()) {
			System.out.println(result.getError());
		}
	}
}
