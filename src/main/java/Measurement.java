import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
//this class will hold jobs
public class Measurement{
   private static List<Job> activeJobs=new ArrayList<Job>();

   public static boolean addMeasurement(JSONObject jobRequest){
            if(jobRequest==null) return false;
            //TODO there is need for error checking for valid jobRequest structure before addition
            JSONObject jobDesc = jobRequest.getJSONObject("job_description");
            Job jobTobeScheduled=new Job(jobDesc);
            activeJobs.add(jobTobeScheduled);
            return true;
        }

    public static JSONArray getActiveJobs() {
            JSONArray sentJobs=new JSONArray();
            Date currentTime=new Date();
            for(Job job:activeJobs) {
                if(job.canStart(currentTime)&&!job.nodesReached()){
                    sentJobs.put(job.getMeasurementDesc());
                    job.addNodeCount();
                }
            }
            return sentJobs;
    }

    public static void recordfailedJob(JSONObject jobDesc){
         //assuming the JsonObj has key field mapping which measurement failed
        //TODO mellar needs to provide me with this info
         String key=jobDesc.getString("key");
         for(Job job:activeJobs){
             String currKey=(String)job.getMeasurementDesc().get("key");
             if(currKey.equals(key)){
                 job.subtractNodeCount();
                 break;
             }
         }
    }

    public static List<Job> getJobs(){
        return activeJobs;
    }
}