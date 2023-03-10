package com.fullcycle.catalogo.domain.category;

import com.fullcycle.catalogo.domain.AggregateRoot;
import com.fullcycle.catalogo.domain.validation.ValidationHandler;

import java.time.Instant;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;

public class Category extends AggregateRoot<CategoryID> implements Cloneable {

    private String name;
    private String description;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    private Category(final CategoryID anId, final String aName, final String aDescription, final boolean isActive,
                     final Instant aCreatedAt, final Instant aUpdatedAt, final Instant aDeletedAt) {
        super(anId);
        this.name = aName;
        this.description = aDescription;
        this.active = isActive;
        this.createdAt = requireNonNull(aCreatedAt, "'createdAt' should not be null");
        this.updatedAt = requireNonNull(aUpdatedAt, "'updatedAt' should not be null");
        this.deletedAt = aDeletedAt;
    }

    public static Category with(final Category aCategory) {
        return with(aCategory.getId(), aCategory.name, aCategory.description, aCategory.isActive(),
                aCategory.createdAt, aCategory.updatedAt, aCategory.deletedAt);
    }

    public static Category with(final CategoryID anId, final String name, final String description, final boolean active,
                                final Instant createdAt, final Instant updatedAt, final Instant deletedAt) {
        return new Category(anId, name,
                description, active, createdAt, updatedAt, deletedAt);
    }

    @Override
    public void validate(final ValidationHandler handler) {
        new CategoryValidator(this, handler).validate();
    }

    public static Category newCategory(final String name, final String description, final boolean isActive) {
        final var id = CategoryID.unique();
        final var now = Instant.now();
        final var deletedAt = isActive ? null : Instant.now();
        return new Category(id, name, description, isActive, now, now, deletedAt);
    }

    public Category deactivate() {
        if (isNull(this.getDeletedAt())) {
            this.deletedAt = Instant.now();
        }

        this.active = false;
        this.updatedAt = Instant.now();
        return this;
    }

    public Category activate() {
        this.deletedAt = null;
        this.active = true;
        this.updatedAt = Instant.now();
        return this;
    }

    public Category update(final String aName, final String aDescription,
                           final boolean isActive) {
        if (isActive) {
            activate();
        } else {
            deactivate();
        }

        this.name = aName;
        this.description = aDescription;
        this.updatedAt = Instant.now();
        return this;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isActive() {
        return active;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    @Override
    public Category clone() {
        try {
            Category clone = (Category) super.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

}
