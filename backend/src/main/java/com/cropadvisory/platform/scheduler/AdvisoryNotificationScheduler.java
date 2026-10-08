package com.cropadvisory.platform.scheduler;

import com.cropadvisory.platform.model.entity.User;
import com.cropadvisory.platform.model.enums.AdviceStatus;
import com.cropadvisory.platform.model.enums.NotificationType;
import com.cropadvisory.platform.model.enums.Role;
import com.cropadvisory.platform.repository.ExpertAdviceRepository;
import com.cropadvisory.platform.repository.UserRepository;
import com.cropadvisory.platform.service.AuditLogService;
import com.cropadvisory.platform.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Scheduled job for daily advisory notifications.
 *
 * <p>Runs daily to check for open expert questions, notify experts,
 * and create audit logs.</p>
 */
@Component
public class AdvisoryNotificationScheduler {

    private static final Logger log = LoggerFactory.getLogger(AdvisoryNotificationScheduler.class);

    private final ExpertAdviceRepository adviceRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final AuditLogService auditLogService;

    public AdvisoryNotificationScheduler(ExpertAdviceRepository adviceRepository,
                                         UserRepository userRepository,
                                         NotificationService notificationService,
                                         AuditLogService auditLogService) {
        this.adviceRepository = adviceRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
        this.auditLogService = auditLogService;
    }

    @Scheduled(cron = "0 0 8 * * ?")
    public void dailyAdvisoryCheck() {
        log.info("Running daily advisory notification scheduler");

        long openQuestions = adviceRepository.countByStatus(AdviceStatus.OPEN);
        if (openQuestions > 0) {
            List<User> experts = userRepository.findAll().stream()
                    .filter(u -> u.getRole() == Role.ROLE_EXPERT)
                    .toList();

            for (User expert : experts) {
                notificationService.createNotification(expert,
                        "Open Expert Questions",
                        "There are " + openQuestions + " open questions requiring attention.",
                        NotificationType.SYSTEM);
            }
            log.info("Notified {} experts about {} open questions", experts.size(), openQuestions);
        }

        auditLogService.log(null, "SCHEDULER_RUN", "SYSTEM", null, "Daily advisory scheduler executed", null);
        log.info("Daily advisory scheduler completed");
    }
}
