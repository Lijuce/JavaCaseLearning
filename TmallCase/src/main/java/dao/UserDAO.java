package dao;



import bean.User;
import utlis.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs;
    /**
     * 增加用户
     * @param bean
     * @return
     */
    public boolean add(User bean){
        boolean addFlag = false;
        String sql = "insert into user values(null ,? ,?)";
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, bean.getName());
            ps.setString(2, bean.getPassword());

            int res = ps.executeUpdate();
            if (res>0){
                addFlag = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtil.close(ps, conn);
        }
        return addFlag;
    }


    /**
     * 删除用户
     * @param id
     */
    public void delete(int id) {
        String sql = "delete from User where id = " + id;

        try {

            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.execute(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtil.close(ps, conn);
        }
    }

    /**
     * 更新用户信息
     * @param bean
     */
    public void update(User bean) {

        String sql = "update user set name= ? , password = ? where id = ? ";
        try {

            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, bean.getName());
            ps.setString(2, bean.getPassword());
            ps.setInt(3, bean.getId());

            ps.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtil.close(ps, conn);
        }

    }

    /**
     * 判断用户名是否已存在
     * @param name
     * @return
     */
    public boolean isExist(String name) {
        User user = get(name);
        return user != null;
    }

    public int getTotal() {
        String sql = "select count(*) from User";
        int total = 0;
        try {

            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery(sql);
            while (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }finally {
            DBUtil.close(rs, ps, conn);
        }
        return total;
    }

    public User get(int id) {
        User bean = null;
        String sql = "select * from User where id = " + id;

        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery(sql);

            if (rs.next()) {
                bean = new User();
                String name = rs.getString("name");
                bean.setName(name);
                String password = rs.getString("password");
                bean.setPassword(password);
                bean.setId(id);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtil.close(rs, ps, conn);
        }
        return bean;
    }

    public List<User> list() {
        return list(0, Short.MAX_VALUE);
    }

    public List<User> list(int start, int count) {
        List<User> beans = new ArrayList<User>();

        String sql = "select * from User order by id desc limit ?,? ";

        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, start);
            ps.setInt(2, count);

            rs = ps.executeQuery();

            while (rs.next()) {
                User bean = new User();
                int id = rs.getInt(1);

                String name = rs.getString("name");
                bean.setName(name);
                String password = rs.getString("password");
                bean.setPassword(password);

                bean.setId(id);
                beans.add(bean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtil.close(rs, ps, conn);
        }
        return beans;
    }



    public User get(String name) {
        User bean = null;

        String sql = "select * from User where name = ?";
        try {

            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            rs = ps.executeQuery();

            if (rs.next()) {
                bean = new User();
                int id = rs.getInt("id");
                bean.setName(name);
                String password = rs.getString("password");
                bean.setPassword(password);
                bean.setId(id);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtil.close(rs, ps, conn);
        }
        return bean;
    }

    public User get(String name, String password) {
        User bean = null;

        String sql = "select * from User where name = ? and password=?";
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, password);
            rs = ps.executeQuery();

            if (rs.next()) {
                bean = new User();
                int id = rs.getInt("id");
                bean.setName(name);
                bean.setPassword(password);
                bean.setId(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtil.close(rs, ps, conn);
        }

        return bean;
    }

}