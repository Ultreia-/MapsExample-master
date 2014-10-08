package com.marvin.mapsexample.HelperPackage;

/**
 * Created by Tobias on 05-10-2014.
 */
public class Game {

    public static String id = null;
    public static Player player = new Player(null, null);
    public static Player partner = new Player(null, null);
    public static boolean playerOne = false;
    public static String currentMission = "s1";

    //Used to control the sinecurve in DialModel, based on wheel rotation
    public static int currentRotation = 25;
    public static int totalNicks = 32;

    //Used in DialTest and SineView. Makes the sine curve distorted when not 0
    public static double scramble = 1.0;
    public static boolean dialTaskCompleted = false;


    /**
     * MISSION VARIABLES
     */

    //S3
    public static String playerOneVirusCommand = "open sesame";
    public static String playerTwoVirusCommand = "run sesame -exe";

    //SR 2
    public static String sr2_amplitude = "-15";
    public static String sr2_scramble = "0.7";


}
