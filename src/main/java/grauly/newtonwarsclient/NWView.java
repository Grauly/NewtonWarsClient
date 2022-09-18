package grauly.newtonwarsclient;

import grauly.newtonwarsclient.network.ConnectionHandler;
import javafx.event.ActionEvent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.io.IOException;

public class NWView {

    public Canvas canvas;
    public TextField powerInputField;
    public TextField angleInputField;
    public TextField ipInputField;
    public TextField portInputField;
    public TextArea mainLog;
    private static ConnectionHandler connectionHandler;
    public TextField rawInputField;
    float power = 10f;
    float angle = 0f;

    public static void stopConnection() throws InterruptedException, IOException {
        if(connectionHandler != null)
            connectionHandler.stop();
    }

    public void onRawMessageSendButtonPressed(ActionEvent actionEvent) {
        ConnectionHandler.outgoingMessages.add(rawInputField.getText());
        rawInputField.clear();
    }

    public void onShootPressed(ActionEvent actionEvent) {
        updateCanvas();
        try {
            var rawPowerText = powerInputField.getText();
            var rawAngleText = angleInputField.getText();
            power = Float.parseFloat(rawPowerText);
            angle = Float.parseFloat(rawAngleText);
            ConnectionHandler.outgoingMessages.add("v " + power);
            ConnectionHandler.outgoingMessages.add(String.valueOf(angle));
        } catch (NumberFormatException e) {

        }
    }

    public void onConnectButtonPressed(ActionEvent actionEvent) {
        updateCanvas();
        try {
            if(connectionHandler == null) {
                connectionHandler = new ConnectionHandler(ipInputField.getText(),Integer.parseInt(portInputField.getText()),this);
            }
        } catch (NumberFormatException e) {
            mainLog.appendText("Invalid port number. Check that it IS a number");
        }
    }

    public void onKeyPressed(KeyEvent keyEvent) {
        float mod = keyEvent.isShiftDown() ? 0.1f : 1f;
        if(keyEvent.getCode() == KeyCode.UP) {
            power += mod;
        }
        if(keyEvent.getCode() == KeyCode.DOWN) {
            power -= mod;
        }
        if(keyEvent.getCode() == KeyCode.LEFT) {
            angle += mod;
        }
        if(keyEvent.getCode() == KeyCode.RIGHT) {
            angle -= mod;
        }
    }

    public void onCanvasClicked(MouseEvent mouseEvent) {
        var x = mouseEvent.getX() - 150f;
        var y = mouseEvent.getY() - 150f;
        angle = (float) Math.toDegrees(Math.atan2(x,-y));
        if(angle < 0) {
            angle += 360;
        }
        updateInputs();
        updateCanvas();
    }

    public synchronized void postUpdate(String update) {
        updateCanvas();
        mainLog.appendText(update);
    }

    private void updateInputs() {
        angleInputField.setText(String.valueOf(angle));
        powerInputField.setText(String.valueOf(power));
    }

    private void updateCanvas() {
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

    private void baseDraw(GraphicsContext context) {
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