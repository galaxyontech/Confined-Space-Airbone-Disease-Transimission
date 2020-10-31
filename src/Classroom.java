import java.util.ArrayList;

public class Classroom {
    public  int time = 20  ; // h
    public  int volume = 65 ;//m^3;
    public  static int number_of_student = 94;
    public  static double ventilation_Rate = 3.6 * number_of_student /1000 * 3600 ; // average ventilation rate of a college classroom depending on the size of students m^3/h;

    public  Person student = new Person("Student","o",0.0);
    public   int current_infected = 1;
    public static int width = 10;	// number of columns
    public static int length = 12; // number of rows
    public int people = 0;


    public static double quanta = 515;	  // quanta per hour


    public static Person[][] classroom  = new Person[length][width];
    public static ArrayList<String> coordinates = new ArrayList<String>();

    public Classroom(){

        for (int i = 0 ; i<length; i ++) {
            for ( int j = 0;  j < width ; j++) {

                classroom[i][j] = new Person("Student","o",0.0); //initialize with healthy individual  o stands for healthy for clarification
            }


        }
        int teacher = width /2;
        for ( int i =0 ; i < width ; i++) {
            for (int j = 0 ; j<2 ; j++ ) {
                classroom[j][i] = new Person();
            }

        }
        classroom[0][teacher] = new Person("Teacher","o", 0.0);

    }
    public static void rand_infec(int current_infected) {
        String xy = "";
        int infected_row = 0;
        int infected_col = 0;
        for (int i = 0 ; i < current_infected; i++) {
            infected_row = 2 + (int)(Math.random() * ((length - 2) ));
            infected_col = (int)(Math.random() * width);
            classroom[infected_row][infected_col] = new Person("Student", "Infected",1);
            xy = String.valueOf(infected_row) + "," +String.valueOf(infected_col);
            coordinates.add(xy);

        }
    }
    public static void print_classroom(){
        for (int row = 0 ; row<length; row ++) {
            for ( int col = 0;  col < width ; col++) {
                System.out.printf("%-15s", classroom[row][col]);
            }
            System.out.println("\n");
        }
    }
}
