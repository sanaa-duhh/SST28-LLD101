import java.util.*;

public class EligibilityEngine {
    private final FakeEligibilityStore store;
    private final List<EligibilityRule> rules = new ArrayList<>();

    public EligibilityEngine(FakeEligibilityStore store) {
        this.store = store;

        
        rules.add(new DisciplinaryRule());
        rules.add(new CgrRule());
        rules.add(new AttendanceRule());
        rules.add(new CreditsRule());
    }

    public void runAndPrint(StudentProfile s) {
        ReportPrinter p = new ReportPrinter();
        EligibilityEngineResult r = evaluate(s);
        p.print(s, r);
        store.save(s.rollNo, r.status);
    }

    public EligibilityEngineResult evaluate(StudentProfile s) {
        List<String> reasons = new ArrayList<>();
        String status = "ELIGIBLE";

        for (EligibilityRule rule : rules) {
            Optional<String> violation = rule.check(s);
            if (violation.isPresent()) {
                status = "NOT_ELIGIBLE";
                reasons.add(violation.get());
                break; 
            }
        }

        return new EligibilityEngineResult(status, reasons);
    }
}



interface EligibilityRule {
    Optional<String> check(StudentProfile s);
}

class DisciplinaryRule implements EligibilityRule {
    public Optional<String> check(StudentProfile s) {
        if (s.disciplinaryFlag != LegacyFlags.NONE) {
            return Optional.of("disciplinary flag present");
        }
        return Optional.empty();
    }
}

class CgrRule implements EligibilityRule {
    public Optional<String> check(StudentProfile s) {
        if (s.cgr < 8.0) {
            return Optional.of("CGR below 8.0");
        }
        return Optional.empty();
    }
}

class AttendanceRule implements EligibilityRule {
    public Optional<String> check(StudentProfile s) {
        if (s.attendancePct < 75) {
            return Optional.of("attendance below 75");
        }
        return Optional.empty();
    }
}

class CreditsRule implements EligibilityRule {
    public Optional<String> check(StudentProfile s) {
        if (s.earnedCredits < 20) {
            return Optional.of("credits below 20");
        }
        return Optional.empty();
    }
}



class EligibilityEngineResult {
    public final String status;
    public final List<String> reasons;

    public EligibilityEngineResult(String status, List<String> reasons) {
        this.status = status;
        this.reasons = reasons;
    }
}