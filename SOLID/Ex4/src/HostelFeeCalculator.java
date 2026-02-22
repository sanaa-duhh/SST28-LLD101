import java.util.*;

public class HostelFeeCalculator {
    private final FakeBookingRepo repo;
    private final List<PricingComponent> pricingComponents = new ArrayList<>();

    public HostelFeeCalculator(FakeBookingRepo repo) {
        this.repo = repo;

        // Room pricing
        pricingComponents.add(new RoomPricing());

        // Add-on pricing
        pricingComponents.add(new AddOnPricing());
    }

    public void process(BookingRequest req) {
        Money monthly = Money.zero();

        for (PricingComponent pc : pricingComponents) {
            monthly = monthly.plus(pc.monthlyCharge(req));
        }

        Money deposit = new Money(5000.00);

        ReceiptPrinter.print(req, monthly, deposit);

        String bookingId =
                "H-" + (7000 + new Random(1).nextInt(1000)); 
        repo.save(bookingId, req, monthly, deposit);
    }
}



interface PricingComponent {
    Money monthlyCharge(BookingRequest req);
}

class RoomPricing implements PricingComponent {
    public Money monthlyCharge(BookingRequest req) {
        return switch (req.roomType) {
            case LegacyRoomTypes.SINGLE -> new Money(14000.0);
            case LegacyRoomTypes.DOUBLE -> new Money(15000.0);
            case LegacyRoomTypes.TRIPLE -> new Money(12000.0);
            default -> new Money(16000.0); // DELUXE
        };
    }
}

class AddOnPricing implements PricingComponent {
    public Money monthlyCharge(BookingRequest req) {
        Money total = Money.zero();

        for (AddOn a : req.addOns) {
            total = total.plus(addOnPrice(a));
        }
        return total;
    }

    private Money addOnPrice(AddOn a) {
        return switch (a) {
            case MESS -> new Money(1000.0);
            case LAUNDRY -> new Money(500.0);
            case GYM -> new Money(300.0);
        };
    }
}