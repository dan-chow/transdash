package chow.dan.main;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;

import chow.dan.common.Content;
import chow.dan.dash.DashEncoder;
import chow.dan.dash.DashTranscoder;

public class Main {

	public static void main(String[] args) {

		try {
			testTranscode();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void testEncode() throws IOException, InterruptedException {
		String outputFolder = DashEncoder.encode("tmp.mp4");
		File folder = new File(outputFolder);
		assert folder.isDirectory() && folder.list().length > 0;
	}

	public static void testTranscode() throws IOException, InterruptedException {
		byte[] initData = Files.readAllBytes(Paths.get("4-init.mp4"));
		byte[] segData = Files.readAllBytes(Paths.get("4-35.m4s"));
		Content initSegment = new Content(initData);
		Content segment = new Content(segData);
		Content target = DashTranscoder.transcode(initSegment, segment, "3-35.m4s");

		FileUtils.writeByteArrayToFile(new File("3-35.m4s"), target.getData());

		assert target.getData().length != 0;
	}

}
