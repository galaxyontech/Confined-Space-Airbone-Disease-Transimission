import sun.lwawt.macosx.CSystemTray;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class continue_simulation {
    public static void main(String[] args) throws IOException, IOException {
        // initializing output files
        String file_name = "Increased_infectors_over_time_step.txt";
        FileWriter myWriter ;
        myWriter = new FileWriter(file_name);


        // initializing variables
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
        double aver_cornerDays = 0;
        double aver_centerDays = 0 ;
        double aver_wallDays = 0;
        int academicWeek = 3; // A week has three academic meetings, two class, 1 recitation, the real class situation may varied
        Double numOfInfectors = 0.0;




        String ini_case;
        String cases;

        ArrayList<Integer> center_days = new ArrayList<>();
        ArrayList<Integer> corner_days = new ArrayList<>();
        ArrayList<Integer> wall_days = new ArrayList<>();
        ArrayList<Integer> days = new ArrayList<>();
        ArrayList<Double> venti_list = new ArrayList<>();
        ArrayList<String> temp_coor = new ArrayList<>();
        ArrayList<Double> aver_days_pv = new ArrayList<>();
        ArrayList<Double> percentage_ventilation = new ArrayList<>();
        ArrayList<Double> aver_percentage_pv = new ArrayList<>();
        ArrayList<Double> aver_percentage_distance = new ArrayList<>();
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
        int top_ventilation_rate = 3000; // upper bound for ventilation rate
        ;   // lower bound for ventilation rate
        // here, we want to see the change of average days for infecting the entire classroom, based on the varying ventilation rate
        // we run 25 simulation results for each different ventilation rate and take mean of all the values
        System.out.println(a.ventilation_Rate);
        FileWriter all_Percentage_Data = new FileWriter("ventilation_all_Experiments_Data.txt");
        double control = 1; // this is the distance variable
        Classroom.ventilation_Rate = 500;
        int num = 1 ;
        while(Classroom.ventilation_Rate < top_ventilation_rate){
            //Classroom.ventilation_Rate = Classroom.ventilation_Rate +200;
            //System.out.println(Classroom.ventilation_Rate);
            Classroom.ventilation_Rate += 100;
            venti_list.add(Classroom.ventilation_Rate); // record the current running ventilation rate

            while(ct<50) {
                numOfInfectors = 0.0;
                num = 1;
                while (a.current_infected < a.number_of_student) {  // while the the classroom has not been entirely affected


                    //System.out.println(a.current_infected < a.number_of_student);
                    for ( int row = 0; row < a.length ; row ++) {
                        for (int col = 0; col < a.width; col++) {

                            temp_quanta = 0;        // temporary quanta for storing the current student quanta
                            current_student = a.classroom[row][col];

                            if (current_student.status == "o") { // if the current student is healthy
                                // calculate the accumulated density(quanta) of virus that this student perceives,
                                // by summing all possible infective quanta that is generated by infected individuals in the confined space
                                for ( int i = 0 ; i < a.coordinates.size() ; i ++) {
                                    coor = a.coordinates.get(i);
                                    values = coor.split(",");
                                    x = Integer.valueOf(values[0]) - row;
                                    y = Integer.valueOf(values[1]) - col;
                                    t_constant = trans_constant_r(x,y,control);
                                    temp_quanta = temp_quanta + a.quanta * t_constant;
                                }

                                current_student.i_quanta += temp_quanta; // now we reassigned the quanta to this student
                                temp = current_student.probability_X_Y(1, 1,temp_quanta);  // make the calculation through the updated Wells-Riley equation

                                if(Math.random() < temp) {
                                    current_student.status = "Infected"; // the student has been infected, we now change the status of the student
                                    xy = String.valueOf(row) + "," +String.valueOf(col);
                                    temp_coor.add(xy);
                                    a.current_infected ++;
                                    infectors_per_time_step++;
                                }
                            }
                        }
                    }
                    // starting a new day, after 24 hours of ventilation, assuming the new classroom should not have
                    for ( int row = 0; row < a.length ; row ++) {
                        for (int col = 0; col < a.width ; col++) {
                            current_student = a.classroom[row][col];
                            current_student.i_quanta = 0;
                            // airbone disease
                        }
                    }
                    /**
                     * temporary output file, can be used to observe the transmission pattern
                     */
                    myWriter.write(String.valueOf(day));
                    myWriter.write(",");
                    myWriter.write(String.valueOf(infectors_per_time_step));
                    myWriter.write("\n");
                    numOfInfectors += infectors_per_time_step;

                    Double percentageOfInfection = 0.0 ;
                    if (day>= num*academicWeek && num == 1) {
                        percentageOfInfection = numOfInfectors/(Classroom.width *(Classroom.length-2)); // depending on the current running variable, one for ventilation rate, 2 for position.
                        percentage_ventilation.add(percentageOfInfection);
                        num++;
                    }
                    infectors_per_time_step = 0;
                    day++;

                    for (String c : temp_coor) {
                        a.coordinates.add(c); // update the coordinates of the infected individual
                    }

                }


                ct++;

                days.add(day); // we know know the day for infection to each individual simulation

                ini_case = a.coordinates.get(0);
                // we know want to see the position which the initial patient 0 emerge.
                // And how that can be factored into the spreading of the whole

                cases = get_case(ini_case);

                if (cases == "ce") {        // center region
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


            }


            Double corner_Sum = sum(corner_days);
            Double center_Sum = sum(center_days);
            Double wall_Sum = sum(wall_days);
            aver_cornerDays = corner_Sum / corner_days.size();
            aver_centerDays = center_Sum / center_days.size();
            aver_wallDays = wall_Sum / wall_days.size();
            Double t_sum = sum(days);
            double aver_day = t_sum/days.size();
            aver_days_pv.add((aver_day));
            Double percentage_ventilation_rate = sumD(percentage_ventilation)/percentage_ventilation.size();
            aver_percentage_pv.add(percentage_ventilation_rate);
            //all_Percentage_Data.write(String.valueOf(Classroom.ventilation_Rate)+ '\t');
            for(int i   = 0 ; i < percentage_ventilation.size(); i++){
                all_Percentage_Data.write(String.valueOf(percentage_ventilation.get(i)));
                all_Percentage_Data.write((","));
            }
            all_Percentage_Data.write('\n');
            percentage_ventilation.clear();
            days.clear();
            ct = 0;

        }
        all_Percentage_Data.flush();
        all_Percentage_Data.close();






        // simulation finished



        myWriter.close();

        double sum = sum(days);
        System.out.println(aver_days_pv.toString());
        double aver_day_for_infection = sum/days.size() ;



        FileWriter myday = new FileWriter("days.txt");
        int check =  0;
        for(int i = 0; i < aver_days_pv.size() ; i++) {
            check += 1;

            myday.write(String.valueOf(aver_days_pv.get(i)) + "\t " + String.valueOf(venti_list.get(i)) +'\n');
        }

        myday.flush();
        myday.close();


        FileWriter percentage = new FileWriter("Amount_of Infectors_After_1_week");
        for(int i = 0; i < aver_days_pv.size() ; i++) {
            percentage.write(String.valueOf(aver_percentage_pv.get(i)) + "\t " + String.valueOf(venti_list.get(i)) +'\n');
        }
        percentage.flush();
        percentage.close();

        FileWriter output = new FileWriter("simulation_outputs.txt");
        output.write("average days for infection,");

        output.write(String.valueOf(aver_day_for_infection) + "\n");

        output.write("corner cases,");
        System.out.println(days);
        System.out.println(corner_days);
        System.out.println(center_days);
        System.out.println(wall_days);
        output.write(String.valueOf(cornercase) +"," + String.valueOf(aver_cornerDays) + "\n");
        output.write("center cases,");
        output.write(String.valueOf(centercase) +"," + String.valueOf(aver_centerDays) + "\n");
        output.write("wall cases," );
        output.write(String.valueOf(wallcase) +"," + String.valueOf(aver_wallDays) + "\n");

        output.close();




    }


    /**
     * heat kernel version of the transmission coefficient
     * @param x  coordinates diff
     * @param y  coordinates diff
     * @return
     */
    public static double trans_constant(int x, int y) {
        return (1/(4*Math.PI))*Math.pow(Math.E, -Math.pow(Math.abs(x-y),2)/(4));
    }
    /**
     * 1/r^2 formula to control the transmission coefficient
     * @param x  coordinates diff
     * @param y  coordinates diff
     * @return transmission coefficient
     */
    public static double trans_constant_r (int x , int y, double control) {
        return 1/(Math.pow(x*control, 2)+ Math.pow(y*control, 2));  // assuming we do not need to consider the influence of the Z axis

    }

    /**
     * get the case of patient 0
     * @param coor
     * @return
     */
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

    /**
     * standard useful array-sum calculation
     * @param x
     * @return
     */
    public static Double sum (ArrayList<Integer> x) {
        Double sum = 0.0;
        for (int c : x) {
            sum += c;
        }
        return sum;
    }
    public static Double sumD (ArrayList<Double> x) {
        Double sum = 0.0;
        for (Double c : x) {
            sum += c;
        }
        return sum;
    }

}
