package com.shop.bike.vm;

import com.shop.bike.entity.enumeration.DiscountType;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Instant;

@Data
public class CouponDiscountVM {
	
	private Long id;
	
	private String name;
	
	private String couponCode;
	
	private Integer quantityLimit;

	private BigDecimal discount;
	
	private DiscountType type;
	
	private BigDecimal maxDiscount;
	
	private Integer quantityLimitForUser;
	
	private Instant startDate;
	
	private Instant endDate;
}
