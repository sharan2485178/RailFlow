package com.example.security;

import com.example.model.AuditLog;
import com.example.repository.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuditService {

    @Autowired
    private AuditLogRepository auditLogRepository;

    public void log(String action, String entityType, String entityId, String performedBy, String details) {
        AuditLog log = new AuditLog(action, entityType, entityId, performedBy, details);
        auditLogRepository.save(log);
    }

    public List<AuditLog> getAllLogs() {
        return auditLogRepository.findAll();
    }
}
