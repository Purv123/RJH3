package s.t_scanner_end;

/**
 * Created by SANKET on 09-08-2017.
 */

public class AddLocationInDatabase {
    String currentId;
    String currentLattitude;
    String currentLongitude;
    String available;

    public AddLocationInDatabase(){
    }
    public AddLocationInDatabase(String currentId, String currentLattitude, String currentLongitude, String available) {
        this.currentId = currentId;
        this.currentLattitude = currentLattitude;
        this.currentLongitude = currentLongitude;
        this.available = available;
    }

    public String getCurrentId() {
        return currentId;
    }

    public String getCurrentLattitude() {
        return currentLattitude;
    }

    public String getCurrentLongitude() {
        return currentLongitude;
    }

    public String getAvailable() {
        return available;
    }

    public void setCurrentId(String currentId) {
        this.currentId = currentId;
    }

    public void setCurrentLattitude(String currentLattitude) {
        this.currentLattitude = currentLattitude;
    }

    public void setCurrentLongitude(String currentLongitude) {
        this.currentLongitude = currentLongitude;
    }
    public void setAvailable(String available) {
        this.available = available;
    }
}
