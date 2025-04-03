package Votingsystem;

public class Candidate {
    private String candidateId;
    private String name;
    private String party;
    private String constituency;
    private int voteCount;

    public Candidate(String candidateId, String name, String party, String constituency) {
        this.candidateId = candidateId;
        this.name = name;
        this.party = party;
        this.constituency = constituency;
        this.voteCount = 0;
    }

    public String getCandidateId() {
        return candidateId;
    }

    public String getName() {
        return name;
    }

    public String getParty() {
        return party;
    }

    public String getConstituency() {
        return constituency;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void incrementVote() {
        this.voteCount++;
    }

    @Override
    public String toString() {
        return name + " (" + party + ")";
    }
}