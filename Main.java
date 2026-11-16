import java.util.*;
import java.io.*;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        ArrayList<Recipe> recipes = new ArrayList<>();
        ArrayList<GroceryItem> groceryList = new ArrayList<>();

        // Load previously saved recipes
        loadRecipes(recipes);

        int choice;

        do {
            System.out.println("\n========== COOKSCALE ==========");
            System.out.println("1. Add Recipe");
            System.out.println("2. View Recipes");
            System.out.println("3. Scale Recipe");
            System.out.println("4. Generate Grocery List");
            System.out.println("5. Manage Grocery List");
            System.out.println("6. Delete Recipe");
            System.out.println("7. Exit");

            System.out.print("Enter your choice: ");
            choice = sc.nextInt();

            switch (choice) {

                case 1:
                    addRecipe(sc, recipes);
                    break;

                case 2:
                    viewRecipes(recipes);
                    break;

                case 3:
                    scaleRecipe(sc, recipes);
                    break;

                case 4:
                    generateGroceryList(sc, recipes, groceryList);
                    break;

                case 5:
                    manageGroceryList(sc, groceryList);
                    break;

                case 6:
                    deleteRecipe(sc, recipes);
                    break;

                case 7:
                    System.out.println("Thank you for using CookScale!");
                    break;

                default:
                    System.out.println("Invalid choice!");
            }

        } while (choice != 7);

        sc.close();
    }


    // FEATURE 1: ADD RECIPE

    public static void addRecipe(
        Scanner sc, ArrayList<Recipe> recipes) {

        sc.nextLine();

        System.out.print("Enter recipe name: ");
        String name = sc.nextLine();

        System.out.print("Enter number of servings: ");
        int servings = sc.nextInt();

        if (servings <= 0) {
            System.out.println("Invalid servings!");
            return;
        }

        Recipe r = new Recipe(name, servings);

        System.out.print("Enter number of ingredients: ");
        int n = sc.nextInt();

        if (n <= 0) {
            System.out.println("Invalid number of ingredients!");
            return;
        }

        sc.nextLine();

        for (int i = 0; i < n; i++) {

            System.out.print("Enter ingredient name: ");
            String ingredient = sc.nextLine();

            System.out.print("Enter quantity: ");
            double quantity = sc.nextDouble();

            sc.nextLine();

            System.out.print("Enter unit: ");
            String unit = sc.nextLine();

            Ingredient ing =
                new Ingredient(ingredient, quantity, unit);

            r.ingredients.add(ing);
        }

        recipes.add(r);

        saveRecipes(recipes);

        System.out.println("\nRecipe added successfully!");
    }


    // FEATURE 2: VIEW RECIPES

    public static void viewRecipes(ArrayList<Recipe> recipes) {

        if (recipes.isEmpty()) {
            System.out.println("No recipes available!");
            return;
        }

        System.out.println("\n========== YOUR RECIPES ==========");

        for (int i = 0; i < recipes.size(); i++) {

            Recipe r = recipes.get(i);

            System.out.println("\nRecipe " + (i + 1));
            System.out.println("Name: " + r.name);
            System.out.println("Servings: " + r.servings);

            System.out.println("Ingredients:");

            for (Ingredient ing : r.ingredients) {

                System.out.println(
                    ing.name + " - " +
                    ing.quantity + " " + ing.unit
                );
            }
        }
    }



    public static void scaleRecipe(
        Scanner sc, ArrayList<Recipe> recipes) {

        if (recipes.isEmpty()) {
            System.out.println("No recipes available!");
            return;
        }

        viewRecipes(recipes);

        System.out.print("\nSelect recipe number: ");
        int index = sc.nextInt();

        if (index < 1 || index > recipes.size()) {
            System.out.println("Invalid recipe number!");
            return;
        }

        Recipe r = recipes.get(index - 1);

        System.out.print("Enter required servings: ");
        int newServings = sc.nextInt();

        if (newServings <= 0) {
            System.out.println("Invalid servings!");
            return;
        }

        double factor = (double) newServings / r.servings;

        System.out.println("\n========== SCALED RECIPE ==========");

        System.out.println("Recipe: " + r.name);
        System.out.println("Original servings: " + r.servings);
        System.out.println("Required servings: " + newServings);

        System.out.println("\nUpdated Ingredients:");

        for (Ingredient ing : r.ingredients) {

            double newQuantity = ing.quantity * factor;

            System.out.println(
                ing.name + " - " +
                newQuantity + " " + ing.unit
            );
        }
    }



    public static void generateGroceryList(
        Scanner sc,
        ArrayList<Recipe> recipes,
        ArrayList<GroceryItem> groceryList) {

        if (recipes.isEmpty()) {
            System.out.println("No recipes available!");
            return;
        }

        viewRecipes(recipes);

        System.out.print("\nSelect recipe number: ");
        int index = sc.nextInt();

        if (index < 1 || index > recipes.size()) {
            System.out.println("Invalid recipe number!");
            return;
        }

        Recipe r = recipes.get(index - 1);

        System.out.print("Enter required servings: ");
        int newServings = sc.nextInt();

        if (newServings <= 0) {
            System.out.println("Invalid servings!");
            return;
        }

        double factor = (double) newServings / r.servings;

        for (Ingredient ing : r.ingredients) {

            double quantity = ing.quantity * factor;

            boolean found = false;

            for (GroceryItem item : groceryList) {

                if (item.name.equalsIgnoreCase(ing.name)
                    && item.unit.equalsIgnoreCase(ing.unit)) {

                    item.quantity += quantity;
                    found = true;
                    break;
                }
            }

            if (!found) {

                groceryList.add(
                    new GroceryItem(
                        ing.name, quantity, ing.unit
                    )
                );
            }
        }

        System.out.println(
            "\nGrocery list generated successfully!"
        );
    }


    public static void manageGroceryList(
        Scanner sc, ArrayList<GroceryItem> groceryList) {

        if (groceryList.isEmpty()) {
            System.out.println("Grocery list is empty!");
            return;
        }

        System.out.println("\n========== GROCERY LIST ==========");

        for (int i = 0; i < groceryList.size(); i++) {

            GroceryItem item = groceryList.get(i);

            String status = item.purchased
                ? "Purchased"
                : "Not Purchased";

            System.out.println(
                (i + 1) + ". " +
                item.name + " - " +
                item.quantity + " " +
                item.unit + " [" + status + "]"
            );
        }

        System.out.println("\n1. Mark Ingredient as Purchased");
        System.out.println("2. Back to Main Menu");

        System.out.print("Enter your choice: ");
        int option = sc.nextInt();

        if (option == 1) {

            System.out.print("Enter ingredient number: ");
            int index = sc.nextInt();

            if (index < 1 || index > groceryList.size()) {
                System.out.println("Invalid ingredient number!");
                return;
            }

            GroceryItem item = groceryList.get(index - 1);

            item.purchased = true;

            System.out.println(
                item.name + " marked as purchased!"
            );

        } else if (option == 2) {

            System.out.println("Returning to main menu...");

        } else {

            System.out.println("Invalid choice!");
        }
    }



    public static void deleteRecipe(
        Scanner sc, ArrayList<Recipe> recipes) {

        if (recipes.isEmpty()) {
            System.out.println("No recipes available to delete!");
            return;
        }

        viewRecipes(recipes);

        System.out.print("\nEnter recipe number to delete: ");
        int index = sc.nextInt();

        if (index < 1 || index > recipes.size()) {
            System.out.println("Invalid recipe number!");
            return;
        }

        Recipe r = recipes.get(index - 1);

        recipes.remove(index - 1);

        saveRecipes(recipes);

        System.out.println(
            "\n" + r.name + " deleted successfully!"
        );
    }


    public static void saveRecipes(ArrayList<Recipe> recipes) {

        try {

            PrintWriter writer =
                new PrintWriter(new FileWriter("recipes.txt"));

            for (Recipe r : recipes) {

                writer.println(r.name);
                writer.println(r.servings);
                writer.println(r.ingredients.size());

                for (Ingredient ing : r.ingredients) {

                    writer.println(
                        ing.name + "|" +
                        ing.quantity + "|" +
                        ing.unit
                    );
                }
            }

            writer.close();

            System.out.println("Recipes saved successfully!");

        } catch (IOException e) {

            System.out.println("Error saving recipes!");
        }
    }

    public static void loadRecipes(ArrayList<Recipe> recipes) {

        File file = new File("recipes.txt");

        if (!file.exists()) {
            return;
        }

        try {

            BufferedReader reader =
                new BufferedReader(new FileReader(file));

            String name;

            while ((name = reader.readLine()) != null) {

                int servings =
                    Integer.parseInt(reader.readLine());

                int n =
                    Integer.parseInt(reader.readLine());

                Recipe r = new Recipe(name, servings);

                for (int i = 0; i < n; i++) {

                    String line = reader.readLine();

                    String[] data = line.split("\\|", -1);

                    String ingredient = data[0];

                    double quantity =
                        Double.parseDouble(data[1]);

                    String unit = data[2];

                    Ingredient ing =
                        new Ingredient(
                            ingredient, quantity, unit
                        );

                    r.ingredients.add(ing);
                }

                recipes.add(r);
            }

            reader.close();

            System.out.println(
                "Previous recipes loaded successfully!"
            );

        } catch (Exception e) {

            System.out.println("Error loading recipes!");
        }
    }

}


class Recipe {

    String name;
    int servings;

    ArrayList<Ingredient> ingredients = new ArrayList<>();

    Recipe(String name, int servings) {

        this.name = name;
        this.servings = servings;
    }
}



class Ingredient {

    String name;
    double quantity;
    String unit;

    Ingredient(String name, double quantity, String unit) {

        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
    }
}



class GroceryItem {

    String name;
    double quantity;
    String unit;
    boolean purchased;

    GroceryItem(String name, double quantity, String unit) {

        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.purchased = false;
    }
}