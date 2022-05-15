package com.example.ccvalidatorsb;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;

import java.util.Arrays;

public class CCValidatorSBController {

    // Variables for the screen
    @FXML   private ImageView imgv_card;
    @FXML   private ImageView imgv_oknok;
    @FXML   private TextField txt_card_number;
    @FXML   private TextField txt_exp_date;
    @FXML   private TextField txt_cvv;

    // Constants for sizing and positioning
    static final double IMG_WIDTH = 35;
    static final double IMG_HEIGHT = 22;

    // Constants for credit card
    static final int CREDIT_CARD_SIZE = 16;
    static final int CVV_NORMAL_SIZE = 3;
    static final int CVV_AMEX_SIZE = 4;

    // Enum for the type of credit card
    public enum CreditCardType {
        NoCard,
        Visa,
        Mastercard,
        Amex,
        JCB
    }

    // Static variables for images for the types of the different credit cards and for the OK/NOK pictures
    static Image img_nocard = new Image("file:img/Nocard.jpg", IMG_WIDTH, IMG_HEIGHT, false, true);
    static Image img_visa = new Image("file:img/Visa.jpg", IMG_WIDTH, IMG_HEIGHT, false, true);
    static Image img_mastercard = new Image("file:img/Mastercard.jpg", IMG_WIDTH, IMG_HEIGHT, false, true);
    static Image img_amex = new Image("file:img/Amex.png", IMG_WIDTH, IMG_HEIGHT, false, true);
    static Image img_jcb = new Image("file:img/Jcb.png", IMG_WIDTH, IMG_HEIGHT, false, true);
    static Image img_ok = new Image("file:img/Ok.png", IMG_WIDTH, IMG_HEIGHT, false, true);
    static Image img_nok = new Image("file:img/Nok.png", IMG_WIDTH, IMG_HEIGHT, false, true);
    static Image img_nooknok = new Image("file:img/NoOkNok.png", IMG_WIDTH, IMG_HEIGHT, false, true);

    // Global variables
    CreditCardType ccType;
    int cvvSize;

    public void initialize() {
        imgv_card.setImage(img_nocard);     // Start with no card type image
        imgv_oknok.setImage(img_nooknok);   // Start with no ok/nok image

        // Set initial global variables
        ccType = CreditCardType.NoCard;
        cvvSize = CVV_NORMAL_SIZE;
    }

    @FXML
    protected void onCCKeyReleased(KeyEvent event) {
        TextField t = (TextField) event.getSource();

        // Limit the size of the text to the length of a credit card
        if (t.getLength() > CREDIT_CARD_SIZE) {
            t.setText(t.getText().substring(0, CREDIT_CARD_SIZE));
            t.positionCaret(t.getLength());
        }

        // Set type of card and cvv size
        ccType = getCardType(t.getText());
        if (ccType == CreditCardType.Amex) {
            cvvSize = CVV_AMEX_SIZE;
        }
        else {
            cvvSize = CVV_NORMAL_SIZE;
        }

        // Set card image according to the type of card
        imgv_card.setImage(getCardImage(ccType));

        // Set ok/nok image according to the numbers (valid/invalid card/no card)
        imgv_oknok.setImage(getOkNokImage(t.getText()));
    }


    @FXML
    protected void onCVVKeyReleased(KeyEvent event) {
        TextField t = (TextField) event.getSource();

        // Limit the size of the text to the length of a credit card
        if (t.getLength() > cvvSize) {
            t.setText(t.getText().substring(0,cvvSize));
            t.positionCaret(t.getLength());
        }
    }


    // Image getOkNokImage(String ccNumber)
    // This function returns an image that represents the validity of the credit card number (ccNumber) informed:
    // - Less than the size of a credit card: img_nooknok
    // - Credit card valid: img_ok
    // - Credit card invalid: img_nok
    private Image getOkNokImage(String ccNumber) {
        if (ccNumber.length() != CREDIT_CARD_SIZE) {
            return img_nooknok;
        }
        if (isCcValid(ccNumber)) {
            return img_ok;
        }

        // Credit card number not valid, return Nok image
        return img_nok;
    }

    // CreditCardType getCardType(String ccNumber)
    // This function returns the type of the credit card informed (ccNumber): Visa, Mastercard, etc.
    private CreditCardType getCardType(String ccNumber) {
        // Return the proper card type according to the numbers in text
        if (ccNumber.length() >= 1 && ccNumber.charAt(0) == '4') {
            return CreditCardType.Visa;
        }
        if (ccNumber.length() >= 1 && ccNumber.charAt(0) == '5') {
            return CreditCardType.Mastercard;
        }
        if (ccNumber.length() >= 2 && (ccNumber.substring(0, 2).equals("34") || ccNumber.substring(0, 2).equals("37"))) {
            return CreditCardType.Amex;
        }
        if (ccNumber.length() >= 2 && ccNumber.substring(0, 2).equals("35")) {
            return CreditCardType.JCB;
        }

        // No rule was validated, use the type for no card
        return CreditCardType.NoCard;
    }

    // Image getCardImage(CreditCardType ccType)
    // This function returns an image representing the credit card type informed (ccType).
    private Image getCardImage(CreditCardType ccType) {
        // Return the proper card image according to the type
        switch (ccType) {
            case NoCard:
                return img_nocard;
            case Visa:
                return img_visa;
            case Mastercard:
                return img_mastercard;
            case Amex:
                return img_amex;
            case JCB:
                return img_jcb;
            default:
                // No known type, so use the image for no known card
                return img_nocard;
        }
    }

    // boolean isCcValid(String ccNumber)
    // This function returns a boolean indicating if the credit card informed is valid (true) or not (false).
    public static boolean isCcValid(String ccNumber) {
        // int array for processing the cardNumber
        int[] cardIntArray = new int[ccNumber.length()];

        for (int i = 0; i < ccNumber.length(); i++) {
            char c = ccNumber.charAt(i);
            if (c < '0' || c > '9') {   // make sure all characters are digits
                return false;
            }
            cardIntArray[i] = Integer.parseInt("" + c);
        }

        for (int i = cardIntArray.length - 2; i >= 0; i = i - 2) {
            int num = cardIntArray[i];
            num = num * 2;  // step 1
            if (num > 9) {
                num = num % 10 + num / 10;  // step 2
            }
            cardIntArray[i] = num;
        }

        int sum = Arrays.stream(cardIntArray).sum();  // step 3

        if (sum % 10 == 0) { // step 4
            return true;
        }

        return false;
    }
}