package com.billcom.app.dto;

public class CommentDto {
	private String content;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "CommentDto [content=" + content + "]";
	}




}
