package rockets.mining;

import com.google.common.collect.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rockets.dataaccess.DAO;
import rockets.model.Launch;
import rockets.model.LaunchServiceProvider;
import rockets.model.Payload;
import rockets.model.Rocket;
import scala.collection.parallel.ParIterableLike;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class RocketMiner {
    private static Logger logger = LoggerFactory.getLogger(RocketMiner.class);

    private DAO dao;


    public RocketMiner(DAO dao) {

        this.dao = dao;

    }

    /**
     * TODO: to be implemented & tested!
     * Returns the top-k most active rockets, as measured by number of completed launches.
     *
     * @param k the number of rockets to be returned.
     * @return the list of k most active rockets.
     */

    public List<Rocket> mostLaunchedRocket(int k) {
        Collection<Launch> launches = dao.loadAll(Launch.class);
        HashMap<Rocket, Integer> mostLaunchRocket = new HashMap<>();

        for(Launch launch : launches){
            if(mostLaunchRocket.containsKey(launch.getLaunchVehicle())){
                mostLaunchRocket.put(launch.getLaunchVehicle(), mostLaunchRocket.get(launch.getLaunchVehicle()) +1);
            }else
                mostLaunchRocket.put(launch.getLaunchVehicle(), 1);

        }

        mostLaunchRocket = sortByValues(mostLaunchRocket, true);

        if(mostLaunchRocket.size() > k)
            return new ArrayList<>(mostLaunchRocket.keySet()).subList(0, k);
        else
            return new ArrayList<>(mostLaunchRocket.keySet());

    }


        /**
         * TODO: to be implemented & tested!
         * <p>
         * Returns the top-k most reliable launch service providers as measured
         * by percentage of successful launches.
         *
         * @param k the number of launch service providers to be returned.
         * @return the list of k most reliable ones.
         */
    public List<LaunchServiceProvider> mostReliableLaunchServiceProviders(int k) {

        int i = 0; int Si = 0;  double Pi = 0;
        int j = 0; int Sj = 0;  double Pj = 0;
        int e = 0; int Se = 0;  double Pe = 0;

        Collection<Launch> launches = dao.loadAll(Launch.class);

        for (Launch launch : launches){

            if(launch.getLaunchServiceProvider().getName().equals("ULA"))  {
                i = i + 1;
                if(launch.getLaunchOutcome().equals(Launch.LaunchOutcome.SUCCESSFUL)){
                  Si = Si +1;
                }

            }

            if(launch.getLaunchServiceProvider().getName().equals("SpaceX"))  {
                j = j + 1;
                if(launch.getLaunchOutcome().equals(Launch.LaunchOutcome.SUCCESSFUL)){
                    Sj = Sj +1;
                }
            }

            if(launch.getLaunchServiceProvider().getName().equals("ESA"))  {
                e = e + 1;
                if(launch.getLaunchOutcome().equals(Launch.LaunchOutcome.SUCCESSFUL)){
                    Se = Se +1;
                }
            }

        }

        Pi = (double) Si / i;
        Pj = (double) Sj / j;
        Pe = (double) Se / e;

        Double DPi = new Double(Pi);
        Double DPj = new Double(Pj);
        Double DPe = new Double(Pe);

        Collection<LaunchServiceProvider> serviceProvider = dao.loadAll(LaunchServiceProvider.class);
        TreeMap<Double, LaunchServiceProvider> successRate = new TreeMap<>(Collections.reverseOrder());

        for (LaunchServiceProvider lsp : serviceProvider) {

            if (lsp.getName().equals("ULA"))
                successRate.put(DPi ,lsp);

            if (lsp.getName().equals("SpaceX"))
                successRate.put(DPj, lsp);

            if (lsp.getName().equals("ESA"))
                successRate.put(DPe, lsp);

        }

        if(successRate.size() > k)
            return new ArrayList<>(successRate.values()).subList(0, k);
        else
            return new ArrayList<>(successRate.values());


    }




    /**
     * TODO: to be implemented & tested!
     * <p>
     * Returns the dominant country who has the most launched rockets in an orbit.
     *
     * @param orbit the orbit
     * @return the country who sends the most payload to the orbit
     */
    public String dominantCountry(String orbit) {
        Collection<Launch> launches = dao.loadAll(Launch.class);

        TreeMap<String, Integer > dominantCountry = new TreeMap<>();
        for(Launch launch: launches){
            if(dominantCountry.containsKey(launch.getLaunchVehicle().getCountry())){
                dominantCountry.put(launch.getLaunchVehicle().getCountry(), dominantCountry.get(launch.getLaunchVehicle().getCountry())  + launch.getPayload().getIdentity().size());
            }

            else {
                dominantCountry.put(launch.getLaunchVehicle().getCountry(),launch.getPayload().getIdentity().size());
            }

        }
        return dominantCountry.firstKey();}

    /**
     * TODO: to be implemented & tested!
     * <p>
     * Returns the top-k most expensive launches.
     *
     * @param k the number of launches to be returned.
     * @return the list of k most expensive launches.
     */


    public List<Launch> mostExpensiveLaunches(int k) {

        logger.info("find most expensive " + k + " launches");

        Collection<Launch> launches = dao.loadAll(Launch.class);
        TreeMap<Double, Launch> launchesByPrice = new TreeMap<>(Collections.reverseOrder());
        for (Launch launch : launches)
            launchesByPrice.put(launch.getPrice(), launch);


        if(launchesByPrice.size() > k)
            return new ArrayList<>(launchesByPrice.values()).subList(0, k);
        else
            return new ArrayList<>(launchesByPrice.values());

    }

    /**
     * TODO: to be implemented & tested!
     * <p>
     * Returns a list of launch service provider that has the top-k highest
     * sales revenue in a year.
     *
     * @param k the number of launch service provider.
     *  year the year in request
     * @return the list of k launch service providers who has the highest sales revenue.
     */



    public List<LaunchServiceProvider> highestRevenueLaunchServiceP(int k) {
        Collection<Launch> launches = dao.loadAll(Launch.class);
        HashMap<LaunchServiceProvider, Double> highestRevenueLSP = new HashMap<>();

        for(Launch launch : launches){
            if(highestRevenueLSP.containsKey(launch.getLaunchServiceProvider())){
                highestRevenueLSP.put(launch.getLaunchServiceProvider(), highestRevenueLSP.get(launch.getLaunchServiceProvider()) + launch.getPrice());
            } else {
                highestRevenueLSP.put(launch.getLaunchServiceProvider(), launch.getPrice());
            }
        }

        if(highestRevenueLSP.size() > k)
            return new ArrayList<>(highestRevenueLSP.keySet()).subList(0, k);
        else
            return new ArrayList<>(highestRevenueLSP.keySet());
    }


    private <T> HashMap sortByValues(HashMap map, boolean isReversed) {

        if(map.size() < 2)
            return map;

        List linkedList = new LinkedList(map.entrySet());
        Comparator comparator = (Object objectOne, Object objectTwo)-> {
            return ((Comparable) ((Map.Entry) (objectOne)).getValue())
                    .compareTo(((Map.Entry) (objectTwo)).getValue());
        };

        if (isReversed)
            linkedList.sort(comparator.reversed());
        else
            linkedList.sort(comparator);

        HashMap linkedHashMap = new LinkedHashMap();
        Map.Entry entry;

        for (Object tempEntry : linkedList){
            entry = (Map.Entry) tempEntry;
            linkedHashMap.put(entry.getKey(), entry.getValue());
        }
        return linkedHashMap;
    }

}
