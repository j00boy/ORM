package mountain;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class CsvUtil {

    List<List<String>> readFromCsv(String fileName) throws IOException {
        List<List<String>> inputs = new ArrayList<>();
        // UTF-8로 읽기
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                new FileInputStream(fileName), StandardCharsets.UTF_8))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] values = line.split(",");
                inputs.add(Arrays.asList(values));
            }
        }

        return inputs;
    }

    // trail 파일 작성하는 코드
    void saveTrailOnExcel(Map<Integer, Trail> allTrails, Map<Integer, Map<Integer, List<Point>>> result, Map<Integer, Integer> mountains) {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream("C:/Users/SSAFY/Desktop/ORM_data_parsing/240808/trail.csv"), StandardCharsets.UTF_8))) {
            // Write the CSV header
            writer.write("id,mountain_id,distance,heuristic,time,start_latitude,start_longitude,peak_latitude,peak_longitude");
            writer.newLine();

            // 여기에 작성
            int id = 1;
            for(Map.Entry<Integer, Trail> entry : allTrails.entrySet()) {

                // trailId
                int trailId = entry.getKey();

                // 등산로 객체 가져오기
                Trail tr = entry.getValue();

                // MCode
                int MCode = tr.mountainId;

                // MId
				Integer MId = mountains.get(MCode);
				if(MId == null || MId == 0) {
					continue;
				}
//				System.out.println("MCode: " + MCode + ", MId: " + MId);

                // length
                double dist = tr.dist;

                // weight
                double huri = tr.huri;
                
                // uppl
                int uppl = tr.uppl;

                // 좌표를 가져오려면 result를 불러와야 함
                List<Point> list = result.get(MCode).get(trailId);
                String start_latitude = "" + list.get(list.size()-1).lat;
                String start_longitude = "" + list.get(list.size()-1).lon;
                String peak_latitude = "" + list.get(0).lat;
                String peak_longitude = "" + list.get(0).lon;

                writer.write(id + "," + MId + "," + dist +"," + huri + "," + + uppl + "," + start_latitude + "," + start_longitude + "," + peak_latitude + "," + peak_longitude);
                writer.newLine();
                id++;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("error");
        }
    }

    // trail_detail 파일을 작성하는 코드
    void saveTrailDetailOnExcel(Map<Integer, Trail> allTrails, Map<Integer, Map<Integer, List<Point>>> result, Map<Integer, Integer> mountains) {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream("C:/Users/SSAFY/Desktop/ORM_data_parsing/240808/trail_detail.csv"), StandardCharsets.UTF_8))) {
            // Write the CSV header
            writer.write("id,trail_id,difficulty,latitude,longitude");
            writer.newLine();

            int pathId = 1;
            int pointId = 1;
            // 여기에 작성
            for(Map.Entry<Integer, Trail> entry : allTrails.entrySet()) {
                int trailId = entry.getKey();
                int MId = entry.getValue().mountainId;
                if(mountains.get(MId) == null || mountains.get(MId) == 0) {
                	continue;
                }
                
                List<Point> list = result.get(MId).get(trailId);

                for(Point p : list) {
                    writer.write(pointId + "," + pathId + "," + p.difficulty + "," + p.lat + "," + p.lon);
                    pointId++;
                    writer.newLine();
                }
                pathId++;
            }

        } catch (Exception e) {
            System.out.println("error");
        }
    }
}
