/**
 * 
 */
package com.shop.bike.service.dto;

import com.shop.bike.entity.enumeration.ActionStatus;
import com.shop.bike.entity.enumeration.MediaType;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class MediaDTO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1877786360018054664L;
	
	private Long id;
	
	private String name;
	
	private String path;
	
	private ActionStatus status;
	
	private MediaType mediaType;
	
	private String extension;
	
	private String originalName;
	
	private BigDecimal fileSize;
	
}
