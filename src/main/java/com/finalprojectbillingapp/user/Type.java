package com.finalprojectbillingapp.user;

import lombok.Getter;

@Getter
public enum Type {
    INDIVIDUAL ("Individual or natural person"),
    SELF_EMPLOYED ("Self-employed"),
    MICRO_ENTERPRISE ("Micro-enterprise"),
    VAT_PAYER ("VAT payer"),
    OTHER ("Other");

    private final String displayTypeName;
    Type(String displayTypeName){
        this.displayTypeName = displayTypeName;
    }

}
