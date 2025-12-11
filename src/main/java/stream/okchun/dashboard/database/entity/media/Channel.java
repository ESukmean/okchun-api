package stream.okchun.dashboard.database.entity.media;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.dialect.type.PostgreSQLEnumJdbcType;
import stream.okchun.dashboard.database.entity.org.Organization;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "channels", schema = "media", indexes = {
		@Index(name = "idx_channels_org_name", columnList = "org_id, name"),
		@Index(name = "idx_channels_org_state", columnList = "org_id, state")
})
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Channel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "org_id", nullable = false)
	private Organization org;

	@Column(name = "name", nullable = false, length = Integer.MAX_VALUE)
	private String name;

	@Column(name = "description", length = Integer.MAX_VALUE)
	private String description;

	@ColumnDefault("'{}'")
	@Column(name = "tags", nullable = false)
	private List<String> tags;

	@ColumnDefault("'ACTIVE'")
	@Column(name = "state", columnDefinition = "channel_state_type not null")
	@Enumerated
	@JdbcType(PostgreSQLEnumJdbcType.class)
	private ChannelStateType state;

	@Column(name = "archived_at")
	private OffsetDateTime archivedAt;

	@ColumnDefault("now()")
	@Column(name = "created_at", nullable = false)
	private OffsetDateTime createdAt;
	@ColumnDefault("now()")
	@Column(name = "updated_at", nullable = false)
	private OffsetDateTime updatedAt;

	@JoinColumn(name = "latest_session", nullable = true)
	@OneToOne
	private ChannelSession latest_session;

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