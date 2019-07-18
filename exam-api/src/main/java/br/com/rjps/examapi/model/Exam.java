package br.com.rjps.examapi.model;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@EntityListeners(AuditingEntityListener.class)
@SequenceGenerator(name = "PRIMARY_KEY", sequenceName = "EXAM_ID_SEQ", allocationSize = 1)
public class Exam extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@NotBlank
	@Length(min = 1, max = 255)
	@Column(nullable = false)
	private String patientName;

	@Max(150)
	@Min(1)
	private Integer patientAge;

	@Enumerated(STRING)
	@Column(length = 20)
	private Gender patientGender;

	@NotBlank
	@Length(max = 255)
	@Column(nullable = false)
	private String physicianName;

	@NotNull
	private Long physicianCrm;

	@NotBlank
	@Length(max = 255)
	@Column(nullable = false)
	private String procedureName;

	@NotNull
	@ManyToOne(fetch = LAZY, optional = false)
	@JoinColumn(updatable = false, nullable = false)
	private HealthcareInstitution healthcareInstitution;

	@JsonProperty(access = Access.READ_ONLY)
	@Column(nullable = false)
	private boolean read;
}
