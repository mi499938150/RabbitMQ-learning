package com.mi.send.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author : Rong
 * @date : 2021/5/1
 * @Desc:
 */
@Getter
@Setter
@ToString
public class OrderMessageDTO implements Serializable{
    private Integer orderId;
    private BigDecimal price;
    private Integer productId;
    // 订单状态 0：未支付，1：已支付，2：订单已取消
    private Integer orderStatus;

}