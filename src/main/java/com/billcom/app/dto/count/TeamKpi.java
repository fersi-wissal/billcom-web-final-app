package com.billcom.app.dto.count;

import java.util.List;

public class TeamKpi {

	private String name;
	private Long id;
	private List<CountDto> series;

	public TeamKpi(String name, Long id, List<CountDto> series) {
		this.name = name;
		this.id = id;
		this.series = series;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<CountDto> getSeries() {
		return series;
	}

	public void setSeries(List<CountDto> series) {
		this.series = series;
	}
}
