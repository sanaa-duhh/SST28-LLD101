public class InvoiceFormatter {

    public static String format(
            String invoiceId,
            PricingResult pricing,
            String customerType
    ) {
        StringBuilder out = new StringBuilder();

        out.append("Invoice# ").append(invoiceId).append("\n");

        for (String line : pricing.lines) {
            out.append(line).append("\n");
        }

        out.append(String.format("Subtotal: %.2f\n", pricing.subtotal));
        out.append(String.format(
                "Tax(%.0f%%): %.2f\n",
                TaxRules.taxPercent(customerType),
                pricing.tax
        ));
        out.append(String.format("Discount: -%.2f\n", pricing.discount));
        out.append(String.format("TOTAL: %.2f\n", pricing.total));

        return out.toString();
    }
}