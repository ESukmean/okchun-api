package stream.okchun.dashboard.database.entity.infra;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Remove;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.dialect.type.PostgreSQLEnumJdbcType;
import org.hibernate.type.SqlTypes;
import stream.okchun.dashboard.database.entity.org.Organization;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Map;

@Getter
@Setter
@Entity
@Table(name = "pools", schema = "infra", indexes = {
		@Index(name = "idx_pools_owner", columnList = "owner_org_id"),
		@Index(name = "idx_pools_region_visibility", columnList = "region_code, visibility")
})
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pool {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "owner_org_id")
	private Organization ownerOrg;

	@Column(name = "name", nullable = false, length = Integer.MAX_VALUE)
	private String name;

	@Column(name = "region_code", nullable = false, length = 32)
	private String regionCode;

	@Column(name = "max_bandwidth_mbps", nullable = false, precision = 20, scale = 4)
	private BigDecimal maxBandwidthMbps;

	@Column(name = "shared_bandwidth_mbps", precision = 20, scale = 4)
	private BigDecimal sharedBandwidthMbps;

	@Column(name = "price_per_gb_credit", precision = 20, scale = 6)
	private BigDecimal pricePerGbCredit;

	@ColumnDefault("now()")
	@Column(name = "created_at", nullable = false)
	private OffsetDateTime createdAt;

	@ColumnDefault("'{}'")
	@Column(name = "metadata", nullable = false)
	@JdbcTypeCode(SqlTypes.JSON)
	private Map<String, Object> metadata;
	@ColumnDefault("now()")
	@Column(name = "updated_at", nullable = false)
	private OffsetDateTime updatedAt;

	@Column(name = "deleted_at", nullable = false)
	private OffsetDateTime deletedAt;


	@Column(name = "visibility", columnDefinition = "pool_visibility_type not null")
	@Enumerated
	@JdbcType(PostgreSQLEnumJdbcType.class)
	private PoolVisibility visibility;

	/*
	 TODO [Reverse Engineering] create field to map the 'status' column
	 Available actions: Define target Java type | Uncomment as is | Remove column mapping
	    @ColumnDefault("'ACTIVE'")
	    @Column(name = "status", columnDefinition = "pool_status_type not null")
	    private Object status;
	*/
	@PrePersist
	public void prePersist() {
		this.createdAt = OffsetDateTime.now();
		this.updatedAt = OffsetDateTime.now();
	}

	@PreUpdate
	public void preUpdate() {
		this.updatedAt = OffsetDateTime.now();
	}
}