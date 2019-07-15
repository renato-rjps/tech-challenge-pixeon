package br.com.rjps.examapi.model;

import static javax.persistence.GenerationType.SEQUENCE;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Persistable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(of= {"id"})
@MappedSuperclass
public class BaseEntity  implements Persistable<Long>, Serializable {

	private static final long serialVersionUID = 6530015471691870459L;

	private static final String PRIMARY_KEY = "PRIMARY_KEY";

	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = PRIMARY_KEY)
	@Column(nullable = false, updatable = false)
	private Long id;

	/**
	 * Verfica se uma entidade é nova.
	 * 
	 * @return {@code true} caso a entidade seja nova e não esteja persistida no
	 *         banco.
	 */
	@Override
	@Transient
	@JsonIgnore
	public boolean isNew() {
		return null == getId();
	}

	/**
	 * Mapeamento da data de criação de um registro para auditoria
	 */
	@CreatedDate
	@Column(nullable = false, updatable = false)
	private LocalDateTime createdDate;

	/**
	 * Mapeamento da data de edição de um registro para auditoria
	 */
	@LastModifiedDate
	@Column(nullable = false)
	private LocalDateTime modifiedDate;

	/**
	 * Flag utilizada para exclusão lógica de registros do sistema
	 */
	@Column(nullable = false)
	private boolean enabled = true;
}
