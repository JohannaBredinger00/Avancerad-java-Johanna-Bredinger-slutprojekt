package org.example.slutprojektfe;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

public class HelloController {

    // Kopplade komponenter från FXML
    @FXML
    private TableView<Task> taskTable;

    @FXML
    private TableColumn<Task, Integer> idColumn;

    @FXML
    private TableColumn<Task, String> nameColumn;

    @FXML
    private TableColumn<Task, String> descriptionColumn;

    @FXML
    private TextField nameField;

    @FXML
    private TextField descriptionField;

    @FXML
    private Button addButton, editButton, deleteButton, viewAllButton;

    // Lista för att hålla uppgifter
    private final ObservableList<Task> tasks = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Initiera TableView-kolumner
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        taskTable.setItems(tasks);

        // Visa alla uppgifter vid start
        viewAllTasks();
    }

    @FXML
    protected void viewAllTasks() {
        try {
            URL url = new URL("http://localhost:8080/api/tasks");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            ObjectMapper mapper = new ObjectMapper();
            Task[] taskArray = mapper.readValue(conn.getInputStream(), Task[].class);
            tasks.setAll(Arrays.asList(taskArray));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void handleAddTask() {
        String name = nameField.getText();
        String description = descriptionField.getText();

        if (name.isEmpty() || description.isEmpty()){
            return;
        }

        try {
            Task newTask = new Task(name, description);
            URL url = new URL("http://localhost:8080/api/tasks");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");

            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(conn.getOutputStream(), newTask);

            if (conn.getResponseCode() == HttpURLConnection.HTTP_CREATED) {
                Task createdTask = mapper.readValue(conn.getInputStream(), Task.class);
                tasks.add(createdTask);

                nameField.clear();
                descriptionField.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void handleEditTask() {
        Task selectedTask = taskTable.getSelectionModel().getSelectedItem();
        if (selectedTask != null) {
            String name = nameField.getText();
            String description = descriptionField.getText();

            if (name.isEmpty() || description.isEmpty()){
                return;
            }

            try {
                selectedTask.setName(name);
                selectedTask.setDescription(description);

                URL url = new URL("http://localhost:8080/api/tasks/" + selectedTask.getId());
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("PUT");
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/json");

                ObjectMapper mapper = new ObjectMapper();
                mapper.writeValue(conn.getOutputStream(), selectedTask);

                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    taskTable.refresh();
                    nameField.clear();
                    descriptionField.clear();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    protected void handleDeleteTask() {
        Task selectedTask = taskTable.getSelectionModel().getSelectedItem();
        if (selectedTask != null) {
            try {
                URL url = new URL("http://localhost:8080/api/tasks/" + selectedTask.getId());
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("DELETE");

                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    tasks.remove(selectedTask);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
