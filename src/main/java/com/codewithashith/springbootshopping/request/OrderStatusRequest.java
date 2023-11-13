package com.codewithashith.springbootshopping.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderStatusRequest {
    private Long orderId;
    private Long statusId;
}
