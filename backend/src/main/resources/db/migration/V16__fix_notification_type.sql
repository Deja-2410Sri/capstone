ALTER TABLE notifications
MODIFY COLUMN notification_type ENUM(
    'new_expert_question',
    'expert_answered',
    'weather_alert',
    'new_recommendation',
    'system'
);