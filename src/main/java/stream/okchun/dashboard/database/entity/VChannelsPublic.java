package stream.okchun.dashboard.database.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

import java.time.OffsetDateTime;

/**
 * Mapping for DB view
 */
@Getter
@Setter
@Entity
@Immutable
@Table(name = "v_channels_public", schema = "public")
public class VChannelsPublic {
	@Id
	@Column(name = "id")
	private Long id;

	@Column(name = "org_id")
	private Long orgId;

	@Column(name = "name", length = Integer.MAX_VALUE)
	private String name;

	@Column(name = "created_at")
	private OffsetDateTime createdAt;

/*
 TODO [Reverse Engineering] create field to map the 'state' column
 Available actions: Define target Java type | Uncomment as is | Remove column mapping
    @Column(name = "state", columnDefinition = "channel_state_type")
    private Object state;
*/
}