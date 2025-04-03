package Votingsystem;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.HashMap;

public class VotingSystemUI extends Frame {
    private GeneralElection election;
    private ElectionCommissioner commissioner;
    private Panel mainPanel, adminPanel, voterPanel, resultsPanel;
    private CardLayout cardLayout;

    public VotingSystemUI(String title) {
        super(title);

        // Initialize the election system
        election = new GeneralElection("E2023", "General Election 2023");
        commissioner = new ElectionCommissioner("EC001", "John Smith");

        // Add some sample data
        setupSampleData();

        // Set up the UI
        setupUI();

        // Window listener for closing
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                System.exit(0);
            }
        });
    }

    private void setupSampleData() {
        // Add constituencies
        String[] constituencies = {"North", "South", "East", "West", "Central"};

        // Add candidates
        Candidate c1 = new Candidate("C001", "Alice Johnson", "Party A", "North");
        Candidate c2 = new Candidate("C002", "Bob Williams", "Party B", "North");
        Candidate c3 = new Candidate("C003", "Carol Davis", "Party C", "South");
        Candidate c4 = new Candidate("C004", "David Brown", "Party A", "South");
        Candidate c5 = new Candidate("C005", "Eve Wilson", "Party B", "East");
        Candidate c6 = new Candidate("C006", "Frank Miller", "Party C", "East");
        Candidate c7 = new Candidate("C007", "Grace Taylor", "Party A", "West");
        Candidate c8 = new Candidate("C008", "Henry Anderson", "Party B", "West");
        Candidate c9 = new Candidate("C009", "Irene Thomas", "Party C", "Central");
        Candidate c10 = new Candidate("C010", "Jack Roberts", "Party A", "Central");

        commissioner.registerCandidate(election, c1);
        commissioner.registerCandidate(election, c2);
        commissioner.registerCandidate(election, c3);
        commissioner.registerCandidate(election, c4);
        commissioner.registerCandidate(election, c5);
        commissioner.registerCandidate(election, c6);
        commissioner.registerCandidate(election, c7);
        commissioner.registerCandidate(election, c8);
        commissioner.registerCandidate(election, c9);
        commissioner.registerCandidate(election, c10);

        // Add voters (20 voters, 4 per constituency)
        for (int i = 1; i <= 20; i++) {
            String voterId = "V" + String.format("%03d", i);
            String name = "Voter " + i;
            int age = 20 + (i % 50); // Ages between 20 and 70
            String constituency = constituencies[(i - 1) % 5];

            Voter voter = new Voter(voterId, name, age, constituency);
            election.registerVoter(voter);
        }

        // Open the election
        commissioner.openElection(election);

        // Simulate some votes
        for (int i = 1; i <= 15; i++) {
            String voterId = "V" + String.format("%03d", i);
            // Cast vote for a random candidate in the voter's constituency
            Voter voter = election.getVoter(voterId);
            if (voter != null) {
                String constituency = voter.getConstituency();
                List<Candidate> constituencyCandidates = election.getAllCandidates().stream()
                        .filter(c -> c.getConstituency().equals(constituency))
                        .toList();

                if (!constituencyCandidates.isEmpty()) {
                    int randomIndex = (int) (Math.random() * constituencyCandidates.size());
                    String candidateId = constituencyCandidates.get(randomIndex).getCandidateId();
                    election.castVote(voterId, candidateId);
                }
            }
        }
    }

    private void setupUI() {
        // Set dimensions and layout
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Use CardLayout for different panels
        cardLayout = new CardLayout();
        mainPanel = new Panel();
        mainPanel.setLayout(cardLayout);

        // Create panels
        createMainMenuPanel();
        createAdminPanel();
        createVoterPanel();
        createResultsPanel();

        // Add main panel to frame
        add(mainPanel);

        // Show main menu initially
        cardLayout.show(mainPanel, "MainMenu");

        // Make the frame visible
        setVisible(true);
    }

    private void createMainMenuPanel() {
        Panel menuPanel = new Panel();
        menuPanel.setLayout(new BorderLayout());

        // Title
        Label titleLabel = new Label("Voting Management System", Label.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        menuPanel.add(titleLabel, BorderLayout.NORTH);

        // Buttons panel
        Panel buttonsPanel = new Panel();
        buttonsPanel.setLayout(new GridLayout(4, 1, 10, 20));

        Button adminButton = new Button("Administrator Login");
        Button voterButton = new Button("Voter Interface");
        Button resultsButton = new Button("View Election Results");
        Button exitButton = new Button("Exit");

        adminButton.addActionListener(e -> cardLayout.show(mainPanel, "Admin"));
        voterButton.addActionListener(e -> cardLayout.show(mainPanel, "Voter"));
        resultsButton.addActionListener(e -> {
            refreshResultsPanel();
            cardLayout.show(mainPanel, "Results");
        });
        exitButton.addActionListener(e -> System.exit(0));

        buttonsPanel.add(adminButton);
        buttonsPanel.add(voterButton);
        buttonsPanel.add(resultsButton);
        buttonsPanel.add(exitButton);

        Panel centerPanel = new Panel();
        centerPanel.setLayout(new FlowLayout());
        centerPanel.add(buttonsPanel);

        menuPanel.add(centerPanel, BorderLayout.CENTER);

        // Status label
        Label statusLabel = new Label("Election status: " + (election.isElectionOpen() ? "OPEN" : "CLOSED"), Label.CENTER);
        menuPanel.add(statusLabel, BorderLayout.SOUTH);

        mainPanel.add(menuPanel, "MainMenu");
    }

    private void createAdminPanel() {
        adminPanel = new Panel();
        adminPanel.setLayout(new BorderLayout());

        // Title
        Label titleLabel = new Label("Administrator Panel", Label.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        adminPanel.add(titleLabel, BorderLayout.NORTH);

        // Admin functions panel
        Panel functionsPanel = new Panel();
        functionsPanel.setLayout(new GridLayout(8, 1, 10, 10));

        Button registerVoterButton = new Button("Register New Voter");
        Button registerCandidateButton = new Button("Register New Candidate");
        Button manageElectionButton = new Button("Open/Close Election");
        Button viewVotersButton = new Button("View All Voters");
        Button viewCandidatesButton = new Button("View All Candidates");
        Button generateReportsButton = new Button("Generate Reports");
        Button viewAuditButton = new Button("View Audit Log");
        Button backButton = new Button("Back to Main Menu");

        registerVoterButton.addActionListener(e -> showRegisterVoterDialog());
        registerCandidateButton.addActionListener(e -> showRegisterCandidateDialog());
        manageElectionButton.addActionListener(e -> manageElection());
        viewVotersButton.addActionListener(e -> showVotersList());
        viewCandidatesButton.addActionListener(e -> showCandidatesList());
        generateReportsButton.addActionListener(e -> generateReports());
        viewAuditButton.addActionListener(e -> showAuditLog());
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "MainMenu"));

        functionsPanel.add(registerVoterButton);
        functionsPanel.add(registerCandidateButton);
        functionsPanel.add(manageElectionButton);
        functionsPanel.add(viewVotersButton);
        functionsPanel.add(viewCandidatesButton);
        functionsPanel.add(generateReportsButton);
        functionsPanel.add(viewAuditButton);
        functionsPanel.add(backButton);

        Panel centerPanel = new Panel();
        centerPanel.setLayout(new FlowLayout());
        centerPanel.add(functionsPanel);

        adminPanel.add(centerPanel, BorderLayout.CENTER);

        // Status label
        Label adminStatusLabel = new Label("Administrator: " + commissioner.getName() + " | Role: " + commissioner.getRole(), Label.CENTER);
        adminPanel.add(adminStatusLabel, BorderLayout.SOUTH);

        mainPanel.add(adminPanel, "Admin");
    }

    private void createVoterPanel() {
        voterPanel = new Panel();
        voterPanel.setLayout(new BorderLayout());

        // Title
        Label titleLabel = new Label("Voter Interface", Label.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        voterPanel.add(titleLabel, BorderLayout.NORTH);

        // Voter ID input and validation
        Panel voterIdPanel = new Panel();
        voterIdPanel.setLayout(new FlowLayout());

        Label voterIdLabel = new Label("Enter Voter ID:");
        TextField voterIdField = new TextField(10);
        Button validateButton = new Button("Validate");

        voterIdPanel.add(voterIdLabel);
        voterIdPanel.add(voterIdField);
        voterIdPanel.add(validateButton);

        // Voting panel (hidden initially)
        Panel votingPanel = new Panel();
        votingPanel.setLayout(new BorderLayout());
        votingPanel.setVisible(false);

        Label voterInfoLabel = new Label("", Label.CENTER);
        Panel candidatesPanel = new Panel();
        candidatesPanel.setLayout(new GridLayout(0, 1));

        votingPanel.add(voterInfoLabel, BorderLayout.NORTH);
        votingPanel.add(candidatesPanel, BorderLayout.CENTER);

        // Combine panels
        Panel centerPanel = new Panel();
        centerPanel.setLayout(new BorderLayout());
        centerPanel.add(voterIdPanel, BorderLayout.NORTH);
        centerPanel.add(votingPanel, BorderLayout.CENTER);

        voterPanel.add(centerPanel, BorderLayout.CENTER);

        // Back button
        Button backButton = new Button("Back to Main Menu");
        backButton.addActionListener(e -> {
            voterIdField.setText("");
            votingPanel.setVisible(false);
            cardLayout.show(mainPanel, "MainMenu");
        });

        Panel bottomPanel = new Panel();
        bottomPanel.add(backButton);
        voterPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Validate button action
        validateButton.addActionListener(e -> {
            String voterId = voterIdField.getText().trim();
            Voter voter = election.getVoter(voterId);

            if (voter == null) {
                voterInfoLabel.setText("Voter ID not found.");
                votingPanel.setVisible(true);
                candidatesPanel.removeAll();
                return;
            }

            if (voter.hasVoted()) {
                voterInfoLabel.setText("You have already cast your vote.");
                votingPanel.setVisible(true);
                candidatesPanel.removeAll();
                return;
            }

            if (!election.isElectionOpen()) {
                voterInfoLabel.setText("The election is currently closed.");
                votingPanel.setVisible(true);
                candidatesPanel.removeAll();
                return;
            }

            // Valid voter who hasn't voted
            voterInfoLabel.setText("Welcome, " + voter.getName() + " | Constituency: " + voter.getConstituency());

            // Show candidates for voter's constituency
            candidatesPanel.removeAll();
            Label selectLabel = new Label("Select a candidate to vote:", Label.CENTER);
            candidatesPanel.add(selectLabel);

            String constituency = voter.getConstituency();
            List<Candidate> constituencyCandidates = election.getAllCandidates().stream()
                    .filter(c -> c.getConstituency().equals(constituency))
                    .toList();

            for (Candidate candidate : constituencyCandidates) {
                Button candidateButton = new Button(candidate.getName() + " (" + candidate.getParty() + ")");
                candidateButton.addActionListener(evt -> {
                    boolean success = election.castVote(voterId, candidate.getCandidateId());
                    if (success) {
                        voterInfoLabel.setText("Vote cast successfully for " + candidate.getName());
                        candidatesPanel.removeAll();
                        Label thanksLabel = new Label("Thank you for voting!", Label.CENTER);
                        candidatesPanel.add(thanksLabel);
                        candidatesPanel.revalidate();
                        candidatesPanel.repaint();
                    } else {
                        voterInfoLabel.setText("Error casting vote. Please try again.");
                    }
                });
                candidatesPanel.add(candidateButton);
            }

            votingPanel.setVisible(true);
            candidatesPanel.revalidate();
            candidatesPanel.repaint();
        });

        mainPanel.add(voterPanel, "Voter");
    }

    private void createResultsPanel() {
        resultsPanel = new Panel();
        resultsPanel.setLayout(new BorderLayout());

        // Title
        Label titleLabel = new Label("Election Results", Label.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        resultsPanel.add(titleLabel, BorderLayout.NORTH);

        // Results content panel
        Panel contentPanel = new Panel();
        contentPanel.setLayout(new BorderLayout());

        TextArea resultsTextArea = new TextArea();
        resultsTextArea.setEditable(false);
        contentPanel.add(resultsTextArea, BorderLayout.CENTER);

        resultsPanel.add(contentPanel, BorderLayout.CENTER);

        // Back button
        Button backButton = new Button("Back to Main Menu");
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "MainMenu"));

        Panel bottomPanel = new Panel();
        bottomPanel.add(backButton);
        resultsPanel.add(bottomPanel, BorderLayout.SOUTH);

        mainPanel.add(resultsPanel, "Results");
    }

    private void refreshResultsPanel() {
        // Find the TextArea in the resultsPanel
        for (Component component : resultsPanel.getComponents()) {
            if (component instanceof Panel) {
                Panel panel = (Panel) component;
                for (Component innerComponent : panel.getComponents()) {
                    if (innerComponent instanceof TextArea) {
                        TextArea resultsTextArea = (TextArea) innerComponent;

                        // Build results string
                        StringBuilder sb = new StringBuilder();

                        sb.append("Election: ").append(election.getElectionName()).append("\n");
                        sb.append("Status: ").append(election.isElectionOpen() ? "OPEN" : "CLOSED").append("\n\n");

                        sb.append("Voter Turnout: ").append(String.format("%.2f", election.calculateVoterTurnout())).append("%\n");
                        sb.append("Total Registered Voters: ").append(election.getAllVoters().size()).append("\n");
                        sb.append("Total Votes Cast: ").append(election.getAllVoters().stream().filter(Voter::hasVoted).count()).append("\n\n");

                        sb.append("Results by Constituency:\n");
                        sb.append("=======================\n\n");

                        // Group candidates by constituency
                        election.getAllCandidates().stream()
                                .collect(java.util.stream.Collectors.groupingBy(Candidate::getConstituency))
                                .forEach((constituency, candidates) -> {
                                    sb.append(constituency).append(":\n");

                                    // Sort candidates by vote count (descending)
                                    candidates.sort((c1, c2) -> Integer.compare(c2.getVoteCount(), c1.getVoteCount()));

                                    for (Candidate candidate : candidates) {
                                        sb.append("  ")
                                                .append(candidate.getName())
                                                .append(" (")
                                                .append(candidate.getParty())
                                                .append("): ")
                                                .append(candidate.getVoteCount())
                                                .append(" votes\n");
                                    }
                                    sb.append("\n");
                                });

                        // Party totals
                        sb.append("Party Totals:\n");
                        sb.append("============\n\n");

                        election.getAllCandidates().stream()
                                .collect(java.util.stream.Collectors.groupingBy(
                                        Candidate::getParty,
                                        java.util.stream.Collectors.summingInt(Candidate::getVoteCount)
                                ))
                                .entrySet().stream()
                                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                                .forEach(entry -> {
                                    sb.append(entry.getKey())
                                            .append(": ")
                                            .append(entry.getValue())
                                            .append(" votes\n");
                                });

                        resultsTextArea.setText(sb.toString());
                        break;
                    }
                }
            }
        }
    }

    private void showRegisterVoterDialog() {
        Dialog dialog = new Dialog(this, "Register New Voter", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);

        Panel formPanel = new Panel();
        formPanel.setLayout(new GridLayout(5, 2, 10, 10));

        Label idLabel = new Label("Voter ID:");
        TextField idField = new TextField();

        Label nameLabel = new Label("Name:");
        TextField nameField = new TextField();

        Label ageLabel = new Label("Age:");
        TextField ageField = new TextField();

        Label constituencyLabel = new Label("Constituency:");
        Choice constituencyChoice = new Choice();
        constituencyChoice.add("North");
        constituencyChoice.add("South");
        constituencyChoice.add("East");
        constituencyChoice.add("West");
        constituencyChoice.add("Central");

        formPanel.add(idLabel);
        formPanel.add(idField);
        formPanel.add(nameLabel);
        formPanel.add(nameField);
        formPanel.add(ageLabel);
        formPanel.add(ageField);
        formPanel.add(constituencyLabel);
        formPanel.add(constituencyChoice);

        Panel buttonsPanel = new Panel();
        Button registerButton = new Button("Register");
        Button cancelButton = new Button("Cancel");

        registerButton.addActionListener(e -> {
            try {
                String id = idField.getText().trim();
                String name = nameField.getText().trim();
                int age = Integer.parseInt(ageField.getText().trim());
                String constituency = constituencyChoice.getSelectedItem();

                if (id.isEmpty() || name.isEmpty()) {
                    showMessage("Please fill all fields");
                    return;
                }

                if (age < 18) {
                    showMessage("Voter must be at least 18 years old");
                    return;
                }

                Voter voter = new Voter(id, name, age, constituency);
                boolean success = election.registerVoter(voter);

                if (success) {
                    showMessage("Voter registered successfully");
                    dialog.dispose();
                } else {
                    showMessage("Failed to register voter. ID may already exist.");
                }
            } catch (NumberFormatException ex) {
                showMessage("Please enter a valid age");
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        buttonsPanel.add(registerButton);
        buttonsPanel.add(cancelButton);

        Panel centerPanel = new Panel();
        centerPanel.setLayout(new FlowLayout());
        centerPanel.add(formPanel);

        dialog.add(centerPanel, BorderLayout.CENTER);
        dialog.add(buttonsPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private void showRegisterCandidateDialog() {
        Dialog dialog = new Dialog(this, "Register New Candidate", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);

        Panel formPanel = new Panel();
        formPanel.setLayout(new GridLayout(5, 2, 10, 10));

        Label idLabel = new Label("Candidate ID:");
        TextField idField = new TextField();

        Label nameLabel = new Label("Name:");
        TextField nameField = new TextField();

        Label partyLabel = new Label("Party:");
        TextField partyField = new TextField();

        Label constituencyLabel = new Label("Constituency:");
        Choice constituencyChoice = new Choice();
        constituencyChoice.add("North");
        constituencyChoice.add("South");
        constituencyChoice.add("East");
        constituencyChoice.add("West");
        constituencyChoice.add("Central");

        formPanel.add(idLabel);
        formPanel.add(idField);
        formPanel.add(nameLabel);
        formPanel.add(nameField);
        formPanel.add(partyLabel);
        formPanel.add(partyField);
        formPanel.add(constituencyLabel);
        formPanel.add(constituencyChoice);

        Panel buttonsPanel = new Panel();
        Button registerButton = new Button("Register");
        Button cancelButton = new Button("Cancel");

        registerButton.addActionListener(e -> {
            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            String party = partyField.getText().trim();
            String constituency = constituencyChoice.getSelectedItem();

            if (id.isEmpty() || name.isEmpty() || party.isEmpty()) {
                showMessage("Please fill all fields");
                return;
            }

            Candidate candidate = new Candidate(id, name, party, constituency);
            boolean success = commissioner.registerCandidate(election, candidate);

            if (success) {
                showMessage("Candidate registered successfully");
                dialog.dispose();
            } else {
                showMessage("Failed to register candidate. ID may already exist.");
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        buttonsPanel.add(registerButton);
        buttonsPanel.add(cancelButton);

        Panel centerPanel = new Panel();
        centerPanel.setLayout(new FlowLayout());
        centerPanel.add(formPanel);

        dialog.add(centerPanel, BorderLayout.CENTER);
        dialog.add(buttonsPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private void manageElection() {
        Dialog dialog = new Dialog(this, "Manage Election", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(300, 150);
        dialog.setLocationRelativeTo(this);

        Panel contentPanel = new Panel();
        contentPanel.setLayout(new FlowLayout());

        Label statusLabel = new Label("Current Status: " + (election.isElectionOpen() ? "OPEN" : "CLOSED"));
        contentPanel.add(statusLabel);

        Panel buttonsPanel = new Panel();
        Button toggleButton = new Button(election.isElectionOpen() ? "Close Election" : "Open Election");
        Button cancelButton = new Button("Cancel");

        toggleButton.addActionListener(e -> {
            if (election.isElectionOpen()) {
                commissioner.closeElection(election);
                showMessage("Election has been closed");
            } else {
                commissioner.openElection(election);
                showMessage("Election has been opened");
            }
            dialog.dispose();
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        buttonsPanel.add(toggleButton);
        buttonsPanel.add(cancelButton);

        dialog.add(contentPanel, BorderLayout.CENTER);
        dialog.add(buttonsPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private void showVotersList() {
        Dialog dialog = new Dialog(this, "Voters List", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(this);

        TextArea votersArea = new TextArea();
        votersArea.setEditable(false);

        StringBuilder sb = new StringBuilder();
        sb.append("Total Registered Voters: ").append(election.getAllVoters().size()).append("\n\n");

        sb.append(String.format("%-10s %-20s %-5s %-15s %-10s\n", "ID", "Name", "Age", "Constituency", "Voted"));
        sb.append("-----------------------------------------------------------\n");

        for (Voter voter : election.getAllVoters()) {
            sb.append(String.format("%-10s %-20s %-5d %-15s %-10s\n",
                    voter.getVoterId(),
                    voter.getName(),
                    voter.getAge(),
                    voter.getConstituency(),
                    voter.hasVoted() ? "Yes" : "No"));
        }

        votersArea.setText(sb.toString());

        Button closeButton = new Button("Close");
        closeButton.addActionListener(e -> dialog.dispose());

        Panel buttonPanel = new Panel();
        buttonPanel.add(closeButton);

        dialog.add(votersArea, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private void showCandidatesList() {
        Dialog dialog = new Dialog(this, "Candidates List", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(this);

        TextArea candidatesArea = new TextArea();
        candidatesArea.setEditable(false);

        StringBuilder sb = new StringBuilder();
        sb.append("Total Registered Candidates: ").append(election.getAllCandidates().size()).append("\n\n");

        sb.append(String.format("%-10s %-20s %-15s %-15s %-10s\n", "ID", "Name", "Party", "Constituency", "Votes"));
        sb.append("-----------------------------------------------------------\n");

        for (Candidate candidate : election.getAllCandidates()) {
            sb.append(String.format("%-10s %-20s %-15s %-15s %-10d\n",
                    candidate.getCandidateId(),
                    candidate.getName(),
                    candidate.getParty(),
                    candidate.getConstituency(),
                    candidate.getVoteCount()));
        }

        candidatesArea.setText(sb.toString());

        Button closeButton = new Button("Close");
        closeButton.addActionListener(e -> dialog.dispose());

        Panel buttonPanel = new Panel();
        buttonPanel.add(closeButton);

        dialog.add(candidatesArea, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private void generateReports() {
        Dialog dialog = new Dialog(this, "Reports", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(700, 500);
        dialog.setLocationRelativeTo(this);

        // Tabs panel using CardLayout
        Panel tabPanel = new Panel();
        CardLayout tabLayout = new CardLayout();
        tabPanel.setLayout(tabLayout);

        // Tab buttons
        Panel buttonPanel = new Panel();
        Button voterParticipationButton = new Button("Voter Participation");
        Button candidatePerformanceButton = new Button("Candidate Performance");
        Button constituencyButton = new Button("Constituency Report");

        buttonPanel.add(voterParticipationButton);
        buttonPanel.add(candidatePerformanceButton);
        buttonPanel.add(constituencyButton);

        // Report content areas
        TextArea voterParticipationArea = new TextArea();
        TextArea candidatePerformanceArea = new TextArea();
        TextArea constituencyArea = new TextArea();

        voterParticipationArea.setEditable(false);
        candidatePerformanceArea.setEditable(false);
        constituencyArea.setEditable(false);

        // Populate text areas with report data
        // Voter Participation Report
        StringBuilder vpSb = new StringBuilder();
        int totalVoters = election.getAllVoters().size();
        long votedCount = election.getAllVoters().stream().filter(Voter::hasVoted).count();
        double turnoutPercentage = election.calculateVoterTurnout();

        vpSb.append("VOTER PARTICIPATION REPORT\n");
        vpSb.append("==========================\n\n");
        vpSb.append("Total registered voters: ").append(totalVoters).append("\n");
        vpSb.append("Total votes cast: ").append(votedCount).append("\n");
        vpSb.append("Voter turnout: ").append(String.format("%.2f", turnoutPercentage)).append("%\n\n");

        vpSb.append("Constituency-wise Turnout:\n");
        vpSb.append("-------------------------\n");

        // Group voters by constituency
        Map<String, List<Voter>> votersByConstituency = election.getAllVoters().stream()
                .collect(Collectors.groupingBy(Voter::getConstituency));

        for (Map.Entry<String, List<Voter>> entry : votersByConstituency.entrySet()) {
            String constituency = entry.getKey();
            List<Voter> constituencyVoters = entry.getValue();
            int constituencyVoterCount = constituencyVoters.size();
            long constituencyVotedCount = constituencyVoters.stream().filter(Voter::hasVoted).count();
            double constituencyTurnoutPercentage = constituencyVoterCount > 0 ?
                    (double) constituencyVotedCount / constituencyVoterCount * 100 : 0;

            vpSb.append(String.format("%-10s: %d/%d voters (%.2f%%)\n",
                    constituency, constituencyVotedCount, constituencyVoterCount, constituencyTurnoutPercentage));
        }

        voterParticipationArea.setText(vpSb.toString());

        // Candidate Performance Report
        StringBuilder cpSb = new StringBuilder();

        cpSb.append("CANDIDATE PERFORMANCE REPORT\n");
        cpSb.append("============================\n\n");

        // Sort candidates by vote count
        List<Candidate> sortedCandidates = new ArrayList<>(election.getAllCandidates());
        sortedCandidates.sort((c1, c2) -> Integer.compare(c2.getVoteCount(), c1.getVoteCount()));

        cpSb.append(String.format("%-20s %-15s %-15s %-10s\n", "Name", "Party", "Constituency", "Votes"));
        cpSb.append("------------------------------------------------------\n");

        for (Candidate candidate : sortedCandidates) {
            cpSb.append(String.format("%-20s %-15s %-15s %-10d\n",
                    candidate.getName(),
                    candidate.getParty(),
                    candidate.getConstituency(),
                    candidate.getVoteCount()));
        }

        cpSb.append("Party Performance:");
        cpSb.append("----------------\n");

        // Group candidates by party and sum votes
        Map<String, Integer> partyVotes = election.getAllCandidates().stream()
                .collect(Collectors.groupingBy(
                        Candidate::getParty,
                        Collectors.summingInt(Candidate::getVoteCount)
                ));

        // Sort parties by vote count
        partyVotes.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .forEach(entry -> {
                    cpSb.append(String.format("%-15s: %d votes\n", entry.getKey(), entry.getValue()));
                });

        candidatePerformanceArea.setText(cpSb.toString());

        // Constituency Report
        StringBuilder crSb = new StringBuilder();

        crSb.append("CONSTITUENCY REPORT\n");
        crSb.append("===================\n\n");

        // Group candidates by constituency
        Map<String, List<Candidate>> candidatesByConstituency = election.getAllCandidates().stream()
                .collect(Collectors.groupingBy(Candidate::getConstituency));

        for (Map.Entry<String, List<Candidate>> entry : candidatesByConstituency.entrySet()) {
            String constituency = entry.getKey();
            List<Candidate> constituencyCandidates = entry.getValue();

            crSb.append(constituency).append(" Constituency\n");
            crSb.append("--------------------\n");
            crSb.append("Number of candidates: ").append(constituencyCandidates.size()).append("\n");

            // Get voter count and turnout for this constituency
            List<Voter> constituencyVoters = votersByConstituency.getOrDefault(constituency, new ArrayList<>());
            int voterCount = constituencyVoters.size();
            long votedInConstituency = constituencyVoters.stream().filter(Voter::hasVoted).count();
            double constituencyTurnout = voterCount > 0 ? (double) votedInConstituency / voterCount * 100 : 0;

            crSb.append("Registered voters: ").append(voterCount).append("\n");
            crSb.append("Voter turnout: ").append(String.format("%.2f", constituencyTurnout)).append("%\n\n");

            // Sort candidates by vote count
            constituencyCandidates.sort((c1, c2) -> Integer.compare(c2.getVoteCount(), c1.getVoteCount()));

            crSb.append(String.format("%-20s %-15s %-10s\n", "Candidate", "Party", "Votes"));
            crSb.append("----------------------------------------\n");

            for (Candidate candidate : constituencyCandidates) {
                crSb.append(String.format("%-20s %-15s %-10d\n",
                        candidate.getName(),
                        candidate.getParty(),
                        candidate.getVoteCount()));
            }
            crSb.append("\n\n");
        }

        constituencyArea.setText(crSb.toString());

        // Add text areas to tab panel
        tabPanel.add(voterParticipationArea, "VoterParticipation");
        tabPanel.add(candidatePerformanceArea, "CandidatePerformance");
        tabPanel.add(constituencyArea, "Constituency");

        // Button actions
        voterParticipationButton.addActionListener(e -> tabLayout.show(tabPanel, "VoterParticipation"));
        candidatePerformanceButton.addActionListener(e -> tabLayout.show(tabPanel, "CandidatePerformance"));
        constituencyButton.addActionListener(e -> tabLayout.show(tabPanel, "Constituency"));

        // Close button
        Button closeButton = new Button("Close");
        closeButton.addActionListener(e -> dialog.dispose());
        Panel closePanel = new Panel();
        closePanel.add(closeButton);

        // Add components to dialog
        dialog.add(buttonPanel, BorderLayout.NORTH);
        dialog.add(tabPanel, BorderLayout.CENTER);
        dialog.add(closePanel, BorderLayout.SOUTH);

        // Show voter participation tab by default
        tabLayout.show(tabPanel, "VoterParticipation");

        dialog.setVisible(true);
    }

    private void showAuditLog() {
        Dialog dialog = new Dialog(this, "Audit Log", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(this);

        TextArea auditArea = new TextArea();
        auditArea.setEditable(false);

        StringBuilder sb = new StringBuilder();
        sb.append("ELECTION AUDIT LOG\n");
        sb.append("=================\n\n");

        sb.append("Election ID: ").append(election.getElectionId()).append("\n");
        sb.append("Election Name: ").append(election.getElectionName()).append("\n");
        sb.append("Commissioner: ").append(commissioner.getName()).append("\n");
        sb.append("Current Status: ").append(election.isElectionOpen() ? "OPEN" : "CLOSED").append("\n\n");

        sb.append("Statistics:\n");
        sb.append("-----------\n");
        sb.append("Total Registered Voters: ").append(election.getAllVoters().size()).append("\n");
        sb.append("Total Votes Cast: ").append(election.getAllVoters().stream().filter(Voter::hasVoted).count()).append("\n");
        sb.append("Voter Turnout: ").append(String.format("%.2f", election.calculateVoterTurnout())).append("%\n\n");

        sb.append("Total Candidates: ").append(election.getAllCandidates().size()).append("\n");
        sb.append("Constituencies: ").append(election.getAllCandidates().stream()
                .map(Candidate::getConstituency)
                .distinct()
                .count()).append("\n");
        sb.append("Political Parties: ").append(election.getAllCandidates().stream()
                .map(Candidate::getParty)
                .distinct()
                .count()).append("\n\n");

        auditArea.setText(sb.toString());

        Button closeButton = new Button("Close");
        closeButton.addActionListener(e -> dialog.dispose());

        Panel buttonPanel = new Panel();
        buttonPanel.add(closeButton);

        dialog.add(auditArea, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private void showMessage(String message) {
        Dialog dialog = new Dialog(this, "Message", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(300, 100);
        dialog.setLocationRelativeTo(this);

        Label messageLabel = new Label(message, Label.CENTER);
        Button okButton = new Button("OK");
        okButton.addActionListener(e -> dialog.dispose());

        Panel buttonPanel = new Panel();
        buttonPanel.add(okButton);

        dialog.add(messageLabel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    public static void main(String[] args) {
        new VotingSystemUI("Voting Management System");
    }
}

