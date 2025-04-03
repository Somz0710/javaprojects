package Votingsystem;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ElectionOfficer extends AbstractAdministrator {
    private String assignedConstituency;
    private Set<String> permissions;

    public ElectionOfficer(String adminId, String name, String assignedConstituency) {
        super(adminId, name, "Election Officer");
        this.assignedConstituency = assignedConstituency;
        this.permissions = new HashSet<>(Arrays.asList(
                "REGISTER_VOTER",
                "VIEW_VOTER",
                "OPEN_STATION",
                "CLOSE_STATION",
                "VIEW_RESULTS"
        ));
    }

    public String getAssignedConstituency() {
        return assignedConstituency;
    }

    @Override
    public boolean hasPermission(String operation) {
        return isActive && permissions.contains(operation);
    }

    public boolean registerVoter(GeneralElection election, Voter voter) {
        if (!hasPermission("REGISTER_VOTER")) {
            return false;
        }

        if (!voter.getConstituency().equals(assignedConstituency)) {
            return false; // Can only register voters from assigned constituency
        }

        return election.registerVoter(voter);
    }

    public void addPermission(String permission) {
        permissions.add(permission);
    }

    public void removePermission(String permission) {
        permissions.remove(permission);
    }
}

