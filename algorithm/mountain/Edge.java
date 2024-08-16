package mountain;

public class Edge {

    int id;
    int start_id;
    int end_id;
    double huri;
    double distance;
    int uppl;
    int difficulty;

    public Edge(int id, int start_id, int end_id, double huri, double distance, int uppl, int difficulty) {
        this.id = id;
        this.start_id = start_id;
        this.end_id = end_id;
        this.huri = huri;
        this.distance = distance;
        this.uppl = uppl;
        this.difficulty = difficulty;
    }
}
