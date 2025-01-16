package com.moblie.management.local.order.validation;

import com.moblie.management.global.exception.CustomException;
import com.moblie.management.global.exception.ErrorCode;

public class OrderProductCartValidation {

    public static void productsIsNotZeroOrNegative(int products) {
        if (products <= 0) {
            throw new CustomException(ErrorCode.ERROR_400, "장바구니 상품 수량을 확인해주세요.");
        }
    }

}
