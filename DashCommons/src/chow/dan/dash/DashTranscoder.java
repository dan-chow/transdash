package chow.dan.dash;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import chow.dan.common.CmdHelper;
import chow.dan.common.CmdHelper.CmdResult;
import chow.dan.common.Content;
import chow.dan.common.FileHelper;

public class DashTranscoder {

	private static String TRANSCODE_SH = "/home/dan/transdash/DashCommons/res/transcode_dash.sh";
	private static String LOCAL_VIDEO_DIR = "/home/dan/videos/";

	private static double[][] delayArr = { { 90.10135135135136 }, { 96.21621621621622, 171.35135135135135 },
			{ 115.22972972972972, 187.31081081081083, 292.72297297297295 },
			{ 146.52702702702702, 220.5337837837838, 326.4797297297298, 620.2364864864865 } };

	public static Content fakeTranscode(String srcM4sUri, String dstM4sUri) throws IOException, InterruptedException {
		String srcName = FilenameUtils.getName(srcM4sUri);
		int srcLevel = Integer.parseInt(srcName.substring(0, srcName.indexOf("-")));

		String dstName = FilenameUtils.getName(dstM4sUri);
		int dstLevel = Integer.parseInt(dstName.substring(0, dstName.indexOf("-")));

		Content content = new Content(FileUtils.readFileToByteArray(new File(LOCAL_VIDEO_DIR + dstName)));

		Thread.sleep((long) delayArr[srcLevel - 2][dstLevel - 1]);

		return content;
	}

	public static Content transcode(Content initSegment, Content segment, String targetSegmentName)
			throws IOException, InterruptedException {

		String initFile = FileHelper.createTmpFile(initSegment);
		String segmentFile = FileHelper.createTmpFile(segment);
		String targetFile = FileHelper.createEmptyFile(targetSegmentName);

		Content content = getTranscodedContent(initFile, segmentFile, targetFile);

		// FileUtils.deleteQuietly(new File(initFile));
		// FileUtils.deleteQuietly(new File(segmentFile));
		// FileUtils.deleteQuietly(new File(targetFile));

		return content;
	}

	// transcode still have problems here, so use fake transcode instead
	private static Content getTranscodedContent(String init, String segment, String target)
			throws IOException, InterruptedException {

		FileUtils.deleteQuietly(new File(target));

		System.out.println(TRANSCODE_SH + " " + init + " " + segment + " " + target);
		String[] transcode = { TRANSCODE_SH, init, segment, target };
		CmdResult result = CmdHelper.runCmd(transcode);
		if (result.failed()) {
			System.out.println(result.getError());
		}

		return new Content(FileUtils.readFileToByteArray(new File(target)));
	}
}
