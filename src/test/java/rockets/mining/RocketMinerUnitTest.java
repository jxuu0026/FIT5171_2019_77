package rockets.mining;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rockets.dataaccess.DAO;
import rockets.dataaccess.neo4j.Neo4jDAO;
import rockets.model.Launch;
import rockets.model.LaunchServiceProvider;
import rockets.model.Rocket;

import java.util.*;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class RocketMinerUnitTest {
    Logger logger = LoggerFactory.getLogger(RocketMinerUnitTest.class);

    private DAO dao;
    private RocketMiner miner;
    private List<Rocket> rockets;
    private List<LaunchServiceProvider> lsps;
    private List<Launch> launches;

    @BeforeEach
    public void setUp() {
        dao = mock(Neo4jDAO.class);
        miner = new RocketMiner(dao);
        rockets = Lists.newArrayList();

        lsps = Arrays.asList(
                new LaunchServiceProvider("ULA", 1990, "USA"),
                new LaunchServiceProvider("SpaceX", 2002, "USA"),
                new LaunchServiceProvider("ESA", 1975, "Europe ")
        );

        // index of lsp of each rocket
        int[] lspIndex = new int[]{0, 1, 2, 1, 2};
        // 5 rockets
        for (int i = 0; i < 5; i++) {
            rockets.add(new Rocket("rocket_" + i, "USA", lsps.get(lspIndex[i])));
        }

        Calendar calendar = new GregorianCalendar(2017, 01, 01);
        // month of each launch
        int[] months = new int[]{1, 6, 4, 3, 4, 11, 6, 5, 12, 5};

        // index of rocket of each launch
        int[] rocketIndex = new int[]{0, 0, 0, 0, 1, 1, 1, 2, 2, 4};


       // 10 launches
        launches = IntStream.range(0, 10).mapToObj(i -> {
            logger.info("create " + i + " launch in month: " + months[i]);
            Launch l = new Launch();
            calendar.set(Calendar.MONTH, months[i]);

            l.setLaunchVehicle(rockets.get(rocketIndex[i]));
            l.setLaunchServiceProvider(rockets.get(rocketIndex[i]).getManufacturer());
            l.setLaunchDate(LocalDate.of(2017, months[i], 1));
            l.setLaunchVehicle(rockets.get(rocketIndex[i]));
            l.setLaunchSite("VAFB");
            l.setOrbit("LEO");
            l.setPrice(i*1000.0);

            if(l.getLaunchServiceProvider().equals(lsps.get(0))) {
                l.setLaunchOutcome(Launch.LaunchOutcome.SUCCESSFUL);
            }

            if(l.getLaunchServiceProvider().equals(lsps.get(1))) {
                l.setLaunchOutcome(Launch.LaunchOutcome.FAILED);
            }

            if(l.getLaunchServiceProvider().equals(lsps.get(2))) {
                l.setLaunchOutcome(Launch.LaunchOutcome.FAILED);
            }
            else {}
            spy(l);
            return l;
        }).collect(Collectors.toList());



    }

    
    @Test
    public void testMostExpensiveLaunches() {

         when(dao.loadAll(Launch.class)).thenReturn(launches);

         int k = 1;

         List<Launch> loadedLaunches = miner.mostExpensiveLaunches(k);
         assertEquals(k, loadedLaunches.size());
         assertEquals( launches.get(9),  loadedLaunches.get(0));

         k = 2;

         loadedLaunches = miner.mostExpensiveLaunches(k);
         assertEquals(k, loadedLaunches.size());
         assertEquals( launches.get(8),  loadedLaunches.get(1));



    }

    @Test
    public void testHighestAveneuLaunchServiceProviders() {

        when(dao.loadAll(LaunchServiceProvider.class)).thenReturn(lsps);
        when(dao.loadAll(Launch.class)).thenReturn(launches);

        int k = 1;
        List<LaunchServiceProvider> loadedLaunchServiceProvider = miner.highestRevenueLaunchServiceProviders(k);
        assertEquals(k, loadedLaunchServiceProvider.size());
        assertEquals( lsps.get(2),  loadedLaunchServiceProvider.get(0));


        k = 2;
        loadedLaunchServiceProvider = miner.highestRevenueLaunchServiceProviders(k);
        assertEquals(k, loadedLaunchServiceProvider.size());
        assertEquals( lsps.get(1),  loadedLaunchServiceProvider.get(1));


    }


    @Test
    public void testMostReliableLaunchServiceProviders() {

        when(dao.loadAll(LaunchServiceProvider.class)).thenReturn(lsps);
        when(dao.loadAll(Launch.class)).thenReturn(launches);

        int k = 1;
        List<LaunchServiceProvider> loadedLaunchServiceProvider = miner.mostReliableLaunchServiceProviders(k);
        assertEquals(k, loadedLaunchServiceProvider.size());
        assertEquals( lsps.get(0),  loadedLaunchServiceProvider.get(0));


        k = 2;
        loadedLaunchServiceProvider = miner.mostReliableLaunchServiceProviders(k);
        assertEquals(k, loadedLaunchServiceProvider.size());

    }

    @Test
    public void testMostLaunchedRocket() {

        when(dao.loadAll(Rocket.class)).thenReturn(rockets);
        when(dao.loadAll(Launch.class)).thenReturn(launches);

        int k = 1;
        List<Rocket> serviveRocket = miner.mostLaunchedRockets(k);
        assertEquals(k, serviveRocket.size());
        assertEquals( rockets.get(0),  serviveRocket.get(0));

        k = 2;
        serviveRocket = miner.mostLaunchedRockets(k);
        assertEquals(k, serviveRocket.size());
        assertEquals(rockets.get(1), serviveRocket.get(1));

    }
    }


