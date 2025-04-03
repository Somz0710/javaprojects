package Votingsystem;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ElectionCommissioner extends AbstractAdministrator {
    private Set<String> permissions;

    public ElectionCommissioner(String adminId, String name) {
        super(adminId, name, "Election Commissioner");
        this.permissions = new HashSet<>(Arrays.asList(
                "REGISTER_VOTER",
                "REGISTER_CANDIDATE",
                "VIEW_VOTER",
                "VIEW_CANDIDATE",
                "OPEN_ELECTION",
                "CLOSE_ELECTION",
                "VIEW_RESULTS",
                "ANNOUNCE_RESULTS",
                "GENERATE_REPORTS",
                "MANAGE_OFFICERS"
        ));
    }

    @Override
    public boolean hasPermission(String operation) {
        return isActive && permissions.contains(operation);
    }

    public boolean registerCandidate(GeneralElection election, Candidate candidate) {
        if (!hasPermission("REGISTER_CANDIDATE")) {
            return false;
        }

        return election.registerCandidate(candidate);
    }

    public void openElection(GeneralElection election) {
        if (hasPermission("OPEN_ELECTION")) {
            election.openElection();
        }
    }

    public void closeElection(GeneralElection election) {
        if (hasPermission("CLOSE_ELECTION")) {
            election.closeElection();
        }
    }

    public void announceResults(GeneralElection election) {
        if (hasPermission("ANNOUNCE_RESULTS")) {
            election.announceWinner();
        }
    }

    public void generateReports(GeneralElection election) {
        if (hasPermission("GENERATE_REPORTS")) {
            election.generateVoterParticipationReport();
            election.generateCandidatePerformanceReport();
            election.generateConstituencyWiseReport();
        }
    }

    public ElectionOfficer appointOfficer(String adminId, String name, String constituency) {
        if (hasPermission("MANAGE_OFFICERS")) {
            return new ElectionOfficer(adminId, name, constituency);
        }
        return null;
    }
}
