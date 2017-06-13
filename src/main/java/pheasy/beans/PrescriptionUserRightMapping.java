package pheasy.beans;

/**
 * Created by jayeshathila
 * on 13/06/17.
 */
public class PrescriptionUserRightMapping {

    private String username;
    private ApprovalState state;
    private PrecriptionRights rights;

    public PrescriptionUserRightMapping(String username, ApprovalState state, PrecriptionRights rights) {
        this.username = username;
        this.state = state;
        this.rights = rights;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ApprovalState getState() {
        return state;
    }

    public void setState(ApprovalState state) {
        this.state = state;
    }

    public PrecriptionRights getRights() {
        return rights;
    }

    public void setRights(PrecriptionRights rights) {
        this.rights = rights;
    }
}
