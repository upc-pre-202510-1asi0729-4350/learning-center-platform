package com.acme.center.platform.profiles.domain.model.aggregates;

import com.acme.center.platform.profiles.domain.model.commands.CreateProfileCommand;
import com.acme.center.platform.profiles.domain.model.valueobjects.EmailAddress;
import com.acme.center.platform.profiles.domain.model.valueobjects.PersonName;
import com.acme.center.platform.profiles.domain.model.valueobjects.StreetAddress;
import com.acme.center.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;

@Entity
public class Profile extends AuditableAbstractAggregateRoot<Profile> {
    @Embedded
    private PersonName name;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "address", column = @Column(name = "email_address"))})
    private EmailAddress emailAddress;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "street", column = @Column(name = "street_address_street")),
            @AttributeOverride(name = "number", column = @Column(name = "street_address_number")),
            @AttributeOverride(name = "city", column = @Column(name = "street_address_city")),
            @AttributeOverride(name = "postalCode", column = @Column(name = "street_address_postal_code")),
            @AttributeOverride(name = "country", column = @Column(name = "street_address_country"))})
    private StreetAddress streetAddress;

    public Profile(String firstName, String lastName, String email, String street, String number, String city, String state, String postalCode, String country) {
        this.name = new PersonName(firstName, lastName);
        this.emailAddress = new EmailAddress(email);
        this.streetAddress = new StreetAddress(street, number, city, state, postalCode, country);
    }

    public Profile() {
        // Default constructor for JPA
    }

    public Profile(CreateProfileCommand command) {
        this(
                command.firstName(),
                command.lastName(),
                command.email(),
                command.street(),
                command.number(),
                command.city(),
                command.state(),
                command.postalCode(),
                command.country()
        );
    }

    public String getFullName() {
        return name.getFullName();
    }

    public String getEmailAddress() {
        return emailAddress.address();
    }

    public String getStreetAddress() {
        return streetAddress.getStreetAddress();
    }

    public void updateName(String firstName, String lastName) {
      this.name = new PersonName(firstName, lastName);
    }

    public void updateEmailAddress(String email) {
        this.emailAddress = new EmailAddress(email);
    }

    public void updateStreetAddress(String street, String number, String city, String state, String postalCode, String country) {
        this.streetAddress = new StreetAddress(street, number, city, state, postalCode, country);
    }
}