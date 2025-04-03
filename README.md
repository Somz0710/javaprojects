## Voting Management System

A comprehensive Java-based voting management system that simulates an electoral process with multiple constituencies, candidates, and voters. This application provides a complete solution for managing elections from voter registration to results tabulation.

## Features

### Administrator Functions
- **Voter Management**: Register new voters with unique IDs, names, age verification, and constituency assignment
- **Candidate Management**: Register candidates with party affiliations and constituency assignments
- **Election Control**: Open or close the election as needed
- **Reporting**: Generate detailed reports on voter participation, candidate performance, and constituency results
- **Audit Trail**: View election statistics and system activity

### Voter Interface
- **Voter Authentication**: Secure voter validation by ID
- **Constituency-Based Ballots**: Voters can only see candidates from their own constituency
- **Vote Confirmation**: Clear feedback on voting success
- **Duplicate Vote Prevention**: System prevents voters from voting multiple times

### Results and Analytics
- **Real-Time Results**: View up-to-date election results by candidate and party
- **Constituency Breakdown**: Results segmented by constituency
- **Statistical Analysis**: Voter turnout percentages and participation metrics
- **Party Totals**: Aggregated vote counts by political party

## Technical Details

The system is built with Java AWT for the user interface and uses object-oriented design with the following key classes:

- **VotingSystemUI**: Main application interface and controller
- **GeneralElection**: Manages election state, voters, and votes
- **ElectionCommissioner**: Administrative authority with special privileges
- **Voter**: Represents registered voters with constituency assignment
- **Candidate**: Represents election candidates with party affiliation

## Getting Started

### Prerequisites
- Java Development Kit (JDK) 8 or higher

### Installation
1. Clone this repository:
   ```
   git clone https://github.com/yourusername/voting-management-system.git
   ```
2. Navigate to the project directory:
   ```
   cd voting-management-system
   ```
3. Compile the application:
   ```
   javac Votingsystem/*.java
   ```
4. Run the application:
   ```
   java Votingsystem.VotingSystemUI
   ```

## Usage

### Admin Mode
1. Click on "Administrator Login" from the main menu
2. Use the admin panel to register voters and candidates
3. Open or close the election as needed
4. Generate reports and view audit logs

### Voter Mode
1. Click on "Voter Interface" from the main menu
2. Enter your Voter ID to authenticate
3. Select a candidate from your constituency to cast your vote
4. Receive confirmation of your vote

### View Results
1. Click on "View Election Results" from the main menu
2. View detailed vote counts by candidate, constituency, and party

## Sample Data

The system is pre-loaded with sample data including:
- 5 constituencies: North, South, East, West, and Central
- 10 candidates from 3 political parties
- 20 registered voters distributed across constituencies
- Some simulated votes to demonstrate functionality

## Screenshots

*[Add screenshots of your application here]*

## Future Enhancements

- Database integration for persistent data storage
- Enhanced security features including encryption
- Mobile application for remote voting
- Real-time result visualization with charts and graphs
- Election management across multiple simultaneous elections

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Acknowledgments

- Developed as a demonstration of Java AWT and election system design
- Inspired by real-world electronic voting systems
