package com.edenhub.gateway.service.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * A DTO for the {@link com.edenhub.service.domain.AccountLink} entity.
 */
public class AccountLinkDTO implements Serializable {
    
	private static final long serialVersionUID = 641091558192599141L;
	
    public AccountLinkDTO() {
		super();
	}

	public AccountLinkDTO(@NotNull Long userId, @NotNull @Size(max = 255) String userName, @Size(max = 255) String fullname, String note) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.fullname = fullname;
		this.note = note;
	}

	@NotNull
	private Long userId;
	
	@NotNull
    @Size(max = 255)
    private String userName;
    
    @Size(max = 255)
    private String fullname;

    private String note;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@Override
	public String toString() {
		return "AccountLinkDTO [userId=" + userId + ", userName=" + userName + ", fullname=" + fullname + ", note="
				+ note + "]";
	}

}
