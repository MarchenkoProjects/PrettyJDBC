package com.github.marchenkoprojects.prettyjdbc;

import com.github.marchenkoprojects.prettyjdbc.model.Film;
import com.github.marchenkoprojects.prettyjdbc.session.Session;
import com.github.marchenkoprojects.prettyjdbc.util.DatabaseInitializer;
import com.github.marchenkoprojects.prettyjdbc.util.JDBCUtils;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Connection;
import java.util.List;

/**
 * @author Oleg Marchenko
 */
public class TypedQueryTest {

    @BeforeClass
    public static void beforeTests() {
        DatabaseInitializer.createAndInitDatabase();
    }

    @Test(expected = IllegalStateException.class)
    public void testNativeTypedQueryExecutionWithoutResultMapper() {
        Connection connection = JDBCUtils.getConnection();
        try(Session session = SessionFactory.newSession(connection)) {
            session
                    .createNativeQuery("SELECT id, original_name, year FROM films WHERE id = ?", Film.class)
                    .setParameter(1, 1)
                    .unique();
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testTypedQueryExecutionWithoutResultMapper() {
        Connection connection = JDBCUtils.getConnection();
        try(Session session = SessionFactory.newSession(connection)) {
            session
                    .createQuery("SELECT * FROM films LIMIT ?", Film.class)
                    .setParameter(1, 10)
                    .list();
        }
    }

    @Test
    public void testNativeTypedQueryExecutionWithUniqueResult() {
        Connection connection = JDBCUtils.getConnection();
        try(Session session = SessionFactory.newSession(connection)) {
            Film film = session
                    .createNativeQuery("SELECT id, original_name, year FROM films WHERE id = ?", Film.class)
                    .setParameter(1, 1)
                    .setResultMapper(resultSet -> {
                        Film newFilm = new Film();
                        newFilm.setId(resultSet.getInt("id"));
                        newFilm.setOriginalName(resultSet.getString("original_name"));
                        newFilm.setYear(resultSet.getShort("year"));
                        return newFilm;
                    })
                    .unique();
            Assert.assertNotNull(film);
            Assert.assertEquals(film.getId(), 1);
            Assert.assertEquals(film.getOriginalName(), "The Lord of the Rings: The Fellowship of the Ring");
            Assert.assertEquals(film.getYear(), 2001);
        }
    }

    @Test
    public void testTypedQueryWithNamedParametersWithListOfResults() {
        Connection connection = JDBCUtils.getConnection();
        try(Session session = SessionFactory.newSession(connection)) {
            List<Film> films = session
                    .createQuery("SELECT id, original_name, year FROM films OFFSET :offset LIMIT :limit", Film.class)
                    .setParameter("offset", 0)
                    .setParameter("limit", 10)
                    .setResultMapper(resultSet -> {
                        Film newFilm = new Film();
                        newFilm.setId(resultSet.getInt("id"));
                        newFilm.setOriginalName(resultSet.getString("original_name"));
                        newFilm.setYear(resultSet.getShort("year"));
                        return newFilm;
                    })
                    .list();
            Assert.assertNotNull(films);
            Assert.assertEquals(films.size(), 3);
        }
    }

    @Test
    public void testQueryExecutionWithListOfResults() {
        Connection connection = JDBCUtils.getConnection();
        try(Session session = SessionFactory.newSession(connection)) {
            List<Film> films = session
                    .createNativeQuery("SELECT id, original_name, year FROM films", Film.class)
                    .setResultMapper(resultSet -> {
                        Film newFilm = new Film();
                        newFilm.setId(resultSet.getInt("id"));
                        newFilm.setOriginalName(resultSet.getString("original_name"));
                        newFilm.setYear(resultSet.getShort("year"));
                        return newFilm;
                    })
                    .list();
            Assert.assertNotNull(films);
            Assert.assertEquals(films.size(), 3);
        }
    }

    @AfterClass
    public static void afterTests() {
        DatabaseInitializer.destroyDatabase();
    }
}
