package com.billcom.app.dto;

import java.time.LocalDate;

import com.billcom.app.entity.TeamMember;

public class TeamMemberDto {
            private String codeMember;
            private LocalDate date;
            private long idUser;

            public TeamMemberDto(String codeMember, LocalDate date, long idUser) {
				this.codeMember = codeMember;
				this.date = date;
				this.idUser = idUser;
			}

			public String getCodeMember() {
				return codeMember;
			}

			public void setCodeMember(String codeMember) {
				this.codeMember = codeMember;
			}

			public LocalDate getDate() {
				return date;
			}

			public void setDate(LocalDate date) {
				this.date = date;
			}

			public long getIdUser() {
				return idUser;
			}

			public void setIdUser(long idUser) {
				this.idUser = idUser;
			}

			@Override
			public String toString() {
				return "TeamMemberDto [codeMember=" + codeMember + ", date=" + date + ", idUser=" + idUser + "]";
			}
           
		/*	public TeamMember fromDtoToTeam(TeamMemberDto teamDto) {
				return new TeamMember(teamDto.getCodeMember(),teamDto.getDate(),teamDto.getIdUser());
            	
            }*/
            
            
            
}
