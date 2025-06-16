package com.acme.center.platform.learning.interfaces.rest.resources;

public record CreateStudentResource(
        String firstName,
        String lastName,
        String email,
        String street,
        String number,
        String city,
        String postalCode,
        String country
) {
    public CreateStudentResource {
        if (firstName == null || firstName.isBlank()) throw new IllegalArgumentException("First name cannot be null or blank");
        if (lastName == null || lastName.isBlank()) throw new IllegalArgumentException("Last name cannot be null or blank");
        if (email == null || email.isBlank()) throw new IllegalArgumentException("Email cannot be null or blank");
        if (street == null || street.isBlank()) throw new IllegalArgumentException("Street cannot be null or blank");
        if (city == null || city.isBlank()) throw new IllegalArgumentException("City cannot be null or blank");
        if (postalCode == null || postalCode.isBlank()) throw new IllegalArgumentException("Postal code cannot be null or blank");
        if (country == null || country.isBlank()) throw new IllegalArgumentException("Country cannot be null or blank");
    }
}
