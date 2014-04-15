/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.novadge.novamail
import java.util.regex.Matcher
import java.util.regex.Pattern
/**
 *
 * @author Omasiri
 */
class NovamailUtils {

    /**
     *Match the dates for equality
     **/
    public static boolean dateMatch(Date dbEmailDate,Date userEmailDate){

        if(dbEmailDate==null){
            return true
        }
        if(dbEmailDate.equals(userEmailDate)){
            print "date match... return true\n\n\n"
            return true
        }
        else{
            print "date mismatch return false\n\n\n"
            return false
        }
    }

    /**
     *This method is used to check if email has been checked before
     */
    public static boolean getChecked(Date dbEmailLastAccessed,Date userMessageReceivedDate){
        
        def dbTime
        def userTime
        
        dbTime = dbEmailLastAccessed.getTime()

        userTime = userMessageReceivedDate.getTime();
        print "DB time is ${dbEmailLastAccessed.getTime()}\n"
        print "User time is ${userMessageReceivedDate.getTime()}\n"

       // System.out.println("minus"+userMessageReceivedDate-dbEmailLastAccessed)

        if((dbEmailLastAccessed<=userMessageReceivedDate)&&(dbTime < userTime)){
            print "db time is less than user email time ...returned false\n\n\n"
            return false
        }
        else{
            print "db time is greater or equal to user email time ....returned true \n\n\n\n"
            return true
        }
    }

    /**
     *This method should take two string parameters and returns a boolean based
     *on criteria implemented inside
     *Parameters will come from email date attribute stored in the db and
    */
    public static boolean subjectMatch(String dbEmailSubject,String emailSubject){

        if(dbEmailSubject == null){
            System.out.println("subject is null... return true")
            return true
        }

       if(emailSubject =~ dbEmailSubject){
           print "email subject is not null but will return true"
           return true
       }
       else{
           print "email subject did not match.. return false"
           return false
       }

    }

    /**
     *This method is supposed to compare the email sender defined in the db
     *for the a match of the sender field
    */
    public static boolean senderMatch(String dbEmailSender,String emailSender){
        if(dbEmailSender == null){

            print "sender field is empty... return true"
            return true
        }
        if(emailSender =~ dbEmailSender){
            print "sender match...return true"
           return true
       }
       else{

            print "sender didn't match return false"
           return false
       }

    }


    /**
     *This method is supposed to search the body of the email for a match
     *of the string defined in the database email record
    */
    public static boolean bodyContains(String dbEmailContains,String emailBody){
        if(emailBody =~ dbEmailContains){
            print "email body match.... return true"
           return true
       }
       else{

            print "email body mismatch ... return false"
           return false
       }


    }	
}

