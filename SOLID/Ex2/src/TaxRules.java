public class TaxRules {

    public static double taxPercent(String customerType) {
        if ("student".equalsIgnoreCase(customerType)) return 5.0;
        if ("staff".equalsIgnoreCase(customerType)) return 2.0;
        return 8.0;
    }

    public static double computeTax(String customerType, double subtotal) {
        double pct = taxPercent(customerType);
        return subtotal * (pct / 100.0);
    }
}