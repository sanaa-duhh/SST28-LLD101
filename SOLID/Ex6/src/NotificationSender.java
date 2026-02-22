public abstract class NotificationSender {
    protected final AuditLog audit;

    protected NotificationSender(AuditLog audit) {
        this.audit = audit;
    }

    // Template method: enforces shared contract
    public final void send(Notification n) {
        if (n == null) {
            audit.add("send failed");
            throw new IllegalArgumentException("notification cannot be null");
        }

        Notification safe = normalize(n);

        doSend(safe);
    }

  
    protected abstract void doSend(Notification n);

    // Shared normalization (no subtype surprises)
    protected Notification normalize(Notification n) {
        return new Notification(
            n.subject == null ? "" : n.subject,
            n.body == null ? "" : n.body,
            n.email == null ? "" : n.email,
            n.phone == null ? "" : n.phone
        );
    }
}