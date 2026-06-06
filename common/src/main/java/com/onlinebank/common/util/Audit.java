package com.onlinebank.common.util;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Abstract base class for audit fields in MongoDB documents.
 * Automatically tracks creation and modification information.
 *
 * @author OnlineBank Team
 * @version 1.0
 */
public abstract class Audit {

    @CreatedBy
    @Field("created_by")
    @JsonProperty("created_by")
    private String createdBy;

    @CreatedDate
    @Field("created_at")
    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @LastModifiedBy
    @Field("updated_by")
    @JsonProperty("updated_by")
    private String updatedBy;

    @LastModifiedDate
    @Field("updated_at")
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    // ==================== CONSTRUCTORS ====================

    protected Audit() {
        // Default constructor for Spring Data MongoDB
    }

    protected Audit(String createdBy) {
        this.createdBy = createdBy;
        this.createdAt = LocalDateTime.now();
    }

    // ==================== GETTERS ====================

    /**
     * Get the username who created the entity
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * Get the creation timestamp
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Get the username who last modified the entity
     */
    public String getUpdatedBy() {
        return updatedBy;
    }

    /**
     * Get the last modification timestamp
     */
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    // ==================== SETTERS (for manual updates if needed) ====================

    /**
     * Set the creator username (usually auto-populated by auditing)
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * Set the creation timestamp (usually auto-populated by auditing)
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Set the last modifier username (usually auto-populated by auditing)
     */
    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    /**
     * Set the last modification timestamp (usually auto-populated by auditing)
     */
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // ==================== HELPER METHODS ====================

    /**
     * Check if the entity was created by a specific user
     */
    public boolean isCreatedBy(String username) {
        return username != null && username.equals(this.createdBy);
    }

    /**
     * Check if the entity was modified after a specific date
     */
    public boolean wasModifiedAfter(LocalDateTime dateTime) {
        return this.updatedAt != null && dateTime != null && this.updatedAt.isAfter(dateTime);
    }

    /**
     * Check if the entity was created before a specific date
     */
    public boolean wasCreatedBefore(LocalDateTime dateTime) {
        return this.createdAt != null && dateTime != null && this.createdAt.isBefore(dateTime);
    }

    @Override
    public String toString() {
        return String.format("Audit{createdBy='%s', createdAt=%s, updatedBy='%s', updatedAt=%s}",
                createdBy, createdAt, updatedBy, updatedAt);
    }
}