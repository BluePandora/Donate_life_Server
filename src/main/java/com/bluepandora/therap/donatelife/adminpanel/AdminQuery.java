/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bluepandora.therap.donatelife.adminpanel;

import com.bluepandora.therap.donatelife.constant.DbConstant;

/**
 *
 * @author Biswajit Debnath
 */
public class AdminQuery extends DbConstant {

    public static String adminLoginQuery(String idUser, String hashKey) {
        return "select * from ( (select first_name, last_name from "+T_PERSON+" where person_id="
                + "(select person_id from "+T_ADMIN_PANEL+" where ("
                + "mobile_number='"+idUser+"' or email='"+idUser+"') and access_key='"+hashKey+"')) as pn join (select * from "+T_ADMIN_PANEL+" where " 
                +"(mobile_number='"+idUser+"' or email='"+idUser+"') and access_key='"+hashKey+"') as pro)";
    }

    public static String getDonatorListQuery() {
        return "select mobile_number, first_name, last_name, dist_name, group_name, gcm_id"
                + " from " + T_PERSON_INFO + " join " + T_PERSON + " using(person_id) join " + T_DISTRICT + " using(dist_id) "
                + "join " + T_BLOOD_GROUP + " using(group_id)";
    }

    public static String getFeedBackQuery() {
        return "select * from " + T_FEEDBACK + " order by req_time";
    }

    public static String deleteFeedBackQuery(String idUser, String reqTime) {
        return "delete from " + T_FEEDBACK + " where id_user='" + idUser + "' and req_time='" + reqTime + "'";
    }

    public static String getAdminListQuery() {
        return "select * from " + T_ADMIN_PANEL + " join " + T_PERSON + " using(person_id)";
    }

    public static String getHospitalListQuery() {
        return "select hospital_id, dist_name, hospital_name, hospital_bname from " + T_HOSPITAL + " join " + T_DISTRICT + " using(dist_id) order by hospital_id desc";
    }
    
    public static String addHospitalQuery(String distId, String hospitalName, String hospitalBName){
        return "insert into "+T_HOSPITAL+" (dist_id, hospital_name, hospital_bname) values("+distId+",'"+hospitalName+"','"+hospitalBName+"')";
    }
    
    public static String removeHospitalQuery(String hospitalId){
        return "delete from "+T_HOSPITAL+" where hospital_id="+hospitalId;
    }
    
    public static String removeFeedBackQuery(String idUser, String reqTime){
        return "delete from "+T_FEEDBACK+" where id_user='"+idUser+"' and req_time='"+reqTime+"'";
    }

}
