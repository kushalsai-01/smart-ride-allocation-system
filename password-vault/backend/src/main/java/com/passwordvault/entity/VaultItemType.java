package com.passwordvault.entity;

/**
 * Enum representing different types of vault items
 */
public enum VaultItemType {
    PASSWORD("password"),
    NOTE("note"),
    CREDIT_CARD("credit_card"),
    IDENTITY("identity"),
    SECURE_NOTE("secure_note");
    
    private final String value;
    
    VaultItemType(String value) {
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }
    
    @Override
    public String toString() {
        return value;
    }
}