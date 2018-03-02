package chow.dan.bll;

import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBException;

import chow.dan.mpd.MPD;
import chow.dan.mpd.MPD.Period.AdaptationSet.Representation;
import chow.dan.mpd.MPD.Period.AdaptationSet.Representation.SegmentList.SegmentURL;

public class SegmentData {

	private static Comparator<Representation> representationComparator = (r1, r2) -> (r1.getWidth() - r2.getWidth());
	private static Function<SegmentURL, String> segmentUrlToFile = seg -> seg.getMedia();
	private static Function<Representation, List<String>> representationToFiles = rep -> rep.getSegmentList()
			.getSegmentURL().stream().map(segmentUrlToFile).collect(Collectors.toList());

	private HashMap<String, Segment> fileFragmentMap;

	public SegmentData(MPD mpd) throws JAXBException, IOException {
		fileFragmentMap = new HashMap<>();

		String initFile = mpd.getPeriod().getAdaptationSet().getSegmentList().getInitialization().getSourceURL();
		fileFragmentMap.put(initFile, new Segment(initFile));

		List<Representation> representations = mpd.getPeriod().getAdaptationSet().getRepresentation();
		representations.sort(representationComparator);

		List<List<String>> medias = representations.stream().map(representationToFiles).collect(Collectors.toList());

		buildMap(medias);
	}

	private void buildMap(List<List<String>> list) {

		String name = null;

		for (int segmentIdx = 0; segmentIdx < list.get(0).size(); segmentIdx++) {
			name = list.get(0).get(segmentIdx);
			Segment prev = new Segment(name);
			fileFragmentMap.put(name, prev);

			for (int resolution = 1; resolution < list.size(); resolution++) {
				name = list.get(resolution).get(segmentIdx);
				Segment next = new Segment(name);
				fileFragmentMap.put(name, next);

				prev.next = next;
				prev = prev.next;
			}
		}
	}

	public Segment getSegment(String key) {
		return fileFragmentMap.get(key);
	}

	public static class Segment {
		String name;
		Segment next;

		public Segment(String name) {
			this.name = name;
			this.next = null;
		}

		boolean hasNext() {
			return next != null;
		}

		Segment getNext() {
			if (hasNext()) {
				return next;
			} else {
				throw new IndexOutOfBoundsException();
			}
		}
	}

}
