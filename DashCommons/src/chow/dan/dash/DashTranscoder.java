package chow.dan.dash;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import chow.dan.common.CmdHelper;
import chow.dan.common.CmdHelper.CmdResult;
import chow.dan.common.Content;
import chow.dan.common.FileHelper;

public class DashTranscoder {

	private static String TRANSCODE_SH = "/home/server/repo/transdash/DashCommons/res/transcode_dash.sh";

	public static Content transcode(Content initSegment, Content segment, String targetSegmentName)
			throws IOException, InterruptedException {

		String initFile = FileHelper.createTmpFile(initSegment);
		String segmentFile = FileHelper.createTmpFile(segment);
		String targetFile = FileHelper.createEmptyFile(targetSegmentName);

		Content content = getTranscodedContent(initFile, segmentFile, targetFile);

		FileUtils.deleteQuietly(new File(initFile));
		FileUtils.deleteQuietly(new File(segmentFile));
		FileUtils.deleteQuietly(new File(targetFile));

		return content;
	}

	private static Content getTranscodedContent(String init, String segment, String target)
			throws IOException, InterruptedException {

		String[] transcode = { TRANSCODE_SH, init, segment, target };
		CmdResult result = CmdHelper.runCmd(transcode);
		if (result.failed()) {
			System.out.println(result.getError());
		}

		return new Content(FileUtils.readFileToByteArray(new File(target)));
	}
}
