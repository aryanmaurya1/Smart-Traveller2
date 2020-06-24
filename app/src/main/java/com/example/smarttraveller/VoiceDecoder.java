package com.example.smarttraveller;

import android.util.Log;

public class VoiceDecoder {
    private static final String TAG = "VoiceDecoder";
    private int count_branch_one;
    private int count_branch_two;
    private int count_branch_three;

    private int branchNumber;

    private String[][] keywords = {{"current", "location", "where","standing",""},
            {"Alert", "message", "send", "emergency","call"},
            {"book", "cab", "bike", "goto", "go","navigation"}};

    public VoiceDecoder() {
        count_branch_one = 0;
        count_branch_two = 0;
        count_branch_three = 0;
    }
    public int checkBranch(String userCommand) {
        /*  function which will return an integer which will determine the branch */
        String[] stringList = userCommand.split(" ");

        for (int i = 0; i < keywords.length; i++) {
            for (int j = 0; j < keywords[i].length; j++) {
                for (String s : stringList) {
                    if (s.equalsIgnoreCase(keywords[i][j])) {
                        switch (i+1) {
                            case 1:
                                count_branch_one++;
                                break;
                            case 2:
                                count_branch_two++;
                                break;
                            case 3:
                                count_branch_three++;
                                break;

                        }

                    }
                }
            }
        }
        Log.d(TAG, "Count: "+count_branch_one+" "+count_branch_two+" "+count_branch_three);
        branchNumber = max(count_branch_one, count_branch_two, count_branch_three);
        if (branchNumber == count_branch_one){
            return 1;
        }else if(branchNumber == count_branch_two){
            return 2;
        }else if (branchNumber == count_branch_three){
            return 3;
        }else {
            return -1;
        }
    }

    public int max(int a, int b , int c){
        // returns max of three numbers
        if(a==0 && b == 0 && c == 0){
            return -1;
        }
        return (a>=b)?((a>=c)?a:c):((b>=c)?b:c);
    }

}
