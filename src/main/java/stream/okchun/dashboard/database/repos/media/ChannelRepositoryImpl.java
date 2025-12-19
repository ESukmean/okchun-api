package stream.okchun.dashboard.database.repos.media;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.annotation.Nullable;
import jakarta.persistence.EntityManager;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import stream.okchun.dashboard.database.entity.media.Channel;
import stream.okchun.dashboard.database.entity.media.ChannelStateType;
import stream.okchun.dashboard.database.entity.media.QChannel;

import java.util.List;

@Repository
public class ChannelRepositoryImpl implements ChannelRepositoryCustom {
	private final EntityManager em;
	private final JPAQueryFactory queryFactory;

	@Autowired
	public ChannelRepositoryImpl(EntityManager entityManager) {
		this.em = entityManager;
		this.queryFactory = new JPAQueryFactory(entityManager);
	}

	@Override
	public List<Channel> searchAllByOrg(@NonNull Long org_id, @Nullable String name,
										@Nullable ChannelStateType state, @Nullable Integer cursor) {
		QChannel channel = QChannel.channel;
		var query = queryFactory.selectFrom(channel)
				.where(
						channel.org.id.eq(org_id),
						name == null ? null : channel.name.contains(name),
						state == null ? null : channel.state.eq(state)
				);
		if (cursor != null) {
			query = query.offset(cursor * 50).limit(50);
		}

		return query.fetch();
	}
}
