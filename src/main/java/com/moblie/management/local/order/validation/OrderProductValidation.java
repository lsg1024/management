package com.moblie.management.local.order.validation;

public class OrderProductValidation {

    public static void amountIsNotZeroOrNegative(int new_amount) {
        if (new_amount <= 0) {
            throw new IllegalArgumentException("수량은 1 이상이어야 합니다.");
        }
    }
}
