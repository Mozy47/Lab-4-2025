import functions.*;
import functions.basic.*;
import java.io.*;

public class Main {
    public static void main(String[] args){
        TestSinCos();
        TabTestSinCos();
        TabTestSquareSinCos();
        TabTestExp();
        TabTestLog();
        TestExter();
        testSerializable();
    }

    // Тест sin и cos
    public static void TestSinCos(){
        System.out.println("=== ТЕСТИРОВАНИЕ SIN И COS ===");
        Function sin = new Sin();
        Function cos = new Cos();
        
        // Выводим информацию о функциях
        System.out.println("Sin область определения: [" + sin.getLeftDomainBorder() + ", " + sin.getRightDomainBorder() + "]");
        System.out.println("Cos область определения: [" + cos.getLeftDomainBorder() + ", " + cos.getRightDomainBorder() + "]");
        System.out.println();

        // Выводим значения на отрезке от 0 до π с шагом 0.1
        System.out.println("x\t\tSin(x)\t\tCos(x)");
        System.out.println("----------------------------------------");
    
        for (double x = 0; x <= Math.PI; x += 0.1) {
            double sinValue = sin.getFunctionValue(x);
            double cosValue = cos.getFunctionValue(x);
            
            System.out.printf("%.2f\t\t%.6f\t%.6f%n", x, sinValue, cosValue);
        }
        double sinValue = sin.getFunctionValue(Math.PI);
        double cosValue = cos.getFunctionValue(Math.PI);
        System.out.printf("%.2f\t\t%.6f\t%.6f%n", Math.PI, sinValue, cosValue);
    }

    // Тест табулированных sin и cos
    public static void TabTestSinCos(){
        System.out.println("=== ТАБУЛИРОВАННЫЕ АНАЛОГИ SIN И COS ===");
        
        // Создаем исходные функции
        Function sin = new Sin();
        Function cos = new Cos();
        
        // Создаем табулированные аналоги с 10 точками
        TabulatedFunction tabulatedSin = TabulatedFunctions.tabulate(sin, 0, Math.PI, 10);
        TabulatedFunction tabulatedCos = TabulatedFunctions.tabulate(cos, 0, Math.PI, 10);
    
        // Выводим информацию о табулированных функциях
        System.out.println("Табулированный Sin:");
        System.out.println("Количество точек: " + tabulatedSin.getPointsCount());
        System.out.println("Область определения: [" + tabulatedSin.getLeftDomainBorder() + ", " + tabulatedSin.getRightDomainBorder() + "]");
        
        System.out.println("\nТабулированный Cos:");
        System.out.println("Количество точек: " + tabulatedCos.getPointsCount());
        System.out.println("Область определения: [" + tabulatedCos.getLeftDomainBorder() + ", " + tabulatedCos.getRightDomainBorder() + "]");
        
        // Выводим точки табулированных функций
        System.out.println("\n=== ТОЧКИ ТАБУЛИРОВАННЫХ ФУНКЦИЙ ===");
        System.out.println("Sin точки:");
        for (int i = 0; i < tabulatedSin.getPointsCount(); i++) {
            System.out.printf("  [%d] x=%.4f, y=%.6f%n", 
                i, tabulatedSin.getPointX(i), tabulatedSin.getPointY(i));
        }
        
        System.out.println("\nCos точки:");
        for (int i = 0; i < tabulatedCos.getPointsCount(); i++) {
            System.out.printf("  [%d] x=%.4f, y=%.6f%n", 
                i, tabulatedCos.getPointX(i), tabulatedCos.getPointY(i));
        }
        
        // Сравниваем значения исходных и табулированных функций
        System.out.println("\n=== СРАВНЕНИЕ ИСХОДНЫХ И ТАБУЛИРОВАННЫХ ФУНКЦИЙ ===");
        System.out.println("x\t\tSin(x)\t\tTabSin(x)\tРазница\t\tCos(x)\t\tTabCos(x)\tРазница");
        System.out.println("---------------------------------------------------------------------------------------------------");
        
        for (double x = 0; x <= Math.PI; x += 0.1) {
            double sinOriginal = sin.getFunctionValue(x);
            double cosOriginal = cos.getFunctionValue(x);
            double sinTabulated = tabulatedSin.getFunctionValue(x);
            double cosTabulated = tabulatedCos.getFunctionValue(x);
            
            double sinDiff = Math.abs(sinOriginal - sinTabulated);
            double cosDiff = Math.abs(cosOriginal - cosTabulated);
            
            System.out.printf("%.2f\t\t%.6f\t%.6f\t%.6f\t%.6f\t%.6f\t%.6f%n", 
                x, sinOriginal, sinTabulated, sinDiff, cosOriginal, cosTabulated, cosDiff);
        }
        
        // Анализ точности
        System.out.println("\n=== АНАЛИЗ ТОЧНОСТИ ===");
        double maxSinError = 0;
        double maxCosError = 0;
        double avgSinError = 0;
        double avgCosError = 0;
        int count = 0;
        
        for (double x = 0; x <= Math.PI; x += 0.01) {
            double sinOriginal = sin.getFunctionValue(x);
            double cosOriginal = cos.getFunctionValue(x);
            double sinTabulated = tabulatedSin.getFunctionValue(x);
            double cosTabulated = tabulatedCos.getFunctionValue(x);
            
            double sinError = Math.abs(sinOriginal - sinTabulated);
            double cosError = Math.abs(cosOriginal - cosTabulated);
            
            maxSinError = Math.max(maxSinError, sinError);
            maxCosError = Math.max(maxCosError, cosError);
            avgSinError += sinError;
            avgCosError += cosError;
            count++;
        }
        
        avgSinError /= count;
        avgCosError /= count;
        
        System.out.printf("Максимальная ошибка Sin: %.8f%n", maxSinError);
        System.out.printf("Максимальная ошибка Cos: %.8f%n", maxCosError);
        System.out.printf("Средняя ошибка Sin: %.8f%n", avgSinError);
        System.out.printf("Средняя ошибка Cos: %.8f%n", avgCosError);
    }

