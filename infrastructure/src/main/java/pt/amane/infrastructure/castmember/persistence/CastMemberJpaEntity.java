package pt.amane.infrastructure.castmember.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import pt.amane.domain.castmember.CastMember;
import pt.amane.domain.castmember.CastMemberType;
import pt.amane.domain.castmember.CastMemberID;

@Entity(name = "CastMember")
@Table(name = "cast_members")
public class CastMemberJpaEntity {

  @Id
  @Column(name = "id", length = 32, columnDefinition = "CHAR(32)")
  private String id;

  @Column(name = "name", nullable = false)
  private String name;

  @Enumerated(EnumType.STRING)
  @Column(name = "type", nullable = false)
  private CastMemberType type;

  @Column(name = "created_at", nullable = false, columnDefinition = "DATETIME(6)")
  private Instant createdAt;

  @Column(name = "updated_at", nullable = false, columnDefinition = "DATETIME(6)")
  private Instant updatedAt;

  public CastMemberJpaEntity() {
  }

  public CastMemberJpaEntity(
      final String id,
      final String name,
      final CastMemberType type,
      final Instant createdAt,
      final Instant updatedAt
  ) {
    this.id = id;
    this.name = name;
    this.type = type;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }

  /**
   * Convert CastMember to CastMemberJpaEntity.
   * @param aMember
   * @return
   */
  public static CastMemberJpaEntity from(final CastMember aMember) {
    return new CastMemberJpaEntity(
        aMember.getId().getValue(),
        aMember.getName(),
        aMember.getType(),
        aMember.getCreatedAt(),
        aMember.getUpdatedAt()
    );
  }

  /**
   * Convert CastMemberJpaEntity to CastMember.
   * @return
   */
  public CastMember toAggregate() {
    return CastMember.with(
        CastMemberID.from(getId()),
        getName(),
        getType(),
        getCreatedAt(),
        getUpdatedAt()
    );
  }

  public String getId() {
    return id;
  }

  public CastMemberJpaEntity setId(final String id) {
    this.id = id;
    return this;
  }

  public String getName() {
    return name;
  }

  public CastMemberJpaEntity setName(final String name) {
    this.name = name;
    return this;
  }

  public CastMemberType getType() {
    return type;
  }

  public CastMemberJpaEntity setType(final CastMemberType type) {
    this.type = type;
    return this;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public CastMemberJpaEntity setCreatedAt(final Instant createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  public Instant getUpdatedAt() {
    return updatedAt;
  }

  public CastMemberJpaEntity setUpdatedAt(final Instant updatedAt) {
    this.updatedAt = updatedAt;
    return this;
  }
}
