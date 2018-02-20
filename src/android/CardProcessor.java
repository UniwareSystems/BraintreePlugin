package com.uniware;

import com.braintreepayments.api.BraintreeFragment;
import com.braintreepayments.api.Card;
import com.braintreepayments.api.models.CardBuilder;

public class CardProcessor {

    private final BraintreeFragment _braintreeFragment;

    public CardProcessor(BraintreeFragment bf) {
        _braintreeFragment = bf;
    }

    public void tokeniseCard(String cardNumber, String expiry, String cvv) {
        CardBuilder cb = new CardBuilder()
                .cardNumber(cardNumber)
                .expirationDate(expiry)
                .cvv(cvv);

        Card.tokenize(_braintreeFragment, cb);
    }
}
