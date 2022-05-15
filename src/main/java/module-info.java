module com.example.ccvalidatorsb {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.ccvalidatorsb to javafx.fxml;
    exports com.example.ccvalidatorsb;
}