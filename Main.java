package phonebook;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

class sortingAndSearching {

    private List<String> directory;
    private List<String> names;
    private Count c;
    sortingAndSearching(Count c) {
        this.c = c;
    }

    private void loadDirectory() throws IOException {

        directory = Files.readAllLines(
                Paths.get("C:\\Users\\panka\\Desktop\\pankaj\\my_text_file\\directory.txt"));

        names = Files.readAllLines(
                Paths.get("C:\\Users\\panka\\Desktop\\pankaj\\my_text_file\\find.txt"));

    }

    void linearSearch() {

        for (String name : names) {
            for (String info : directory) {
                if (info.split(" ", 2)[1].equals(name)) {
                    c.setCount();
                    break;
                }
            }
        }
    }

    void binarySearch(String element) {

        int left = 0;
        int right = directory.size() - 1;

        while (left <= right) {

            int mid = left + (right - left) / 2;
            if (element.equals(directory.get(mid).split(" ", 2)[1])) {
                c.setCount();
                break;
            } else if (element.compareTo(directory.get(mid).split(" ", 2)[1]) < 0) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
    }

    void jumpSearch(String element) {

        int left = 0;
        int right = 0;

        int step = (int) Math.sqrt(directory.size());
        if (element.equals(directory.get(right).split(" ", 2)[1])) {
            c.setCount();
        }
        while (right < directory.size() - 1) {
            right = Math.min(step + right, directory.size() - 1);
            if (element.compareTo(directory.get(right).split(" ", 2)[1]) <= 0) {
                break;
            }
            left = right;
        }
        Search(element, left, right);
    }

    private void Search(String element, int left, int right) {

        for (int i = right; i > left; i--) {
            if (element.equals(directory.get(i).split(" ", 2)[1])) {
                c.setCount();
                break;
            }
        }
    }

    void bubbleSort(long start) {

        boolean sorted = false;

        for (int j = 0; j < directory.size() && !sorted; j++) {

            sorted = true;
            long interval = System.currentTimeMillis();
            for (int i = 0; i < directory.size() - j - 1; i++) {

                if (directory.get(i).split(" ", 2)[1]
                        .compareTo(directory.get(i + 1).split(" ", 2)[1]) > 0) {
                   swap(directory, i, i + 1);
                    sorted = false;
                }
            }
            if (interval - start > start * 10) {
                linearSearch();
                return;
            }
        }
    }

    void quickSort(List<String> names, int left, int right) {

        if (left < right) {
            int pivotIndex = partition(names, left, right);
            quickSort(names, left, pivotIndex - 1);
            quickSort(names, pivotIndex + 1, right);
        }
    }

    private int partition(List<String> names, int left, int right) {

        String pivot = names.get(right).split(" ", 2)[1];
        int partitionIndex = left;
        for(int i = left; i < right; i++) {

            if (names.get(i).split(" ", 2)[1].compareTo(pivot) < 0) {
                swap(names, i, partitionIndex);
                partitionIndex++;
            }
        }
        swap(names, partitionIndex, right);
        return partitionIndex;
    }

    private void swap(List<String> names, int i, int partitionIndex) {

        String temp;
        temp = names.get(i);
        names.set(i, names.get(partitionIndex));
        names.set(partitionIndex, temp);

    }

    String getTime(String type, long start, long stop) {

        return (type + ": " + (stop-start) / 60000 + " min. "
                + ((stop-start) / 1000) % 60 + " sec. "
                + (stop-start) % 1000 + " ms.");
    }

    void setDirectory() {
        try {
            loadDirectory();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    List<String> getDirectory() {
        return directory;
    }

    List<String> getNames() {
        return names;
    }
}
class Count {
    private int count;

    void setCount() {
        this.count++;
    }

    void resetCount() {
        count = 0;
    }

    int getCount() {
        return count;
    }
}
class TableEntry<T, L> {
    private T key;
    private L value;

    TableEntry(T key, L value) {
        this.key = key;
        this.value = value;
    }

    T getKey() {
        return key;
    }

}
class HashTable<E, L>{
    private int size;
    private TableEntry[] table;
    private Count c;

    HashTable(int size, Count c) {
        this.size  = size;
        table = new TableEntry[this.size];
        this.c = c;
    }

    void put(E key, L value) {
        int index = findKey(key);
        if (index == -1) {
            return;
        }
        table[index] = new TableEntry<>(key, value);
    }
    private int findKey(E Key) {
        int key = keyCreator(Key);
        int hash = key % size;
//        int count = 0;

        while (!(table[hash] == null || table[hash].getKey().equals(Key))) {
            hash = (hash + 3) % size;
            if (hash == key % size) {
               return -1;
            }
//            count++;
        }
//        System.out.println("Collision = " + count);
        return hash;
    }
    void get(E key) {
        int index = findKey(key);
        if (index == -1 || table[index] == null) {
            return;
        }
        if(table[index].getKey().equals(key)) {
            c.setCount();
        }
    }

    private int keyCreator(E key) {
       int hash = key.hashCode();
        return hash < 0 ? hash * -1 : hash;
    }
}

class timeIt {
    private long start;
    private long stop;

    void setStart() {
        start = System.currentTimeMillis();
    }

    void setStop() {
        stop = System.currentTimeMillis();
    }

    long getStart() {
        return start;
    }

    long getStop() {
        return stop;
    }
}

public class Main {
    public static void main(String[] args) {

        Count c = new Count();
        sortingAndSearching s = new sortingAndSearching(c);
        timeIt timer1 = new timeIt();
        timeIt timer2 = new timeIt();
        s.setDirectory();

        System.out.println("Start searching (linear search)...");

        timer1.setStart();
        s.linearSearch();
        timer1.setStop();

        System.out.print("Found " + c.getCount() + " / " + s.getNames().size() + ". ");
        System.out.println(s.getTime("Time taken", timer1.getStart(), timer1.getStop()));
        c.resetCount();
        System.out.println();

        System.out.println("Start searching (bubble sort + jump search)...");
        timer2.setStart();
        s.bubbleSort(timer1.getStop() - timer1.getStart());
        timer2.setStop();

        timer1.setStart();
        for (String name : s.getNames()) {
            s.jumpSearch(name);
        }
        timer1.setStop();

        System.out.print("Found " + c.getCount() + " / " + s.getNames().size() + ". ");
        System.out.println(s.getTime("Time taken", timer2.getStart() + timer1.getStart(),
                timer2.getStop() + timer1.getStop()));
        System.out.println(s.getTime("Sorting time", timer2.getStart(), timer2.getStop()));
        System.out.println(s.getTime("Searching time", timer1.getStart(), timer1.getStop()));
        c.resetCount();
        System.out.println();

        s.setDirectory();
        System.out.println("Start searching (quick sort + binary search)...");
        timer1.setStart();
        s.quickSort(s.getDirectory(), 0, s.getDirectory().size() - 1);
        timer1.setStop();

        timer2.setStart();
        for (String name : s.getNames()) {
            s.binarySearch(name);
        }
        timer2.setStop();

        System.out.print("Found " + c.getCount() + " / " + s.getNames().size() + ". ");
        System.out.println(s.getTime("TIme taken", timer1.getStart() + timer2.getStart(),
                timer1.getStop() + timer2.getStop()));
        System.out.println(s.getTime("Sorting time", timer1.getStart(), timer1.getStop()));
        System.out.println(s.getTime("Searching time", timer2.getStart(), timer2.getStop()));
        c.resetCount();
        System.out.println();


        System.out.println("Start searching (hash table)...");
        HashTable<String, Long> table = new HashTable<>(s.getDirectory().size() * 2, c);

        timer1.setStart();

        for (String info : s.getDirectory()) {

            String[] data = info.split(" ", 2);
            table.put(data[1], Long.parseLong(data[0]));
        }
        timer1.setStop();

        timer2.setStart();

        for (String name : s.getNames()) {
            table.get(name);
        }
        timer2.setStop();

        System.out.print("Found " + c.getCount() + " / " + s.getNames().size() + ". ");
        System.out.println(s.getTime("TIme taken", timer1.getStart() + timer2.getStart(),
                timer1.getStop() + timer2.getStop()));
        System.out.println(s.getTime("Creating time", timer1.getStart(), timer1.getStop()));
        System.out.println(s.getTime("Searching time", timer2.getStart(), timer2.getStop()));
        c.resetCount();
    }
}
