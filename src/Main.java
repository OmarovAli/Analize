import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingDeque;

public class Main {
    public static BlockingDeque<String> queue1 = new ArrayBlockingQueue<>(100);
    public static BlockingDeque<String> queue2 = new ArrayBlockingQueue<>(100);
    public static BlockingDeque<String> queue3 = new ArrayBlockingQueue<>(100);
    public static Thread textGenerator;
    public static void main(String[] args) throws InterruptedException {
        textGenerator = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                String text = generateText("abc", 100000);
                try {
                    queue1.put(text);
                    queue2.put(text);
                    queue3.put(text);
                } catch (InterruptedException e) {
                    throw new RuntimeException();
                }
            }
        });
        textGenerator.start();

        Thread a = getThread(queue1, 'a');
        Thread b = getThread(queue2, 'b');
        Thread c = getThread(queue3, 'c');

        a.start();
        b.start();
        c.start();

        a.join();
        b.join();
        c.join();


    }
    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
    public static Thread getThread(BlockingDeque<String> queue, char letter) {
        return new Thread(() -> {
                int max = findMaxCharCount(queue, letter);
        System.out.println("Max qty of " + letter + " int all texts: " + max);

      });
    }
    public static int findMaxCharCount(BlockingDeque<String> queue, char letter) {
        int count = 0;
        int max = 0;
        String text;
        try {
            while (textGenerator.isAlive()) {
                text = queue.take();
                for (char c : text.toCharArray()) {
                    if (c == letter);
                }
                if (count > max) max = count;
                count = 0;
                }
            } catch (InterruptedException e ) {
            System.out.println(Thread.currentThread().getName() + "was interrupted");
            return -1;
        }
        return max;
    }
}