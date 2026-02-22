import java.util.*;

public class OnboardingService {
    private final FakeDb db;
    private final ConsoleInput input;

    public OnboardingService(FakeDb db) {
        this.db = db;
        this.input = new ConsoleInput();
    }

    public void registerFromRawInput(String raw) {
        System.out.println("INPUT: " + raw);

        // PARSING (delegated)
        Map<String, String> data = input.parse(raw);

        // VALIDATION (isolated section)
        List<String> errors = validate(data);
        if (!errors.isEmpty()) {
            System.out.println("ERROR: cannot register");
            for (String e : errors) {
                System.out.println("- " + e);
            }
            return;
        }

        // CREATION
        String id = IdUtil.nextStudentId(db.count());
        StudentRecord rec = new StudentRecord(
                id,
                data.get("name"),
                data.get("email"),
                data.get("phone"),
                data.get("program")
        );

        // PERSISTENCE
        db.save(rec);

        // PRESENTATION
        System.out.println("OK: created student " + id);
        System.out.println("Saved. Total students: " + db.count());
        System.out.println("CONFIRMATION:");
        System.out.println(rec);
    }

    // Validation responsibility isolated 
    private List<String> validate(Map<String, String> data) {
        List<String> errors = new ArrayList<>();

        String name = data.getOrDefault("name", "");
        String email = data.getOrDefault("email", "");
        String phone = data.getOrDefault("phone", "");
        String program = data.getOrDefault("program", "");

        if (name.isBlank()) errors.add("name is required");
        if (email.isBlank() || !email.contains("@")) errors.add("email is invalid");
        if (phone.isBlank() || !phone.chars().allMatch(Character::isDigit))
            errors.add("phone is invalid");
        if (!(program.equals("CSE") || program.equals("AI") || program.equals("SWE")))
            errors.add("program is invalid");

        return errors;
    }
}