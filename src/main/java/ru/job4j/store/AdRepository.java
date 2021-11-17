package ru.job4j.store;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.job4j.model.Brand;
import ru.job4j.model.Post;

import java.util.Date;
import java.util.List;
import java.util.function.Function;

public class AdRepository {
    private static final StandardServiceRegistry REGISTRY = new StandardServiceRegistryBuilder().configure().build();
    private static final SessionFactory SF = new MetadataSources(REGISTRY).buildMetadata().buildSessionFactory();

    private AdRepository() {
    }

    private static final class Lazy {
        private static final AdRepository INST = new AdRepository();
    }

    public static AdRepository instOf() {
        return Lazy.INST;
    }

    public List<Post> getPostsByBrand(Brand brand) {
        return tx(session -> session
                .createQuery("from Post where brand = :brand")
                .setParameter("brand", brand).getResultList()
        );
    }

    public List<Post> getPostsWithPhoto() {
        return tx(session -> session
                .createQuery("from Post p where p.images is not empty")
                .getResultList()
        );
    }

    public List<Post> getPostsByCurrentDay(Date prevDate, Date currDate) {
        return tx(session -> session
                .createQuery("from Post where created between :prevDate and :currDate")
                .setParameter("prevDate", prevDate)
                .setParameter("currDate", currDate)
                .getResultList()
        );
    }

    private <T> T tx(final Function<Session, T> command) {
        final Session session = SF.openSession();
        final Transaction tx = session.beginTransaction();
        try {
            T rsl = command.apply(session);
            tx.commit();
            return rsl;
        } catch (final Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }
}
