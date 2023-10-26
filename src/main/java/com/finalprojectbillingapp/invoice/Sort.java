package com.finalprojectbillingapp.invoice;

public enum Sort {
    CREATED_AT ("Created at"),
    ISSUED_AT ("Issued on"),
    DUE_BY ("Due by"),
    SELLER ("Seller"),
    BUYER ("Buyer"),
    PRODUCTS ("Product"),
    TOTAL ("Total"),
    CURRENCY ("Currency"),
    STATUS ("Status");
    private final String sortBy;
    Sort (String sortBy){
        this.sortBy = sortBy;
    }
}
