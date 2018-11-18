package com.bisol.calcard.domain;


import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.bisol.calcard.domain.enumeration.CreditProposalStatus;
import com.bisol.calcard.domain.enumeration.FederationUnit;
import com.bisol.calcard.domain.enumeration.Gender;
import com.bisol.calcard.domain.enumeration.MaritalStatus;
import com.bisol.calcard.domain.enumeration.RejectionReason;

/**
 * A CreditProposal.
 */
@Entity
@Table(name = "credit_proposal")
public class CreditProposal implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "client_name", nullable = false)
    private String clientName;

    @NotNull
    @Min(value = 15)
    @Column(name = "client_age", nullable = false)
    private Integer clientAge;

    @NotNull
    @Column(name = "taxpayer_id", nullable = false)
    private String taxpayerId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "client_gender", nullable = false)
    private Gender clientGender;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "marital_status", nullable = false)
    private MaritalStatus maritalStatus;

    @NotNull
    @Min(value = 0)
    @Column(name = "dependents", nullable = false)
    private Integer dependents;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "income", precision = 10, scale = 2, nullable = false)
    private BigDecimal income;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "federation_unit", nullable = false)
    private FederationUnit federationUnit;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
	private CreditProposalStatus status = CreditProposalStatus.PROCESSING;

    @NotNull
    @Column(name = "creation_date", nullable = false)
	private LocalDate creationDate = LocalDate.now();

    @Column(name = "processing_date")
    private LocalDate processingDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "rejection_reason")
    private RejectionReason rejectionReason;

    @Column(name = "aproved_min", precision = 10, scale = 2)
    private BigDecimal aprovedMin;

    @Column(name = "aproved_max", precision = 10, scale = 2)
    private BigDecimal aprovedMax;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClientName() {
        return clientName;
    }

    public CreditProposal clientName(String clientName) {
        this.clientName = clientName;
        return this;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public Integer getClientAge() {
        return clientAge;
    }

    public CreditProposal clientAge(Integer clientAge) {
        this.clientAge = clientAge;
        return this;
    }

    public void setClientAge(Integer clientAge) {
        this.clientAge = clientAge;
    }

    public String getTaxpayerId() {
        return taxpayerId;
    }

    public CreditProposal taxpayerId(String taxpayerId) {
        this.taxpayerId = taxpayerId;
        return this;
    }

    public void setTaxpayerId(String taxpayerId) {
        this.taxpayerId = taxpayerId;
    }

    public Gender getClientGender() {
        return clientGender;
    }

    public CreditProposal clientGender(Gender clientGender) {
        this.clientGender = clientGender;
        return this;
    }

    public void setClientGender(Gender clientGender) {
        this.clientGender = clientGender;
    }

    public MaritalStatus getMaritalStatus() {
        return maritalStatus;
    }

    public CreditProposal maritalStatus(MaritalStatus maritalStatus) {
        this.maritalStatus = maritalStatus;
        return this;
    }

    public void setMaritalStatus(MaritalStatus maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public Integer getDependents() {
        return dependents;
    }

    public CreditProposal dependents(Integer dependents) {
        this.dependents = dependents;
        return this;
    }

    public void setDependents(Integer dependents) {
        this.dependents = dependents;
    }

    public BigDecimal getIncome() {
        return income;
    }

    public CreditProposal income(BigDecimal income) {
        this.income = income;
        return this;
    }

    public void setIncome(BigDecimal income) {
        this.income = income;
    }

    public FederationUnit getFederationUnit() {
        return federationUnit;
    }

    public CreditProposal federationUnit(FederationUnit federationUnit) {
        this.federationUnit = federationUnit;
        return this;
    }

    public void setFederationUnit(FederationUnit federationUnit) {
        this.federationUnit = federationUnit;
    }

    public CreditProposalStatus getStatus() {
        return status;
    }

    public CreditProposal status(CreditProposalStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(CreditProposalStatus status) {
        this.status = status;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public CreditProposal creationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDate getProcessingDate() {
        return processingDate;
    }

    public CreditProposal processingDate(LocalDate processingDate) {
        this.processingDate = processingDate;
        return this;
    }

    public void setProcessingDate(LocalDate processingDate) {
        this.processingDate = processingDate;
    }

    public RejectionReason getRejectionReason() {
        return rejectionReason;
    }

    public CreditProposal rejectionReason(RejectionReason rejectionReason) {
        this.rejectionReason = rejectionReason;
        return this;
    }

    public void setRejectionReason(RejectionReason rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public BigDecimal getAprovedMin() {
        return aprovedMin;
    }

    public CreditProposal aprovedMin(BigDecimal aprovedMin) {
        this.aprovedMin = aprovedMin;
        return this;
    }

    public void setAprovedMin(BigDecimal aprovedMin) {
        this.aprovedMin = aprovedMin;
    }

    public BigDecimal getAprovedMax() {
        return aprovedMax;
    }

    public CreditProposal aprovedMax(BigDecimal aprovedMax) {
        this.aprovedMax = aprovedMax;
        return this;
    }

    public void setAprovedMax(BigDecimal aprovedMax) {
        this.aprovedMax = aprovedMax;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CreditProposal creditProposal = (CreditProposal) o;
        if (creditProposal.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), creditProposal.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CreditProposal{" +
            "id=" + getId() +
            ", clientName='" + getClientName() + "'" +
            ", clientAge=" + getClientAge() +
            ", taxpayerId='" + getTaxpayerId() + "'" +
            ", clientGender='" + getClientGender() + "'" +
            ", maritalStatus='" + getMaritalStatus() + "'" +
            ", dependents=" + getDependents() +
            ", income=" + getIncome() +
            ", federationUnit='" + getFederationUnit() + "'" +
            ", status='" + getStatus() + "'" +
            ", creationDate='" + getCreationDate() + "'" +
            ", processingDate='" + getProcessingDate() + "'" +
            ", rejectionReason='" + getRejectionReason() + "'" +
            ", aprovedMin=" + getAprovedMin() +
            ", aprovedMax=" + getAprovedMax() +
            "}";
    }
}
