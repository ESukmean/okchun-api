package stream.okchun.dashboard.database.entity.media;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.type.SqlTypes;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Entity
@Table(name = "channel_settings", schema = "media")
public class ChannelSetting {
	@Id
	@Column(name = "channel_id", nullable = false)
	private Long id;

	@MapsId
	@OneToOne(fetch = FetchType.LAZY, optional = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "channel_id", nullable = false)
	private Channel channels;

	@ColumnDefault("'{}'")
	@Column(name = "allowed_countries", nullable = false)
	private List<String> allowedCountries;

	@Column(name = "viewer_limit")
	private Integer viewerLimit;

	@Column(name = "default_pool_selection", length = 16)
	private String defaultPoolSelection;

	@ColumnDefault("'{}'")
	@Column(name = "extra_settings", nullable = false)
	@JdbcTypeCode(SqlTypes.JSON)
	private Map<String, Object> extraSettings;

}