package project3;
/**
 * @author zeruiji
 * @Date_created: June 7th
 * @version: v4, current function includes random simulation through updated well's reily formula, outputs two 'txt' files involving infectors 
 * 			increased per time step
 * 
 */
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.io.FileWriter;
public class Continous__Simulation {
	public static void main(String[] args) throws IOException {
		String file_name = "Increased_infectors_over_time_step.txt";
		FileWriter myWriter ;		
		myWriter = new FileWriter(file_name);
		
		Person current_student ;
		int ct = 0;
		int day  = 1;
		double temp_quanta = 0 ;
		int x = 0;
		int y = 0;
		double t_constant = 0;
		int centercase = 0;
		int cornercase = 0;
		int wallcase = 0;
		int infectors_per_time_step = 0; 
		double temp = 0;
		
		
		String ini_case;
		String cases;

		ArrayList<Integer> center_days = new ArrayList<Integer>();
		ArrayList<Integer> corner_days = new ArrayList<Integer>();
		ArrayList<Integer> wall_days = new ArrayList<Integer>();
		ArrayList<Integer> days = new ArrayList<Integer>();
		ArrayList<Double> venti_list = new ArrayList<Double>();
		ArrayList<String> temp_coor = new ArrayList<String>();
		ArrayList<Double> aver_days_pv = new ArrayList<Double>();
		String coor = "";
		String[] values = new String[2];
		String xy = "";
		


		/**
		 * initializing classroom environment 
		 */
		Classroom a = new Classroom();
		System.out.println("Initializing Classroom Simulation");
		a.print_classroom();
		
		/**
		 * initializing random pos infector
		 */
		System.out.println("Initializing Classroom Infection");
		a.rand_infec(a.current_infected);
		a.print_classroom();
		
		
		
		
		
		/**
		 * starting 100 simulations of classroom envrionments 
		 */
		int top_ventilation_rate = 2000;
		while(Classroom.ventilation_Rate < top_ventilation_rate){
			Classroom.ventilation_Rate = Classroom.ventilation_Rate +50;
			venti_list.add(Classroom.ventilation_Rate);
		
		while(ct<25) {	
			while (a.current_infected < a.number_of_student) {
				
				
				//System.out.println(a.current_infected < a.number_of_student);
				for ( int row = 0; row < a.length ; row ++) {
					for (int col = 0; col < a.width ; col++) {
						
						temp_quanta = 0;
						current_student = a.classroom[row][col];
	
						if (current_student.status == "o") {
							// calculate the accumulated density(quanta) of virus that this student perceives
								for ( int i = 0 ; i < a.coordinates.size() ; i ++) {
									coor = a.coordinates.get(i);
									values = coor.split(",");
									x = Integer.valueOf(values[0]) - row;
									y = Integer.valueOf(values[1]) - col;
									t_constant = trans_constant_r(x,y);
									temp_quanta = temp_quanta + a.quanta * t_constant; 
							}
							
							current_student.i_quanta += temp_quanta;
							temp = current_student.probability_X_Y(1, 1,temp_quanta);
		
							if(Math.random() < temp) {								
								current_student.status = "Infected";
								xy = String.valueOf(row) + "," +String.valueOf(col);
								temp_coor.add(xy);
								a.current_infected ++;
								infectors_per_time_step++;
							}				
						}	
					}				
				}
	
				for ( int row = 0; row < a.length ; row ++) {
					for (int col = 0; col < a.width ; col++) {
						current_student = a.classroom[row][col];
						current_student.i_quanta = 0;   // starting a new day, after 24 hours of ventilation, assuming the new classroom should not have
															// airbone disease
						}
				}
				
				//System.out.println("The increase of infectors on day" + day+  "; " + infectors_per_time_step);
				myWriter.write(String.valueOf(day));
				myWriter.write(",");
				myWriter.write(String.valueOf(infectors_per_time_step));
				myWriter.write("\n");
				infectors_per_time_step = 0;
				day++;
			
				for (String c : temp_coor) {
					a.coordinates.add(c);
				}
				
			}
			
			ct++;
			day--;
			days.add(day);
			
			ini_case = a.coordinates.get(0);
			cases = get_case(ini_case);
			
			if (cases == "ce") {
				center_days.add(day);
				centercase++;
			}else if (cases == "co") {
				corner_days.add(day);
				cornercase++;
			}else {
				wallcase++;
				wall_days.add(day);
			}
			
			
			// reset environment variables and reaquire cases
			
			a = new Classroom();
			a.coordinates.clear();
			a.rand_infec(a.current_infected);

			
			
			// create infectors output file to record the increased infectors per day
			// file creation and initialization complete 
			
			
			temp = 0;
			coor = "";
			values = new String[2];
			temp_coor.clear();
			xy = "";
			infectors_per_time_step = 0; 
			day = 1;
			
			//System.out.println(a.current_infected);
			}
			int t_sum = sum(days);
			aver_days_pv.add((double) (t_sum/days.size()));
		
	}
		
		
		
		
		
		
		// simulation finished 

		
		
		myWriter.close();
		
		double sum = sum(days);
		//for ( )
		double aver_day_for_infection = sum/days.size() ;
		double aver_day_corner = sum(corner_days)/corner_days.size();
		double aver_day_center = sum(center_days)/center_days.size();
		double aver_day_wall = sum(wall_days)/wall_days.size();
		
		aver_day_for_infection =  (sum/days.size());
		System.out.print(sum/days.size());
		FileWriter myday = new FileWriter("days.txt");
		int check =  0;
		for(int i = 0; i < days.size() ; i++) {
			check += 1;
			myday.write(String.valueOf(aver_days_pv.get(i)) + "\t " + String.valueOf(venti_list.get(i)) +'\n');
		}
		myday.flush();
		myday.close();
		
		FileWriter output = new FileWriter("simulation_outputs.txt");
		output.write("average days for infection,");
		
		output.write(String.valueOf(aver_day_for_infection) + "\n");
		
		output.write("center cases,");
		System.out.println(check);
		output.write(String.valueOf(centercase) +"," + String.valueOf(aver_day_center) + "\n");
		output.write("corner cases,");
		output.write(String.valueOf(cornercase) +"," + String.valueOf(aver_day_corner) + "\n");
		output.write("wall cases," );
		output.write(String.valueOf(wallcase) +"," + String.valueOf(aver_day_wall) + "\n");
		
		output.close();
		
		
		
	
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static double trans_constant(int x, int y) {
		return (1/(4*Math.PI))*Math.pow(Math.E, -Math.pow(Math.abs(x-y),2)/(4));
	}
	/**
	 * 1/r^2 formula to control the transmission coefficient
	 * @param x
	 * @param y
	 * @return transmission coefficient
	 */
	public static double trans_constant_r (int x , int y) {
		return 1/(Math.pow(x, 2)+ Math.pow(y, 2));  // assuming we do not need to consider the influence of the Z axis 

	}
	public static String get_case(String coor) {
		String inf_case;
		int length = Classroom.length;
		int width = Classroom.width;
		String[] values = coor.split(",");
		int row = Integer.valueOf(values[0]) ;
		int col = Integer.valueOf(values[1]) ;
		if ( row < (length*0.8535) && (row > length*0.1465) && (col < width * 0.8535) && (col > width *0.1465)) {
			inf_case = "centercase";
			return "ce";
		}else if ((row > (length*0.8535) || (row <length*0.1465)) && (col < width * 0.8535) && (col > width *0.1465) 
				||(row < (length*0.8535) && (row >length*0.1465) && ((col >(width * 0.8535)) || (col < width *0.1465)))) {
			inf_case = "wallcase";
			return "w";
		}else {
			inf_case = "cornercase";
			return "co";
		}
		
	}
	public static int sum (ArrayList<Integer> x) {
		int sum = 0;
		for (int c : x) {
			sum += c;
		}
		return sum;
	}
}
	
	


