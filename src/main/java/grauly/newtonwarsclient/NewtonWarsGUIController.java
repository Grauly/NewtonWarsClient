package grauly.newtonwarsclient;

import javafx.event.ActionEvent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class NewtonWarsGUIController {

    public Canvas canvas;
    public TextField powerInputField;
    public TextField angleInputField;

    public void onRawMessageSendButtonPressed(ActionEvent actionEvent) {

    }

    public void onShootPressed(ActionEvent actionEvent) {
        updateCanvas();
    }

    public void updateCanvas() {
        try {
            var rawPowerText = powerInputField.getText();
            var rawAngleText = angleInputField.getText();
            float power = Float.parseFloat(rawPowerText);
            float angle = Float.parseFloat(rawAngleText);

            var context = canvas.getGraphicsContext2D();
            context.clearRect(0,0,300,300);
            context.setFill(Paint.valueOf(Color.BLACK.toString()));
            context.setStroke(Paint.valueOf(Color.BLACK.toString()));
            baseDraw(context);
            var x = Math.sin(Math.toRadians(angle)) * power * 10;
            var y = -Math.cos(Math.toRadians(angle)) * power * 10;
            context.setFill(Paint.valueOf(Color.RED.toString()));
            context.setStroke(Paint.valueOf(Color.RED.toString()));
            context.strokeLine(150,150, 150 + x,150 + y);
        } catch (NumberFormatException e) {

        }
    }

    public void baseDraw(GraphicsContext context) {
        context.strokeOval(50, 50, 200, 200);
        context.strokeRect(0, 0, 300, 300);
        context.fillOval(145, 145, 10, 10);

        context.strokeLine(25, 149, 275, 151);
        context.strokeLine(149, 25, 151, 275);
        context.fillText("0", 150 - 5, 20);
        context.fillText("90", 280, 150 + 5);
        context.fillText("180", 150 - 10, 280 + 7);
        context.fillText("270", 2, 150 + 5);
    }
}