package com.novara.novara_demo.repository;

import com.novara.novara_demo.model.entity.User;
import jakarta.persistence.EntityManager;
import org.hibernate.search.engine.search.query.SearchResult;
import org.hibernate.search.mapper.orm.Search;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class UserSearchRepository extends SimpleJpaRepository<User, UUID> {

    private final EntityManager entityManager;
    public UserSearchRepository(EntityManager entityManager) {
        super(User.class, entityManager);
        this.entityManager = entityManager;
    }

    public List<User> searchByEmail(String email, int limit) {
        SearchResult<User> result = Search.session(entityManager)
                .search(User.class)
                .where(f -> f.match().field("email").matching(email).fuzzy(2))
                .fetch(limit);
        return result.hits();
    }
}
