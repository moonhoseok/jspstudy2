package model;

public class Book {
	private String writer;
	private String title;
	private String content;
	//getter, setter, toString
	public String getWriter() {
		return writer;
	}
	public void setWriter(String writer) {
		this.writer = writer;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	@Override
	public String toString() {
		return "Book [writer=" + writer + ", title=" + title + ", content=" + content + "]";
	}
	
}