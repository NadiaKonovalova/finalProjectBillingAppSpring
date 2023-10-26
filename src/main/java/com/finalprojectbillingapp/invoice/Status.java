package com.finalprojectbillingapp.invoice;

public enum Status {
    NEW("New"),
    PAID("Paid"),
    DUE("Due"),
    OVERDUE("Overdue"),
    VOID("Cancelled"),
    UNCOLLECTIBLE("Uncollectible");

    private final String displayStatusName;
    Status(String displayStatusName){
        this.displayStatusName = displayStatusName;
    }
}
