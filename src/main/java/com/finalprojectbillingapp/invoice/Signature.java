package com.finalprojectbillingapp.invoice;

import lombok.Getter;

@Getter
public enum Signature {

    PRINT_OUT ("Signed by hand"),
    ELECTRONIC_SIGNATURE ("Electronic signature"),
    DRAFTED_ELECTRONICALLY_NO_SIGNATURE ("Drafted electronically, valid without signature"),
    NO_SIGNATURE ("No signature");

    private final String displaySignatureName;
    Signature(String displaySignatureName){
        this.displaySignatureName = displaySignatureName;
    }

}
