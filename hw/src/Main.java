import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.InputMismatchException;
import java.util.Objects;
import java.util.Scanner;


public class Main {
    private static String folder = "test";
    private static String result = "output/ans.txt";

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int option = 0;
        do {
            menu();
            while (option != 1 && option != 2) {
                try {
                    option = in.nextInt();
                } catch (InputMismatchException ex) {
                    System.out.println("Неккоректный ввод. Введите '1' или '2'.");
                }
            }
            if (option == 1 || (getFolderDirectory(in) && getFileDirectory(in))) {
                try {
                    FileSorter sorter = new FileSorter(folder, result);
                    if (sorter.sort()) {
                        sorter.mergeFiles();
                    } else {
                        System.out.println("Ошибка при сортировки файлов.");
                    }
                } catch (IOException ex) {
                    System.out.println("Ошибка при получении файлов.");
                } catch (RuntimeException ex) {
                    System.out.println("Ошибка при конкатенации отсортированных файлов.");
                }
            }
            System.out.println("Завершение программы.");
            System.out.println("Если вы желаете воспользоваться программой снова, введите 'y'.\n" +
                    "Для завершения работы программы нажмите на любую кнопку.");
        } while (Objects.equals(in.next(), "y"));

    }

    private static boolean getFolderDirectory(Scanner in) {
        System.out.print("Введите директорию рабочей папки: ");
        folder = in.next();
        Path directory = Paths.get(folder);
        if (!Files.exists(directory)) {
            System.out.println("Введена неккоректная директория или программа не имеет к ней доступа.");
            return false;
        }
        return true;
    }

    private static boolean getFileDirectory(Scanner in) {
        System.out.print("Введите путь до итогового файла: ");
        result = in.next();
        Path directory = Paths.get(result);
        if (!Files.exists(directory)) {
            System.out.println("Введена неккоректная директория или программа не имеет к ней доступа.");
            return false;
        }
        return true;
    }

    private static void menu() {
        System.out.println("1. Использовать директории по умолчанию.\n" +
                "2. Выбрать директории.");
    }
}