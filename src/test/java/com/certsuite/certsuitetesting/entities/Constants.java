package com.certsuite.certsuitetesting.entities;

import java.util.HashMap;
import java.util.Map;

public class Constants {

    public static Map<String, String> env;
    static {
        env = new HashMap<>();
        env.put("live",          "https://certsuite.app/");
        env.put("new",           "https://certsuite-new.azurewebsites.net/");
        env.put("test-joe",      "https://certsuite-testjoe.azurewebsites.net/");
        env.put("test-leo",      "https://certsuite-testleo.azurewebsites.net/");
        env.put("test-rachel",   "https://certsuite-testrachel.azurewebsites.net/");
        env.put("test-tony",     "https://certsuite-testtony.azurewebsites.net/");
    }

    public static Map<String, User> account;
    static {
        account = new HashMap<>();
        account.put("mike",         new User("mike.edge@vespula.co.uk", "manything"));
        account.put("mikeCrap",     new User("mike.edge@vespula.co.uk", "crapbad8"));
        account.put("joe",          new User("joe.arnold@megger.com", "bigmerge"));
        account.put("joey",         new User("joey.arnold@megger.com", "jobsmith"));
        account.put("leo",          new User("leo.midwinter @megger.com", "bigmerge"));
        account.put("tony",         new User("tony.chambers @megger.com", "bigmerge"));
    }

    public static Map<String, int[]> filterBlockIDs;
    static
    {
        filterBlockIDs = new HashMap<>();
        filterBlockIDs.put("EICR",      new int[]{14, 1, 0, 21, 55, 2, 3, 85, 84, 90, 97, 98, 99, 113, 101});
        filterBlockIDs.put("MEWMulti",  new int[]{14, 1, 0, 21, 55, 3, 85, 84, 90, 113, 101});
        filterBlockIDs.put("EIC",       new int[]{14, 1, 0, 12, 27, 21, 55, 3, 85, 84, 2, 90, 97, 98, 99, 113, 101});
        filterBlockIDs.put("ELCC",      new int[]{58, 1, 0, 12, 27, 21, 98, 113});
        filterBlockIDs.put("MEWSingle", new int[]{14, 1, 0, 21, 55, 3, 85, 132, 19, 2, 113, 101});
        filterBlockIDs.put("FDADIC",    new int[]{61, 1, 0, 21, 55, 98, 113});
        filterBlockIDs.put("ELITC",     new int[]{58, 1, 0, 21, 98, 113});
        filterBlockIDs.put("FAIRE",     new int[]{102, 103, 1, 0, 21, 98, 113});
        filterBlockIDs.put("FMIR",      new int[]{105, 1, 0, 21, 98, 113});
        filterBlockIDs.put("FAVC",      new int[] { 107,1,0,21,55,113 });
        //filterBlockIDs.put("SPIR",      new int[] { 108,1,21,55,98,113 });
        filterBlockIDs.put("FAAC",      new int[] { 110,1,0,21,55,98,113 });
        filterBlockIDs.put("FAMC",      new int[] { 112,1,0,21,55,113 });
        filterBlockIDs.put("MEWB",      new int[] { 14,1,0,21,3,85,132,19,2,113,101 });
        filterBlockIDs.put("FAIC",      new int[] { 102,1,0,21,55,113 });
        filterBlockIDs.put("EVCIR",     new int[] { 14,1,0,21,55,2,3,85,84,90,97,98,113,101 });
        filterBlockIDs.put("EVCIC",     new int[] { 14,1,0,12,27,21,55,3,85,84,2,90,97,98,99,113,101 });
        filterBlockIDs.put("DE01",      new int[] { 14,1,0,21,55,2,3,85,84,90,98,99,113,101 });
        filterBlockIDs.put("IEIC",      new int[] { 14,1,0,12,27,21,55,3,85,84,2,90,98,99,113,101 });
        filterBlockIDs.put("IEICR",     new int[] { 14,1,0,21,55,2,3,85,84,90,98,99,113,101 });
    }

    public static HashMap<String, JobType> jobType = new HashMap<String, JobType>();
    public static HashMap<Integer, JobType> entries = new HashMap<Integer, JobType>();

    public static String LoremIpsum = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";

    static
    {
        // TODO: Create SQLHelper class in Java. Work with Leo
        //// Add Collection of JobTypes for use around the site
        //SQLHelper sqlh = new SQLHelper("Wasp_V2_ObsTestEntities");
        //DataSet ds = new DataSet();
        //ds = sqlh.GetDatasetByCommand("select reporttype.reporttypename, filter.filtername, filter.filterid, reporttype.reporttypeid from re" +
        //        "porttype inner join [filter] on filter.reporttypeid = reporttype.reporttypeid");
        //for (DataRow dr : ds.Tables[0].Rows) {
        //    JobType jt = new JobType(dr);
        //    jobType.Add(dr["filtername"].ToString(), jt);
        //}
    }
}