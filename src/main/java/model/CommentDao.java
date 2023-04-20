package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;



public class CommentDao {
	public List<Comment> list(int num) {
		Connection conn = DBConnection.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs= null;
		List<Comment> list = new ArrayList<>();
		try {
			pstmt = conn.prepareStatement("select *from comment where num =?");
			pstmt.setInt(1, num);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				Comment c = new Comment();
				c.setNum(rs.getInt("num"));
				c.setSeq(rs.getInt("seq"));
				c.setContent(rs.getString("content"));
				c.setWriter(rs.getString("writer"));
				c.setRegdate(rs.getTimestamp("regdate"));
				list.add(c);
			}
			return list; // 게시글 num에 해당하는 댓글 목록 저장객체
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			DBConnection.close(conn, pstmt, rs); 
		}
		return list;
	}
	
	public int maxseq(int num) {
		Connection conn = DBConnection.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql ="select ifnull(max(seq),0) from Comment where num=?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, num);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				return rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt, rs);
		}
		return 1;
	}
	
	public boolean insert(Comment c) {
		Connection conn = DBConnection.getConnection();
		PreparedStatement pstmt = null;
		String sql = "insert into comment"
				+"(num,seq,writer,content,regdate)"
				+"values (?,?,?,?,now())";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1,c.getNum());
			pstmt.setInt(2,c.getSeq());
			pstmt.setString(3,c.getWriter());
			pstmt.setString(4,c.getContent());
			return pstmt.executeUpdate() >0;
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt, null);
		}
		return false;
	}
	
	public boolean delete(int num, int seq) {
		Connection conn = DBConnection.getConnection();
		PreparedStatement pstmt = null;
		String sql = "delete from comment where num=? and seq=?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, num);
			pstmt.setInt(2, seq);
			return pstmt.executeUpdate()>0;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt, null);
		}
		return false;
	}
}













