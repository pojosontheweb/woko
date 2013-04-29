package woko.hibernate.search;

import org.hibernate.Transaction;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import woko.hibernate.HibernateStore;
import woko.persistence.ResultIterator;

import java.util.List;

public class HibernateSearchStore extends HibernateStore {

    public HibernateSearchStore(List<String> packageNames) {
        super(packageNames);
    }

    public ResultIterator<?> search(final Object query, Integer start, Integer limit) {
        logger.debug("Executing query : " + query + ", start=" + start + ", limit=" + limit);
        FullTextSession fts = Search.getFullTextSession(getSession());


//        QueryBuilder qb = fts.getSearchFactory().buildQueryBuilder().forEntity(entityType).get();
//        org.apache.lucene.search.Query query = qb
//                .keyword()
//                .onFields("title", "subtitle", "authors.name", "publicationDate")
//                .matching("Java rocks!")
//                .createQuery();
//
//
//        // wrap Lucene query in a org.hibernate.Query
//        org.hibernate.Query hibQuery =
//                fullTextSession.createFullTextQuery(query, Book.class);
//
//        // execute search
//        List result = hibQuery.list();

        return null;

    }


}
