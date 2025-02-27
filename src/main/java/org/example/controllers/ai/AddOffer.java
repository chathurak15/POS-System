package org.example.controllers.ai;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import org.example.service.custom.OfferService;
import org.example.service.custom.impl.ProductServiceIMPL;
import org.example.tm.RelationProductTM;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class AddOffer {

    public AnchorPane addDiscountPane;
    public TextField txtDiscount;
    public ComboBox cmbFree;

    private Map<String, Integer> productMap = new HashMap<>();
    private final OfferService offerService = new OfferService();


    public void closeOnClick(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to exit?", ButtonType.YES, ButtonType.NO);
        alert.setHeaderText(null);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.YES) {

            try {
                addDiscountPane.getChildren().clear();
                Parent product = FXMLLoader.load(getClass().getResource("/view/admin/foodRelationships/FoodRelationships.fxml"));
                addDiscountPane.getChildren().add(product);

            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, "UI- Load error || please Contact Developer||").show();
                e.printStackTrace();
            }
        }
    }

    public void AddOnClick(ActionEvent actionEvent) {
        if (cmbFree.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select a product first.");
            alert.setHeaderText(null);
            alert.showAndWait();
            return;
        }
        String selectedProduct = String.valueOf(cmbFree.getValue()); // Directly retrieve the selected value
        Integer freeProductId = productMap.get(selectedProduct);

        if (freeProductId == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Selected product is invalid.");
            alert.setHeaderText(null);
            alert.showAndWait();
            return;
        }

        Integer mainProductId=0;

        List<String>products=  cmbFree.getItems();
        for (String item : products) {
            Integer productId = productMap.get(item);
            if (productId != null && !productId.equals(freeProductId)) {
                mainProductId = productId;
                break;
            }
        }

        if (mainProductId == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Unable to determine main product.");
            alert.setHeaderText(null);
            alert.showAndWait();
            return;
        }

        boolean rs = offerService.saveOffer(mainProductId, freeProductId);
        if (rs) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Offer added successfully!");
            alert.showAndWait();

            try {
                addDiscountPane.getChildren().clear();
                Parent user = FXMLLoader.load(getClass().getResource("/view/admin/foodRelationships/FoodRelationships.fxml"));
                addDiscountPane.getChildren().add(user);

            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, "UI- Load error || please Contact Developer||").show();
                e.printStackTrace();
            }
        }else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);alert.setContentText("Offer add Failed! Try again!");alert.showAndWait();;
        }

    }



    public void cmbFree(List<RelationProductTM> relationProductTMList) {
        ObservableList<String> productNames = FXCollections.observableArrayList();

        for (RelationProductTM relation : relationProductTMList) {
            // Parse product details to get names and IDs
            String[] firstProductDetails = relation.getFirstProduct().split(" - ");
            String[] secondProductDetails = relation.getSecondProduct().split(" - ");

            // Ensure proper format before extracting
            if (firstProductDetails.length >= 2) {
                String firstProductName = firstProductDetails[1]; // Product name
                int firstProductId = Integer.parseInt(firstProductDetails[0]); // Product ID
                productNames.add(firstProductName);
                productMap.put(firstProductName, firstProductId); // Map name to ID
            }

            if (secondProductDetails.length >= 2) {
                String secondProductName = secondProductDetails[1];
                int secondProductId = Integer.parseInt(secondProductDetails[0]);
                productNames.add(secondProductName);
                productMap.put(secondProductName, secondProductId);
            }
        }

        cmbFree.setItems(productNames);
    }
}
