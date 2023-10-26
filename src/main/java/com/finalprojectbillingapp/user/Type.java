package com.finalprojectbillingapp.user;

import lombok.Getter;
@Getter
public enum Type {
    INDIVIDUAL ("Individual / Natural Person"),
    SELF_EMPLOYED ("Self-Employed"),
    MICRO_ENTERPRISE ("Micro-Enterprise"),
    VAT_PAYER ("VAT Payer");

    private final String displayTypeName;
    Type(String displayTypeName){
        this.displayTypeName = displayTypeName;
    }
}
