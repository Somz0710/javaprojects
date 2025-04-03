package Votingsystem;

import java.util.*;

public class VotingStationManager {
    private String stationId;
    private String stationName;
    private String constituency;
    private GeneralElection election;
    private Queue<String> voterQueue;
    private Set<String> processedVoters;

    public VotingStationManager(String stationId, String stationName, String constituency, GeneralElection election) {
        this.stationId = stationId;
        this.stationName = stationName;
        this.constituency = constituency;
        this.election = election;
        this.voterQueue = new LinkedList<>();
        this.processedVoters = new HashSet<>();
    }

    public void addVoterToQueue(String voterId) {
        if (!processedVoters.contains(voterId)) {
            voterQueue.add(voterId);
            System.out.println("Voter " + voterId + " added to queue at station " + stationName);
        } else {
            System.out.println("Voter " + voterId + " has already voted at this station");
        }
    }

    public void processNextVoter(String candidateId) {
        if (voterQueue.isEmpty()) {
            System.out.println("No voters in queue");
            return;
        }

        String voterId = voterQueue.poll();
        Voter voter = election.getVoter(voterId);

        if (voter == null) {
            System.out.println("Voter ID not found in the system");
            return;
        }

        if (!voter.getConstituency().equals(constituency)) {
            System.out.println("Voter does not belong to this constituency");
            return;
        }

        if (voter.hasVoted()) {
            System.out.println("Voter has already cast a vote");
            return;
        }

        boolean voteSuccessful = election.castVote(voterId, candidateId);
        if (voteSuccessful) {
            processedVoters.add(voterId);
            System.out.println("Vote cast successfully for voter " + voter.getName());
        } else {
            System.out.println("Failed to cast vote");
        }
    }

    public int getQueueLength() {
        return voterQueue.size();
    }

    public int getProcessedVoterCount() {
        return processedVoters.size();
    }
}
