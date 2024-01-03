/**
 * 
 */
package com.edenhub.gateway.service.dto;

import lombok.Data;

import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * @author admin
 *
 */
@Data
public class ProfileAccountPayload {
    private static final long serialVersionUID = -8034380177762838241L;

    private Long id;

    @Size(max = 255)
    private String name;

    @Size(max = 255)
    private String shortName;

    private Instant birthday;

    @Size(max = 15)
    private String phone;

    private String email;

    private String fullName;

    private String avatar;

    private String logo;

    private String website;

    private String description;

    private Long professionId;

    private Long literacyId;

    private String job;

    private BigDecimal salary;

    private Integer dayWorks;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public Instant getBirthday() {
        return birthday;
    }

    public void setBirthday(Instant birthday) {
        this.birthday = birthday;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getProfessionId() {
        return professionId;
    }

    public void setProfessionId(Long professionId) {
        this.professionId = professionId;
    }

    public Long getLiteracyId() {
        return literacyId;
    }

    public void setLiteracyId(Long literacyId) {
        this.literacyId = literacyId;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public Integer getDayWorks() {
        return dayWorks;
    }

    public void setDayWorks(Integer dayWorks) {
        this.dayWorks = dayWorks;
    }
}
