package com.billcom.app.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.billcom.app.dto.TeamDto;
import com.billcom.app.entity.Team;
import com.billcom.app.entity.TeamLeader;
import com.billcom.app.entity.TeamMember;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

	Team save(TeamDto team);

	@Query("SELECT t.id FROM team t JOIN t.teamMember tt WHERE  tt.id = :id")
	public Long getJoin(Long id);

	List<Team> findAllByleader(TeamLeader leader);

	Set<TeamMember> findTeamMemberById(long id);

}
