import java.util.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class DataGenerator {
	public static class Person{
		double exp;
		double alt;
		double heuri;
		double gender;
		int age;
		int avg;
		double time;
		
		public Person(double exp, double alt, double heuri, double gender, int age, int avg) {
			super();
			this.exp = exp;
			this.alt = alt;
			this.heuri = heuri;
			this.gender = gender;
			this.age = age;
			this.avg = avg;
			this.time = calculateTime();
		}
		
		Person() {
			age = makeAge();
			double tempExp = rand();
			if(tempExp > 0.2) exp = 0.7;
			else if(tempExp < -0.15) exp = 1.3;
			else exp = 1.0;
			double tempgender = rand();
			gender = tempgender >= 0.25 ? 1 : 0.9;
			
			Random random = new Random();
			int idx = random.nextInt(allTrail.size());
			heuri = allTrail.get(idx).heuri * 1000;
			avg = allTrail.get(idx).avg;
			alt = allTrail.get(idx).alt;
			
			double tempTime = calculateTime();
			time = tempTime + tempTime * 0.1 * rand();
		}
		
		int makeAge() {
			double[] ageDistribution = {0.10, 0.14, 0.52, 0.14, 0.10}; // 5-14, 15-24, 25-54, 55-64, 65+
	        int[] ageRanges = {14, 24, 54, 64, 99}; // 각 분포에 해당하는 최대 나이

	        Random random = new Random();

	        double rand = random.nextDouble();
	        double cumulativeProbability = 0.0;
	        int selectedAge = -1;

	        for (int i = 0; i < ageDistribution.length; i++) {
	            cumulativeProbability += ageDistribution[i];
	            if (rand <= cumulativeProbability) {
	                int minAge = (i == 0) ? 5 : ageRanges[i - 1] + 1;
	                int maxAge = ageRanges[i];
	                selectedAge = random.nextInt(maxAge - minAge + 1) + minAge;
	                return selectedAge;
	            }
	        }
	        return selectedAge;
		}
		
		double calculateAge(int age, double exp) {
	        double numerator = Math.log((1.0 / 9) * (age + 10));
	        double denominator = (1.0 / 9) * (age * exp + 21);
	        double result = 3.9 * (numerator / denominator);
			return result;
		}
		
		double calculateHeuriAlt(double heuri, double alt) {
			double m = alt / Math.sqrt(Math.pow(heuri, 2) - Math.pow(alt, 2));
	        double result = (heuri * (1 + 15 * m)) / 60;
	        return result;
		}
		
		double calculateTime() {
			double weight = gender / calculateAge(age, exp);
			double x1 = avg * 0.75;
			double x2 = 0.25 * calculateHeuriAlt(heuri, alt);
			double result = (x1 + x2) * weight;
			return result;
		}
	}
	public static class Trail {
		int mId;
		double heuri;
		int avg;
		double alt;
		
		public Trail(int mId, double heuri, int avg, double alt) {
			super();
			this.mId = mId;
			this.heuri = heuri;
			this.avg = avg;
			this.alt = alt;
		}
	}
	
	static List<Trail> allTrail = new ArrayList<>();
	static long seed = System.currentTimeMillis();
	static Random rand = new Random(seed);
	
	public static void main(String[] args) throws Exception {
		// trail 집어넣기
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("C:/Users/SSAFY/Desktop/deepLearning/trail_information.csv"), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                allTrail.add(new Trail(
                		Integer.parseInt(values[1]),
                		Double.parseDouble(values[3]),
                		Integer.parseInt(values[4]),
                		Double.parseDouble(values[5])
                		));
            }
		}
		
		// 객체 생성
		ArrayList<Person> persons = new ArrayList<>();
		for(int i = 0; i < 1000000; i++) {
			Person temp = new Person();
			if(temp.alt >= temp.heuri) {
				System.out.println("error");
				continue;
			}
			persons.add(temp);
		}
		
		savePersonsToCsv(persons, "C:/Users/SSAFY/Desktop/deepLearning/dataSet.csv");
	}
	
	static double rand() {
		return rand.nextGaussian();
	}
	
	public static void savePersonsToCsv(ArrayList<Person> persons, String fileName) {
        String[] headers = {"altitude", "gender", "age", "distance", "experience", "average_time", "time"};

        try (FileWriter writer = new FileWriter(fileName)) {
            // 헤더 작성
            for (String header : headers) {
                writer.append(header).append(",");
            }
            writer.append("\n");

            // 데이터 작성
            for (Person person : persons) {
                writer.append(String.valueOf(person.alt)).append(",");
                writer.append(person.gender == 0.9 ? "male" : "female").append(",");
                writer.append(String.valueOf(person.age)).append(",");
                writer.append(String.valueOf(person.heuri)).append(","); // distance를 heuristic으로 매핑
                writer.append(person.exp < 1.0 ? "high" :
                              person.exp == 1.0 ? "medium" : "low").append(",");
                writer.append(String.valueOf(person.avg)).append(",");
                writer.append(String.valueOf(person.time)).append("\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
