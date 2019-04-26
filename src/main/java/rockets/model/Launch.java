package rockets.model;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;

public class Launch extends Entity {
    public enum LaunchOutcome {
        FAILED, SUCCESSFUL
    }

    private LocalDate launchDate;

    private Rocket launchVehicle;

    private LaunchServiceProvider launchServiceProvider;

    private String launchSite;

    private String orbit;

    private String function;

    private double price;

    private LaunchOutcome launchOutcome;


    public LocalDate getLaunchDate() {
        return launchDate;
    }

    public void setLaunchDate(LocalDate launchDate) {

        LaunchValidation.yearCanNotGreaterThanCurrentYear(launchDate.getYear());
        this.launchDate = launchDate;
    }

    public Rocket getLaunchVehicle() {
        return launchVehicle;
    }

    public void setLaunchVehicle(Rocket launchVehicle) {

        notNull(launchVehicle);
        this.launchVehicle = launchVehicle;
    }

    public LaunchServiceProvider getLaunchServiceProvider() {
        return launchServiceProvider;
    }

    public void setLaunchServiceProvider(LaunchServiceProvider launchServiceProvider) {
        this.launchServiceProvider = launchServiceProvider;
    }



    public String getLaunchSite() {

        return launchSite;
    }

    public void setLaunchSite(String launchSite) {

        notBlank(launchSite,"launchSite cannot be null or empty");
        this.launchSite = launchSite;
    }

    public String getOrbit() {
        return orbit;
    }

    public void setOrbit(String orbit) {

        notBlank(orbit,"orbit cannot be null or empty");
        this.orbit = orbit;
    }

    public String getFunction() { return function; }

    public void setFunction(String function) {

        notBlank(function,"function cannot be null or empty");
        this.function = function;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public LaunchOutcome getLaunchOutcome() {
        return launchOutcome;
    }

    public void setLaunchOutcome(LaunchOutcome launchOutcome) {
        this.launchOutcome = launchOutcome;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Launch launch = (Launch) o;
        return Objects.equals(launchDate, launch.launchDate) &&
                Objects.equals(launchVehicle, launch.launchVehicle) &&
                Objects.equals(launchServiceProvider, launch.launchServiceProvider) &&
                Objects.equals(orbit, launch.orbit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(launchDate, launchVehicle, launchServiceProvider, orbit);
    }
}
