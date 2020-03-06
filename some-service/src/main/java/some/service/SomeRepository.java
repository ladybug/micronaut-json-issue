package some.service;

import io.micronaut.configuration.hibernate.jpa.scope.CurrentSession;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;
import io.micronaut.spring.tx.annotation.Transactional;
import org.springframework.context.annotation.ComponentScan;

import javax.inject.Inject;
import javax.persistence.EntityManager;

@ComponentScan
@Repository
public abstract class SomeRepository implements JpaRepository<Something, Long> {

    @Inject
    private EntityManager entityManager;

    public SomeRepository(@CurrentSession EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public Something mergeAndSave(Something something) {
        something = entityManager.merge(something);
        return save(something);
    }
}
