package net.binhnguyen.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@MappedSuperclass
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor @Getter @Setter
abstract public class Persistable<PK extends Serializable>
  implements org.springframework.data.domain.Persistable<PK>, Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  protected PK id;

  @Override
  public boolean isNew() {
    return null == getId();
  }
}