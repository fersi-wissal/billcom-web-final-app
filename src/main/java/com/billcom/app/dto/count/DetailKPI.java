package com.billcom.app.dto.count;

import java.util.List;

public class DetailKPI {
         private String name;
         private List<CountDto> series;
		public DetailKPI(String name, List<CountDto> series) {
			this.name = name;
			this.series = series;
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
