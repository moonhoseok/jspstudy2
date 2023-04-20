package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import model.mapper.MemberMapper;

public class MemberMybatisDao {
	private Class<MemberMapper> cls = MemberMapper.class;
	private Map<String,Object> map = new HashMap<>();
	
	public boolean insert(Member mem) {
		SqlSession session = MybatisConnection.getConnection();
		try {
			int cnt = session.getMapper(cls).insert(mem);
			if( cnt > 0) {
				return true;
			}else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			MybatisConnection.close(session);
		}
		return false;
	}
	public Member selectOne(String id) {
		SqlSession session = MybatisConnection.getConnection();
		try {
			return session.getMapper(cls).selectOne(id);
		
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			MybatisConnection.close(session);
		}
		return null;
	}
	public boolean update(Member mem) {
		SqlSession session = MybatisConnection.getConnection();
		try {
			int cnt = session.getMapper(cls).update(mem);
			if( cnt > 0) return true;
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			MybatisConnection.close(session);
		}
		return false;
	}
	public List<Member> list(){
		SqlSession session = MybatisConnection.getConnection();
		try {
			return session.getMapper(cls).list(null);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			MybatisConnection.close(session);
		}
		return null;
	}
	//http://localhost:8080/jspstudy1/model1/member/delete.jsp?id=test2&pass=1111
	public boolean delete(String id) {
		SqlSession session = MybatisConnection.getConnection();
		try {
			int cnt = session.getMapper(cls).delete(id);
			return  cnt >0;
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			MybatisConnection.close(session);
		}
		return false;
	}
	
	public String idSearch(String email, String tel) {
		SqlSession session = MybatisConnection.getConnection();
		try {
			return session.getMapper(cls).idSearch(email,tel);	
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			MybatisConnection.close(session);
		}
		return null;
	}
	
	
	public String pwSearch(String id, String email, String tel) {
		SqlSession session = MybatisConnection.getConnection();
		try {
			map.clear();
			map.put("id", id);
			map.put("email", email);
			map.put("tel", tel);
			return session.getMapper(cls).pwSearch(map);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			MybatisConnection.close(session);
		}
		return null;
	}
	
	
	public boolean updatePass(String id, String pass) {
		// pass : 변경 될 비밀번호
		// id : 로그인된 아이디값
		SqlSession session = MybatisConnection.getConnection();
		try {
			int cnt = session.getMapper(cls).updatePass(id,pass);
			return cnt >0; //true : 변경된 레코드 존재
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			MybatisConnection.close(session);
		}
		return false;
	}
	
	public List<Member> selectEmail(String[] ids){
		SqlSession session = MybatisConnection.getConnection();
		try { 
			map.clear();
			map.put("ids", ids);
			return session.getMapper(cls).list(map);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			MybatisConnection.close(session);
		}
		return null;
	}

}
