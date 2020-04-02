package nl.tudelft.oopp.demo.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import nl.tudelft.oopp.demo.communication.Authenticator;

public class HamburgerMenuSceneController implements Initializable {

    private MainSceneController mainSceneController;
    private HeaderSceneController headerSceneController;
    private RestaurantSceneController restaurantSceneController;
    private ReservationSceneController reservationSceneController;
    private AdminSceneController adminSceneController;
    private OrderSceneController orderSceneController;

    private Parent reservationRoot;
    private Parent restaurantRoot;
    private Parent sidebarFilterRoot;
    private Parent sidebarRoot;
    private Parent adminPanelRoot;
    private Parent orderRoot;

    public FXMLLoader sidebarFilterLoader;

    @FXML public Button adminButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        FXMLLoader reservationLoader = new FXMLLoader(getClass().getResource("/Scenes/reservationScene.fxml"));
        sidebarFilterLoader = new FXMLLoader(getClass().getResource("/Scenes/sidebarFilterScene.fxml"));
        FXMLLoader sidebarLoader = new FXMLLoader(getClass().getResource("/Scenes/sidebarScene.fxml"));
        FXMLLoader restaurantLoader = new FXMLLoader(getClass().getResource("/Scenes/restaurantScene.fxml"));
        FXMLLoader adminPanelLoader = new FXMLLoader(getClass().getResource("/Scenes/adminScene.fxml"));
        FXMLLoader orderLoader = new FXMLLoader(getClass().getResource("/Scenes/orderScene.fxml"));

        try {
            reservationRoot = reservationLoader.load();
            sidebarFilterRoot = sidebarFilterLoader.load();
            sidebarRoot = sidebarLoader.load();
            restaurantRoot = restaurantLoader.load();
            adminPanelRoot = adminPanelLoader.load();
            orderRoot = orderLoader.load();

        } catch (IOException e) {
            e.printStackTrace();
        }

        reservationSceneController = reservationLoader.getController();
        restaurantSceneController = restaurantLoader.getController();
        adminSceneController = adminPanelLoader.getController();

        restaurantSceneController.setController(mainSceneController, this);

        reservationSceneController.setControllers(this);
        reservationSceneController.init();
        adminSceneController.setControllers(mainSceneController);
        if (!Authenticator.isAdmin()) {
            adminButton.setVisible(false);
        }
    }

    /**
     * Set controllers for the class.
     * @param mainSceneController Main Scene
     * @param headerSceneController Header Scene
     */
    public void setController(MainSceneController mainSceneController, HeaderSceneController headerSceneController) {
        this.mainSceneController = mainSceneController;
        this.headerSceneController = headerSceneController;

    }

    /**
     * Open reservations page.
     * @param event mouse click
     */
    public void openReservations(MouseEvent event) {
        mainSceneController.borderPane.setRight(mainSceneController.emptySidebarRightRoot);
        mainSceneController.changeCenter(reservationRoot);
        mainSceneController.sidebar = (sidebarFilterRoot);
        headerSceneController.changeLeft();
    }

    /**
     * Open calendar page.
     * @param event mouse click
     */
    public void openCalendar(MouseEvent event) {
        mainSceneController.borderPane.setRight(mainSceneController.emptySidebarRightRoot);
        mainSceneController.changeCenter(mainSceneController.calendarRoot);
        mainSceneController.sidebar = (MainSceneController.emptySidebarLeftRoot);
        headerSceneController.changeLeft();
        CalendarSceneController calendarSceneController = mainSceneController.calendarLoader.getController();
        calendarSceneController.init();
    }

    /**
     * Open restaurant page.
     * @param event mouse click
     */
    public void openRestaurants(MouseEvent event) {
        mainSceneController.borderPane.setRight(mainSceneController.emptySidebarRightRoot);
        mainSceneController.changeCenter(restaurantRoot);
        mainSceneController.sidebar = (MainSceneController.emptySidebarLeftRoot);
        headerSceneController.changeLeft();
    }

    /**
     * Open admin page.
     * @param event mouse click
     */
    public void openAdminPanel(MouseEvent event) {
        mainSceneController.borderPane.setRight(mainSceneController.emptySidebarRightRoot);
        mainSceneController.changeCenter(adminPanelRoot);
        mainSceneController.sidebar = (MainSceneController.emptySidebarLeftRoot);
        headerSceneController.changeLeft();
    }

    /**
     * Open Basket scene
     */
    public void openOrder(){
        mainSceneController.borderPane.setRight(orderRoot);
    }
}
