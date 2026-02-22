public class WhatsAppSender extends NotificationSender {
    public WhatsAppSender(AuditLog audit) {
        super(audit);
    }

    @Override
    protected void doSend(Notification n) {
        // Explicit, consistent failure
        if (!n.phone.startsWith("+")) {
            audit.add("wa failed");
            throw new IllegalArgumentException(
                "phone must start with + and country code"
            );
        }

        System.out.println(
            "WA -> to=" + n.phone +
            " body=" + n.body
        );
        audit.add("wa sent");
    }
}