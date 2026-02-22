import java.util.*;

public class CafeteriaSystem {
    private final Map<String, MenuItem> menu = new LinkedHashMap<>();
    private final FileStore store = new FileStore();
    private int invoiceSeq = 1000;

    public void addToMenu(MenuItem i) {
        menu.put(i.id, i);
    }

    // Orchestrates checkout only
    public void checkout(String customerType, List<OrderLine> lines) {
        String invId = "INV-" + (++invoiceSeq);

        PricingResult pricing = PricingEngine.calculate(
                customerType,
                lines,
                menu
        );

        String invoiceText = InvoiceFormatter.format(
                invId,
                pricing,
                customerType
        );

        System.out.print(invoiceText);

        store.save(invId, invoiceText);
        System.out.println(
                "Saved invoice: " + invId +
                " (lines=" + store.countLines(invId) + ")"
        );
    }
}



class PricingEngine {

    static PricingResult calculate(
            String customerType,
            List<OrderLine> lines,
            Map<String, MenuItem> menu
    ) {
        List<String> printedLines = new ArrayList<>();
        double subtotal = 0.0;

        for (OrderLine l : lines) {
            MenuItem item = menu.get(l.itemId);
            double lineTotal = item.price * l.qty;
            subtotal += lineTotal;
            printedLines.add(
                    String.format(
                            "- %s x%d = %.2f",
                            item.name,
                            l.qty,
                            lineTotal
                    )
            );
        }

        double tax = TaxRules.computeTax(customerType, subtotal);
        double discount = DiscountRules.discountAmount(
                customerType,
                subtotal,
                lines.size()
        );

        double total = subtotal + tax - discount;

        return new PricingResult(
                printedLines,
                subtotal,
                tax,
                discount,
                total
        );
    }
}

class PricingResult {
    final List<String> lines;
    final double subtotal;
    final double tax;
    final double discount;
    final double total;

    PricingResult(
            List<String> lines,
            double subtotal,
            double tax,
            double discount,
            double total
    ) {
        this.lines = lines;
        this.subtotal = subtotal;
        this.tax = tax;
        this.discount = discount;
        this.total = total;
    }
}