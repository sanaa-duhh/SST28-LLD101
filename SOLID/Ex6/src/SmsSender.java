public class SmsSender extends NotificationSender {
    public SmsSender(AuditLog audit) {
        super(audit);
    }

    @Override
    protected void doSend(Notification n) {
        // SMS officially ignores subject 
        System.out.println(
            "SMS -> to=" + n.phone +
            " body=" + n.body
        );
        audit.add("sms sent");
    }
}