    // Тест сумма квадратов табулированных sin и cos
    public static void TabTestSquareSinCos(){
        System.out.println("=== СУММА КВАДРАТОВ ТАБУЛИРОВАННЫХ SIN И COS ===");
        
        // Создаем исходные функции
        Function sin = new Sin();
        Function cos = new Cos();
        
        // Тестируем с разным количеством точек
        int[] pointsCounts = {5, 10, 20, 50};
        
        for (int pointsCount : pointsCounts) {
            System.out.println("\n--- Количество точек: " + pointsCount + " ---");
            
            // Создаем табулированные аналоги
            TabulatedFunction tabulatedSin = TabulatedFunctions.tabulate(sin, 0, Math.PI, pointsCount);
            TabulatedFunction tabulatedCos = TabulatedFunctions.tabulate(cos, 0, Math.PI, pointsCount);
            
            // Создаем квадраты функций
            Function sinSquared = Functions.power(tabulatedSin, 2);
            Function cosSquared = Functions.power(tabulatedCos, 2);
            
            // Создаем сумму квадратов
            Function sumOfSquares = Functions.sum(sinSquared, cosSquared);
            
            // Выводим значения
            System.out.println("x\t\tSin^2+Cos^2\tОжидаемое\tОшибка");
            System.out.println("--------------------------------------------------");
            
            double maxError = 0;
            double avgError = 0;
            int count = 0;
            
            for (double x = 0; x <= Math.PI; x += 0.1) {
                double actual = sumOfSquares.getFunctionValue(x);
                double expected = 1.0; // sin^2(x) + cos^2(x) = 1
                double error = Math.abs(actual - expected);
                
                maxError = Math.max(maxError, error);
                avgError += error;
                count++;
                
                System.out.printf("%.2f\t\t%.6f\t%.1f\t\t%.6f%n", 
                    x, actual, expected, error);
            }
            
            avgError /= count;
            
            System.out.printf("\nСтатистика для %d точек:%n", pointsCount);
            System.out.printf("Максимальная ошибка: %.8f%n", maxError);
            System.out.printf("Средняя ошибка: %.8f%n", avgError);
        }
    }

