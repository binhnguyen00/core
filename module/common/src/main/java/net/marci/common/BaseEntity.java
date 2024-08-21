package net.marci.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.marci.lib.utils.DateUtils;

import java.time.LocalDateTime;
import java.util.Objects;

@MappedSuperclass
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor @Getter @Setter
abstract public class BaseEntity extends Persistable<Long> {

  @Column(name = "creator")
  protected String creator;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateUtils.timestamp)
  @Column(name = "created_time")
  protected LocalDateTime createdTime;

  @Column(name = "modifier")
  protected String modifier;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateUtils.timestamp)
  @Column(name = "modified_time")
  protected LocalDateTime modifiedTime;

  @Column(name = "storage_state")
  @Enumerated(EnumType.STRING)
  protected StorageState storageState = StorageState.ACTIVE;

  @Override
  public String toString() {
    return String.format("Entity of type %s with id: %s", this.getClass().getName(), getId());
  }

  /**
   * Update modifier, modified time.
   * If the record is new, set creator, created time.
   */
  public void setUserInteract(/* Client */) {
    // TODO: setCreator(), setModifier()
    this.setMilestone(LocalDateTime.now());
  }

  public void setMilestone(LocalDateTime time) {
    if (this.isNew() && Objects.isNull(createdTime)) setCreatedTime(time);
    setModifiedTime(time);
  }

  public enum StorageState {
    ACTIVE, ARCHIVED
  }
}