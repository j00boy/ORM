package mountain;

public class Trail {

    int mountainId;
    int start_id;
    int end_id;
    double huri;
    double dist;
    int uppl;

    public Trail(int MId, int start_id, int end_id, double huri, double dist, int uppl) {
        super();
        this.mountainId = MId;
        this.start_id = start_id;
        this.end_id = end_id;
        this.huri = huri;
        this.dist = dist;
        this.uppl = uppl;
    }
}
