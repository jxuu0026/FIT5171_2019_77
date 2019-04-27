package rockets.mining;

import com.google.common.collect.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rockets.dataaccess.DAO;
import rockets.model.Launch;
import rockets.model.LaunchServiceProvider;
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
    public List<Rocket> mostLaunchedRockets(int k) {

        logger.info("find the top " + k + " active rockets!");

        Collection<Launch> launches = dao.loadAll(Launch.class);

        int count0 = 0;
        int count1 = 0;
        int count2 = 0;
        int count3 = 0;
        int count4 = 0;

        for (Launch launch : launches) {

            String name = launch.getLaunchVehicle().getName();

            if (launch.getLaunchVehicle().getName().equals("rocket_0")) {
                count0 ++;
            }

            if (launch.getLaunchVehicle().getName().equals("rocket_1")) {
                count1 = count1 + 1;
            }

            if (launch.getLaunchVehicle().getName().equals("rocket_2")) {
                count2 = count2 + 1;
            }

            if (launch.getLaunchVehicle().getName().equals("rocket_3")) {
                count3 = count3 + 1;
            }

            if (launch.getLaunchVehicle().getName().equals("rocket_4")) {
                count4 = count4 + 1;
            }

        }

        Integer Icount0 = new Integer(count0);
        Integer Icount1 = new Integer(count1);
        Integer Icount2 = new Integer(count2);
        Integer Icount3 = new Integer(count3);
        Integer Icount4 = new Integer(count4);

        Collection<Rocket> rockets = dao.loadAll(Rocket.class);
        TreeMap<Integer, Rocket> frenquencyRocket = new TreeMap<>(Collections.reverseOrder());
        for (Rocket rocket : rockets) {

            if (rocket.getName().equals("rocket_0"))
                frenquencyRocket.put(Icount0, rocket);

            if (rocket.getName().equals("rocket_1"))
                frenquencyRocket.put(Icount1, rocket);

            if (rocket.getName().equals("rocket_2"))
                frenquencyRocket.put(Icount2, rocket);

            if (rocket.getName().equals("rocket_3"))
                frenquencyRocket.put(Icount3, rocket);

            if (rocket.getName().equals("rocket_4"))
                frenquencyRocket.put(Icount4, rocket);
        }

        if(frenquencyRocket.size() > k)
            return new ArrayList<>(frenquencyRocket.values()).subList(0, k);
        else
            return new ArrayList<>(frenquencyRocket.values());

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
    public String dominantCountry(String orbit) { return null;}

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

    public List<LaunchServiceProvider> highestRevenueLaunchServiceProviders(int k) {

        double ULAMoney = 0;
        double SpaceXMoney = 0;
        double ESAMoney = 0;


        Collection<Launch> launches = dao.loadAll(Launch.class);

        for (Launch launch : launches){

            if(launch.getLaunchServiceProvider().getName().equals("ULA"))  {
                ULAMoney = ULAMoney + launch.getPrice();

            }

            if(launch.getLaunchServiceProvider().getName().equals("SpaceX"))  {
                SpaceXMoney = SpaceXMoney + launch.getPrice();
            }

            if(launch.getLaunchServiceProvider().getName().equals("ESA"))  {
                ESAMoney = ESAMoney + launch.getPrice();
            }

        }

        Double ULAM =new Double(ULAMoney);
        Double SpaceM = new Double(SpaceXMoney);
        Double ESAM = new Double(ESAMoney);

        Collection<LaunchServiceProvider> serviceProvider = dao.loadAll(LaunchServiceProvider.class);
        TreeMap<Double, LaunchServiceProvider> providerMoney = new TreeMap<>(Collections.reverseOrder());

        for (LaunchServiceProvider lsp : serviceProvider) {

            if (lsp.getName().equals("ULA"))
                providerMoney.put(ULAM ,lsp);

            if (lsp.getName().equals("SpaceX"))
                providerMoney.put(SpaceM, lsp);

            if (lsp.getName().equals("ESA"))
                providerMoney.put(ESAM, lsp);

        }

        if(providerMoney.size() > k)
            return new ArrayList<>(providerMoney.values()).subList(0, k);
        else
            return new ArrayList<>(providerMoney.values());


    }



}
