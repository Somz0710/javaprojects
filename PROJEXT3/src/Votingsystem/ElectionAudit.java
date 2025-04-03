package Votingsystem;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ElectionAudit implements IElectionAudit {
    private List<String> auditTrail;
    private SimpleDateFormat dateFormat;

    public ElectionAudit() {
        this.auditTrail = new ArrayList<>();
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    @Override
    public void logVoterActivity(String voterId, String action) {
        String timestamp = dateFormat.format(new Date());
        auditTrail.add(timestamp + " | VOTER | " + voterId + " | " + action);
    }

    @Override
    public void logAdminActivity(String adminId, String action) {
        String timestamp = dateFormat.format(new Date());
        auditTrail.add(timestamp + " | ADMIN | " + adminId + " | " + action);
    }

    @Override
    public List<String> getAuditTrail() {
        return new ArrayList<>(auditTrail);
    }

    @Override
    public void saveAuditToFile(String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("TIMESTAMP | TYPE | ID | ACTION\n");
            writer.write("--------------------------------\n");

            for (String entry : auditTrail) {
                writer.write(entry + "\n");
            }

            System.out.println("Audit log saved to " + filePath);
        } catch (IOException e) {
            System.err.println("Error saving audit log: " + e.getMessage());
        }
    }
}

