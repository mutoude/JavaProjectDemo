package com.cxl.dao;

import com.cxl.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class UserDao {

    private JdbcTemplate jdbcTemplate;

    private static final String MATCH_COUNT_SQL = "SELECT count(*) FROM " + "t_user WHERE user_name =? and password=? ";

    private static final String UPDATE_LOGIN_INFO_SQL = "UPDATE t_user SET " + " last_visit=?,last_ip=?credits=? WHERE user_id=? ";

    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int getMatchCount(String userName, String password) {
        return jdbcTemplate.queryForObject(MATCH_COUNT_SQL, new Object[]{userName, password}, Integer.class);
    }


    public User findUserByUserName(String userName) {
        final User user = new User();
        jdbcTemplate.query(MATCH_COUNT_SQL, new Object[]{userName}, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet resultSet) throws SQLException {
                user.setUserId(resultSet.getInt("user_id"));
                user.setUserName(userName);
                user.setCredits(resultSet.getInt("credits"));
            }
        });
        return user;
    }

    public void updateUserLoginInfo(User user) {
        jdbcTemplate.update(UPDATE_LOGIN_INFO_SQL, new Object[]{user.getLastVisit()}, user.getLastIp(), user.getCredits(), user.getUserId());
    }
}
