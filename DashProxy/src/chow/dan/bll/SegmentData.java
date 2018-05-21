package chow.dan.bll;

import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import chow.dan.mpd.MPD;
import chow.dan.mpd.MPD.Period.AdaptationSet.Representation;

public class SegmentData {

	private static Function<Representation, List<String>> representationToFileList = rep -> rep.getSegmentList()
			.getSegmentURL().stream().map(segmentUrl -> segmentUrl.getMedia()).collect(Collectors.toList());

	private HashMap<String, Segment> filename2segment;

	public SegmentData(MPD mpd) {
		filename2segment = new HashMap<>();

		List<Representation> representations = mpd.getPeriod().getAdaptationSet().getRepresentation();
		representations.sort((r1, r2) -> (r1.getId() - r2.getId()));

		List<List<String>> medias = representations.stream().map(representationToFileList).collect(Collectors.toList());
		buildMap(medias);
	}

	private void buildMap(List<List<String>> list) {
		String name = null;
		for (int segmentIdx = 0; segmentIdx < list.get(0).size(); segmentIdx++) {
			name = list.get(0).get(segmentIdx);
			Segment prev = new Segment(name);
			filename2segment.put(name, prev);

			for (int resolution = 1; resolution < list.size(); resolution++) {
				name = list.get(resolution).get(segmentIdx);
				Segment next = new Segment(name);
				filename2segment.put(name, next);

				prev.next = next;
				prev = prev.next;
			}
		}

		System.out.println();
	}

	public Segment getSegment(String key) {
		return filename2segment.get(key);
	}

	public static class Segment {
		public String name;
		public Segment next;

		public Segment(String name) {
			this.name = name;
			this.next = null;
		}
	}

}
