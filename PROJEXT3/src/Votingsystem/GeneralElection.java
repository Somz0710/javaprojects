package Votingsystem;
import java.util.*;
import java.util.stream.Collectors;

    public class GeneralElection extends AbstractElection implements IVotingSystem, Ireport {
        private Map<String, Set<String>> constituencyVoters;
        private Map<String, List<Candidate>> constituencyCandidates;
        private IElectionAudit audit;

        public GeneralElection(String electionId, String electionName) {
            super(electionId, electionName);
            this.constituencyVoters = new HashMap<>();
            this.constituencyCandidates = new HashMap<>();
            this.audit = new ElectionAudit();
        }

        @Override
        public boolean registerVoter(Voter voter) {
            if (voters.containsKey(voter.getVoterId())) {
                return false;
            }

            voters.put(voter.getVoterId(), voter);

            // Add voter to constituency
            String constituency = voter.getConstituency();
            if (!constituencyVoters.containsKey(constituency)) {
                constituencyVoters.put(constituency, new HashSet<>());
            }
            constituencyVoters.get(constituency).add(voter.getVoterId());

            audit.logAdminActivity("Admin", "Registered voter: " + voter.getVoterId());
            return true;
        }

        @Override
        public boolean registerCandidate(Candidate candidate) {
            if (candidates.containsKey(candidate.getCandidateId())) {
                return false;
            }

            candidates.put(candidate.getCandidateId(), candidate);

            // Add candidate to constituency
            String constituency = candidate.getConstituency();
            if (!constituencyCandidates.containsKey(constituency)) {
                constituencyCandidates.put(constituency, new ArrayList<>());
            }
            constituencyCandidates.get(constituency).add(candidate);

            audit.logAdminActivity("Admin", "Registered candidate: " + candidate.getCandidateId());
            return true;
        }

        @Override
        public boolean castVote(String voterId, String candidateId) {
            if (!isActive) {
                return false;
            }

            Voter voter = voters.get(voterId);
            Candidate candidate = candidates.get(candidateId);

            if (voter == null || candidate == null) {
                return false;
            }

            if (voter.hasVoted()) {
                return false;
            }

            if (!voter.getConstituency().equals(candidate.getConstituency())) {
                return false; // Voter can only vote in their constituency
            }

            candidate.incrementVote();
            voter.setVoted(true);

            audit.logVoterActivity(voterId, "Voted for candidate: " + candidateId);
            return true;
        }

        @Override
        public void displayResults() {
            System.out.println("Election Results for " + electionName);
            System.out.println("===================================");

            for (String constituency : constituencyCandidates.keySet()) {
                System.out.println("\nConstituency: " + constituency);

                List<Candidate> constituencyCandidateList = constituencyCandidates.get(constituency);
                constituencyCandidateList.sort((c1, c2) -> Integer.compare(c2.getVoteCount(), c1.getVoteCount()));

                for (Candidate candidate : constituencyCandidateList) {
                    System.out.println(candidate.getName() + " (" + candidate.getParty() + "): " + candidate.getVoteCount() + " votes");
                }
            }
        }

        @Override
        public boolean isElectionOpen() {
            return isActive;
        }

        @Override
        public void openElection() {
            isActive = true;
            startDate = new Date();
            audit.logAdminActivity("Admin", "Opened election: " + electionId);
        }

        @Override
        public void closeElection() {
            isActive = false;
            endDate = new Date();
            audit.logAdminActivity("Admin", "Closed election: " + electionId);
        }

        @Override
        public void announceWinner() {
            Map<String, Candidate> winners = new HashMap<>();

            for (String constituency : constituencyCandidates.keySet()) {
                List<Candidate> constituencyCandidateList = constituencyCandidates.get(constituency);
                constituencyCandidateList.sort((c1, c2) -> Integer.compare(c2.getVoteCount(), c1.getVoteCount()));

                if (!constituencyCandidateList.isEmpty()) {
                    winners.put(constituency, constituencyCandidateList.get(0));
                }
            }

            System.out.println("\nWinners by Constituency:");
            System.out.println("=======================");

            for (Map.Entry<String, Candidate> entry : winners.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue().getName() +
                        " (" + entry.getValue().getParty() + ") with " +
                        entry.getValue().getVoteCount() + " votes");
            }

            // Count parties that won the most constituencies
            Map<String, Integer> partySeats = new HashMap<>();

            for (Candidate winner : winners.values()) {
                partySeats.put(winner.getParty(), partySeats.getOrDefault(winner.getParty(), 0) + 1);
            }

            // Find the party with the most seats
            String winningParty = "";
            int maxSeats = 0;

            for (Map.Entry<String, Integer> entry : partySeats.entrySet()) {
                if (entry.getValue() > maxSeats) {
                    maxSeats = entry.getValue();
                    winningParty = entry.getKey();
                }
            }

            System.out.println("\nOverall Winner Party: " + winningParty + " with " + maxSeats + " constituencies won.");
        }

        @Override
        public double calculateVoterTurnout() {
            int totalVoters = voters.size();
            int votedCount = (int) voters.values().stream().filter(Voter::hasVoted).count();

            return totalVoters > 0 ? (double) votedCount / totalVoters * 100 : 0;
        }

        @Override
        public void generateVoterParticipationReport() {
            System.out.println("\nVoter Participation Report");
            System.out.println("=========================");

            int totalVoters = voters.size();
            int votedCount = (int) voters.values().stream().filter(Voter::hasVoted).count();
            double turnoutPercentage = calculateVoterTurnout();

            System.out.println("Total registered voters: " + totalVoters);
            System.out.println("Total votes cast: " + votedCount);
            System.out.println("Voter turnout: " + String.format("%.2f", turnoutPercentage) + "%");

            // Constituency-wise turnout
            System.out.println("\nConstituency-wise Turnout:");
            for (String constituency : constituencyVoters.keySet()) {
                Set<String> voterIds = constituencyVoters.get(constituency);
                int constituencyVoterCount = voterIds.size();
                int constituencyVotedCount = (int) voterIds.stream()
                        .map(id -> voters.get(id))
                        .filter(Voter::hasVoted)
                        .count();

                double constituencyTurnoutPercentage = constituencyVoterCount > 0 ?
                        (double) constituencyVotedCount / constituencyVoterCount * 100 : 0;

                System.out.println(constituency + ": " +
                        constituencyVotedCount + "/" + constituencyVoterCount + " (" +
                        String.format("%.2f", constituencyTurnoutPercentage) + "%)");
            }
        }

        @Override
        public void generateCandidatePerformanceReport() {
            System.out.println("\nCandidate Performance Report");
            System.out.println("===========================");

            // Sort candidates by vote count
            List<Candidate> sortedCandidates = new ArrayList<>(candidates.values());
            sortedCandidates.sort((c1, c2) -> Integer.compare(c2.getVoteCount(), c1.getVoteCount()));

            for (Candidate candidate : sortedCandidates) {
                String constituency = candidate.getConstituency();
                int constituencyVoterCount = constituencyVoters.getOrDefault(constituency, new HashSet<>()).size();
                double votePercentage = constituencyVoterCount > 0 ?
                        (double) candidate.getVoteCount() / constituencyVoterCount * 100 : 0;

                System.out.println(candidate.getName() + " (" + candidate.getParty() + "), " + constituency + ": " +
                        candidate.getVoteCount() + " votes (" + String.format("%.2f", votePercentage) + "%)");
            }
        }

        @Override
        public void generateConstituencyWiseReport() {
            System.out.println("\nConstituency-wise Report");
            System.out.println("=======================");

            for (String constituency : constituencyCandidates.keySet()) {
                System.out.println("\nConstituency: " + constituency);

                int constituencyVoterCount = constituencyVoters.getOrDefault(constituency, new HashSet<>()).size();
                int constituencyVotedCount = (int) constituencyVoters.getOrDefault(constituency, new HashSet<>()).stream()
                        .map(id -> voters.get(id))
                        .filter(Voter::hasVoted)
                        .count();

                double constituencyTurnoutPercentage = constituencyVoterCount > 0 ?
                        (double) constituencyVotedCount / constituencyVoterCount * 100 : 0;

                System.out.println("Registered voters: " + constituencyVoterCount);
                System.out.println("Votes cast: " + constituencyVotedCount);
                System.out.println("Turnout: " + String.format("%.2f", constituencyTurnoutPercentage) + "%");

                List<Candidate> constituencyCandidateList = constituencyCandidates.get(constituency);
                constituencyCandidateList.sort((c1, c2) -> Integer.compare(c2.getVoteCount(), c1.getVoteCount()));

                System.out.println("\nCandidate results:");
                for (Candidate candidate : constituencyCandidateList) {
                    double votePercentage = constituencyVotedCount > 0 ?
                            (double) candidate.getVoteCount() / constituencyVotedCount * 100 : 0;

                    System.out.println(candidate.getName() + " (" + candidate.getParty() + "): " +
                            candidate.getVoteCount() + " votes (" + String.format("%.2f", votePercentage) + "%)");
                }
            }
        }
    }

