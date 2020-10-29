package project3;

public class Discrete_Simulation {
	public static void main(String[] args) {
		int leftr;
		int leftc;
		int upr;
		int upc;
		int rightr;
		int rightc;
		int centerr;
		int centerc;
		int infected_days = 1;
		Person current_infected ;
		Person current_student ;
		
		
		
		
		Classroom a = new Classroom();
		System.out.println("Initializing Classroom Simulation");
		a.print_classroom();
		System.out.println("Initializing Classroom Infection");
		a.rand_infec(a.current_infected);
		a.print_classroom();

		
		while (a.current_infected < a.number_of_student) {
		
			for ( int row = 0; row < a.length ; row ++) {
				for (int col = 0; col < a.width ; col++) {
					 
					if (a.classroom[row][col].status == "Infected") {
						current_infected = a.classroom[row][col];
						
						
						// the percentage of a right student getting infected
						if (col+1<a.width ) {
							
							current_student = a.classroom[row][col+1];
							
							
							if (current_student.status == "o") {
								
								current_student.infect_perc = current_student.infec_change_1m(current_student.infect_perc);
								
								
								if(Math.random() < current_student.infect_perc) {
									current_student.status = "Infected";
									
									a.current_infected ++;
								}
							}
						}
						
						
						// the percentage of a student who is sitting right in front of him getting infected.
						if (row-1>=0 ) {
							
							current_student = a.classroom[row-1][col];
							
							
							if (current_student.status == "o") {
								
								current_student.infect_perc = current_student.infec_change_1m(current_student.infect_perc);
								
								
								if(Math.random() < current_student.infect_perc) {
									current_student.status = "Infected";
									
									a.current_infected ++;
								}
							}
						}
						// the percentage of a student who is sitting left of the infected gets infected 
						if (col-1>=0 ) {
							
							current_student = a.classroom[row][col-1];
							
							
							if (current_student.status == "o") {
								
								current_student.infect_perc = current_student.infec_change_1m(current_student.infect_perc);
								
								
								if(Math.random() < current_student.infect_perc) {
									current_student.status = "Infected";
									
									a.current_infected ++;
								}
							}
						}
						if (row+1<a.length ) {
							
							current_student = a.classroom[row+1][col];
							
							
							if (current_student.status == "o") {
								
								current_student.infect_perc = current_student.infec_change_1m(current_student.infect_perc);
								
								
								if(Math.random() < current_student.infect_perc) {
									current_student.status = "Infected";
									
									a.current_infected ++;
								}
							}
						}
					}
					
					
				}
			}

		a.print_classroom();
		infected_days++;
		}
		System.out.println(infected_days);
		System.out.println(a.total_exhaled_air_remained);
		
	}

}
