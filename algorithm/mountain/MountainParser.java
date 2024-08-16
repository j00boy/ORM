package mountain;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class MountainParser {
	
	// 산 코드를 저장하는 맵
	private static Map<Integer, Integer> mountains;

	// 산 코드별로 시작점 저장하는 맵
	private static Map<Integer, List<Integer>> startPoints = new HashMap<>();

	// 산 코드별로 정상점 저장하는 맵
	private static Map<Integer, Integer> peaks = new HashMap<>();

	// 간선별로 모든 좌표를 저장하는 맵
	private static Map<Integer, List<Point>> pointRecord;

	// 산 코드별로 모든 노드를 저장하는 맵
	private static Map<Integer, List<Point>> allNodes;

	// 산 코드별로 모든 간선을 저장하는 맵
	private static Map<Integer, Map<Integer, Edge>> allEdges;

	// 등산로 번호별로 거리와 가중치를 저장하는 맵
	private static Map<Integer, Trail> allTrails = new HashMap<>();

	// 산 코드별로 결과값을 저장하는 맵
	private static Map<Integer, Map<Integer, List<Point>>> result = new HashMap<>();

	private static CsvUtil csvUtil = new CsvUtil();

	public static void main(String[] args) throws Exception {
		pointRecord = readPointRecord();
		allNodes = readAllNodes();
		allEdges = readAllEdges();

		//////////////// 산 코드 읽기
		mountains = readMountains();

		parseTrailsOnEveryMount();

		csvUtil.saveTrailOnExcel(allTrails, result, mountains);
		csvUtil.saveTrailDetailOnExcel(allTrails, result, mountains);
	}

	// 전체 좌표 읽고, 변환해서 저장
	private static Map<Integer, List<Point>> readPointRecord() throws IOException {
		Map<Integer, List<Point>> pointRecord = new HashMap<>();
		List<List<String>> pointRecordtemp = csvUtil.readFromCsv("C:/Users/SSAFY/Desktop/ORM_data_parsing/240802/allpoints.csv");
		int idx = 0;
		for (List<String> li : pointRecordtemp) {
			if (idx++ == 0)
				continue;
			int pathId = Integer.parseInt(li.get(1).trim());
			int id = Integer.parseInt(li.get(0).trim());
			double lat = Double.parseDouble(li.get(2).trim());
			double lon = Double.parseDouble(li.get(3).trim());
			Point temp = new Point(id, lat, lon);
			if (pointRecord.containsKey(pathId)) {
				pointRecord.get(pathId).add(temp);
			} else {
				pointRecord.put(pathId, new ArrayList<Point>());
				pointRecord.get(pathId).add(temp);
			}
		}
		return pointRecord;
	}

	// 산 코드별로 모든 노드 저장
	private static Map<Integer, List<Point>> readAllNodes() throws IOException {
		Map<Integer, List<Point>> allNodes = new HashMap<>();
		List<List<String>> nodeRecords = csvUtil.readFromCsv("C:/Users/SSAFY/Desktop/ORM_data_parsing/240808/release_updated.csv");
		int idx = 0;
		for (List<String> li : nodeRecords) {
			if (idx++ == 0)
				continue;

			int nodeId = Integer.parseInt(li.get(0).trim());
			int mountainId = (int) Double.parseDouble(li.get(1).trim());
			double lat = Double.parseDouble(li.get(2).trim());
			double lon = Double.parseDouble(li.get(3).trim());

			if (!allNodes.containsKey(mountainId)) {
				allNodes.put(mountainId, new ArrayList<>());
				startPoints.put(mountainId, new ArrayList<>());
			}

			allNodes.get(mountainId).add(new Point(nodeId, lat, lon));
			if (li.size() == 4)
				continue;
			if (li.get(4).equals("시종점")) {
				startPoints.get(mountainId).add(nodeId);
			} else if (li.get(4).equals("정상")) {
				peaks.put(mountainId, nodeId);
			}
		}

		return allNodes;
	}

	//////////////// 산 코드 읽고, 저장해서 반환
	private static Map<Integer, Integer> readMountains() throws IOException {
		Map<Integer, Integer> mountains = new HashMap<>();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream("C:/Users/SSAFY/Desktop/ORM_data_parsing/sss/mntn.csv"), StandardCharsets.UTF_8))) {

			String line;
			while ((line = br.readLine()) != null) {
				String[] values = line.split(",");
				if(values[0].equals("id")) continue;
				int id = Integer.parseInt(values[0].trim());
				int code = Integer.parseInt(values[1].trim());
				System.out.println("code : id = " + code + " : " + id);
				mountains.put(code, id); // code : id
			}
		}
		return mountains;
	}

	// 산 코드별로 모든 간선을 읽고, 저장해서 반환
	private static Map<Integer, Map<Integer, Edge>> readAllEdges() throws IOException {
		Map<Integer, Map<Integer, Edge>> allEdges = new HashMap<>();
		List<List<String>> edgeRecords = csvUtil.readFromCsv("C:/Users/SSAFY/Desktop/ORM_data_parsing/240802/finaledges.csv");
		int idx = 0;
		for (List<String> li : edgeRecords) {
			if (idx++ == 0)
				continue;
			int id = Integer.parseInt(li.get(0).trim());
			int MId = Integer.parseInt(li.get(1).trim());
			int startp = Integer.parseInt(li.get(2).trim());
			int endp = Integer.parseInt(li.get(3).trim());
			double len = Double.parseDouble(li.get(4).trim());
			int uppl = Integer.parseInt(li.get(5).trim());
			int dif = Integer.parseInt(li.get(6).trim());
			double difNum = (double) ((dif - 1) * 0.05 + 1);

			if (!allEdges.containsKey(MId)) {
				allEdges.put(MId, new HashMap<>());
			}
			allEdges.get(MId).put(id, new Edge(id, startp, endp, len * difNum, len, uppl, dif));
		}
		return allEdges;
	}

	private static void parseTrailsOnEveryMount() throws UnsupportedEncodingException {
		int idx = 1;
		for (Integer Mount : peaks.keySet()) {
			Graph graph = new Graph();
			allNodes.get(Mount).stream().map(point -> point.id).forEach(graph::addNode);
			allEdges.get(Mount).values().forEach(graph::addBidirectionalEdge);

			// 최단 경로 찾기
			int peak = peaks.get(Mount);
			Map<Integer, Integer> previousNodes = new HashMap<>();
			Map<Integer, Integer> previousEdges = new HashMap<>();
			Map<Integer, Double> distancesFromPeak = Dijkstra.computeShortestPaths(graph, peak, previousNodes,
					previousEdges);
			// UTF-8 인코딩을 사용하여 결과 출력
			System.setOut(new java.io.PrintStream(System.out, true, StandardCharsets.UTF_8.name()));

			Point peakNode = findPeak(Mount, peaks.get(Mount), allNodes.get(Mount));

			for (int start : startPoints.get(Mount)) {
				double distanceFromPeak = distancesFromPeak.get(start);
				parseTrailsOnMountain(Mount, idx, peak, start, distanceFromPeak,
						peakNode, previousNodes, previousEdges);
				++idx;
			}
		}
	}

	private static void parseTrailsOnMountain(int mountainId, int idx, int peak, int start, double distanceFromPeak,
											  Point peakNode, Map<Integer, Integer> previousNodes,
											  Map<Integer, Integer> previousEdges) {

		double distance = Math.round(distanceFromPeak * 1000);
		if (distance > 1000000000) {
			return;
		}

		// 경로 추적 시 정상에서 출발지로 변경
		List<Integer> path = Dijkstra.getPath(previousNodes, previousEdges, peak, start);
		if (!result.containsKey(mountainId)) {
			result.put(mountainId, new HashMap<>());
		}
		generateTrail(idx, peakNode, path, mountainId);
	}

	private static Point findPeak(int mountainId, int peakId, List<Point> nodes) {
		for (Point p : nodes) {
			if (p.id == peakId) {
				return p;
			}
		}
		return null;
	}

	private static void generateTrail(int idx, Point peak, List<Integer> path, int mountainId) {
		List<Point> allPointsOnTrail = new ArrayList<>();
		Point curPoint = peak;
		double dist = 0;
		double huri = 0;
		int uppl = 0;
		for (Integer edgeId : path) {
			List<Point> points = pointRecord.get(edgeId);

			reversePointsInEdge(points, curPoint);
			curPoint = new Point(0, points.get(points.size() - 1).lat, points.get(points.size() - 1).lon);
			Edge edge = allEdges.get(mountainId).get(edgeId);

			updatePointDifficulty(points, edge.difficulty);
			dist += edge.distance;
			huri += edge.huri;
			uppl += edge.uppl;
			allPointsOnTrail.addAll(points);
		}
		allTrails.put(idx, new Trail(mountainId, allPointsOnTrail.get(allPointsOnTrail.size() - 1).id, allPointsOnTrail.get(0).id, huri, dist, uppl));
		result.get(mountainId).put(idx, allPointsOnTrail);
	}

	private static void reversePointsInEdge(List<Point> points, Point curPoint) {
		// 출발이 정상에서 내려와야된다.
		// 시종점부터 출발하는거를 뒤집는 로직
		if (!(points.get(0).lat == curPoint.lat && points.get(0).lon == curPoint.lon)) {
			if (!(points.get(points.size() - 1).lat == curPoint.lat
					&& points.get(points.size() - 1).lon == curPoint.lon)) {
				System.out.println("BUG");
			}
			Collections.reverse(points);
		}
	}

	private static void updatePointDifficulty(List<Point> points, int difficulty) {
		for (Point point : points) {
			point.updateDifficulty(difficulty);
		}
	}
}