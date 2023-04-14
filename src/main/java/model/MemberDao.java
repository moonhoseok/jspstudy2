package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//DB와  관련있는 함수 들의 모임
public class MemberDao {
	public boolean insert(Member mem) {
		Connection conn = DBConnection.getConnection();
		PreparedStatement pstmt = null;
		String sql = "insert into member(id, pass, name, gender, tel, email, picture)"
				+"values(?,?,?,?,?,?,?)";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,mem.getId());
			pstmt.setString(2,mem.getPass());
			pstmt.setString(3,mem.getName());
			pstmt.setInt(4,mem.getGender());
			pstmt.setString(5,mem.getTel());
			pstmt.setString(6,mem.getEmail());
			pstmt.setString(7,mem.getPicture());
			if(pstmt.executeUpdate() > 0) {
				return true;
			}else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DBConnection.close(conn, pstmt, null);
		}
		return false;
	}
	public Member selectOne(String id) {
		Connection conn=DBConnection.getConnection();
		String sql = "select * from member where id=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			//rs.next() : select된 결과 정보 조회.
			if(rs.next()) {
				Member mem = new Member();
				mem.setId(rs.getString("id"));
				mem.setPass(rs.getString("pass"));
				mem.setName(rs.getString("name"));
				mem.setGender(rs.getInt("gender"));
				mem.setTel(rs.getString("tel"));
				mem.setEmail(rs.getString("email"));
				mem.setPicture(rs.getString("picture"));
				return mem; //입력된 id 값의 해당 정보를 DB에서 조회된 데이터 저장
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			DBConnection.close(conn, pstmt, rs);
		}
		return null;
	}
	public boolean update(Member mem) {
		Connection conn = DBConnection.getConnection();
		PreparedStatement pstmt = null;
		String sql = "update member set name=?,gender=?,tel=?,email=?,"
				+"picture=? where id=?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, mem.getName());
			pstmt.setInt(2, mem.getGender());
			pstmt.setString(3, mem.getTel());
			pstmt.setString(4, mem.getEmail());
			pstmt.setString(5, mem.getPicture());
			pstmt.setString(6, mem.getId());
			return pstmt.executeLargeUpdate()>0;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			DBConnection.close(conn, pstmt, null);
		}
		return false;
	}
	public List<Member> list(){
		Connection conn = DBConnection.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<Member> list = new ArrayList<>();
		try {
			pstmt = conn.prepareStatement("select *from member");
			rs = pstmt.executeQuery();
			while(rs.next()) {
				Member m = new Member();
				m.setId(rs.getString("id"));
				m.setPass(rs.getString("pass"));
				m.setName(rs.getString("name"));
				m.setGender(rs.getInt("gender"));
				m.setTel(rs.getString("tel"));
				m.setEmail(rs.getString("email"));
				m.setPicture(rs.getString("picture"));
				list.add(m);
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			DBConnection.close(conn, pstmt, rs);
		}
		return null;
	}
	//http://localhost:8080/jspstudy1/model1/member/delete.jsp?id=test2&pass=1111
	public boolean delete(String id) {
		Connection conn = DBConnection.getConnection();
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement("delete from member where id=?");
			pstmt.setString(1,id);
			return pstmt.executeUpdate() >0;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			DBConnection.close(conn, pstmt, null);
		}
		return false;
	}
	public String idSearch(String email, String tel) {
		Connection conn = DBConnection.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs =null;
		try {
			pstmt = conn.prepareStatement
					("select id from member where email=? and tel=?");
			pstmt.setString(1, email);
			pstmt.setString(2, tel);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				Member mem = new Member();
				mem.setId(rs.getString("id"));
				return mem.getId();
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt, null);
		}
		return null;
	}
	public String pwSearch(String id, String email, String tel) {
		Connection conn = DBConnection.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs =null;
		try {
			pstmt = conn.prepareStatement
					("select pass from member where id=?and email=? and tel=?");
			pstmt.setString(1, id);
			pstmt.setString(2, email);
			pstmt.setString(3, tel);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				//Member mem = new Member();
				//mem.setPass(rs.getString("pass"));
				return rs.getString("pass");//mem.getPass();
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt, null);
		}
		return null;
	}
	public boolean updatePass(String id, String pass) {
		// pass : 변경 될 비밀번호
		// id : 로그인된 아이디값
		Connection conn = DBConnection.getConnection();
		PreparedStatement pstmt = null;
		
		try {
			pstmt = conn.prepareStatement
					("update member set pass=? where id =?");
			pstmt.setString(1, pass);
			pstmt.setString(2, id);
			
			return pstmt.executeUpdate() >0; //true : 변경된 레코드 존재
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			DBConnection.close(conn, pstmt, null);
		}
		return false;
	}
	
	public List<Member> selectEmail(String[] ids){
		Connection conn = DBConnection.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<ids.length;i++) {
			sb.append("'"+ids[i]+((i<ids.length-1)?"',":"'"));
		}
		List<Member> list = new ArrayList<>();
		String sql = "select * from member "
				+"where id in ("+sb.toString()+")";
		try { 
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				Member m =new Member();
				m.setId(rs.getString("id"));
				m.setPass(rs.getString("pass"));
				m.setName(rs.getString("name"));
				m.setGender(rs.getInt("gender"));
				m.setTel(rs.getString("tel"));
				m.setEmail(rs.getString("email"));
				m.setPicture(rs.getString("picture"));
				list.add(m);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			DBConnection.close(conn, pstmt, rs);
		}
		return null;
	}
}
























