package Votingsystem;
public class Voter {
    private String voterId;
    private String name;
    private int age;
    private boolean hasVoted;
    private String constituency;

    public Voter(String voterId, String name, int age, String constituency) {
        this.voterId = voterId;
        this.name = name;
        this.age = age;
        this.hasVoted = false;
        this.constituency = constituency;
    }

    public String getVoterId() {
        return voterId;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public boolean hasVoted() {
        return hasVoted;
    }

    public void setVoted(boolean hasVoted) {
        this.hasVoted = hasVoted;
    }

    public String getConstituency() {
        return constituency;
    }

    @Override
    public String toString() {
        return "Voter{" +
                "voterId='" + voterId + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", hasVoted=" + hasVoted +
                ", constituency='" + constituency + '\'' +
                '}';
    }
}
