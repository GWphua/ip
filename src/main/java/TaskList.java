import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class TaskList {
    /**
     * This is the array that Virushade keeps the tasks in.
     */
    private static final ArrayList<Task> TASK_ARRAY_LIST = new ArrayList<>();

    /**
     * This is the file name of our file that stores data on TaskList.
     */
    private static final String TASK_LIST_FILE_NAME = "data/Virushade.txt";

    /**
     * This variable keeps track of the size of the TaskList.
     */
    private static int listCount = 0;

    /**
     * A function that writes an input string to a file.
     *
     * @param filePath The name of the file
     * @param text The input string to write into the file.
     */
    private static void updateFile(String filePath, String text) {
        try {
            File f = new File(filePath);
            if (!f.exists()) {
                f.getParentFile().mkdirs();
                f.createNewFile();
            }

            FileWriter fw = new FileWriter(f);
            fw.write(text);
            fw.close();
        } catch (IOException e) {
            System.out.println("Unable to update file.\n" + e);
        }
    }

    /**
     * Partitions the string into 2, seperated by the first '/'.
     * @param str The input string
     * @return The pair of strings.
     */
    private static String[] slashPartition(String str) {
        int index = str.indexOf('/');

        String[] pair = new String[2];

        if (index > -1) {
            // Gets rid of a space in the end of the first partition (If there is one).
            if (str.charAt(index - 1) == ' ') {
                pair[0] = str.substring(0, index - 1);
            } else {
                pair[0] = str.substring(0, index);
            }

            pair[1] = str.substring(index + 1);
        } else {
            // If there exists no '/' in the string, set the tail of the pair as an empty string.
            pair[0] = str;
            pair[1] = "";
        }
        return pair;
    }

    /**
     * Adds a Task to taskList.
     * @param addedTaskDescription Name of the added Task.
     */
    public static void add(String addedTaskDescription, String taskType){
        if (listCount < 100) {
            Task addedTask;
            String[] pair = slashPartition(addedTaskDescription);

            // Checking if the Task Name is empty (Or filled with Spacebars):
            if (pair[0].replace(" ", "").equals("")) {
                System.out.println("OOPS!!! Please enter the task description!!!");
            }

            switch (taskType) {
            case "TODO":
                addedTask = new ToDo(addedTaskDescription);
                break;

            case "DEADLINE":
                if (pair[1].startsWith("by ")) {
                    addedTask = new Deadline(pair[0], pair[1].substring(3));
                } else {
                    System.out.println("Please include a deadline after your task name. " +
                            "(e.g. /by Sunday)");
                    return;
                }
                break;

            case "EVENT":

                pair = slashPartition(addedTaskDescription);
                if (pair[1].startsWith("at ")) {
                    addedTask = new Event(pair[0], pair[1].substring(3));
                } else {
                    System.out.println("Please include a time after your task name. " +
                            "(e.g. /at 12 noon)");
                    return;
                }
                break;

            // The add function would not reach this line at all.
            default:
                addedTask = new Task(addedTaskDescription);

            }

            TASK_ARRAY_LIST.add(addedTask);
            listCount++;
            System.out.println("Added: " + addedTask.getTaskDescription());
            System.out.printf("Now you have %d tasks in the list.\n", listCount);
            updateFile(TASK_LIST_FILE_NAME, generateList());

        } else {
            System.out.println("Sorry, Virushade cannot keep track of more than 100 tasks!!!");
        }
    }

    /**
     * Deletes the specified task.
     * @param str Input string, determines which task to delete.
     */
    public static void delete(String str) {
        try {
            // If what comes after "delete " is not an integer, this will throw a NumberFormatException.
            int index = Integer.parseInt(str);

            if (index <= 0) {
                System.out.println("Please enter an integer greater than 0.");
            } else if (index <= listCount) {
                Task deletedTask = TASK_ARRAY_LIST.get(index - 1);
                deletedTask.deleteMessage();
                listCount--;
                System.out.printf("You have %d tasks in the list.\n", listCount);
                updateFile(TASK_LIST_FILE_NAME, generateList());
            } else {
                System.out.println("Please check that you have entered the correct number!");
            }
        } catch (NumberFormatException e) {

            // Tells the user that he did not enter a number.
            System.out.println("Please enter an integer after 'done ' instead.\n" + e);
        }
    }

    /**
     * Marks a task as complete.
     * @param str Input string, determines which task to mark as complete.
     */
    public static void completeTask(String str) {

        try {
            // If what comes after "done " is not an integer, this will throw a NumberFormatException.
            int index = Integer.parseInt(str);

            if (index <= 0) {
                System.out.println("Please enter an integer greater than 0.");
            } else if (index <= listCount) {
                Task doneTask = TASK_ARRAY_LIST.get(index - 1);
                doneTask.completeTask();
                updateFile(TASK_LIST_FILE_NAME, generateList());
            } else {
                System.out.println("Please check that you have entered the correct number!");
            }
        } catch (NumberFormatException e) {

            // Tells the user that he did not enter a number.
            System.out.println("Please enter an integer after 'done ' instead.\n" + e);
        }
    }

    /**
     * Display the stored values in taskList for the user.
     */
    public static void list() {
        System.out.println(generateList());
    }

    /**
     * @return String representation of the tasks within TASK_ARRAY_LIST.
     */
    private static String generateList() {
        if (listCount == 0) {
            return "Nothing in the list as of now.";
        }

        int index = 0;
        StringBuilder sb = new StringBuilder("Here are the tasks in your list:");

        while (index < listCount) {
            String taskName = (index + 1) + "." + TASK_ARRAY_LIST.get(index).toString();
            sb.append(System.lineSeparator()).append(taskName);
            index++;
        }

        return sb.toString();
    }
}
