package project3;

public class Person {
	public static void main(String[] args) {
		//System.out.println(probability(0.1,1));
	}
	
	public static Virus current_virus = new Virus();
	public  String identity  = "";
	public  String status;
	public  static double aver_breath_air =0.345; // average breathing rate of an college student per h m^3/h
	public  double out_infected_particles ;
	public  int in_infected_particles ; 
	public double continous_probability = 0;
	public double infect_perc ;
	public static double i_quanta = 0; 
	
	
	public Person(String iden, String status, double perc){
		this.status = status;
		this.infect_perc = perc;
		this.identity = iden;
		this.continous_probability = 0;
	}
	public Person() {
		status = " ";
		infect_perc = 0.0;
		identity = "none";
	}
	//  standard formula for Wells- Riley model 
	public  static double probability(double hour, int day, double current_infected) { // without considering the influence of x,y coordinates.
		double x ;
		 x = 1-(Math.exp(-(current_infected *aver_breath_air * Classroom.quanta * hour/Classroom.ventilation_Rate)));
		return x;
	}
	
	public static double probability_X_Y (double hour, int day , double cumulated_quanta) {
		return 1-(Math.exp(-(cumulated_quanta * aver_breath_air * hour/Classroom.ventilation_Rate)));

	}
	@Override 
	public String toString() {
		return this.status;
		
	}
	public double infec_change_1m(double infect_perc) {
		if ( infect_perc == 0) {
			return infect_perc = current_virus.discrete_1m;
		}
		return 1-(1-infect_perc)  * (1-current_virus.discrete_1m);
		
	}
}
