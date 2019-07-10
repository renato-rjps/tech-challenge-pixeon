package br.com.rjps.pixeon.entities;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@SequenceGenerator(name = "PRIMARY_KEY", sequenceName = "EXAM_ID_SEQ", allocationSize = 1)
public class Exam extends BaseEntity {

	@NotBlank
	@Size(max = 255)
	@Column(nullable = false)
	private String patientName;

	@Max(150)
	@Min(1)
	private Integer patientAge;

	@Size(max = 20)
	@Enumerated(STRING)
	@Column(length = 20)
	private Gender patientGender;

	@NotBlank
	@Size(max = 255)
	@Column(nullable = false)
	private String physicianName;

	@NotNull
	private Long physicianCrm;

	@NotBlank
	@Size(max = 255)
	@Column(nullable = false)
	private String procedureName;

	@ManyToOne(fetch = LAZY, optional = false)
	private HealthcareInstitution healthcareInstitution;
}
