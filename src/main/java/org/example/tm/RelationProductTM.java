package org.example.tm;

import javafx.scene.control.Button;
import lombok.Data;

@Data
public class RelationProductTM {
    private int offerId;
    private String firstProduct;
    private String secondProduct;
    private Button addOffer;
}
