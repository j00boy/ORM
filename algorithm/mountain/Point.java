package mountain;

public class Point {

    int id;
    double lat;
    double lon;
    int difficulty = 1;

    public Point(int id, double lat, double lon) {
        this.id = id;
        this.lon = lon;
        this.lat = lat;
    }

    public Point(int id, double lat, double lon, int difficulty) {
        super();
        this.id = id;
        this.lat = lat;
        this.lon = lon;
        this.difficulty = difficulty;
    }

    public void updateDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }
}
