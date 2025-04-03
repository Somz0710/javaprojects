package Votingsystem;
import java.util.*;

    public abstract class AbstractElection {
        protected String electionId;
        protected String electionName;
        protected Date startDate;
        protected Date endDate;
        protected boolean isActive;
        protected Map<String, Candidate> candidates;
        protected Map<String, Voter> voters;

        public AbstractElection(String electionId, String electionName) {
            this.electionId = electionId;
            this.electionName = electionName;
            this.candidates = new HashMap<>();
            this.voters = new HashMap<>();
            this.isActive = false;
        }

        public String getElectionId() {
            return electionId;
        }

        public String getElectionName() {
            return electionName;
        }

        public Date getStartDate() {
            return startDate;
        }

        public Date getEndDate() {
            return endDate;
        }

        public boolean isActive() {
            return isActive;
        }

        public void setStartDate(Date startDate) {
            this.startDate = startDate;
        }

        public void setEndDate(Date endDate) {
            this.endDate = endDate;
        }

        public void addCandidate(Candidate candidate) {
            candidates.put(candidate.getCandidateId(), candidate);
        }

        public void addVoter(Voter voter) {
            voters.put(voter.getVoterId(), voter);
        }

        public Candidate getCandidate(String candidateId) {
            return candidates.get(candidateId);
        }

        public Voter getVoter(String voterId) {
            return voters.get(voterId);
        }

        public List<Candidate> getAllCandidates() {
            return new ArrayList<>(candidates.values());
        }

        public List<Voter> getAllVoters() {
            return new ArrayList<>(voters.values());
        }

        public abstract void announceWinner();
        public abstract double calculateVoterTurnout();
    }

