package Votingsystem;

public interface IVotingSystem {
    boolean registerVoter(Voter voter);
    boolean registerCandidate(Candidate candidate);
    boolean castVote(String voterId, String candidateId);
    void displayResults();
    boolean isElectionOpen();
    void openElection();
    void closeElection();
}