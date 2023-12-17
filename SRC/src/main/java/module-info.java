module org.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;




    requires com.almasb.fxgl.all;
    requires org.antlr.antlr4.runtime;

    opens org.example.j_p_moldmaker to javafx.fxml;
    exports org.example.j_p_moldmaker;
}