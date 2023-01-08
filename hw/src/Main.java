import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.InputMismatchException;
import java.util.Objects;
import java.util.Scanner;

/**
 * Запуск программы и логика работы с пользователем
 * */
public class Main {
    /**
     * Директория папки теста
     * */
    private static String folder = "./test";

    /**
     * Путь к файлу вывода
     * */
    private static String result = "./result.txt";

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String option = "0";
        do {
            menu();
            while (!Objects.equals(option, "1") && !Objects.equals(option, "2")) {
                try {
                    option = in.next();
                    if (!Objects.equals(option, "1") && !Objects.equals(option, "2")){
                        System.out.println("Неккоректный ввод. Введите '1' или '2'.");
                    }
                } catch (InputMismatchException ex) {
                    System.out.println("Неккоректный ввод. Введите '1' или '2'.");
                }
            }
            if (option.equals("1") || (getFolderDirectory(in) && getFileDirectory(in))) {
                try {
                    FileSorter sorter = new FileSorter(folder, result);
                    if (sorter.sort()) {
                        sorter.mergeFiles();
                    } else {
                        System.out.println("Ошибка при сортировке файлов.");
                    }
                } catch (IOException ex) {
                    System.out.println("Ошибка при получении файлов.");
                } catch (RuntimeException ex) {
                    System.out.println("Ошибка при конкатенации отсортированных файлов.");
                }
            }
            System.out.println("Завершение программы.");
            System.out.println("Если вы желаете воспользоваться программой снова, введите 'y'.\n" +
                    "Для завершения работы программы введите любой другой текст.");
            option = "0";
        } while (Objects.equals(in.next(), "y"));
    }

    /**
     * Получение пользовательской директории с консоли
     * */
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

    /**
     * Получение пути до файла вывода с консоли
     * */
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

    /**
     * Меню
     * */
    private static void menu() {
        System.out.println("1. Использовать директории по умолчанию.\n" +
                "2. Выбрать директории.");
    }
}