    public static void TabTestExp(){
        try {
            System.out.println("=== ТЕСТИРОВАНИЕ ЭКСПОНЕНТЫ ===");
            
            // Создаем табулированный аналог экспоненты
            Function exp = new Exp();
            TabulatedFunction tabulatedExp = TabulatedFunctions.tabulate(exp, 0, 10, 11);
            
            // Выводим информацию об исходной функции
            System.out.println("Исходная табулированная экспонента:");
            System.out.println("Количество точек: " + tabulatedExp.getPointsCount());
            System.out.println("Область определения: [" + tabulatedExp.getLeftDomainBorder() + ", " + tabulatedExp.getRightDomainBorder() + "]");
            
            System.out.println("\nТочки исходной функции:");
            for (int i = 0; i < tabulatedExp.getPointsCount(); i++) {
                System.out.printf("  [%d] x=%.1f, y=%.6f%n", 
                    i, tabulatedExp.getPointX(i), tabulatedExp.getPointY(i));
            }
            
            // Записываем в файл
            String filename = "exp_function.txt";
            try (FileWriter writer = new FileWriter(filename)) {
                TabulatedFunctions.writeTabulatedFunction(tabulatedExp, writer);
                System.out.println("\nФункция записана в файл: " + filename);
            }
            
            // Читаем из файла
            TabulatedFunction readExp;
            try (FileReader reader = new FileReader(filename)) {
                readExp = TabulatedFunctions.readTabulatedFunction(reader);
                System.out.println("\nФункция прочитана из файла: " + filename);
            }
            
            // Выводим информацию о прочитанной функции
            System.out.println("Прочитанная табулированная экспонента:");
            System.out.println("Количество точек: " + readExp.getPointsCount());
            System.out.println("Область определения: [" + readExp.getLeftDomainBorder() + ", " + readExp.getRightDomainBorder() + "]");
            
            System.out.println("\nТочки прочитанной функции:");
            for (int i = 0; i < readExp.getPointsCount(); i++) {
                System.out.printf("  [%d] x=%.1f, y=%.6f%n", 
                    i, readExp.getPointX(i), readExp.getPointY(i));
            }
            
            // Сравниваем значения
            System.out.println("\n=== СРАВНЕНИЕ ИСХОДНОЙ И ПРОЧИТАННОЙ ФУНКЦИЙ ===");
            System.out.println("x\t\tИсходная\tПрочитанная\tРазница");
            System.out.println("-------------------------------------------------------");
            
            double maxError = 0;
            double avgError = 0;
            int count = 0;
            
            for (double x = 0; x <= 10; x += 1.0) {
                double original = tabulatedExp.getFunctionValue(x);
                double read = readExp.getFunctionValue(x);
                double diff = Math.abs(original - read);
                
                maxError = Math.max(maxError, diff);
                avgError += diff;
                count++;
                
                System.out.printf("%.1f\t\t%.6f\t%.6f\t%.10f%n", 
                    x, original, read, diff);
            }
            
            avgError /= count;
            
            System.out.println("\n=== СТАТИСТИКА ТОЧНОСТИ ===");
            System.out.printf("Максимальная ошибка: %.10f%n", maxError);
            System.out.printf("Средняя ошибка: %.10f%n", avgError);
                
        } catch (IOException e) {
            System.err.println("Ошибка ввода-вывода: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Ошибка: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void TabTestLog(){
        try {
            System.out.println("=== ТЕСТИРОВАНИЕ НАТУРАЛЬНОГО ЛОГАРИФМА ===");
            
            // Создаем табулированный аналог натурального логарифма
            // Начинаем с 0.1, т.к. ln(0) не определен
            Function log = new Log(Math.E); // натуральный логарифм
            TabulatedFunction tabulatedLog = TabulatedFunctions.tabulate(log, 0.1, 10, 11);
            
            // Выводим информацию об исходной функции
            System.out.println("Исходная табулированная логарифмическая функция:");
            System.out.println("Количество точек: " + tabulatedLog.getPointsCount());
            System.out.println("Область определения: [" + tabulatedLog.getLeftDomainBorder() + ", " + tabulatedLog.getRightDomainBorder() + "]");
            
            System.out.println("\nТочки исходной функции:");
            for (int i = 0; i < tabulatedLog.getPointsCount(); i++) {
                System.out.printf("  [%d] x=%.1f, y=%.6f%n", 
                    i, tabulatedLog.getPointX(i), tabulatedLog.getPointY(i));
            }
            
            // Записываем в бинарный файл
            String filename = "log_function.bin";
            try (FileOutputStream fos = new FileOutputStream(filename)) {
                TabulatedFunctions.outputTabulatedFunction(tabulatedLog, fos);
                System.out.println("\nФункция записана в бинарный файл: " + filename);
            }
            
            // Для сравнения - также записываем в текстовый формат
            String textFilename = "log_function.txt";
            try (FileWriter writer = new FileWriter(textFilename)) {
                TabulatedFunctions.writeTabulatedFunction(tabulatedLog, writer);
                System.out.println("Функция также записана в текстовый файл: " + textFilename);
            }
            File textFile = new File(textFilename);
            
            // Читаем из бинарного файла
            TabulatedFunction readLog;
            try (FileInputStream fis = new FileInputStream(filename)) {
                readLog = TabulatedFunctions.inputTabulatedFunction(fis);
                System.out.println("\nФункция прочитана из бинарного файла: " + filename);
            }
            
            // Выводим информацию о прочитанной функции
            System.out.println("Прочитанная табулированная логарифмическая функция:");
            System.out.println("Количество точек: " + readLog.getPointsCount());
            System.out.println("Область определения: [" + readLog.getLeftDomainBorder() + ", " + readLog.getRightDomainBorder() + "]");
            
            System.out.println("\nТочки прочитанной функции:");
            for (int i = 0; i < readLog.getPointsCount(); i++) {
                System.out.printf("  [%d] x=%.1f, y=%.6f%n", 
                    i, readLog.getPointX(i), readLog.getPointY(i));
            }
            
            // Сравниваем значения
            System.out.println("\n=== СРАВНЕНИЕ ИСХОДНОЙ И ПРОЧИТАННОЙ ФУНКЦИЙ ===");
            System.out.println("x\t\tИсходная\tПрочитанная\tРазница");
            System.out.println("-------------------------------------------------------");
            
            double maxError = 0;
            double avgError = 0;
            int count = 0;
            
            for (double x = 0.1; x <= 10; x += 1.0) {
                double original = tabulatedLog.getFunctionValue(x);
                double read = readLog.getFunctionValue(x);
                double diff = Math.abs(original - read);
                
                maxError = Math.max(maxError, diff);
                avgError += diff;
                count++;
                
                System.out.printf("%.1f\t\t%.6f\t%.6f\t%.10f%n", 
                    x, original, read, diff);
            }
            
            avgError /= count;
            
            System.out.println("\n=== СТАТИСТИКА ТОЧНОСТИ ===");
            System.out.printf("Максимальная ошибка: %.10f%n", maxError);
            System.out.printf("Средняя ошибка: %.10f%n", avgError);
            
        } catch (IOException e) {
            System.err.println("Ошибка ввода-вывода: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Ошибка: " + e.getMessage());
            e.printStackTrace();
        }
    }   

    public static void TestExter(){
        try {
            System.out.println("=== ТЕСТИРОВАНИЕ СЕРИАЛИЗАЦИИ ===");
            
            // Создаем композитную функцию: ln(exp(x)) = x
            Function exp = new Exp();
            Function log = new Log(Math.E);
            Function composition = Functions.composition(log, exp);
            
            // Табулируем функцию
            TabulatedFunction tabulatedFunction = TabulatedFunctions.tabulate(composition, 0, 10, 11);
            
            System.out.println("Исходная функция: ln(exp(x))");
            System.out.println("Теоретически должна быть: f(x) = x");
            
            // Тестируем Externalizable
            testExternalizable(tabulatedFunction, "function_Externalizable.ser");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static void testExternalizable(TabulatedFunction function, String filename) throws IOException, ClassNotFoundException {
        System.out.println("\n--- Тестирование Externalizable ---");
        
        // Сериализация
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(function);
            System.out.println("Функция сериализована в: " + filename);
        }
        // Десериализация
        TabulatedFunction deserializedFunction;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            deserializedFunction = (TabulatedFunction) ois.readObject();
            System.out.println("Функция десериализована из: " + filename);
        }
        // Сравнение
        System.out.println("\nСравнение исходной и десериализованной функций:");
        System.out.println("x\t\tИсходная\tДесериализ.\tРазница");
        System.out.println("---------------------------------------------------");
        
        for (double x = 0; x <= 10; x += 1.0) {
            double original = function.getFunctionValue(x);
            double deserialized = deserializedFunction.getFunctionValue(x);
            double diff = Math.abs(original - deserialized);
            
            System.out.printf("%.1f\t\t%.6f\t%.6f\t%.10f%n", 
                x, original, deserialized, diff);
        }
    }

    private static void testSerializable() {
        System.out.println("\n--- Тестирование Serializable ---");

        try {
            Exp exp = new Exp();
            Log log = new Log(Math.E);
            Function composition = Functions.composition(exp, log);
            TabulatedFunction original = TabulatedFunctions.tabulate(composition, 0, 10, 11);

            System.out.println("Оригинальная функция ln(exp(x)) = x (0 до 10, 11 точек):");
            printFunc(original, 0, 10, 1);

            // Сериализация
            String serFile = "func_serializable.ser";
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(serFile))) {
                oos.writeObject(original);
            }

            // Десериализация
            TabulatedFunction deserialized;
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(serFile))) {
                deserialized = (TabulatedFunction) ois.readObject();
            }

            System.out.println("Десериализованная функция:");
            printFunc(deserialized, 0, 10, 1);

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Ошибка сериализации: " + e.getMessage());
        }
        System.out.println();
    }
    
    private static void printFunc(TabulatedFunction func, double start, double end, double step) {
        for (double x = start; x <= end; x += step) {
            System.out.printf("f(%.1f) = %.6f%n", x, func.getFunctionValue(x));
        }
    }

}
   
    
    



