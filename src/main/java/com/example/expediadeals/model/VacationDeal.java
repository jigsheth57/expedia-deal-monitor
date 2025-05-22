package com.example.expediadeals.model;

// Using a record for simplicity and immutability
public record VacationDeal(
    String title,
    String price,
    String dates, // Or make this more structured (e.g., LocalDate)
    String origin,
    String detailsUrl
) {
    @Override
    public String toString() {
        return String.format("Deal: %s | Price: %s | Dates: %s | From: %s | URL: %s",
                             title, price, dates, origin, detailsUrl);
    }
}
