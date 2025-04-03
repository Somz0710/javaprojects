package Votingsystem;

import java.util.List;

public interface IElectionAudit {
    void logVoterActivity(String voterId, String action);
    void logAdminActivity(String adminId, String action);
    List<String> getAuditTrail();
    void saveAuditToFile(String filePath);
}